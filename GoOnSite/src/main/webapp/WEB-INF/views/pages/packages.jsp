<div class="jumbotron text-center">
	<h3>Registro de Encomiendas</h3>
	
	<p>{{ message }}</p>
	<p id="packageFormLink" ng-show="packageOrigin != null"><button class="btn btn-sm btn-primary" ng-click="showPackageForm()"><i class="fa fa-plus fa-md pull-left"></i>Nueva</button></p>
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

<div id="divPackageForm" class="hidden" ng-show="packageOrigin != null">
	<div class="row">
		<div class="col-xs-2"></div>
		<div class="col-xs-8">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hidePackageForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h4 ng-show="packagePrice != null" style="margin-left: 15px; color: green; float: right; margin-right: 40px;">Precio: $ {{packagePrice}}</h4>
							<h5 class="modal-title">Datos de Encomienda</h5>
						</div>
					</div>
					<div class="panel-body">
						<form class="form-horizontal" name="cForm" role="form" ng-submit="calcPackage()"">
							<div class="form-group">
						    	<div class="col-sm-6">
							    	<label class="control-label col-sm-3" for="destino">Destino:</label>
								    <div class="col-sm-9">
								    	<select name="destino" class="form-control" ng-model="packageForm.destino" ng-change="getPackageTravels()" ng-options="terminal as terminal.descripcion for terminal in destinationTerminals" required>
											<option value="">Seleccione una sucursal</option>
										</select>	
							    	</div>
						    	</div>
						    	<div class="col-sm-6">
									<label class="control-label col-sm-5" for="packageHeigth">Alto (cm):</label>
									<div class="col-sm-7">
										<input type="number" min="5" step="5" class="form-control" name="packageHeigth" ng-model="packageForm.alto" required>
								    </div>				
								</div>	
					    	</div>				
					    	<div class="form-group">
							    <div class="col-sm-6">
						    	</div>
								<div class="col-sm-6">
									<label class="control-label col-sm-5" for="packageBaseLenght">Largo de base (cm):</label>
									<div class="col-sm-7">
										<input type="number" min="5" step="5" class="form-control" name="packageBaseLenght" ng-model="packageForm.largo" required>
								    </div>				
								</div>	    
							</div>		
							<div class="form-group">
								<div class="col-sm-6">
									<label class="control-label col-sm-3" for="packageWeight">Peso (Kg):</label>
									<div class="col-sm-9">
										<input type="number" min="0.1" step="0.1" title="Solo se aceptan números" class="form-control" name="packageWeight" ng-model="packageForm.peso" required>
								    </div>
								</div>
								<div class="col-sm-6">
									<label class="control-label col-sm-5" for="packageBaseWidth">Ancho de base (cm):</label>
									<div class="col-sm-7">
										<input type="number" min="5" step="5" class="form-control" name="packageBaseWidth" ng-model="packageForm.ancho" required>
								    </div>				
								</div>
							</div>
							<div ng-show="calc_error != null" class="alert alert-danger text-center col-sm-12">
							  <strong>Error! </strong>{{calc_error}}
							</div>
							
							<div class="form-group"> 
					    		<div class="col-sm-12">
					      			<button style="float: right; margin-top: 10px;" type="submit" class="btn btn-sm btn-info">Calcular</button>
					    		</div>
						    </div>
					    </form>
			    	</div>				  
					<div class="panel-body">
						<form class="form-horizontal" name="pForm" role="form" ng-submit="createPackage()">
							<div class="form-group">
						    	<div class="col-sm-6">
							    	<label class="control-label col-sm-5" for="emisorOpt">Emisor registrado?:</label>
								    <div class="col-sm-7">
								    	<select name="emisorOpt" class="form-control" ng-model="eOption" required>
											<option value="1">Si</option>
											<option value="2">No</option>
										</select>	
							    	</div>
						    	</div>
						    	<div class="col-sm-6">
							    	<label class="control-label col-sm-5" for="receptorOpt">Receptor registrado?:</label>
								    <div class="col-sm-7">
								    	<select name="receptorOpt" class="form-control" ng-model="rOption" required>
											<option value="1">Si</option>
											<option value="2">No</option>
										</select>	
							    	</div>
						    	</div>
					    	</div>				
					    	<div class="form-group">
							    <div class="col-sm-6">
							    	<div ng-show="eOption == 2">
								    	<label class="control-label col-sm-5" for="emisorDoc">CI Emisor:</label>
										<div class="col-sm-7">
											<input type="text" pattern="^[0-9]{6,7}$" title="Ingrese solo números sin el dígito verificador" class="form-control" name="emisorDoc" ng-model="packageForm.eDoc" ng-required="eOption == 2">
									    </div>
							    	</div>
							    	<div ng-show="eOption == 1">
								    	<label class="control-label col-sm-5" for="emisorUser">Usuario emisor:</label>
										<div class="col-sm-7">
											<input type="text" class="form-control" placeholder="username" name="emisorUser" ng-model="packageForm.eUser" ng-required="eOption == 1" clientexists>
									    </div>
									    <div class="form-group has-error">
											<div ng-messages="pForm.emisorUser.$error" role="alert" class="col-sm-12 text-center">
												<span ng-message="clientexists" class="help-block">El cliente Emisor no existe</span>
										    </div>
										</div>	
							    	</div>
						    	</div>
								<div class="col-sm-6">
									<div ng-show="rOption == 2">
										<label class="control-label col-sm-5" for="receptorDoc">CI Receptor:</label>
										<div class="col-sm-7">
											<input type="text" pattern="^[0-9]{6,7}$" title="Ingrese solo números sin el dígito verificador" class="form-control" name="receptorDoc" ng-model="packageForm.rDoc" ng-required="rOption == 2">
									    </div>
								    </div>
								    <div ng-show="rOption == 1">
								    	<label class="control-label col-sm-5" for="receptorUser">Usuario receptor:</label>
										<div class="col-sm-7">
											<input type="text" class="form-control" placeholder="username" name="receptorUser" ng-model="packageForm.rUser" ng-required="rOption == 1" clientexists>
									    </div>
									    <div class="form-group has-error">
											<div ng-messages="pForm.receptorUser.$error" role="alert" class="col-sm-12 text-center">
												<span ng-message="clientexists" class="help-block">El cliente Receptor no existe</span>
										    </div>
										</div>
							    	</div>			
								</div>    
							</div>	
							
							<div class="form-group"> 
					    		<div class="col-sm-12">
					      			<button style="float: right; margin-top: 10px;" class="btn btn-info">Crear Encomienda</button>
					    		</div>
						    </div>
					    </form>
			    	</div>				  
	    		</div>
			</form>
		</div>
		<div class="col-xs-2"></div>
	</div>
	
	<div class="row" ng-if="travels.length > 0" style="margin-top: 50px;">
		<div class="col-xs-2"></div>	
		<div class="col-xs-8">
			<h4 class="text-center">Selecciona un viaje</h4>
			<div ui-grid="travelsGrid" style=" height: 55%;" ui-grid-selection ui-grid-pagination></div>
		</div>
		<div class="col-xs-2"></div>
	</div>
		
</div>

<div id="packagesGridDiv" class="row" style="margin-top: 50px;">
	<div class="col-xs-2"></div>	
	<div class="col-xs-8">
		<div ui-grid="packagesGrid" style=" height: 55%;" ui-grid-pagination></div>
	</div>
	<div class="col-xs-2"></div>
</div>

<div class="modal fade" id="deliverModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" id="myModalLabel">Entrega de Encomienda</h3>
      </div>
      <div class="modal-body">
	  	<h5>¿Comfirma la entrega de esta encomienda?</h5>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
			<button type="button" class="btn btn-warning" ng-click="deliverPackage()">Entregar</button>
		</div>        
      </div>
    </div>
  </div>
 </div>
 
<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" id="myModalLabel">Atención</h3>
      </div>
      <div class="modal-body">
      	<h5>{{ error_message }}</h5>        
      </div>
    </div>
  </div>
 </div>