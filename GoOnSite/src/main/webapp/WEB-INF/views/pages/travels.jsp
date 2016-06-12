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
        <p>¿Está seguro que desea comprar el boleto?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" ng-click="buyTicket()">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
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