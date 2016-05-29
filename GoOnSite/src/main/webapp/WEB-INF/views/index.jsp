<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="goOnApp" ng-controller="mainController as main" ng-show="company != null">
    <head>
      <meta charset=UTF-8">
      <!-- STYLES -->
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.6/{{company.css}}/bootstrap.min.css" />
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.6.0/css/font-awesome.css" />
	  <link rel="stylesheet" href="<c:url value='/static/css/custom.css' />" />
	  <link rel="stylesheet" href="<c:url value='/static/css/ui-grid.min.css' />" />
      
      <!-- SCRIPTS -->
      <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
      <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
      <script src="<c:url value='/static/js/jquery/jquery.blockUI.js' />"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-route.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-animate.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-messages.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
	  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCXLMRyM-qhBcFx4Lvv6XxACYvWYY8ey-U&libraries=places" async defer></script>
      <script src="<c:url value='/static/js/ui-grid/ui-grid.min.js' />"></script>
      
      
	  <!--BUS CONTROLLER -->
      <script src="<c:url value='/static/js/controllers/busController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/travelController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/contactController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/employeesController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/registerController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/userPanelController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/branchController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/companyController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/outBranchesController.js' />"></script>
    </head>
    <body>

        <!-- HEADER AND NAVBAR -->
        <header>
            <nav class="navbar navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand"><b>{{company.nombre}}</b></a>
                </div>

                <ul class="nav navbar-nav navbar-right">
                
					<!--PUBLICAS -->
                    <li><a href="#home"><i class="fa fa-home"></i> Inicio</a></li>
                    <li><a href="#packagingCalc"><i class="fa fa-calculator"></i> Calcular Encomienda</a></li>
                    <li><a href="#travels"><i class="fa fa-bus"></i> Pasajes</a></li>
                    
                    <!--ADMINISTRADOR-->
                    <li ng-show="user != null && user.rol_id_rol == 1" class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown">
<!-- 	                        <span class="glyphicon glyphicon-user"></span>  -->
	                        <i class="fa fa-cogs"></i>
	                        <strong>Administrar</strong>
	                        <span class="glyphicon glyphicon-chevron-down"></span>
	                    </a>
	                    <ul class="dropdown-menu admin-menu">
	                    	<li ng-show="user != null && user.rol_id_rol == 1"><a href="#company"><i class="fa fa-cog"></i> Empresa</a></li>
	                    	<li ng-show="user != null && user.rol_id_rol == 1"><a href="#manageTravels"><i class="fa fa-calendar-check-o"></i> Viajes</a></li>
		                    <li ng-show="user != null && user.rol_id_rol == 1"><a href="#lines"><i class="fa fa-map-o"></i> Lineas</a></li>
		                    <li ng-show="user != null && user.rol_id_rol == 1"><a href="#bus"><i class="fa fa-wrench"></i> Vehículos</a></li>
		                    <li ng-show="user != null && user.rol_id_rol == 1"><a href="#employees"><i class="fa fa-users"></i> Personal</a></li>
							<li ng-show="user != null && user.rol_id_rol == 1"><a href="#branches"><i class="fa fa-bars"></i> Sucursales</a></li>
							<li ng-show="user != null && user.rol_id_rol == 1"><a href="#parameters"><i class="fa fa-building"></i> Parámetros</a></li>
	                    </ul>
               		</li>
                    <li ng-show="user == null || user.rol_id_rol != 1"><a href="#outbranches"><i class="fa fa-building"></i> Nuestras Sucursales</a></li>
                    
					<!--DE INGRESO -->
                    <li ng-show="user == null"><a onclick="shorSignInForm()"><i class="fa fa-sign-in" aria-hidden="true"></i> Entrar</a></li>
      				<li ng-show="user == null"><a href="#register"><i class="fa fa-user-plus" aria-hidden="true"></i> Registrarme</a></li>
      				
      				<!-- USUARIO LOGEADO -->
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
	                                        <p class="text-center"><strong>{{user.nombre}} {{user.apellido}}</strong></p>
	                                        <p class="text-left small">{{user.email}}</p>
	                                        <p class="text-left" ng-show="user != null && user.rol_id_rol == 4">
	                                            <a href="#userPanel" class="btn btn-primary btn-block btn-sm">Mi Cuenta</a>
	                                        </p>
	                                        <p class="text-left">
	                                            <a ng-click="signOut()" class="btn btn-danger btn-block btn-sm">Salir</a>
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
        <div id="main" style="min-height: 400px;">
            <!-- angular templating -->
            <!-- this is where content will be injected -->
			<div ng-view ng-animate	 class="viewContainer"></div>
        </div>
    </body>
    <footer>
    	<div class="panel-footer">
    	<p class="text-center"><b>{{company.razonSocial}}  |   Dirección: {{company.direccion}}   |   Tel: {{company.telefono}}</b></p>
    	<p class="text-center">Powered by <b>GoOn Systems</b></p>
    	</div>
    </footer>
    
    <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document" style="width: 340px">
	    <div class="modal-content">
	      <div class="modal-header" style="background-color: green">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">Ingreso al sitio</h3>
	      </div>
	      <div class="modal-body">
 	        <form class="form-signin" id="loginForm" name="lform" role="form" ng-submit="signIn()">
				<input type="text" name="username" id="username" ng-model="loginForm.username" class="form-control" placeholder="nombre de usuario" required="" autofocus="">
				<input type="password" name="password" id="password" ng-model="loginForm.password" class="form-control" placeholder="contraseña" required="">
				
				<div id="loginAlert" class="alert alert-danger" style="display:none">
				  <strong>Error! </strong> El nombre de usuario y la contraseña no coinciden
				</div>
				
				<button class="btn btn-single btn-primary btn-block"">
					Entrar
				</button>
			</form>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header" style="background-color: red">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">Error</h3>
	      </div>
	      <div class="modal-body">
 	        <h3>Ups! Ha ocurrido un error. Intente de nuevo en unos instantes.</h3>
	      </div>
	    </div>
	  </div>
	</div>

	<script>
// 		$(document).ready(function(){
// 			$('div.modal-backdrop.fade.in').remove();
// 		});
		$("#loginModal").on('hidden.bs.modal', function (e) {
			$("#loginAlert").hide();
		});
		
		function shorSignInForm()
		{
			$("#loginModal").modal("toggle");
		}
		
		$('#loginModal').on('shown.bs.modal', function () {
		    $('#username').focus();
		})  
	</script>
 </html>
