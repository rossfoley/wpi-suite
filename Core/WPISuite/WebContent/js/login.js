// Generated by CoffeeScript 1.7.1
(function() {
  $(function() {
    return $('.form-signin').on('submit', function() {
      var auth, authString;
      auth = "" + ($('#username').val()) + ":" + ($('#password').val());
      authString = 'Basic ' + window.btoa(auth);
      $.ajax({
        type: 'POST',
        url: 'API/login',
        headers: {
          'Authorization': authString
        },
        async: false,
        error: function() {
          return console.log('Error logging in!');
        }
      });
      $.ajax({
        type: 'PUT',
        url: 'API/login',
        data: $('#project').val(),
        async: false,
        error: function() {
          return console.log('Error setting project!');
        },
        success: function() {
          var url;
          url = "" + window.location.protocol + "//" + window.location.hostname;
          if (window.location.port) {
            url += ":" + window.location.port;
          }
          url += '/WPISuite/planningpoker.html';
          return window.location = url;
        }
      });
      return false;
    });
  });

}).call(this);
