<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<div class="jumbotron text-center">
	<h3>Datos de la Empresa</h2>
	
	<p>{{ message }}</p>
</div>

<div id="successAlert" class="row" style="display: none">
	<div class="col-xs-2"></div>
	<div class="col-xs-8">
		<div class="alert alert-success" style="">
		  <button type="button" class="close" ng-click="closeSuccessAlert()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Éxito! </strong> <p id="successMessage"></p>
		</div>
	</div>
	<div class="col-xs-2"></div>
</div>

<div id="userForm">
	<div class="row">
		<div class="col-xs-2"></div>
		<div class="col-xs-8">
			<form class="form-horizontal" role="form" name="form" ng-submit="showUpdateCompanyModal()">
				<div class="col-xs-6">
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="email">Nombre:</label>
				    <div class="col-sm-8">
				    	<input type="text" class="form-control" name="name" ng-model="companyForm.nombre" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="trueName">Razón Social:</label>
				  	<div class="col-sm-8">
				    	<input type="text" name="trueName" class="form-control" ng-model="companyForm.razonSocial" required>
			    	</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="rut">Rut:</label>
				  	<div class="col-sm-8">
				    	<input type="text" name="rut" class="form-control" ng-model="companyForm.rut" required>
			    	</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="phone">Teléfono:</label>
				  	<div class="col-sm-8">
				    	<input type="text" name="rut" class="form-control" ng-model="companyForm.telefono" required>
			    	</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="country">País:</label>
				    <div class="col-sm-8">
					    <select name="country" class="form-control" ng-model="companyForm.pais.id_pais" ng-options="country.id_pais as country.nombre for country in countries" required>
				        </select>
			        </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="address">Dirección:</label>
				    <div class="col-sm-8">
				    	<input type="text" name="address" class="form-control" ng-model="companyForm.direccion" required>
			    	</div>
				  </div>
	
				  <div class="form-group"> 
				    <div class="col-sm-offset-2 col-sm-10">
				      <button style="float: right" type="submit" class="btn btn-info">Actualizar</button>
				    </div>
				  </div>
				</div>
				<div class="col-xs-6">
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="logo">Logo:</label>
				    <div class="col-sm-8">
				    	<input type="file" class="form-control" name="logo" accept=".png" fileread="companyForm.logo">
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="css">Estilo visual:</label>
				    <div class="col-sm-8">
					    <select name="country" id="stylec" class="form-control" ng-model="$parent.company.css" ng-options="style.value as style.name for style in styles" required>
				        </select>
			    	</div>
				  </div>
				  <div class="form-group has-error text-center">
					<span class="help-block">Los cambios no tendrán efecto para todos los usuarios hasta que no presione ACTUALIZAR</span>
				  </div>
				</div>
			</form>
		</div>
		<div class="col-xs-2"></div>
	</div>
</div>





<div class="modal fade" id="updateCompanyModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title">Solicitud de confirmación</h4>
      </div>
      <div class="modal-body">
        <p>¿Desea actualizar los datos de la empresa?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-warning" ng-click="updateCompany()">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>