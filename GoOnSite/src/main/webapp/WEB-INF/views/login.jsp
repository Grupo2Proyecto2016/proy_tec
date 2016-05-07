<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- This file has been downloaded from Bootsnipp.com. Enjoy! -->
    <title>GoOn</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/static/css/login.css' />" rel="stylesheet"></link>
    <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
    <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
</head>
<body>
<div id="fullscreen_bg" class="fullscreen_bg"/>
<div class="alert alert-danger" style="display:none">
  <strong>Error!</strong> Username and Password do not match.
</div>
<div class="container">

	<form class="form-signin" method="post" id="loginForm">
		<h1 class="form-signin-heading text-muted">Sign In</h1>
		<input type="text" name="username" class="form-control" placeholder="Email address" required="" autofocus="">
		<input type="password" name="password" class="form-control" placeholder="Password" required="">
		<input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
		<button class="btn btn-lg btn-primary btn-block" type="submit">
			Sign In
		</button>
	</form>

</div>
<script type="text/javascript">

	$("#loginForm").submit(function (event) {
	    event.preventDefault();
	
	    var $form = $(this);
	    var formData = {
	        username: $form.find('input[name="username"]').val(),
	        password: $form.find('input[name="password"]').val()
	    };
	
	    doLogin(formData);
	});
		
	function doLogin(loginData) {
        $.ajax({
            url:  AppName + urlTenant + "/auth",
            type: "POST",
            data: JSON.stringify(loginData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                var tenant = window.location.pathname.split('/').pop();
            	setJwtToken(data.token, tenant);
                
                $.ajax({
                    url: window.location.href,
                    type: "GET",
                    dataType: "html",
                    headers: createAuthorizationTokenHeader(),
                    success: function (data) {
                    	var newDoc = document.open("text/html", "replace");
                        newDoc.write(data);
                        newDoc.close();
                    }
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401) 
                {
                    $(".alert").show();
                } 
                else 
                {
                    throw new Error("an unexpected error occured: " + errorThrown);
                }
            }
        });
    }
</script>
</body>
</html>
