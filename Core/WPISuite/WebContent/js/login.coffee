$ ->
  $('.form-signin').on 'submit', ->
    # First we need to login to WPI Suite
    auth = "#{$('#username').val()}:#{$('#password').val()}"
    authString = 'Basic ' + window.btoa(auth)
    $.ajax
      type: 'POST'
      url: 'API/login'
      headers:
        'Authorization': authString
      async: no
      error: ->
      	$('#badPassAlert').css display: 'block'
      	$('#badPassAlert').html 'Error: Invalid Details Provided!'

    # Now we need to set the project that we will be using
    $.ajax
      type: 'PUT'
      url: 'API/login'
      data: $('#project').val()
      async: no
      error: -> console.log 'Error setting project!'
      success: ->
        url = "#{window.location.protocol}//#{window.location.hostname}"
        url += ":#{window.location.port}" if window.location.port
        url += "/WPISuite/planningpoker.html?username=#{$('#username').val()}"
        queryString = window.location.search.split('=')
        if queryString.length > 1
          url += "&session=#{queryString[1]}"
        window.location = url

    off