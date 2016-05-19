<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="goOnApp">
    <head>
      <!-- SCROLLS -->
      <!-- load bootstrap and fontawesome via CDN -->
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" />
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css" />

      <!-- SPELLS -->
      <!-- load angular and angular route via CDN -->
      <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
      <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-route.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
    </head>
    <body ng-controller="mainController as main">

        <!-- HEADER AND NAVBAR -->
        <header>
            <nav class="navbar navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#"><b>{{company.nombre}}</b></a>
                </div>

                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#"><i class="fa fa-home"></i> Inicio</a></li>
                    <li><a href="#about"><i class="fa fa-shield"></i> Pasajes</a></li>
                    <li><a href="#contact"><i class="fa fa-comment"></i> Contacto</a></li>
                    <li><a href="#" ng-click="signOut()"><i class="fa fa-sign-out" aria-hidden="true"></i> Salir</a></li>
                    
                    
                </ul>
            </div>
            </nav>
        </header>

        <!-- MAIN CONTENT AND INJECTED VIEWS -->
        <div id="main">
			{{ message }}
            <!-- angular templating -->
            <!-- this is where content will be injected -->
			<div ng-view></div>
        </div>
    </body>
    <footer style="background-color: beige; height: 60px;">
    	<p style="text-align: center; padding-top: 20px;"><b>{{company.razonSocial}}  |   Dirección: {{company.direccion}}   |   Tel: {{company.telefono}}</b></p>
    	<br/>
    	<p style="text-align: center; padding-top: 20px;">Powered by <b>GoOn Systems</b></p>
    </footer>
 </html>
