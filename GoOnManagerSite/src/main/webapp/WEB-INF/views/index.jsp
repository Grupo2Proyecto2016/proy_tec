<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="goOnApp">
    <head>
      <meta charset=UTF-8">
      <!-- SCROLLS -->
      <!-- load bootstrap and fontawesome via CDN -->
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" />
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css" />

      <!-- SPELLS -->
      <!-- load angular and angular route via CDN -->
      <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
      <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.25/angular.min.js"></script>
	  <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.25/angular-route.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
    </head>
    <body ng-controller="mainController as main">

        <!-- HEADER AND NAVBAR -->
        <header>
            <nav class="navbar navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="/">Angular Routing Example</a>
                </div>

                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#"><i class="fa fa-home"></i> Inicio</a></li>
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
			<div ng-view></div>
        </div>
    </body>
 </html>
