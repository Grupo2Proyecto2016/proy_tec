<div id="divTitleBuses" class="jumbotron text-center">
	<h2>Vehículos</h2>
	
	<p>{{ message }}</p>
	<p><a class="btn btn-md btn-primary" ng-click="showForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</a></p>
</div>

<div id="divBusForm" class="hidden">
	<div class="row" style="margin-top: 50px;">
		<div class="col-xs-3"></div>
		<div class="col-xs-6">
			<form class="form-horizontal" role="form" name="form" ng-submit="createBus()">
				<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h4 class="modal-title">Datos de Vehiculo</h4>
					</div>
				</div>
				<div class="panel-body">
				<div class="form-group">
				    <label class="control-label col-sm-4" for="matricula">Matricula:</label>
				    <div class="col-sm-6">
				    	<input type="text" class="form-control" name="matricula" ng-model="busForm.matricula" required>
			    	</div>
			  	</div>
			  	<div class="form-group">		  		
				    <label class="control-label col-sm-4" for="marca">Marca:</label>
				    <div class="col-sm-6">
				    	<input type="text" class="form-control" name="marca" ng-model="busForm.marca" required>
			    	</div>
			    </div>
				<div class="form-group">				    
			  	    <label class="control-label col-sm-4" for="modelo">Modelo:</label>
				    <div class="col-sm-6">
				    	<input type="text" class="form-control" name="modelo" ng-model="busForm.modelo" required>
			    	</div>
			  	</div>
			  	<div class="form-group">
				    <label class="control-label col-sm-4" for="ano">Año:</label>
				    <div class="col-sm-6">
				    	<input type="number" class="form-control" name="ano" ng-model="busForm.ano" min="1990" max="2016" required>
			    	</div>
			  	</div>
			  	<div class="form-group">
				    <label class="control-label col-sm-4" for="cantAsientos">Cant.Asientos:</label>
				    <div class="col-sm-6">
				    	<input type="number" class="form-control" name="cantAsientos" ng-model="busForm.cantAsientos" min="1" max="150" required>
			    	</div>
			  	</div>
			  	<div class="form-group">
			    	<label class="control-label col-sm-4" for="cantParados">Cant.Parados:</label>
			    	<div class="col-sm-6">
			    		<input type="number" class="form-control" name="cantParados" ng-model="busForm.cantParados" min="0" max ="20" value="0" required>
		    		</div>
		    	</div>
		    	<div class="form-group">
		    	<label class="control-label col-sm-4" for="cantAccesibles">Tiene Baño:</label>
				  <div class="col-sm-6">
				    <div class="checkbox">
				      <label>
				        <input type="checkbox" name="tieneBano" ng-model="busForm.tieneBano">
				      </label>
				    </div>
				  </div>
				</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-4" for="cantAccesibles">Asientos Accesibles:</label>
			    	<div class="col-sm-6">
			    		<input type="number" class="form-control" name="cantAccesibles" ng-model="busForm.cantAccesibles"  min="0" value="0" max="150" required>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-4" for="cantAnimales">Lugares Animales:</label>
			    	<div class="col-sm-6">
			    		<input type="number" class="form-control" name="cantAnimales" ng-model="busForm.cantAnimales" min="0" value="2" required>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-4" for="cantBultos">Lugares Equipaje:</label>
			    	<div class="col-sm-6">
			    		<input type="number" class="form-control" name="cantBultos" ng-model="busForm.cantBultos" min="0" value="0" max="150" required>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-4" for="cantEncomiendas">Lugares Encomienda:</label>
			    	<div class="col-sm-6">
			    		<input type="number" class="form-control" name="cantEncomiendas" ng-model="busForm.cantEncomiendas" min="0" value="0" required>
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
	</div>
</div>


<div id="successAlert" class="row hidden">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div class="alert alert-success" style="">
		  <strong>Exito!</strong> Vehiculo creado.
		  <button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideSuccess()"><span aria-hidden="true">×</span>
		</div>		
	</div>
	<div class="col-xs-1"></div>
</div>

<div class="row">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div class="panel panel-default">
			<div class="panel-heading"></div>
			<table class="table">
				<thead>
					<tr>
						<th>Matricula</th>
						<th>Marca</th>
						<th>Modelo</th>
						<th>Año</th>
						<th>Asientos</th>
						<th>Lug. Parados</th>
						<th>Operaciones</th>												
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="c in buses">
						<td>{{c.matricula}}</td>
						<td>{{c.marca}}</td>
						<td>{{c.modelo}}</td>
						<td>{{c.ano}}</td>
						<td>{{c.cantAsientos}}</td>
						<td>{{c.cantParados}}</td>
						<td>
							<button type="button" ng-click="getBusDetails(c)" class="btn btn-primary btn-sm">Detalles</button>
							<button type="button" ng-click="deleteBus(c)" class="btn btn-danger btn-sm">Eliminar</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div class="modal fade" id="busDetailsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header" style="background-color: green">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" style="color: white" id="myModalLabel">Detalles</h3>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Tiene Baño:</label>
						<div ng-show="elBus.tieneBano == true" class="col-sm-8">
							<p>Si</p>
						</div>
						<div ng-show="elBus.tieneBano == false"  class="col-sm-8">
							<p>No</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="surname">Asientos Accesibles:</label>
						<div class="col-sm-8">
							<p>{{elBus.cantAccesibles}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="birth">Lugar para Animales:</label>
						<div class="col-sm-8">
							<p>{{elBus.cantAnimales}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="mail">Lugares Equipaje:</label>
						<div class="col-sm-8">
							<p>{{elBus.cantBultos}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="address">Lugares Encomienda:</label>
						<div class="col-sm-8">
							<p>{{elBus.cantEncomiendas}}</p>
						</div>
					</div>
				</form>
			</div>
		</div>
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
