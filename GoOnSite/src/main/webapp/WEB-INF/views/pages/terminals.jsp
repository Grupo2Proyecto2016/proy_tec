<div id="divTitleBranches" class="jumbotron text-center">
	<h3>Terminales</h3>
	
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

<div id="divTerminalForm" class="hidden">
	<div class="row">
		<div class="col-xs-1"></div>
		<div class="col-xs-5">
			<form class="form-horizontal" role="form" name="form" ng-submit="createTerminal()">
				<div class="panel panel-default">				
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h4 class="modal-title">Datos de Terminal</h4>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
						    <label class="control-label col-sm-4" for="nombre">Nombre:</label>
						    <div class="col-sm-6">
						    	<input type="text" class="form-control" name="nombre" ng-model="terminalForm.descripcion" required>
					    	</div>
				    	</div>
				  		<div class="form-group">
						    <label class="control-label col-sm-4" for="direccion">Direccion:</label>
						    <div class="col-sm-6">
						    	<input type="text" class="form-control" name="direccion" ng-model="terminalForm.direccion" required>
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
						<h4 class="modal-title">Mapa</h4>
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
	<div class="col-xs-3"></div>
	<div class="col-xs-6">
		<div ui-grid="terminalsGrid" ui-grid-pagination class="genericGridHeader" ng-if="terminals.length"></div>
	</div>
	<div class="col-xs-3"></div>
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