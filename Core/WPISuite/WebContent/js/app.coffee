###################
# Main View Model #
###################

class PlanningPokerViewModel
  constructor: ->
    @planningPokerSessions = ko.observableArray([])
    @requirements = []
    @team = []
    @activeSession = ko.observable()
    # Get the current username
    firstSplit = window.location.search.split('&')
    if firstSplit.length > 1
      @username = firstSplit[0].split('=')[1]
      @querySession = firstSplit[1].split('=')[1]
    else
      @username = window.location.search.split('=')[1]

    # Load in the requirements
    $.ajax
      dataType: 'json'
      url: 'API/requirementmanager/requirement'
      async: no
      success: (data) =>
        for requirement in data
          requirement['voteValue'] = 0
        @requirements = data

    # Load in the planning poker sessions
    $.ajax
      dataType: 'json'
      url: 'API/core/user'
      async: no
      success: (data) => 
        @team = data

    @params = 
      requirements: @requirements
      team: @team
      username: @username

    # Load in the planning poker sessions
    $.ajax
      dataType: 'json'
      url: 'API/planningpoker/planningpokersession'
      async: no
      success: (data) =>
        for session in data
          @planningPokerSessions.push(new SessionViewModel(session, @params))

    @openSessions = ko.computed =>
      @planningPokerSessions().filter (session) =>
        session.gameState() == 'OPEN'

    @setActiveSession = (session) =>
      @activeSession(session)

    @checkForUpdates = =>
      $.ajax
        dataType: 'json'
        url: 'API/requirementmanager/requirement'
        success: (data) =>
          for requirement in data
            requirement['voteValue'] = 0
          @requirements = data
          @params.requirements = data
          $.ajax
            type: 'GET'
            dataType: 'json'
            url: 'API/Advanced/planningpoker/planningpokersession/check-for-updates'
            success: @applyUpdates

    @applyUpdates = (updates) =>
      for changedSession in updates
        newSession = yes
        for session in @planningPokerSessions()
          if changedSession['uuid'] == session.uuid()
            # The session exists, so apply the changes
            newSession = no
            session.applyUpdate(changedSession)
        if newSession
          @planningPokerSessions.push(new SessionViewModel(changedSession, @params))

    # Check for new updates every 5 seconds
    setInterval @checkForUpdates, 5000

    # If the session query string was provided, make it the active session
    if @querySession
      for session in @planningPokerSessions()
        if @querySession == session.uuid()
          @activeSession(session)


#################################
# Individual Session View Model #
#################################

class SessionViewModel
  constructor: (data, params) ->
    @parent = parent
    @allRequirements = ko.observableArray(params.requirements)
    @team = ko.observableArray(params.team)
    @username = params.username
    @params = params

    # Set up the session fields as observable fields
    for field, value of data
      @[field] = ko.observable(value)

    @params = params
    @params['usingDeck'] = @isUsingDeck()
    @params['deck'] = {}
    if @isUsingDeck()
      @params['deck'] = @sessionDeck()

    # Set up the estimate view models
    @requirementEstimates = ko.observableArray([])
    for requirement in @allRequirements()
      if requirement['id'] in @requirementIDs()
        # Set up a blank estimate for the current user
        userEstimate = 
          sessionID: @uuid()
          requirementID: requirement['id']
          ownerName: @username
          vote: 0

        # Create a list of people who have already voted
        voterList = [] 
        for estimate in @estimates()
          if estimate['requirementID'] == requirement['id']
            # If a person already has an estimate, add them to voterList
            if estimate['vote'] > -1
              voterList.push estimate['ownerName']

            # If the current user has an estimate, update the vote in userEstimate
            if estimate['ownerName'] == @username
              userEstimate['vote'] = estimate['vote']

        # Create the view model for this requirement estimate
        @requirementEstimates.push(new EstimateViewModel(userEstimate, voterList, requirement, @params))


    @requirements = ko.computed =>
      result = []
      for requirement in @allRequirements()
        if requirement['id'] in @requirementIDs()
          result.push requirement
      result

    # Apply an update received from the server
    @applyUpdate = (update) =>
      for changedEstimate in update['estimates']
        for requirementEstimate in @requirementEstimates()
          if changedEstimate['requirementID'] == requirementEstimate.requirement()['id']
            requirementEstimate.applyUpdate(changedEstimate)



#####################################
# Individual Requirement View Model #
#####################################

class EstimateViewModel
  constructor: (userEstimate, voterList, req, params) ->
    @requirement = ko.observable(req)
    @requirements = ko.observable(params.requirements)
    @team = ko.observable(params.team)
    @usingDeck = ko.observable(params.usingDeck)
    @deck = ko.observable(params.deck)
    @username = params.username
    @voted = ko.observableArray(voterList)
    @showSuccessMessage = ko.observable(false)
    @voteValue = ko.observable(0).extend
      required:
        message: 'Please enter a vote!'
      pattern:
        message: 'Please enter a positive integer!'
        params: '^\\d+$'

    # Load in the user's estimate as observable fields
    observableEstimate = {}
    for key, value of userEstimate
      observableEstimate[key] = ko.observable(value)
    @estimate = ko.observable(observableEstimate)

    # Setup the cards for selection
    if @usingDeck()
      @cards = ko.observableArray([])
      for cardValue in @deck().numbersInDeck
        @cards.push(new CardViewModel(cardValue, no, @))

    @widthPercent = ko.computed =>
      numVotes = @voted().length
      teamSize = @team().length
      percent = 0
      if teamSize > 0
        percent = parseInt((numVotes / teamSize) * 100)
      "#{percent}%"

    @totalValue = ko.computed =>
      if @usingDeck()
        total = 0
        for card in @cards()
          if card.selected()
            total += card.value()
        total 
      else
        @voteValue()

    @setVoteValue = (value) =>
      if @usingDeck()
        deckNumbers = @deck().numbersInDeck.slice(0) # Copy the deck array
        deckNumbers = deckNumbers.sort().reverse()
        selectedCards = []
        remaining = value
        # Starting with the largest card, select cards until the value is reached
        for cardValue in deckNumbers
          if remaining >= cardValue
            selectedCards.push cardValue
            remaining -= cardValue
        # Now mark the cards as selected
        for card in @cards()
          if card.value() in selectedCards
            if card.value() > 0
              card.selected(yes)
      else
        # If there is no deck, then just set the value of the input
        @voteValue(value)

    # Load in the existing vote
    @setVoteValue(@estimate().vote())

    @notifySelection = (cardViewModel) =>
      unless @deck().allowMultipleSelections
        for card in @cards()
          card.selected(no)
        cardViewModel.selected(yes)

    @submitVote = =>
      unless @voteValue.error()
        @estimate().vote(parseInt(@totalValue()))
        $.ajax
          type: 'POST'
          dataType: 'json'
          url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website'
          data: ko.toJSON(@estimate())
          success: (data) => 
            unless @username in @voted()
              @voted.push @username
            # Show a success message
            @showSuccessMessage(true)
            # Fade out the success message after 2 seconds
            setTimeout (=> @showSuccessMessage(false)), 2000

    @applyUpdate = (update) =>
      unless update['ownerName'] in @voted()
        if update['vote'] > -1
          @voted.push update['ownerName']

    @voteError = ko.computed =>
      vote = @voteValue() # Access voteValue to ensure this updates correctly
      @voteValue.error() != null


#####################################
# Individual Requirement View Model #
#####################################

class CardViewModel
  constructor: (value, selected, parentViewModel) ->
    @value = ko.observable(value)
    @selected = ko.observable(selected)
    @parent = parentViewModel

    @cardClicked = =>
      if @selected()
        @selected(no)
      else
        @selected(yes)
        @parent.notifySelection(@)



############################################
# Initialize all the bindings on page load #
############################################

$ ->
  ko.bindingHandlers.fadeVisible =
    init: (element, valueAccessor) ->
        value = valueAccessor()
        $(element).toggle(ko.utils.unwrapObservable(value))

    update: (element, valueAccessor) ->
        value = valueAccessor()
        if ko.utils.unwrapObservable(value)
          $(element).fadeIn('fast')
        else
          $(element).fadeOut('fast')

  window.PokerVM = new PlanningPokerViewModel()
  ko.applyBindings(window.PokerVM)
