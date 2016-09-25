<div id="divTitleMantenimientos" class="jumbotron text-center">
	<h3>Mantenimientos</h3>	
	<p>{{ message }}</p>
	<p><button class="btn btn-sm btn-primary" ng-click="showForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</button></p>
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
							<span aria-hidden="true">×</span>
							</button> 
							<h5 class="modal-title">Datos Básicos del Mantenimiento</h5>
						</div>
					</div>
					<div class="panel-body">											
						<div class="form-group">
						    <div class="col-sm-6">
							    <label class="control-label col-sm-3" for="origen">Vehículo:</label>
							    <div class="col-sm-9">			
									<select name="vehiculo" class="form-control"
										ng-model="mantenimientoForm.vehiculo"
										ng-options="bus as bus.numerov for bus in buses"
										required>
										<option value="">Seleccione un vehículo</option>
									</select>							    	
						    	</div>
					    	</div>
					    	<div class="col-sm-6">
						    	<label class="control-label col-sm-3" for="destino">Taller:</label>
							    <div class="col-sm-9">
							    	<select name="taller" class="form-control"
										ng-model="mantenimientoForm.taller"
										ng-options="taller as taller.nombre for taller in talleres"
										required>
										<option value="">Seleccione un taller</option>
									</select>	
						    	</div>
					    	</div>
				    	</div>	
				    	<div class="form-group">
				    		<div class="col-sm-6">
									<label class="control-label col-sm-3" for="dayFrom">Desde el día:</label>
									<div class="col-sm-9">
										<input type="date" class="form-control" min="{{minDate | date:'yyyy-MM-dd'}}" max="{{mantenimientoForm.dayTo | date:'yyyy-MM-dd'}}" name="mantenimientoForm.dayFrom" ng-model="mantenimientoForm.dayFrom" required>
								    </div>
							</div>
							<div class="col-sm-6" style="padding: 0;">
								<div class="col-sm-12">
								    <label class="control-label col-sm-3" for="dayTo">Hasta el día:</label>
									<div class="col-sm-9">
										<input type="date" class="form-control" name="dayTo" min="{{mantenimientoForm.dayFrom | date:'yyyy-MM-dd'}}" max="{{maxDate | date:'yyyy-MM-dd'}}" ng-model="mantenimientoForm.dayTo" required>
								    </div>
								</div>	
							</div>  
						</div>						
				  		<div class="form-group">
				  			<div class="col-sm-6">
								<label class="control-label col-sm-3" for="dayFrom">Descripción:</label>
								<div class="col-sm-9">
									<textarea rows="5" cols="20" class="form-control" ng-model="mantenimientoForm.descripcion" required></textarea>
							    </div>
							</div> 
				    		<div class="col-sm-6">
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
 
 <div class="row" style="margin-top: 50px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div ui-grid="mantenimientosGrid" ui-grid-pagination class="genericGridHeader"></div>
	</div>
	<div class="col-xs-1"></div>
</div>

	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content" style="width: 400px">
				<div class="modal-header warning">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title" id="myModalLabel">Solicitud de confirmación</h3>
				</div>
				<div class="modal-body">
					<form class="form-signin" id="loginForm" name="costoForm"
						role="form" ng-submit="deleteMantenimiento()">
						<div class="form-group">
							<label class="control-label col-sm-4" for="costo">Costo:</label>
							<div class="col-sm-8">
							
								<input type="text" pattern="[0-9]+" title="Solo se aceptan números" name="costo" class="form-control"
									ng-model="mantenimientoToDelete.costo" required>
							</div>
						</div>
					  	<div class="form-group">
						  <label class="control-label col-sm-4" for="factura">Factura:</label>
						  	<div class="col-sm-8">
						  	
						  		<input type="file" class="form-control" name="factura" accept=".png" fileread="mantenimientoToDelete.factura">
						  		
						 	</div>
						</div>
						<button class="btn btn-single btn-warning btn-block"">
							Confirmar</button>
					</form>
				</div>
			</div>
		</div>
	</div>


<!-- <div class="modal fade" id="mantenimientoDetailsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"> -->
<!-- 	<div class="modal-dialog" role="document"> -->
<!-- 		<div class="modal-content"> -->
<!-- 			<div class="modal-header" style="background-color: green"> -->
<!-- 				<button type="button" class="close" data-dismiss="modal" aria-label="Close"> -->
<!-- 					<span aria-hidden="true">&times;</span> -->
<!-- 				</button> -->
<!-- 				<h3 class="modal-title" style="color: white" id="myModalLabel">Detalles</h3> -->
<!-- 			</div> -->
<!-- 			<div class="modal-body"> -->
<!-- 				<form class="form-horizontal"> -->
<!-- 					<div class="form-group"> -->
<!-- 						<label class="control-label col-sm-4" for="vehiculo">Vehiculo::</label> -->
<!-- 						<div class="col-sm-8"> -->
<!-- 							<p>{{elMantenimiento.vehiculo.id_vehiculo}}</p> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group"> -->
<!-- 						<label class="control-label col-sm-4" for="taller">Taller:</label> -->
<!-- 						<div class="col-sm-8"> -->
<!-- 							<p>{{elMantenimiento.taller.id_taller}}</p> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</form> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->


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