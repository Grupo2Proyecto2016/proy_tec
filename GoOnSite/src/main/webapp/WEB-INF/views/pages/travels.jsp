<style>
	.ui-grid { color: #242729; }
	.ui-grid-top-panel-background {
	  background: #f00000;
	  background: -webkit-gradient(linear, left bottom, left top, color-stop(0, #d3741c), color-stop(1, #d3741c));
	  background: -ms-linear-gradient(bottom, #d3741c, #d3741c);
	  background: -moz-linear-gradient(center bottom, #d3741c 0%, #d3741c 100%);
	  background: -o-linear-gradient(#d3741c, #d3741c);
	  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d3741c', endColorstr='#d3741c', GradientType=0);
	}
</style>

<div class="jumbotron text-center">
	<h3>Viajemos juntos</h3>
	
	<p>{{ message }}</p>
</div>


<div id="successAlert" class="row" style="display: none">
	<div class="col-xs-1"></div>
	<div class="col-sm-12">
		<div class="alert alert-success" style="">
		  <button type="button" class="close" ng-click="closeSuccessAlert()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Éxito! </strong> <p id="successMessage"></p>
		</div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div class="row">
	<div class="col-xs-2"></div>
	<div class="col-xs-8">
		<div class="panel panel-default hidden" id="seatsInfo">
			<div class="panel-heading">
				<h4 class="modal-title">Asientos Confirmados</h4>
				<p ng-click="printDiv('forPrint')"> &nbsp;<i class="fa fa-print" aria-hidden="true"></i> Imprimir</p>
			</div>
			<div class="panel-body" id="printSection">	
				<div class="container" id="forPrint">
				    <div class="row" ng-repeat="cs in confirmedSeats track by $index">
				        <div class="col-xs-8">
				            <div class="well well-sm">
				                <div class="row">
				                    <div class="col-sm-6 col-md-4">
				                    	<qrcode version="3" error-correction-level="M" size="150" data="{{cs.numero}}"></qrcode>
				                    </div>
				                    <div class="col-sm-6 col-md-8">
				                    	<h4><i class="fa fa-bus" aria-hidden="true"></i> Línea {{cs.viaje.linea.id_linea}} - Coche: {{cs.viaje.vehiculo.numerov}} - <i class="fa fa-hashtag" aria-hidden="true"> Asiento</i> {{cs.asiento.numero}}</h4>
				                        <small> <i class="fa fa-map-marker"></i>&nbsp;Desde:<cite title="Montevideo">{{cs.parada_sube.descripcion}}</cite></small>
				                        <small> <i class="fa fa-map-marker"></i>&nbsp;Hasta:<cite title="Piriapolis">{{cs.parada_baja.descripcion}}</cite></small>
				                        <p>
				                        	<i class="fa fa-calendar"></i>&nbsp;Fecha: {{cs.viaje.inicio | date:'dd-MM-yyyy'}}
				                            <br />
				                            <i class="fa fa-clock-o"></i>&nbsp;Salida de Terminal:{{cs.viaje.inicio | date:'HH:mm'}} 
				                            <br />
				                        </p>		                        
				                    </div>
				                </div>
				            </div>
				        </div>
				    </div>
				</div>
			</div>
		</div>
	</div>
	<div class="col-xs-2"></div>
</div>	

<div id="divTravelForm">
	<div class="row">
		<div class="col-xs-2"></div>
		<div class="col-xs-8">			
			<form class="form-horizontal" role="form" name="form" ng-submit="searchTravels()">
				<div class="panel panel-default">
					<div class="panel-body">											
						<div class="form-group">
						    <div class="col-sm-12 col-md-6">
							    <h5 class="control-label col-sm-6">
							    	¿Dónde quieres ir?
							    </h5>
							    <div class="col-sm-6">			
									<a style="margin-top: 12px;" class="btn btn-primary" ng-click="showDestinationMap()">
										<i class="fa fa-map-marker fa-lg " ></i> Indicar destino
									</a>					    	
						    	</div>
					    	</div>
						    <div class="col-sm-12 col-md-6">
							    <h5 class="control-label col-sm-6">
							    	¿Dónde subes?
							    </h5>
							    <div class="col-sm-6">			
									<a style="margin-top: 12px;" class="btn btn-primary" ng-click="showOriginMap()" ng-disabled="origenMarkers.length<=0">
										<i class="fa fa-map-marker fa-lg "></i> Indicar origen
									</a>					    	
						    	</div>
					    	</div>
				    	</div>
				    	<div class="form-group">
						    <div class="col-sm-8 col-sm-offset-2">
							    <label class="control-label col-sm-6" for="dateFrom">
							    	Día del viaje:
							    </label>
							    <div class="col-sm-6">
									<input type="date" class="form-control" min="{{minDate | date:'yyyy-MM-dd'}}" max="{{maxDate | date:'yyyy-MM-dd'}}" name="dateFrom" ng-model="travelSearch.dateFrom" required>
							    </div>
					    	</div>
				    	</div>
						<div class="form-group"> 
				    		<div class="col-sm-12">
				      			<button style="float: right" type="submit" class="btn btn-info" ng-disabled="listaIDSeleccionadosOrigin.length == 0 || listaIDSeleccionados.length == 0">Buscar</button>
				    		</div>
				    	</div>
		    		</div>
		    	</div>
			</form>			
		</div>
		<div class="col-xs-1"></div>
	</div>
</div>


<div ng-if="travels.length == 0 && firstSearch"  class="row">
	<div class="col-xs-8 col-xs-offset-2">
		<div class="alert alert-warning" >
		  <h4>Ups! </h4> <h4>No se encontraron viajes en la fecha seleccionada</h4>
		</div>
	</div>
</div>

<div class="row" ng-if="travels.length > 0" style="margin-top: 50px;" id="travelsSearchGrid">
	<div class="col-sm-12 col-md-10 col-md-offset-1">
		<div ui-grid="travelsSearchGrid" class="genericGridHeader"></div>
	</div>
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
				<p>{{travelForm.bus.numerov}}</p>
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
        <p>¿Está seguro que desea comprar el boleto?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" ng-click="buyTicket()">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>
 
 <div class="modal fade" id="destinationModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content" style="width: 650px;">
      <div class="modal-header" style="background-color: cornflowerblue;">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="text-center modal-title">Indicar destino</h4>
      </div>
      <div class="modal-body">
      	<div class="alert alert-success">
	        <p>Marca o busca una dirección del mapa como destino.</p>
	        <p>Los marcadores son solo de referencia e indican las paradas disponibles.</p>
		</div>
		<div class="alert alert-warning alertPasaje" id="warningRadio" style="display: none">
			<strong>&nbsp;&nbsp;Aviso: </strong> No se encontraron paradas en un rango de 1km de su ubicación. 
		</div>
		<input id="destination-pac-input" class="controls" type="text" placeholder="Search Box">
		<div id="destinationMap" style="height: 50%">
		</div>
		<div class="referencias">
			<p>
				<img src="static/images/marker_green.png"> Paradas disponibles
				<img src="static/images/marker_blue.png"> Terminales disponibles
				<img src="static/images/marker_sm.png">Tú ubicación
				&nbsp;&nbsp;<button class="btn btn-sm btn-primary" ng-click="searchOrigins()" ng-disabled="listaIDSeleccionados.length<=0"><i class="fa fa-check-square fa-lg pull-left"></i>Continuar</button>
			</p> 
		</div>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="originModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content" style="width: 650px;height: 570px;">
      <div class="modal-header" style="background-color: cornflowerblue;">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="text-center modal-title">Indicar el origen</h4>
      </div>
      <div class="modal-body">
      	<div class="alert alert-success">
        	<p>Marca o busca una dirección del mapa donde quieres iniciar tu viaje.</p>
			<p>Los marcadores son solo de referencia e indican las paradas disponibles.</p>
		</div>
		<input id="origin-pac-input" class="controls" type="text" placeholder="Buscar..">
		<div id="originMap" style="height: 50%">
		</div>
		<div class="referencias">
			<p>
				<img src="static/images/marker_green.png"> Paradas disponibles
				<img src="static/images/marker_blue.png"> Terminales disponibles
				<img src="static/images/marker_sm.png">Tú ubicación
				&nbsp;&nbsp;<button class="btn btn-sm btn-primary" data-dismiss="modal" ng-disabled="listaIDSeleccionadosOrigin.length<=0"><i class="fa fa-check-square fa-lg pull-left"></i>Continuar</button>				
			</p> 
		</div>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="selectTicketsModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content" style="width: 650px;height: 650px;">
			<div class="modal-header" style="background-color: cornflowerblue;">
			  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			  <h3 class="text-center modal-title">Seleccione sus asientos</h4>
			</div>
			<div class="modal-body">
		    	<div id="seat-map">
					<div class="front-indicator"><i class="fa fa-user" aria-hidden="true"></i>&nbsp;Frente</div>					
				</div>	
				<div class="booking-details">
					<h2>Detalle de Seleccion</h2>
					
					<!--<h3> Asientos Seleccionados (<span id="counter">0</span>):</h3>-->
					<!-- <ul id="selected-seats"></ul> -->
					<div  id="selected-seats">
						<table class="table table-condensed">
							<thead>
								<tr>
									<th>Asiento</th>
									<th>Precio&nbsp;(UYU)</th>								
								</tr>							
							</thead>
							<tbody>
								<tr ng-repeat="s in reservados track by $index">
									<td>{{s.txt}}</td>								
									<td>{{s.price}}</td>	
								</tr>
							</tbody>						
						</table>
					</div>
					Total: <b>$<span id="total">0</span></b>									
					<div id="legend"></div>					
				</div>					   
				<div>
					<p align="right"><button class="btn btn-sm btn-primary" ng-click="confirmSeats()" ng-disabled="reservados.length<=0">Continuar</button></p>
				</div>   	
		    </div>
		</div>
	</div>
</div>

		

<div class="row">
	<div class="col-xs-2"></div>
	<div class="col-xs-8">
		<div class="panel panel-default hidden" id="seatsConfirmForm">
		<div class="panel-heading">
			<h3 class="modal-title">Asientos Seleccionados</h3>
		</div>
		<div class="panel-body">		
			<table class="table table-condensed">
				<thead>
					<tr>
						<th>Asiento</th>
						<th>Precio&nbsp;(UYU)</th>								
					</tr>							
				</thead>
				<tbody>
					<tr ng-repeat="s in reservados track by $index">
						<td>{{s.txt}}</td>								
						<td>{{s.price}}</td>	
					</tr>
				</tbody>						
			</table>
			<div>Total:<b>$<span>{{precio_total}}</span></b></div>
			<form class="form-horizontal" name="pForm" role="form" ng-submit="travelSubmit()" >
<!-- 			ng-submit="reserveTicket()" -->
				<div class="form-group" ng-show="$root.user.rol_id_rol == 2">			    	
			    	<div class="col-sm-6">
				    	<label class="control-label col-sm-5" for="receptorOpt">Receptor registrado?:</label>
					    <div class="col-sm-7">
					    	<select name="receptorOpt" class="form-control" ng-model="rOption" required>
								<option value="1">Si</option>
								<option value="2">No</option>
							</select>	
				    	</div>
			    	</div>
		    	</div>
		    	<div class="form-group" ng-show="$root.user.rol_id_rol == 2">				    
					<div class="col-sm-6">
						<div ng-show="rOption == 2">
							<label class="control-label col-sm-5" for="receptorDoc">CI Receptor:</label>
							<div class="col-sm-7">
								<input type="text" pattern="^[0-9]{6,7}$" title="Ingrese solo números sin el dígito verificador" class="form-control" name="receptorDoc" ng-model="seatsForm.rDoc" ng-required="(rOption == 2) && ($root.user.rol_id_rol == 2)">
						    </div>
					    </div>
					    <div ng-show="rOption == 1">
					    	<label class="control-label col-sm-5" for="receptorUser">Usuario receptor:</label>
							<div class="col-sm-7">
								<input type="text" class="form-control" placeholder="username" name="receptorUser" ng-model="seatsForm.rUser" ng-required="rOption == 1 && $root.user.rol_id_rol == 2" clientexists>
						    </div>
						    <div class="form-group has-error">
								<div ng-messages="pForm.receptorUser.$error" role="alert" class="col-sm-12 text-center">
									<span ng-message="clientexists" class="help-block">El cliente Receptor no existe</span>
							    </div>
							</div>
				    	</div>			
					</div>    
				</div>	
				<div class="form-group"  ng-show="$root.user.rol_id_rol == 2"> 
					<div class="col-sm-6">
				 		<button style="float: right; margin-top: 10px;" class="btn btn-info" ng-click="frmOpt=1">Reservar</button>
					</div>					
					<div class="col-sm-6">
				 		<button style="float: right; margin-top: 10px;" class="btn btn-info" ng-click="frmOpt=2">Comprar</button>
					</div>
				</div>
				<div class="form-group"  ng-show="$root.user.rol_id_rol == 4"> 
					<div class="col-sm-12">
				 		<input type='image' name='paypal_pay' id='paypal_pay' ng-click="frmOpt=3" style="float: right; margin-top: 10px;" src='https://www.paypal.com/es_ES/i/btn/btn_dg_pay_w_paypal.gif' border='0' align='top' alt='Pagar con Paypal'/>
					</div>					
				</div>
			</form>			
		</div>
	</div>
	</div>
	<div class="col-xs-2"></div>
</div>



<div class="modal fade" id="rutaModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content" style="width: 650px;">
      <div class="modal-header" style="background-color: cornflowerblue;">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="text-center modal-title">Recorrido</h3>
      </div>
      <div class="modal-body">
      	<div id="rutaMap" style="height: 50%">
		</div>
		<div class="referencias">
			<p><div id="caja_verde"></div> Recorrido de la linea</p>
			<p><div id="caja_roja"></div> Recorrido de su viaje</p>
		</div>
      </div>
    </div>
  </div>
</div>


