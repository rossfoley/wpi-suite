
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
      console.log(session)
      console.log(@requirements)
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

    # @width = ->
    #   numUsers = App.Team.length
    #   numVotes = 0
    #   session = App.Sessions.findBy('uuid', @get('uuid'))
    #   for estimate in session.get('estimates')
    #     if estimate['requirementID'] == @get('requirement')['id']
    #       numVotes++
    #   percent = 0
    #   if numUsers > 0
    #     percent = parseInt((numVotes / numUsers) * 100)
    #   "#{percent}%"

    # @widthStyle = -> "width: #{@get('width')}"

$ ->
  ko.applyBindings(new PlanningPokerViewModel())
