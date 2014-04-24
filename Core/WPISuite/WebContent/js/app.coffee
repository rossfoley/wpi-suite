$ ->
  # Create our Ember app
  App = Ember.Application.create()

  # Routes
  App.Router.map ->
    @resource('sessions', ->
      @resource('session', path: '/sessions/:session_uuid'))

  # Route for the list of sessions
  App.SessionsRoute = Ember.Route.extend
    model: -> 
      $.getJSON 'API/planningpoker/planningpokersession'


  # Route for an individual session
  App.SessionRoute = Ember.Route.extend
    serialize: (session) ->
      session_uuid: session['uuid']
