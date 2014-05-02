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

    # Load in the planning poker sessions
    $.ajax
      dataType: 'json'
      url: 'API/planningpoker/planningpokersession'
      async: no
      success: (data) =>
        for session in data
          params = 
            requirements: @requirements
            team: @team
            username: @username
          @planningPokerSessions.push(new SessionViewModel(session, params))

    @openSessions = ko.computed =>
      @planningPokerSessions().filter (session) =>
        session.gameState() == 'OPEN'

    @setActiveSession = (session) =>
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
      @estimate().vote(parseInt(@totalValue()))
      $.ajax
        type: 'POST'
        dataType: 'json'
        url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website'
        data: ko.toJSON(@estimate())
        success: (data) => 
          console.log('Vote successfully submitted')
          unless @username in @voted()
            @voted.push @username
        error: => console.log 'Error updating the estimate'


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
  window.PokerVM = new PlanningPokerViewModel()
  ko.applyBindings(window.PokerVM)
