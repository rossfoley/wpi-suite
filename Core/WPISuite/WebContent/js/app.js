// Generated by CoffeeScript 1.7.1
(function() {
  $(function() {
    var attr;
    window.App = Ember.Application.create();
    App.ApplicationAdapter = DS.FixtureAdapter.extend({
      serializer: 'App.SessionSerializer'
    });
    App.Store = DS.Store.extend({
      adapter: DS.FixtureAdapter
    });
    App.ApplicationAdapter.registerTransform('raw', {
      deserialize: function(serialized) {
        return serialized;
      },
      serialize: function(deserialized) {
        return deserialized;
      }
    });
    attr = DS.attr;
    App.Session = DS.Model.extend({
      name: attr('string'),
      endDate: attr('date'),
      sessionCreatorName: attr('string'),
      description: attr('string'),
      uuid: attr('string'),
      gameState: attr('string'),
      requirementIDs: attr('raw'),
      isUsingDeck: attr('boolean')
    });
    $.ajax({
      dataType: 'json',
      url: 'API/planningpoker/planningpokersession',
      async: false,
      success: (function(_this) {
        return function(data) {
          var session, _i, _len;
          for (_i = 0, _len = data.length; _i < _len; _i++) {
            session = data[_i];
            session['id'] = session['uuid'];
          }
          return App.Session.FIXTURES = data;
        };
      })(this)
    });
    App.Router.map(function() {
      return this.resource('sessions', {
        path: '/'
      }, function() {
        return this.resource('session', {
          path: '/:session_uuid'
        });
      });
    });
    App.SessionsRoute = Ember.Route.extend({
      model: function() {
        return this.store.find('session');
      }
    });
    return App.SessionRoute = Ember.Route.extend({
      serialize: function(session) {
        return {
          session_uuid: session.get('uuid')
        };
      }
    });
  });

}).call(this);