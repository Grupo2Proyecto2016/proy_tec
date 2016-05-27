<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCXLMRyM-qhBcFx4Lvv6XxACYvWYY8ey-U&callback=initMap" async defer></script>
<div id="divTitleBranches" class="jumbotron text-center">
	<h2>Sucursales</h2>
	
	<p>{{ message }}</p>
	<p><a class="btn btn-md btn-primary" ng-click="showForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</a></p>
</div>

<div id="successAlert" class="row" style="display: none">
	<div class="col-xs-1"></div>
	<div class="col-xs-5">
		<div class="alert alert-success" style="">
		  <button type="button" class="close" ng-click="closeSuccessAlert()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Exito! </strong> <p id="successMessage"></p>
		</div>
	</div>
	<div class="col-xs-5"></div>
</div>

<div id="divBranchForm" class="hidden">
	<div class="row" style="margin-top: 50px;">
		<div class="col-xs-1"></div>
		<div class="col-xs-5">
			<form class="form-horizontal" role="form" name="form" ng-submit="createBranch()">
				<div class="panel panel-default">				
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h4 class="modal-title">Datos de Sucursal</h4>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
						    <label class="control-label col-sm-4" for="nombre">Nombre:</label>
						    <div class="col-sm-6">
						    	<input type="text" class="form-control" name="nombre" ng-model="branchForm.nombre" required>
					    	</div>
				    	</div>
				  		<div class="form-group">
						    <label class="control-label col-sm-4" for="direccion">Direccion:</label>
						    <div class="col-sm-6">
						    	<input type="text" class="form-control" name="direccion" ng-model="branchForm.direccion" required>
					    	</div>
				    	</div>
				  		<div class="form-group">
						    <label class="control-label col-sm-4" for="telefono">Teléfono:</label>
						    <div class="col-sm-6">
						    	<input type="text" class="form-control" name="telefono" ng-model="branchForm.telefono" required>
					    	</div>
				    	</div>
				  		<div class="form-group">
						    <label class="control-label col-sm-4" for="mail">Correo:</label>
						    <div class="col-sm-6">
						    	<input type="text" class="form-control" name="mail" ng-model="branchForm.mail" required>
					    	</div>
				    	</div>
				    	<div class="form-group"> 
			    		<div class="col-sm-10">
			      			<button style="float: right" type="submit" class="btn btn-info">Crear</button>
			    		</div>
				  	</div>					  			  	
				</div>
			</form>
		</div>		
	</div>
		<div class="col-xs-5">
			<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h4 class="modal-title">Mapa</h4>
					</div>				
				</div>
				<div class="panel-body">
					<div id="map" style="height: 50%" ng-controller="branchController">
					
					</div>
				</div>
			</div>
		</div>
</div>

<script>
var map;

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -34.893819, lng: -56.166349}, 
    zoom: 12
  });
  
  map.addListener('click', function(e) {
	    placeMarkerAndPanTo(e.latLng, map);
	  });
  
  function placeMarkerAndPanTo(latLng, map) {
	  var marker = new google.maps.Marker({
	    position: latLng,
	    map: map
	  });
	  angular.element(document.getElementById('map')).scope().actualizoMarker(latLng.lat(), latLng.lng());
	  map.panTo(latLng);
	}
}
</script>

 