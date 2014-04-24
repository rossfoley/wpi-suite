$ ->
  # Create our Ember app
  window.App = Ember.Application.create()

  # Set Ember Data to use fixtures instead of an API
  App.ApplicationAdapter = DS.FixtureAdapter.extend
    serializer: 'App.SessionSerializer'

  # Create our data store
  App.Store = DS.Store.extend
    adapter: DS.FixtureAdapter

  # Define a raw attr for use with arrays
  App.ApplicationAdapter.registerTransform 'raw',
    deserialize: (serialized) -> serialized
    serialize: (deserialized) -> deserialized
  attr = DS.attr 

  # Define our models
  App.Session = DS.Model.extend
    name: attr 'string'
    endDate: attr 'date'
    sessionCreatorName: attr 'string'
    description: 'string'
    uuid: attr 'string'
    gameState: attr 'string'
    requirementIDs: attr 'raw'
    isUsingDeck: attr 'boolean'

  # Load in the planning poker sessions
  $.ajax
    dataType: 'json'
    url: 'API/planningpoker/planningpokersession'
    async: no
    success: (data) => 
      for session in data
        session['id'] = session['uuid']
      App.Session.FIXTURES = data

  # Routes
  App.Router.map ->
    @resource 'sessions', path: '/', ->
      @resource 'session', path: '/:session_uuid'

  # Route for the list of sessions
  App.SessionsRoute = Ember.Route.extend
    model: -> @store.find('session')

  # Route for an individual session
  App.SessionRoute = Ember.Route.extend
    serialize: (session) ->
      session_uuid: session.get('uuid')
    # Add a model method eventually
