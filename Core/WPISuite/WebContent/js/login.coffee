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