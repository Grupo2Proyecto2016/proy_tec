<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="jumbotron text-center">
	<h2>Registrar Empresa</h2>
	
	<p>{{ message }}</p>
</div>
<div class="row" style="margin-top: 50px;">
	<div class="col-xs-3"></div>
	<div class="col-xs-6">
		<form class="form-horizontal" role="form" ng-submit="createCompany()">
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
		    	<input type="text" name="url" pattern="[a-zA-Z]+" title="Solo se aceptan letras." class="form-control" ng-model="companyForm.tenantName" required>
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
		    <label class="control-label col-sm-2" for="address">Dirección:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="address" class="form-control" ng-model="companyForm.user.direccion" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="username">Nombre de usuario:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="username" class="form-control" ng-model="companyForm.user.usrname" required>
	    	</div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="password">Contraseña:</label>
		    <div class="col-sm-10">
		    	<input type="password" name="password" class="form-control" ng-model="companyForm.user.passwd" required>
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
