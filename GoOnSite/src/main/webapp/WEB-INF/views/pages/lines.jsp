<div id="divTitleLines" class="jumbotron text-center">
	<h3>Líneas</h3>	
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

<div id="divLineForm" class="hidden">
	<div class="row">
		<div class="col-xs-1"></div>
		<div class="col-xs-10">			
			<form class="form-horizontal" role="form" name="form" ng-submit="createLine()">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h5 class="modal-title">Datos Básicos</h5>
						</div>
					</div>
					<div class="panel-body">											
						<div class="form-group">
						    <div class="col-sm-6">
							    <label class="control-label col-sm-3" for="origen">Desde:</label>
							    <div class="col-sm-9">			
									<select name="origen" class="form-control"
										ng-model="lineForm.origen"
										ng-options="terminal.id_parada as terminal.descripcion for terminal in terminals"
										ng-change="updateTerminalOrigen()"
										required>
										<option value="">Seleccione una terminal</option>
									</select>							    	
						    	</div>
					    	</div>
					    	<div class="col-sm-6">
						    	<label class="control-label col-sm-3" for="destino">Hasta:</label>
							    <div class="col-sm-9">
							    	<select name="destino" class="form-control"
										ng-model="lineForm.destino"
										ng-options="terminal.id_parada as terminal.descripcion for terminal in terminals"
										ng-change="updateTerminalDestino()"
										required>
										<option value="">Seleccione una terminal</option>
									</select>	
						    	</div>
					    	</div>
				    	</div>				
				    	<div class="form-group">
				    		<div class="col-sm-6">
								<label class="control-label col-sm-3" for="numero">Número:</label>
								<div class="col-sm-9">
									<input type="text" pattern="[0-9]+" title="Solo se aceptan números" class="form-control" name="numero" ng-model="lineForm.numero" required>
							    </div>
							</div>
							<div class="col-sm-6">
								<label class="control-label col-sm-3" for="tiempo_estimado">Duración:</label>
								<div class="col-sm-9">
									<input type="text" pattern="[0-9]+" title="Solo se aceptan números" class="form-control" name="tiempo_estimado" ng-model="lineForm.tiempo_estimado" required>
							    </div>				
							</div>		    
						</div>
						<div class="form-group">
							<div class="col-sm-6">
					    		<label class="control-label col-sm-3" for="viaja_parado">Admite Parados:</label>
								<div class="col-sm-9">
								  <div class="checkbox">
								    <label>
								      <input type="checkbox" name="viaja_parado" ng-model="busForm.viaja_parado">
								    </label>
								  </div>
								</div>						
							</div>
							<div class="col-sm-6">
								<label class="control-label 	col-sm-3" for="costo_fijo">Valor Fijo (UYU):</label>
								<div class="col-sm-9">
									<input type="text" pattern="[0-9]+" title="Solo se aceptan números" class="form-control" name="costo_fijo" ng-model="lineForm.costo_fijo" required>
							    </div>						    
							</div>
						</div>
						<div class="form-group"> 
			    		<div class="col-sm-12">
			      			<button style="float: right" type="submit" class="btn btn-info">Crear</button>
			    		</div>
				    </div>
		    	</div>
		    	</div>
			</form>			
		</div>
		<div class="col-xs-1"></div>
	</div>
	<div class="row">
		<div class="col-xs-1"></div>	
		<div class="col-xs-5">
			<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h5 class="modal-title">Mapa
							<button type="button" class="btn btn-default btn-xs" id="btnRuta" ng-click="createRoute()"><i class="fa fa-road fa-lg"></i>Trazar Ruta</button>							
						</h5>						
					</div>				
				</div>
				<div class="panel-body">
					<input id="pac-input" class="controls" type="text" placeholder="Search Box">
					<div id="map" style="height: 50%">
					
 					</div>
				</div>
			</div>
		</div>
		<div class="col-xs-5">
			<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h5 class="modal-title">Lista Paradas</h5>
					</div>				
				</div>
				<div class="panel-body" style="height: 55%;overflow: auto;">	
					<table class="table table-striped">
						<colgroup>
					       <col span="1">
					       <col span="1" style="width: 130px;">
					       <col span="1" style="width: 95px;">
					    </colgroup>
						<thead>
							<tr>
								<th>Dirección</th>
								<th>Reajuste&nbsp;(UYU)</th>
								<th>Acciones</th>
							</tr>
						</thead>
	   					<tr ng-repeat="m in markers track by $index">	   					
	   						<td>
	   							<span style="float:left;">{{$index + 1}} - {{m.descripcion}}</span>	   								   							
	   						</td>
	   						<td>
	  	 						<div class="col-xs-10">  						
  									<input type="text" class="form-control" name="origen" ng-model="m.reajuste" required>
								</div>	  							 							
	   						</td>
	   						<td>
	   							<span style="float:right">
	   								<a><i class="fa fa-chevron-circle-down fa-lg" ng-click="changeIndex($index, $index+1)">&nbsp;</i></a>
	   								<a><i class="fa fa-chevron-circle-up fa-lg" ng-click="changeIndex($index, $index-1)">&nbsp;</i></a>
	   								<a><i class="fa fa-trash fa-lg" ng-click="deleteMarker($index)">&nbsp;</i></a>
	   							</span>
	   						</td>
	   					</tr>
	   				</table>				
				</div>
			</div>
			<div class="panel panel-default">				
				<div class="panel-heading">
					<div>
						<h4 class="modal-title">Estimados: &nbsp;<i class="fa fa-clock-o fa-lg">&nbsp;</i>{{txt_minutos}} min.&nbsp;<i class="fa fa-arrows-h fa-lg">&nbsp;</i>{{txt_km}} km.</h4>
					</div>
				</div>
			</div> 
		</div>
		<div class="col-xs-1"></div>		
	</div>
</div>

<div class="row" style="margin-top: 50px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div ui-grid="linesGrid" ui-grid-pagination class="genericGridHeader"></div>
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
        <p>¿Está seguro que desea eliminar esta linea?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" ng-click="deleteLine()">Aceptar</button>
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