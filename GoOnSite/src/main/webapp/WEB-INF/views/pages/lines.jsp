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

<div id="divLinesForm" class="hidden">

</div>