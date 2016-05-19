<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="goOnApp">
    <head>
      <meta charset=UTF-8">
      <!-- STYLES -->
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" />
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.6.0/css/font-awesome.css" />
	  <link rel="stylesheet" href="<c:url value='/static/css/custom.css' />" />
      
      <!-- SCRIPTS -->
      <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
      <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
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
                    <li><a href="#travels"><i class="fa fa-bus"></i> Pasajes</a></li>
                    <li><a href="#contact"><i class="fa fa-comment"></i> Contacto</a></li>
                    <li><a ng-show="user == null" onclick="shorSignInForm()"><i class="fa fa-sign-in" aria-hidden="true"></i> Entrar</a></li>
      
      				<li ng-show="user != null" class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown">
	                        <span class="glyphicon glyphicon-user"></span> 
	                        <strong>{{user.usrname}}</strong>
	                        <span class="glyphicon glyphicon-chevron-down"></span>
	                    </a>
	                    <ul class="dropdown-menu">
	                        <li>
	                            <div class="navbar-login">
	                                <div class="row" style="padding: 10px;">
	                                    <div class="col-sm-12">
	                                        <p class="text-left"><strong>{{user.nombre}} {{user.apellido}}</strong></p>
	                                        <p class="text-left small">{{user.email}}</p>
	                                        <p class="text-left">
	                                            <a href="#" ng-click="signOut()" class="btn btn-danger btn-block btn-sm">Salir</a>
	                                        </p>
	                                    </div>
	                                </div>
	                            </div>
	                        </li>
	                    </ul>
               		</li>
                
                </ul>
            </div>
            </nav>
        </header>

        <!-- MAIN CONTENT AND INJECTED VIEWS -->
        <div id="main">
            <!-- angular templating -->
            <!-- this is where content will be injected -->
			<div ng-view></div>
        </div>
    </body>
    <footer>
    	<div class="panel-footer">
    	<p style="text-align: center; padding-top: 20px;"><b>{{company.razonSocial}}  |   Dirección: {{company.direccion}}   |   Tel: {{company.telefono}}</b></p>
    	<p style="text-align: center; padding-top: 20px;">Powered by <b>GoOn Systems</b></p>
    	</div>
    </footer>
    
    <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header" style="background-color: green">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" style="color:white" id="myModalLabel">Ingreso al sitio</h3>
	      </div>
	      <div class="modal-body">
 	        <form class="form-signin" id="loginForm" name="lform" role="form" ng-submit="signIn()">
				<input type="text" name="username" id="username" ng-model="loginForm.username" class="form-control" placeholder="nombre de usuario" required="" autofocus="">
				<input type="password" name="password" id="password" ng-model="loginForm.password" class="form-control" placeholder="contraseña" required="">
				
				<div id="loginAlert" class="alert alert-danger" style="display:none">
				  <strong>Error! </strong> El nombre de usuario y la contraseña no coinciden
				</div>
				
				<button class="btn btn-lg btn-primary btn-block"">
					Entrar
				</button>
			</form>
	      </div>
	    </div>
	  </div>
	</div>
	
	<script>
		function shorSignInForm()
		{
			$("#loginModal").modal("toggle");
		}
		
		$('#loginModal').on('shown.bs.modal', function () {
		    $('#username').focus();
		})  
	</script>
 </html>
