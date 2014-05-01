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
        reqEstimates = []
        for user in @team()
          reqEstimates[user['username']] = 
            sessionID: @uuid()
            requirementID: requirement['id']
            ownerName: user['username']
            vote: 0
            isSaved: no

        for estimate in @estimates()
          if estimate['requirementID'] == requirement['id']
            estimate['isSaved'] = yes
            reqEstimates[estimate['ownerName']] = estimate
        @requirementEstimates.push(new EstimateViewModel(reqEstimates, requirement, @params))


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
  constructor: (estimatesObject, req, params) ->
    @requirement = ko.observable(req)
    @requirements = ko.observable(params.requirements)
    @team = ko.observable(params.team)
    @usingDeck = ko.observable(params.usingDeck)
    @deck = ko.observable(params.deck)
    @username = params.username

    # Load in the estimates as observable fields
    for user, estimate of estimatesObject
      observableEstimate = {}
      for key, value of estimate
        observableEstimate[key] = ko.observable(value)
      @[user] = ko.observable(observableEstimate)

    # Setup the cards for selection
    if @usingDeck()
      @cards = ko.observableArray([])
      for cardValue in @deck().numbersInDeck
        @cards.push(new CardViewModel(cardValue, no))

    @voteValue = ko.observable(@[@username]().vote())

    @widthPercent = ko.computed =>
      numVotes = 0
      for user in @team()
        if @[user['username']]().isSaved()
          numVotes++
      percent = 0
      if @team().length > 0
        percent = parseInt((numVotes / @team().length) * 100)
      "#{percent}%"

    @submitVote = =>
      @[@username]().vote(parseInt(@voteValue()))
      $.ajax
        type: 'POST'
        dataType: 'json'
        url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website'
        data: ko.toJSON(@[@username]())
        success: (data) => 
          console.log('Vote successfully submitted')
          @[@username]().isSaved(yes)
        error: => console.log 'Error updating the estimate'


#####################################
# Individual Requirement View Model #
#####################################

class CardViewModel
  constructor: (value, selected) ->
    @value = ko.observable(value)
    @selected = ko.observable(selected)

    @cardClicked = =>
      if @selected()
        @selected(no)
      else
        @selected(yes)



############################################
# Initialize all the bindings on page load #
############################################

$ ->
  window.PokerVM = new PlanningPokerViewModel()
  ko.applyBindings(window.PokerVM)
