$ ->
  # Create our Ember app
  window.App = Ember.Application.create()

  # Set Ember Data to use fixtures instead of an API
  App.ApplicationAdapter = DS.FixtureAdapter.extend
    queryFixtures: (fixtures, query, type) ->
      fixtures.filter (item) ->
          for prop in query
              if item[prop] != query[prop] then false
          true

  # Create our data store
  App.Store = DS.Store.extend
    adapter: DS.FixtureAdapter

  # Define a raw attr for use with arrays
  App.ApplicationAdapter.registerTransform 'raw',
    deserialize: (serialized) -> serialized
    serialize: (deserialized) -> deserialized

  # Make it easier to define attributes
  attr = DS.attr 

  # Define our session model
  App.Session = DS.Model.extend
    name: attr 'string'
    endDate: attr 'date'
    sessionCreatorName: attr 'string'
    description: attr 'string'
    uuid: attr 'string'
    gameState: attr 'string'
    requirementIDs: attr 'raw'
    isUsingDeck: attr 'boolean'
    estimates: attr 'raw'

  # Define our requirement model
  App.Requirement = DS.Model.extend
    name: attr 'string'
    description: attr 'string'

  # Define our estimate model
  # Remember to include a "requirement" computed property

  # Load in the planning poker sessions
  $.ajax
    dataType: 'json'
    url: 'API/planningpoker/planningpokersession'
    async: no
    success: (data) => 
      for session in data
        session['id'] = session['uuid']
      App.Session.FIXTURES = data

  # Load in the requirements
  $.ajax
    dataType: 'json'
    url: 'API/requirementmanager/requirement'
    async: no
    success: (data) => 
      App.Requirement.FIXTURES = data

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
    model: (params) ->
      @store.find('session', uuid: params.session_uuid)

  # Controller for individual sessions
  App.SessionController = Ember.ObjectController.extend
    requirementByID: (id) ->
      @store.find('requirement', id)

  # Template helpers
  Ember.Handlebars.helper 'requirementName', (requirementID) ->
    App.Requirement.FIXTURES.findBy('id', requirementID)['name']
