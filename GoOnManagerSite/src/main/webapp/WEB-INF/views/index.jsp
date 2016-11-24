<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="goOnApp">
    <head>
      <meta charset=UTF-8">
      <title>GoOn Manager</title>
      <!-- CSS -->
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" />
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css" />
      <link rel="stylesheet" href="<c:url value='/static/css/ui-grid.min.css' />" />
      <link rel="stylesheet" href="<c:url value='/static/css/custom.css' />" />
      
      <!-- SCRIPTS -->
      <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
      <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
      <script src="<c:url value='/static/js/jquery/jquery.blockUI.js' />"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-route.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-animate.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-messages.js"></script>
	  
      <script src="<c:url value='/static/js/app.js' />"></script>
	  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCXLMRyM-qhBcFx4Lvv6XxACYvWYY8ey-U&libraries=places&v=3.24" async defer></script>
	  <script src="<c:url value='/static/js/ui-grid/ui-grid.min.js' />"></script>
	
            
    </head>
    <body ng-controller="mainController as main">

        <!-- HEADER AND NAVBAR -->
        <header>
            <nav class="navbar navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#"><b>GoOn Manager</b></a>
                </div>

                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#companies"><i class="fa fa-list"></i> Empresas</a></li>
                    <li><a href="#newCompany"><i class="fa fa-plus-circle"></i> Registrar Empresa</a></li>
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
			<div ng-view  class="viewContainer"></div>
        </div>
    </body>
    <footer>
    	<div class="panel-footer">
	    	<p class="text-center">Powered by <b>GoOn Systems</b></p>
    	</div>
    </footer>
 </html>
