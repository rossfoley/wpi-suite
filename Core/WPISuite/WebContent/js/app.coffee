$ ->
  # Create our Ember app
  window.App = Ember.Application.create()

  # Load in the planning poker sessions
  $.ajax
    dataType: 'json'
    url: 'API/planningpoker/planningpokersession'
    async: no
    success: (data) => 
      openSessions = []
      for session in data
        session['requirements'] = []
        if session['gameState'] == 'OPEN'
          openSessions.push session
      App.Sessions = openSessions

  # Load in the requirements
  $.ajax
    dataType: 'json'
    url: 'API/requirementmanager/requirement'
    async: no
    success: (data) =>
      App.Requirements = data
      for requirement in data
        for session in App.Sessions
          # Also add each requirement to the session they belong to
          if requirement['id'] in session['requirementIDs']
            session['requirements'].push requirement

            # Add the requirement object to each estimate
            for estimate in session['estimates']
              if requirement['id'] == estimate['requirementID']
                estimate['requirement'] = requirement

  # Load in the planning poker sessions
  $.ajax
    dataType: 'json'
    url: 'API/core/user'
    async: no
    success: (data) => 
      App.Team = data

  # Routes
  App.Router.map ->
    @resource 'sessions', path: '/', ->
      @resource 'session', path: '/:session_uuid'

  # Route for the list of sessions
  App.SessionsRoute = Ember.Route.extend
    model: -> App.Sessions

  # Route for an individual session
  App.SessionRoute = Ember.Route.extend
    serialize: (session) ->
      session_uuid: session['uuid']
    model: (params) ->
      App.Sessions.findBy('uuid', params.session_uuid)

  App.RequirementProgressComponent = Ember.Component.extend
    classNames: ['progress', 'requirement-progress']

    width: (->
      numUsers = App.Team.length
      numVotes = 0
      session = App.Sessions.findBy('uuid', @get('uuid'))
      for estimate in session['estimates']
        if estimate['requirementID'] == @get('requirement')['id']
          numVotes++
      percent = 0
      if numUsers > 0
        percent = parseInt((numVotes / numUsers) * 100)
      "#{percent}%"
      ).property('uuid', 'requirement')

    widthStyle: (-> "width: #{@get('width')}" ).property('width')

  App.RequirementVoteComponent = Ember.Component.extend
    classNames: ['requirement-vote']
    showVoteForm: no
    voteValue: ''

    actions:
      startVote: ->
        @toggleProperty 'showVoteForm'

      submitVote: ->
        vote = parseInt(@voteValue)
        estimate = 
          sessionID: @get('uuid')
          requirementID: @get('requirement')['id']
          vote: vote
        $.ajax
          type: 'POST'
          dataType: 'json'
          url: 'API/Advanced/planningpoker/planningpokersession/update-estimate-website'
          data: JSON.stringify(estimate)
          success: => @toggleProperty 'showVoteForm'
          error: => console.log 'Error updating the estimate'
