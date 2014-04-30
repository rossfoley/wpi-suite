
class PlanningPokerViewModel
  constructor: ->
    @planningPokerSessions = ko.observableArray([])
    @requirements = []
    @team = []
    @activeSession = ko.observable()

    # Load in the planning poker sessions
    $.ajax
      dataType: 'json'
      url: 'API/planningpoker/planningpokersession'
      async: no
      success: (data) =>
        @planningPokerSessions(ko.mapping.fromJS(data))
        @planningPokerSessions(data)

    # Load in the requirements
    $.ajax
      dataType: 'json'
      url: 'API/requirementmanager/requirement'
      async: no
      success: (data) =>
        @requirements = data

    # Load in the planning poker sessions
    $.ajax
      dataType: 'json'
      url: 'API/core/user'
      async: no
      success: (data) => 
        @team = data

    @setActiveSession = (session) =>
      @activeSession(session)

    @requirementsForSession = (session) =>
      result = []
      for requirement in @requirements
        if requirement['id'] in session['requirementIDs']
          result.push requirement
      result

    # @submitVote = ->
    #   vote = parseInt(@voteValue)
    #   estimate = 
    #     sessionID: @get('uuid')
    #     requirementID: @get('requirement')['id']
    #     vote: vote
    #   $.ajax
    #     type: 'POST'
    #     dataType: 'json'
    #     url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website'
    #     data: JSON.stringify(estimate)
    #     success: (data) => 
    #       window.location.reload()
    #     error: => console.log 'Error updating the estimate'

    @widthPercent = (session, requirementID) ->
      numVotes = 0
      for estimate in session['estimates']
        if parseInt(estimate['requirementID']) == parseInt(requirementID)
          numVotes++
      percent = 0
      if @team.length > 0
        percent = parseInt((numVotes / @team.length) * 100)
      "#{percent}%"

$ ->
  window.PokerVM = new PlanningPokerViewModel()
  ko.applyBindings(window.PokerVM)
