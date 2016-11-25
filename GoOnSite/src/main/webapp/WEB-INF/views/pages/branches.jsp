<div>
<div id="divTitleBranches" class="jumbotron text-center">
	<h3>Sucursales</h3>
	
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

<div id="divBranchForm" class="hidden">
	<div class="row">
		<div class="col-xs-1"></div>
		<div class="col-xs-5">
			<form class="form-horizontal" role="form" name="form" ng-submit="createBranch()">
				<div class="panel panel-default">				
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h5 class="modal-title">Datos de Sucursal</h5>
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
							<label class="control-label col-sm-4" for="addTerminal">Agregar terminal:</label>
							<div class="col-sm-6">
								<div class="checkbox">
									<label> <input type="checkbox" name="addTerminal" ng-model="branchForm.addTerminal">
									</label>
								</div>
							</div>
						</div>
				  		<div class="form-group">
						    <label class="control-label col-sm-4" for="telefono">Teléfono:</label>
						    <div class="col-sm-6">
						    	<input type="text" pattern="[0-9]+" title="Solo se aceptan números" class="form-control" name="telefono" ng-model="branchForm.telefono" required>
					    	</div>
				    	</div>
				  		<div class="form-group">
						    <label class="control-label col-sm-4" for="mail">Correo:</label>
						    <div class="col-sm-6">
						    	<input type="email" class="form-control" name="mail" ng-model="branchForm.mail" required>
					    	</div>
				    	</div>
				    	<div class="form-group"> 
			    		<div class="col-sm-10">
			      			<button style="float: right" type="submit" class="btn btn-info">Crear</button>
			    		</div>
				  	</div>					  			  	
				</div>
				</div>
			</form>
		</div>		
		<div class="col-xs-5">
			<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h5 class="modal-title">Mapa</h5>
					</div>				
				</div>
				<div class="panel-body">
					<input id="pac-input" class="controls" type="text" placeholder="Search Box">
					<div id="map" style="height: 50%">
					
 					</div>
				</div>
			</div>
		</div>
	</div>		
</div>
 <div class="row" style="margin-top: 50px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div ui-grid="branchesGrid" ui-grid-pagination class="genericGridHeader"></div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title">Solicitud de confirmación</h4>
      </div>
      <div class="modal-body">
        <p>¿Está seguro que desea eliminar la sucursal?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" ng-click="deleteBranch()">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
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
</div>