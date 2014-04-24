$ ->
  # First we need to login to WPI Suite
  authString = 'Basic ' + window.btoa('admin:a')
  $.ajax(
    type: 'POST'
    url: 'API/login'
    headers:
      'Authorization': authString
    async: false
    success: -> console.log('Successfully logged in!')
  )

  # Now we need to set the project that we will be using
  $.ajax(
    type: 'PUT'
    url: 'API/login'
    data: 'test'
    async: false
    success: -> console.log('Successfully set the project!')
  )

  # Now we can create our Ember app
  App = Ember.Application.create()

  App.Router.map( ->
    @resource('sessions', ->
      @resource('session', path: '/sessions/:session_uuid'))
  )

  App.SessionsRoute = Ember.Route.extend(
    model: -> $.getJSON('API/planningpoker/planningpokersession')
  )

  App.SessionRoute = Ember.Route.extend(
    serialize: (session) ->
      session_uuid: session['uuid']
  )

  # App.PlanningPokerSession = DS.Model.extend(
  #   name: DS.attr('string')
  #   endDate: DS.attr('date')
  # )
