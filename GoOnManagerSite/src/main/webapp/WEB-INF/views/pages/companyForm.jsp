<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="jumbotron text-center">
	<h2>Registrar Empresa</h2>
	
	<p>{{ message }}</p>
</div>
<div class="row" style="margin-top: 50px;">
	<div class="col-xs-3"></div>
	<div class="col-xs-6">
		<form class="form-horizontal" role="form" name="form" ng-submit="createCompany()">
		  <h3 style="text-align: center">Datos de la Empresa:</h3>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="email">Nombre:</label>
		    <div class="col-sm-10">
		    	<input type="text" class="form-control" name="name" ng-model="companyForm.name" required>
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="trueName">Razón Social:</label>
		  	<div class="col-sm-10">
		    	<input type="text" name="trueName" class="form-control" ng-model="companyForm.trueName" required>
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="rut">Rut:</label>
		  	<div class="col-sm-10">
		    	<input type="number" name="rut" class="form-control" ng-model="companyForm.rut" required>
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="phone">Teléfono:</label>
		  	<div class="col-sm-10">
		    	<input type="number" name="rut" class="form-control" ng-model="companyForm.phone" required>
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="country">País:</label>
		    <div class="col-sm-10">
			    <select name="country" class="form-control" ng-model="companyForm.countryId" ng-options="country.id_pais as country.nombre for country in countries" required>
		            <option value="">Selecciones un país</option>
		        </select>
	        </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="address">Dirección:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="address" class="form-control" ng-model="companyForm.address" required>
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="url">Url del sitio:</label>
	    	<div class="col-sm-10">
		    	<input type="text" autocomplete="off" name="url" pattern="[a-zA-Z]+" title="Solo se aceptan letras." class="form-control" ng-model="companyForm.tenantName" tenantexists>
		    	<img src="<c:url value='/static/images/loader.gif' />" style="margin-top: -25px; margin-left: 80;" ng-show="form.url.$pending.tenantexists"/>
<!--     			<span class="label label-danger" ng-show="form.url.$error.tenantexists">La Url está en uso</span> -->
	    	</div>
		  </div>
		  <div class="form-group has-error">
			<div ng-messages="form.url.$error" role="alert" class="col-sm-offset-2 col-sm-10 text-center">
				<span ng-message="tenantexists" class="help-block">La Url ya está en uso</span>
		    </div>
		  </div>
		  
		  <h3 style="text-align: center; padding-top: 30px;">Datos del Administrador:</h3>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="name">Nombre:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="name" class="form-control" ng-model="companyForm.user.nombre" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="surname">Apellido:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="surname" class="form-control" ng-model="companyForm.user.apellido" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="birth">Fecha de nacimiento:</label>
		    <div class="col-sm-10">
		    	<input type="date" name="birth" class="form-control" ng-model="companyForm.user.fch_nacimiento" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="mail">Correo:</label>
		    <div class="col-sm-10">
		    	<input type="email" placeholder="Email" name="mail" class="form-control" ng-model="companyForm.user.email" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="telefono">Teléfono:</label>
		    <div class="col-sm-10">
		    	<input type="text" pattern="[0-9]+" title="Solo se aceptan números" name="telefono" class="form-control" ng-model="companyForm.user.telefono" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="address">Dirección:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="address" class="form-control" ng-model="companyForm.user.direccion" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="username">Nombre de usuario:</label>
		    <div class="col-sm-10">
		    	<input type="text" autocomplete="off" name="username" class="form-control" ng-model="companyForm.user.usrname" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="password">Contraseña:</label>
		    <div class="col-sm-10">
		    	<input type="password" name="password" class="form-control" ng-model="companyForm.password" required>
	    	</div>
		  </div>	  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="passwordConfirm">Confirmación:</label>
		    <div class="col-sm-10">
		    	<input type="password" name="passwordConfirm" class="form-control" ng-model="companyForm.passwordConfirm" required compare-to="companyForm.password">
	    	</div>
		  </div>
		  <div class="form-group has-error">
			<div ng-messages="form.passwordConfirm.$error" role="alert" class="col-sm-offset-2 col-sm-10 text-center">
				<span ng-message="compareTo" class="help-block">La contraseña y su confirmación no coinciden</span>
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

<div class="modal fade" id="successModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header" style="background-color: green">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" style="color:white" id="myModalLabel">Éxito</h3>
      </div>
      <div class="modal-body">
        La empresa ha sido creada.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" ng-click="changeView('companies')">Ok</button>
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
        Ha ocurrido un error al crear la empresa.
        Intente de nuevo en unos instantes. 
      </div>
    </div>
  </div>
</div>