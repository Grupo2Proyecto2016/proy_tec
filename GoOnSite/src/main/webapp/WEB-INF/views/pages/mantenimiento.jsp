<div id="divTitleMantenimientos" class="jumbotron text-center">
	<h3>Mantenimientos</h3>	
	<p>{{ message }}</p>
	<p><button class="btn btn-sm btn-primary" ng-click="showForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</button></p>
</div>

<div id="successAlert" class="row" style="display: none">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div class="alert alert-success" style="">
		  <button type="button" class="close" ng-click="closeSuccessAlert()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Exito! </strong> <p id="successMessage"></p>
		</div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div id="divMantenimientoForm" class="hidden">
	<div class="row">
		<div class="col-xs-1"></div>
		<div class="col-xs-10">			
			<form class="form-horizontal" role="form" name="form" ng-submit="createMantenimiento()">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
							<span aria-hidden="true">�</span>
							</button> 
							<h5 class="modal-title">Datos B�sicos</h5>
						</div>
					</div>
					<div class="panel-body">											
						<div class="form-group">
						    <div class="col-sm-6">
							    <label class="control-label col-sm-3" for="origen">Veh�culo:</label>
							    <div class="col-sm-9">			
									<select name="origen" class="form-control"
										ng-model="mantenimientoForm.vehiculo"
										ng-options="vehiculo.id_vehiculo as vehiculo.matricula for vehiculo in vehiculos"
										ng-change="updateVehiculo()"
										required>
										<option value="">Seleccione un vehiculo</option>
									</select>							    	
						    	</div>
					    	</div>
					    	<div class="col-sm-6">
						    	<label class="control-label col-sm-3" for="destino">Taller:</label>
							    <div class="col-sm-9">
							    	<select name="destino" class="form-control"
										ng-model="mantenimientoForm.taller"
										ng-options="taller.id_taller as taller.nombre for taller in tallers"
										ng-change="updateTaller()"
										required>
										<option value="">Seleccione una taller</option>
									</select>	
						    	</div>
					    	</div>
				    	</div>								
			    		<div class="col-sm-12">
			      			<button style="float: right" type="submit" class="btn btn-info">Crear</button>
			    		</div>
				    </div>				  
		    	</div>		    	
			</form>			
		</div>
		<div class="col-xs-1"></div>
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