$ ->
  # Create our Ember app
  window.App = Ember.Application.create()

  # Load in the planning poker sessions
  $.ajax
    dataType: 'json'
    url: 'API/planningpoker/planningpokersession'
    async: no
    success: (data) => 
      for session in data
        session['requirements'] = []
      App.Sessions = data

  # Load in the requirements
  $.ajax
    dataType: 'json'
    url: 'API/requirementmanager/requirement'
    async: no
    success: (data) =>
      for requirement in data
        for session in App.Sessions
          # Also add each requirement to the session they belong to
          if requirement['id'] in session['requirementIDs']
            numUsers = session['project']['team'].length
            numVotes = 0
            for estimate in session['estimates']
              if estimate['requirementID'] == requirement['id']
                numVotes++
            percent = 0
            if numUsers > 0
              percent = parseInt((numVotes / numUsers) * 100)
            requirement['percent'] = "#{percent}%"
            session['requirements'].push requirement

            # Add the requirement object to each estimate
            for estimate in session['estimates']
              if requirement['id'] == estimate['requirementID']
                estimate['requirement'] = requirement

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
