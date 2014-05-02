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
    	document.getElementById("badPassAlert").style.display = 'block';
        document.getElementById("badPassAlert").innerHTML = "ERROR: Invalid Details Provided!";
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
        url += "/WPISuite/planningpoker.html?username=" + ($('#username').val());
        return window.location = url;
      }
    });
    return false;
  });
});
