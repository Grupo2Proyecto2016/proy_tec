<div class="jumbotron text-center">
	<h3>Viajemos juntos</h3>
	
	<p>{{ message }}</p>
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

<div id="divTravelForm">
	<div class="row">
		<div class="col-xs-2"></div>
		<div class="col-xs-8">			
			<form class="form-horizontal" role="form" name="form" ng-submit="searchTravels()">
				<div class="panel panel-default">
					<div class="panel-body">											
						<div class="form-group">
						    <div class="col-sm-6">
							    <h5 class="control-label col-sm-6">
							    	¿Dónde quieres ir?
							    </h5>
							    <div class="col-sm-6">			
									<a style="margin-top: 12px;" class="btn btn-primary" ng-click="showDestinationMap()">
										<i class="fa fa-map-marker fa-lg " style="margin-right: 5px;"></i> Indicar destino
									</a>					    	
						    	</div>
					    	</div>
						    <div class="col-sm-6">
							    <h5 class="control-label col-sm-6">
							    	¿Dónde subes?
							    </h5>
							    <div class="col-sm-6">			
									<a style="margin-top: 12px;" class="btn btn-primary" ng-click="showOriginMap()" ng-disabled="origenMarkers.length<=0">
										<i class="fa fa-map-marker fa-lg " style="margin-right: 5px;"></i> Indicar origen
									</a>					    	
						    	</div>
					    	</div>
				    	</div>
				    	<div class="form-group">
						    <div class="col-sm-6">
							    <label class="control-label col-sm-6" for="dateFrom">
							    	Día del viaje:
							    </label>
							    <div class="col-sm-6">
									<input type="date" class="form-control" min="{{minDate | date:'yyyy-MM-dd'}}" max="{{maxDate | date:'yyyy-MM-dd'}}" name="dateFrom" ng-model="travelSearch.dateFrom" required>
							    </div>
					    	</div>
					    	<div class="col-sm-6">
							    <label class="control-label col-sm-6" for="ticketsCount">
							    	Cantidad de asientos:
							    </label>
							    <div class="col-sm-6">
									<input type="number" class="form-control" min="0" max="10" name="ticketsCount" ng-model="travelSearch.ticketsCount">
							    </div>
					    	</div>
				    	</div>
						<div class="form-group"> 
				    		<div class="col-sm-12">
				      			<button style="float: right" type="submit" class="btn btn-info">Buscar</button>
				    		</div>
				    	</div>
		    		</div>
		    	</div>
			</form>			
		</div>
		<div class="col-xs-1"></div>
	</div>
</div>

<div class="row" style="margin-top: 50px;" id="travelsSearchGrid">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div ui-grid="travelsSearchGrid" ui-grid-pagination class="genericGridHeader"></div>
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
			</p> 
		</div>
      </div>
    </div>
  </div>
</div>
	
 <script type="text/javascript">
     
//      $('#lineDetailsLink').popover({
//    	    html: true,
//    	    title: function () {
//    	        return $('#lineDetailHeader').html();
//    	    },
//    	    content: function () {
//    	        return $('#lineDetailContent').html();
//    	    }
//    	});
     
//      $('#busDetailsLink').popover({
//     	    html: true,
//     	    title: function () {
//     	        return $('#busDetailHeader').html();
//     	    },
//     	    content: function () {
//     	        return $('#busDetailContent').html();
//     	    }
//    	});
     
//      $('#driverDetailsLink').popover({
//     	    html: true,
//     	    title: function () {
//     	        return $('#driverDetailHeader').html();
//     	    },
//     	    content: function () {
//     	        return $('#driverDetailContent').html();
//     	    }
//    	});
 </script>