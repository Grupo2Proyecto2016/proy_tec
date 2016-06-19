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
			<form class="form-horizontal" name="packageForm" role="form" ng-submit="calcPackage()"">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div> 
							<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hidePackageForm()">
							<span aria-hidden="true">×</span>
							</button> 
							<h5 class="modal-title">Datos de Encomienda</h5>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
					    	<div class="col-sm-6">
						    	<label class="control-label col-sm-3" for="destino">Destino:</label>
							    <div class="col-sm-9">
							    	<select name="destino" class="form-control" ng-model="packageForm.destino" ng-options="terminal as terminal.descripcion for terminal in destinationTerminals" required>
										<option value="">Seleccione una sucursal</option>
									</select>	
						    	</div>
					    	</div>
					    	<div class="col-sm-6">
								<label class="control-label col-sm-5" for="packageHeigth">Alto (cm):</label>
								<div class="col-sm-7">
									<input type="number" min="5" step="5" class="form-control" name="packageHeigth" ng-model="packageForm.alto">
							    </div>				
							</div>	
				    	</div>				
				    	<div class="form-group">
						    <div class="col-sm-6">
					    	</div>
							<div class="col-sm-6">
								<label class="control-label col-sm-5" for="packageBaseLenght">Largo de base (cm):</label>
								<div class="col-sm-7">
									<input type="number" min="5" step="5" class="form-control" name="packageBaseLenght" ng-model="packageForm.largo">
							    </div>				
							</div>	    
						</div>		
						<div class="form-group">
							<div class="col-sm-6">
								<label class="control-label col-sm-3" for="packageWeight">Peso (Kg):</label>
								<div class="col-sm-9">
									<input type="number" min="0.1" step="0.1" title="Solo se aceptan números" class="form-control" name="packageWeight" ng-model="packageForm.peso">
							    </div>
							</div>
							<div class="col-sm-6">
								<label class="control-label col-sm-5" for="packageBaseWidth">Ancho de base (cm):</label>
								<div class="col-sm-7">
									<input type="number" min="5" step="5" class="form-control" name="packageBaseWidth" ng-model="packageForm.ancho">
							    </div>				
							</div>
						</div>
						<div ng-show="calc_error != null" class="alert alert-danger text-center col-sm-12">
						  <strong>Error! </strong>{{calc_error}}
						</div>
						
						<div class="form-group"> 
				    		<div class="col-sm-6">
				    			<h4 style="margin-left: 15px;">Precio: {{packagePrice}}</h4>
				    		</div>
				    		<div class="col-sm-6">
				      			<button style="float: right; margin-top: 10px;" type="submit" class="btn btn-info">Calcular</button>
				    		</div>
					    </div>
			    	</div>				  
	    		</div>
			</form>
					
<!-- 			<form class="form-horizontal" role="form" name="form"> -->
<!-- 				<div class="panel panel-default"> -->
<!-- 					<div class="panel-body">											 -->
<!-- 						<div class="form-group"> -->
<!-- 						    <div class="col-sm-6"> -->
<!-- 							    <h5 class="control-label col-sm-6"> -->
<!-- 							    	¿Dónde quieres ir? -->
<!-- 							    </h5> -->
<!-- 							    <div class="col-sm-6">			 -->
<!-- 									<a style="margin-top: 12px;" class="btn btn-primary" ng-click="showDestinationMap()"> -->
<!-- 										<i class="fa fa-map-marker fa-lg " style="margin-right: 5px;"></i> Indicar destino -->
<!-- 									</a>					    	 -->
<!-- 						    	</div> -->
<!-- 					    	</div> -->
<!-- 						    <div class="col-sm-6"> -->
<!-- 							    <h5 class="control-label col-sm-6"> -->
<!-- 							    	¿Dónde subes? -->
<!-- 							    </h5> -->
<!-- 							    <div class="col-sm-6">			 -->
<!-- 									<a style="margin-top: 12px;" class="btn btn-primary" ng-click="showOriginMap()"> -->
<!-- 										<i class="fa fa-map-marker fa-lg " style="margin-right: 5px;"></i> Indicar origen -->
<!-- 									</a>					    	 -->
<!-- 						    	</div> -->
<!-- 					    	</div> -->
<!-- 				    	</div> -->
<!-- 				    	<div class="form-group"> -->
<!-- 						    <div class="col-sm-6"> -->
<!-- 							    <label class="control-label col-sm-6" for="traveldate"> -->
<!-- 							    	Día del viaje: -->
<!-- 							    </label> -->
<!-- 							    <div class="col-sm-6"> -->
<!-- 									<input type="date" class="form-control" min="{{minDate | date:'yyyy-MM-dd'}}" max="{{maxDate | date:'yyyy-MM-dd'}}" name="traveldate" ng-model="travelSearch.date" required> -->
<!-- 							    </div> -->
<!-- 					    	</div> -->
<!-- 					    	<div class="col-sm-6"> -->
<!-- 							    <label class="control-label col-sm-6" for="ticketsCount"> -->
<!-- 							    	Cantidad de asientos: -->
<!-- 							    </label> -->
<!-- 							    <div class="col-sm-6"> -->
<!-- 									<input type="number" class="form-control" min="0" max="10" name="ticketsCount" ng-model="travelSearch.ticketsCount"> -->
<!-- 							    </div> -->
<!-- 					    	</div> -->
<!-- 				    	</div> -->
<!-- 						<div class="form-group">  -->
<!-- 				    		<div class="col-sm-12"> -->
<!-- 				      			<button style="float: right" type="submit" class="btn btn-info">Buscar</button> -->
<!-- 				    		</div> -->
<!-- 				    	</div> -->
<!-- 		    		</div> -->
<!-- 		    	</div> -->
<!-- 			</form>			 -->
		</div>
		<div class="col-xs-1"></div>
	</div>
</div>

<div class="row hidden" style="margin-top: 50px;">
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

 <script type="text/javascript">
     
 </script>