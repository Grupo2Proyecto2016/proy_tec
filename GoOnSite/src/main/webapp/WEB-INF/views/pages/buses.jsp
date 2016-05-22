<div id="divTitleBuses" class="jumbotron text-center">
	<h2>Vehículos</h2>
	
	<p>{{ message }}</p>
	<p><a class="btn btn-md btn-primary" ng-click="muestraForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</a></p>
</div>

<div id="divBusForm" class="hidden">
	<div class="row" style="margin-top: 50px;">
		<div class="col-xs-3"></div>
		<div class="col-xs-6">
			<form class="form-horizontal" role="form" name="form" ng-submit="createBus()">
				<h3 style="text-align: center">Datos del Vehiculo:</h3>
				<div class="form-group">
				    <label class="control-label col-sm-2" for="matricula">Matricula:</label>
				    <div class="col-sm-10">
				    	<input type="text" class="form-control" name="matricula" ng-model="busForm.matricula" required>
			    	</div>
			  	</div>
			  	<div class="form-group">		  		
				    <label class="control-label col-sm-2" for="marca">Marca:</label>
				    <div class="col-sm-10">
				    	<input type="text" class="form-control" name="marca" ng-model="busForm.marca" required>
			    	</div>
			    </div>
				<div class="form-group">				    
			  	    <label class="control-label col-sm-2" for="modelo">Modelo:</label>
				    <div class="col-sm-10">
				    	<input type="text" class="form-control" name="modelo" ng-model="busForm.modelo" required>
			    	</div>
			  	</div>
			  	<div class="form-group">
				    <label class="control-label col-sm-2" for="ano">Año:</label>
				    <div class="col-sm-10">
				    	<input type="number" class="form-control" name="ano" ng-model="busForm.ano" required>
			    	</div>
			  	</div>
			  	<div class="form-group">
				    <label class="control-label col-sm-2" for="cantAsientos">Cant.Asientos:</label>
				    <div class="col-sm-10">
				    	<input type="number" class="form-control" name="cantAsientos" ng-model="busForm.cantAsientos" required>
			    	</div>
			  	</div>
			  	<div class="form-group">
			    	<label class="control-label col-sm-2" for="cantParados">Cant.Parados:</label>
			    	<div class="col-sm-10">
			    		<input type="number" class="form-control" name="cantParados" ng-model="busForm.cantParados" required>
		    		</div>
		    	</div>
		    	<div class="form-group">
				  <div class="col-sm-offset-2 col-sm-10">
				    <div class="checkbox">
				      <label>
				        <input type="checkbox" name="tieneBano" ng-model="busForm.tieneBano">Tiene Baño
				      </label>
				    </div>
				  </div>
				</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-2" for="cantAccesibles">Asientos Accesibles:</label>
			    	<div class="col-sm-10">
			    		<input type="number" class="form-control" name="cantAccesibles" ng-model="busForm.cantAccesibles" required>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-2" for="cantAnimales">Lugares Animales:</label>
			    	<div class="col-sm-10">
			    		<input type="number" class="form-control" name="cantAnimales" ng-model="busForm.cantAnimales" required>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-2" for="cantAnimales">Lugares Equipaje:</label>
			    	<div class="col-sm-10">
			    		<input type="number" class="form-control" name="cantAnimales" ng-model="busForm.cantAnimales" required>
		    		</div>
		  		</div>
		  		<div class="form-group">
			    	<label class="control-label col-sm-2" for="cantAnimales">Lugares Encomienda:</label>
			    	<div class="col-sm-10">
			    		<input type="number" class="form-control" name="cantAnimales" ng-model="busForm.cantAnimales" required>
		    		</div>
		  		</div>
		  		<div class="form-group"> 
		    		<div class="col-sm-offset-2 col-sm-10">
		      		<button style="float: right" type="submit" class="btn btn-info">Crear</button>
		    	</div>
		  </div>
			</form>
		</div>
	</div>
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
							<button type="button" ng-click="getBusDetails(c)"
								class="btn btn-primary btn-sm">Detalles</button>
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
							<p>SI</p>
						</div>
						<div ng-show="elBus.tieneBano == false"  class="col-sm-8">
							<p>NO</p>
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

