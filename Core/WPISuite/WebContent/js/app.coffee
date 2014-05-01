###################
# Main View Model #
###################

class PlanningPokerViewModel
  constructor: ->
    @planningPokerSessions = ko.observableArray([])
    @requirements = []
    @team = []
    @activeSession = ko.observable()

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
          @planningPokerSessions.push(new SessionViewModel(session, @requirements, @team))

    @setActiveSession = (session) =>
      @activeSession(session)



#################################
# Individual Session View Model #
#################################

class SessionViewModel
  constructor: (data, requirements, team) ->
    @parent = parent
    @allRequirements = ko.observableArray(requirements)
    @team = ko.observableArray(team)
    # Set up the session fields as observable fields
    for field, value of data
      @[field] = ko.observable(value)

    @requirements = =>
        result = []
        for requirement in @allRequirements()
          if requirement['id'] in @requirementIDs()
            result.push requirement
        result

    @widthPercent = (requirementID) =>
      numVotes = 0
      for estimate in @estimates()
        if parseInt(estimate['requirementID']) == parseInt(requirementID)
          numVotes++
      percent = 0
      if @team().length > 0
        percent = parseInt((numVotes / @team().length) * 100)
      "#{percent}%"

    @submitVote = (voteValue, requirementID) =>
      vote = parseInt(voteValue)
      estimate = 
        sessionID: @uuid()
        requirementID: requirementID
        vote: vote
      $.ajax
        type: 'POST'
        dataType: 'json'
        url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website'
        data: JSON.stringify(estimate)
        success: (data) => 
          console.log('We did it!')
        error: => console.log 'Error updating the estimate'


############################################
# Initialize all the bindings on page load #
############################################

$ ->
  window.PokerVM = new PlanningPokerViewModel()
  ko.applyBindings(window.PokerVM)
