<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="<c:url value='/static/css/bootstrap-timepicker.min.css' />" />
<script src="<c:url value='/static/js/time-picker/bootstrap-timepicker.min.js' />"></script>


<div class="jumbotron text-center">
	<h3>Viajes</h3>
	<p>{{ message }}</p>
	<p><button class="btn btn-sm btn-primary" ng-click="showForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</button></p>
</div>

<div id="successAlert" class="row" style="display: none">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div class="alert alert-success" style="">
		  <button type="button" class="close" ng-click="closeSuccessAlert()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Éxito! </strong> <p id="successMessage"></p>
		</div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div id="divTravelForm" class="hidden">
	<div class="row">
		<div class="col-xs-1"></div>
		<div class="col-xs-10">			
			<form class="form-horizontal" role="form" name="form" ng-submit="createTravels()">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h5 class="modal-title">Datos del Viaje</h5>
						</div>
					</div>
					<div class="panel-body">											
						<div class="form-group">
						    <div class="col-sm-6">
							    <label class="control-label col-sm-3" for="origen">
							    	<a ng-show="travelForm.line != null" id="lineDetailsLink" class="trigger"><i class="fa fa-info-circle"></i></a>
							    	Línea:
							    </label>
							    <div class="col-sm-9">			
									<select name="origen" class="form-control"
										ng-model="travelForm.line"
										ng-options="line as line.numero for line in lines"
										required>
										<option value="">Seleccione una línea</option>
									</select>							    	
						    	</div>
					    	</div>
					    	<div class="col-sm-6">
						    	<label class="control-label col-sm-3" for="destino">
						    		<a ng-show="travelForm.driver != null" id="driverDetailsLink" class="trigger"><i class="fa fa-info-circle"></i></a>
						    		Chofer:
					    		</label>
							    <div class="col-sm-9">
							    	<select name="conductor" class="form-control"
										ng-model="travelForm.driver"
										ng-options="driver as driver.usrname for driver in drivers"
										required>
										<option value="">Seleccione un chofer</option>
									</select>	
						    	</div>
					    	</div>
				    	</div>				
				    	<div class="form-group">
				    		<div class="col-sm-6">
						    	<label class="control-label col-sm-3" for="vehiculo">
						    		<a ng-show="travelForm.bus != null" id="busDetailsLink" class="trigger"><i class="fa fa-info-circle"></i></a>
						    		Coche:
					    		</label>
							    <div class="col-sm-9">
							    	<select name="vehiculo" class="form-control"
										ng-model="travelForm.bus"
										ng-options="bus as bus.id_vehiculo for bus in buses"
										required>
										<option value="">Seleccione un coche</option>
									</select>	
						    	</div>
					    	</div>
					    	<div class="col-sm-6">
								<label class="control-label col-sm-3" for="costo_fijo">Hora de salida:</label>
								<div style="padding-left: 15px; padding-right: 15px;" class="col-sm-9 input-group bootstrap-timepicker timepicker">
            						<input id="timepicker" ng-model="travelForm.time" type="text" class="form-control input-small" required>
            						<span class="input-group-addon"><i class="glyphicon glyphicon-time"></i></span>
           						</div>				    
							</div>    
						</div>	
						<div class="form-group">
							<div class="col-sm-6">
					    		<label class="control-label col-sm-3" for="viaja_parado">Todos los días:</label>
								<div class="col-sm-9" style="margin-top: 10px">
							    	<div class="col-sm-4">
								    	<input type="checkbox" name="monday" ng-model="travelForm.monday">
							    		<label for="monday">Lun</label>
							    	</div>
								    <div class="col-sm-4">
									    <input type="checkbox" name="tuesday" ng-model="travelForm.tuesday">
									    <label for="tuesday">Mar</label>
							    	</div>
								    <div class="col-sm-4">
									    <input type="checkbox" name="wednesday" ng-model="travelForm.wednesday">
									    <label for="wednesday">Mier</label>
  							    	</div>
		  							<div class="col-sm-4">
										<input type="checkbox" name="thursday" ng-model="travelForm.thursday">
										<label for="thursday">Jue</label>
								    </div>
								    <div class="col-sm-4">
									    <input type="checkbox" name="friday" ng-model="travelForm.friday">
									    <label for="friday">Vie</label>
								    </div>
								    <div class="col-sm-4">
							      		<input type="checkbox" name="saturday" ng-model="travelForm.saturday">
							      		<label for="saturday">Sab</label>
								    </div>
								    <div class="col-sm-4">
							        	<input type="checkbox" name="sunday" ng-model="travelForm.sunday">
							      		<label for="sunday">Dom</label>
								    </div>
								</div>						
							</div>
							<div class="col-sm-6" style="padding: 0;">
								<div class="col-sm-12">
									<label class="control-label col-sm-3" for="travelForm.dayFrom">Desde el día:</label>
									<div class="col-sm-9">
										<input type="date" class="form-control" min="{{minDate | date:'yyyy-MM-dd'}}" max="{{travelForm.dayTo | date:'yyyy-MM-dd'}}" name="travelForm.dayFrom" ng-model="travelForm.dayFrom" required>
								    </div>
								</div>
								<div class="col-sm-12">
									<label class="control-label col-sm-3" for="dayTo">Hasta el día:</label>
									<div class="col-sm-9">
										<input type="date" class="form-control" name="dayTo" min="{{travelForm.dayFrom | date:'yyyy-MM-dd'}}" max="{{maxDate | date:'yyyy-MM-dd'}}" ng-model="travelForm.dayTo" required>
								    </div>				
								</div>		
							</div>
						</div>
						<div class="form-group has-error">
							<div role="alert" class="col-sm-6 text-center">
								<span class="help-block" 
									ng-show="!travelForm.monday && !travelForm.tuesday && !travelForm.wednesday && !travelForm.thursday && !travelForm.friday && !travelForm.saturday && !travelForm.sunday">
									Debe seleccionar al menos un día de la semana.
								</span>
						    </div>
						    <div role="alert" class="col-sm-6 text-center">
								<span class="help-block">Solo se permite agregar viajes para 30 días a partir del día de mañana.</span>
						    </div>
					  	</div>
						<div class="form-group"> 
				    		<div class="col-sm-12">
				      			<button style="float: right" type="submit" class="btn btn-info">Crear</button>
				    		</div>
				    	</div>
		    		</div>
		    	</div>
			</form>			
		</div>
		<div class="col-xs-1"></div>
	</div>
</div>

<div class="row" style="margin-top: 50px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div ui-grid="travelsGrid" ui-grid-pagination class="genericGridHeader"></div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header" style="background-color: lightcoral">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" id="myModalLabel">Error</h3>
      </div>
      <div class="modal-body">
      	{{ error_message }}        
      </div>
    </div>
  </div>
 </div>
 
 <div id="lineDetailHeader" class="head hide">Detalles de línea</div>
 <div id="lineDetailContent" class="content hide">
     <div style="width: 245px;">
	    <div class="form-group row">
			<label class="control-label col-sm-3" for="name">Origen:</label>
			<div class="col-sm-9">
				<p>{{travelForm.line.origen.direccion}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-3" for="name">Destino:</label>
			<div class="col-sm-9">
				<p>{{travelForm.line.destino.direccion}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-3" for="mail">Duración:</label>
			<div class="col-sm-9" style="word-wrap: break-word;">
				<p>{{travelForm.line.tiempo_estimado}} minutos</p>
			</div>
		</div>
 	</div>
 </div>
 
 <div id="busDetailHeader" class="head hide">Detalles del vehículo</div>
 <div id="busDetailContent" class="content hide">
 	<div style="width: 180px;">
	    <div class="form-group row">
			<label class="control-label col-sm-9" for="name">Número:</label>
			<div class="col-sm-3">
				<p>{{travelForm.bus.id_vehiculo}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-9" for="name">Asientos:</label>
			<div class="col-sm-3">
				<p>{{travelForm.bus.cantAsientos}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-9" for="mail">Pasajeros parados:</label>
			<div class="col-sm-3" style="word-wrap: break-word;">
				<p>{{travelForm.bus.cantParados}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-9" for="address">Asientos accesibles:</label>
			<div class="col-sm-3">
				<p>{{travelForm.bus.cantAccesibles}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-9" for="address">Espacio de encomiendas:</label>
			<div class="col-sm-3">
				<p>{{travelForm.bus.cantEncomiendas}}</p>
			</div>
		</div>
 	</div>
 </div>
 
 <div id="driverDetailHeader" class="head hide">Detalles del chofer</div>
 <div id="driverDetailContent" class="content hide">
 	<div style="width: 245px;">
	 	<div class="form-group row">
			<label class="control-label col-sm-4" for="name">Nombre:</label>
			<div class="col-sm-8">
				<p>{{travelForm.driver.nombre}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-4" for="name">Apellido:</label>
			<div class="col-sm-8">
				<p>{{travelForm.driver.apellido}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-4" for="mail">Correo:</label>
			<div class="col-sm-8" style="word-wrap: break-word;">
				<p>{{travelForm.driver.email}}</p>
			</div>
		</div>
		<div class="form-group row">
			<label class="control-label col-sm-4" for="address">Dirección:</label>
			<div class="col-sm-8">
				<p>{{travelForm.driver.direccion}}</p>
			</div>
		</div>
 	</div>
 </div>
 
 <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title">Solicitud de confirmación</h4>
      </div>
      <div class="modal-body">
        <p>¿Está seguro que desea eliminar esta viaje?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" ng-click="deleteTravel()">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>
 
 <script type="text/javascript">
     $('#timepicker').timepicker();
     
     $('#lineDetailsLink').popover({
   	    html: true,
   	    title: function () {
   	        return $('#lineDetailHeader').html();
   	    },
   	    content: function () {
   	        return $('#lineDetailContent').html();
   	    }
   	});
     
     $('#busDetailsLink').popover({
    	    html: true,
    	    title: function () {
    	        return $('#busDetailHeader').html();
    	    },
    	    content: function () {
    	        return $('#busDetailContent').html();
    	    }
   	});
     
     $('#driverDetailsLink').popover({
    	    html: true,
    	    title: function () {
    	        return $('#driverDetailHeader').html();
    	    },
    	    content: function () {
    	        return $('#driverDetailContent').html();
    	    }
   	});
 </script>