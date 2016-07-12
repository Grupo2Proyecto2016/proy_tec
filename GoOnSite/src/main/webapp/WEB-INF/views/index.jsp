<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="goOnApp" ng-controller="mainController as main">
    <head>
      <meta charset=UTF-8">
      <!-- STYLES -->
	  <link rel="stylesheet" href="<c:url value='/static/css/custom.css' />" />
      <link rel="stylesheet" href="<c:url value='/static/css/font-awesome.min.css'/>" />
      
      <!-- SCRIPTS -->
	  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCXLMRyM-qhBcFx4Lvv6XxACYvWYY8ey-U&libraries=places,geometry" async defer></script>
      <script src="<c:url value='/static/js/tokenLogic.js' />"></script>
      <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
      <script src="<c:url value='/static/js/jquery/jquery.blockUI.js' />"></script>
      <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-route.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-animate.js"></script>
	  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-messages.js"></script>
	  
	  <script src="<c:url value='/static/js/qr/qrcode.js' />"></script>
	  <script src="<c:url value='/static/js/qr/angular-qrcode.js' />"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
      
      <!-- PAYPAL -->
      <script src='https://www.paypalobjects.com/js/external/dg.js' type='text/javascript'></script>
      
	  <!--ESTILO CUSTOM -->
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.6/{{company.css}}/bootstrap.min.css" />
      
	  <link rel="stylesheet" href="<c:url value='/static/css/ui-grid.min.css' />" />
      <script src="<c:url value='/static/js/ui-grid/ui-grid.min.js' />"></script>
      <script src="<c:url value='/static/js/seatCharts/jquery.seat-charts.js' />"></script>
      <link rel="stylesheet" href="<c:url value='/static/css/jquery.seat-charts.css' />" />
      
      
	  <!--BUS CONTROLLER -->
      <script src="<c:url value='/static/js/controllers/busController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/tallerController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/travelController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/contactController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/employeesController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/registerController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/userPanelController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/branchController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/companyController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/outBranchesController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/parametersController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/linesController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/terminalController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/manageTravelsController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/mantenimientoController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/packageController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/ticketController.js' />"></script>
      <script src="<c:url value='/static/js/controllers/payPalCheckoutController.js' />"></script>
	  <script src="<c:url value='/static/js/controllers/payPalErrorController.js' />"></script>
    </head>
    <body style="visibility: hidden">

        <!-- HEADER AND NAVBAR -->
        <header>
            <nav class="navbar navbar-default">
            <img class="img-responsive" src="static/images/google_play_icon.png" alt="" style="width: 150px; float: right; padding: 5px; margin-left: 55px;">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand pull-left">
	                    <img id="logo" ng-show="company.logo != null" src="">
                    	<b>{{company.nombre}}</b>
  					</a>
                </div>

                <ul class="nav navbar-nav navbar-right">
                
					<!--PUBLICAS -->
                    <li><a href="#home"><i class="fa fa-home"></i> Inicio</a></li>
                    <li><a ng-click="showPackageCalc()" ng-show="$root.user == null || $root.user.rol_id_rol != 2"><i class="fa fa-calculator"></i> Calcular Encomienda</a></li>
                    <li><a href="#packages" ng-show="$root.user != null && $root.user.rol_id_rol == 2"><i class="fa fa-cubes"></i> Encomiendas</a></li>
                    <li><a href="#tickets" ng-show="$root.user != null && $root.user.rol_id_rol == 2"><i class="fa fa-bus"></i> Boletos</a></li>
                    <li><a href="#travels" ng-show="$root.user != null && $root.user.rol_id_rol == 2"><i class="fa fa-money"></i> Vender Pasaje</a></li>
                    <li><a href="#travels" ng-show="$root.user == null || ($root.user != null && $root.user.rol_id_rol == 4)"><i class="fa fa-bus"></i> Pasajes</a></li>
                    
                    <li ng-show="$root.user == null || user.rol_id_rol != 1"><a href="#outbranches"><i class="fa fa-building"></i> Nuestras Sucursales</a></li>

                    <!--ADMINISTRADOR-->
                    <li ng-show="$root.user != null && ($root.user.rol_id_rol == 1 || $root.user.rol_id_rol == 5)" class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown">
<!-- 	                        <span class="glyphicon glyphicon-user"></span>  -->
	                        <i class="fa fa-cogs"></i>
	                        <strong>Administrar</strong>
	                        <span class="glyphicon glyphicon-chevron-down"></span>
	                    </a>
	                    <ul class="dropdown-menu admin-menu">
	                    	<li ng-show="$root.user != null && $root.user.rol_id_rol == 1"><a href="#company"><i class="fa fa-cog"></i> Empresa</a></li>
							<li ng-show="$root.user != null && $root.user.rol_id_rol == 1"><a href="#parameters"><i class="fa fa-bars"></i> Parámetros</a></li>
							<li ng-show="$root.user != null && $root.user.rol_id_rol == 1"><a href="#branches"><i class="fa fa-building"></i> Sucursales</a></li>
		                    <li ng-show="$root.user != null && $root.user.rol_id_rol == 1"><a href="#terminals"><i class="fa fa-flag-o"></i> Terminales</a></li>
		                    <li ng-show="$root.user != null && $root.user.rol_id_rol == 1"><a href="#employees"><i class="fa fa-users"></i> Personal</a></li>
		                    <li ng-show="$root.user != null && $root.user.rol_id_rol == 1"><a href="#bus"><i class="fa fa-bus"></i> Vehículos</a></li>

		                    <li ng-show="$root.user != null && $root.user.rol_id_rol == 5"><a href="#taller"><i class="fa fa-industry"></i> Talleres</a></li>
		                    <li ng-show="$root.user != null && $root.user.rol_id_rol == 5"><a href="#mantenimiento"><i class="fa fa-wrench"></i> Service</a></li>
		                    <li ng-show="$root.user != null && $root.user.rol_id_rol == 5"><a href="#lines"><i class="fa fa-map-o"></i> Lineas</a></li>
	                    	<li ng-show="$root.user != null && $root.user.rol_id_rol == 5"><a href="#manageTravels"><i class="fa fa-calendar-check-o"></i> Viajes</a></li>
	                    </ul>
               		</li>
                    
					<!--DE INGRESO -->
                    <li ng-show="user == null"><a onclick="shorSignInForm()"><i class="fa fa-sign-in" aria-hidden="true"></i> Entrar</a></li>
      				<li ng-show="user == null"><a href="#register"><i class="fa fa-user-plus" aria-hidden="true"></i> Registrarme</a></li>
      				
      				<!-- USUARIO LOGEADO -->
      				<li ng-show="$root.user != null" class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown">
	                        <span class="glyphicon glyphicon-user"></span> 
	                        <strong>{{$root.user.usrname}}</strong>
	                        <span class="glyphicon glyphicon-chevron-down"></span>
	                    </a>
	                    <ul class="dropdown-menu">
	                        <li>
	                            <div class="navbar-login">
	                                <div class="row" style="padding: 10px;">
	                                    <div class="col-sm-12">
	                                        <p class="text-center"><strong>{{$root.user.nombre}} {{$root.user.apellido}}</strong></p>
	                                        <p class="text-left small">{{$root.user.email}}</p>
	                                        <p class="text-left" ng-show="$root.user != null && $root.user.rol_id_rol == 4">
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
	  <div class="modal-dialog" role="document" style="width: 400px">
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
	
	<div class="modal fade" id="genericErrorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header" style="background-color: red">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">Error</h3>
	      </div>
	      <div class="modal-body">
 	        <h3>Ups! Ha ocurrido un error. Intenta de nuevo en unos instantes.</h3>
	      </div>
	    </div>
	  </div>
	</div>

	<div class="modal fade" id="connectErrorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header warning">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">Mantenimiento</h3>
	      </div>
	      <div class="modal-body">
 	        <h4>Estamos realizando mantenimiento en el sitio. Intenta de nuevo en unos instantes.</h4>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="packageCalcModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document" style="width: 700;">
	    <div class="modal-content">
	      <div class="modal-header calc text-center">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">
	        	<i class="fa fa-calculator" style="float: left;"></i>Cálculo de encomienda
        	</h3>
	      </div>
	      <div class="modal-body">
 	        <form class="form-horizontal" name="calcForm" role="form" ng-submit="calcPackage()"">
				<div class="form-group">
			    	<div class="col-sm-6">
				    	<label class="control-label col-sm-3" for="destino">Destino:</label>
					    <div class="col-sm-9">
					    	<select name="destino" class="form-control" ng-model="calcForm.destino" ng-change="updateOrigins()" ng-options="terminal as terminal.descripcion for terminal in destinationTerminals" required>
								<option value="">Seleccione una sucursal</option>
							</select>	
				    	</div>
			    	</div>
			    	<div class="col-sm-6">
						<label class="control-label col-sm-7" for="packageHeigth">Alto (cm):</label>
						<div class="col-sm-5">
							<input type="number" min="5" step="5" class="form-control" name="packageHeigth" ng-model="calcForm.alto">
					    </div>				
					</div>	
		    	</div>				
		    	<div class="form-group">
				    <div class="col-sm-6">
					    <label class="control-label col-sm-3" for="origen">Origen:</label>
					    <div class="col-sm-9">			
							<select name="origen" class="form-control" ng-disabled="calcForm.destino == null || calcForm.destino == undefined" ng-model="calcForm.origen" ng-options="terminal as terminal.descripcion for terminal in originTerminals" required>
								<option value="">Seleccione una sucursal</option>
							</select>							    	
				    	</div>
			    	</div>
					<div class="col-sm-6">
						<label class="control-label col-sm-7" for="packageBaseLenght">Largo de base (cm):</label>
						<div class="col-sm-5">
							<input type="number" min="5" step="5" class="form-control" name="packageBaseLenght" ng-model="calcForm.largo">
					    </div>				
					</div>	    
				</div>		
				<div class="form-group">
					<div class="col-sm-6">
						<label class="control-label col-sm-3" for="packageWeight">Peso (kg):</label>
						<div class="col-sm-9">
							<input type="number" min="0.1" step="0.1" title="Solo se aceptan números" class="form-control" name="packageWeight" ng-model="calcForm.peso">
					    </div>
					</div>
					<div class="col-sm-6">
						<label class="control-label col-sm-7" for="packageBaseWidth">Ancho de base (cm):</label>
						<div class="col-sm-5">
							<input type="number" min="5" step="5" class="form-control" name="packageBaseWidth" ng-model="calcForm.ancho">
					    </div>				
					</div>
				</div>
				<div ng-show="calc_error != null" class="alert alert-danger text-center col-sm-12">
				  <strong>Error! </strong>{{calc_error}}
				</div>
				
				<div class="form-group"> 
		    		<div class="col-sm-6">
		    			<h4 style="margin-left: 15px; color: green;">Precio: $ {{packagePrice}}</h4>
		    		</div>
		    		<div class="col-sm-6">
		      			<button style="float: right; margin-top: 10px;" type="submit" class="btn btn-info">Calcular</button>
		    		</div>
			    </div>
			</form>
	      </div>
	    </div>
	  </div>
	</div>

	<script>
		$(window).on("load", function(){
			$('body').css('visibility', 'visible');
			$(window).resize();
		});
		
		$("#loginModal").on('hidden.bs.modal', function (e) {
			$("#loginAlert").hide();
		});
		
		function shorSignInForm()
		{
			$("#loginAlert").hide()
			$("#loginModal").modal("toggle");
		}
		
		$('#loginModal').on('shown.bs.modal', function () {
		    $('#username').focus();
		})  
	</script>
 </html>
