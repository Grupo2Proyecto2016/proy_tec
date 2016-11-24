<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <!-- This file has been downloaded from Bootsnipp.com. Enjoy! -->
    <title>GoOn Manager</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/static/css/login.css' />" rel="stylesheet"></link>
    <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
    <link href="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
</head>
<body>
<div id="fullscreen_bg" class="fullscreen_bg"/>
<div class="alert alert-danger" style="display:none">
  <strong>Error!</strong>El nombre de usuario y la contraseña no coinciden
</div>
<div class="container">

	<form class="form-signin" method="post" id="loginForm">
		<h1 class="form-signin-heading text-muted">Ingreso al sitio</h1>
		<input type="text" name="username" id="username" class="form-control" placeholder="nombre de usuario" required="" autofocus="">
		<input type="password" name="password" id="password" class="form-control" placeholder="contraseña" required="">
		<input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
		<button class="btn btn-lg btn-primary btn-block" type="submit">
			Entrar
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
            url:  AppName + "auth",
            type: "POST",
            data: JSON.stringify(loginData),
            headers: {"AppId": "MainAPP"},
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                setJwtToken(data.token);
                
                $.ajax({
                    url: ".",
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
                	$("#username").val("");
                	$("#password").val("");
                    $(".alert").show();
                    $("#username").focus();
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
