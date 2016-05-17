<div class="jumbotron text-center">
	<h2>Registrar Empresa</h2>
	
	<p>{{ message }}</p>
</div>
<div class="row" style="margin-top: 50px;">
	<div class="col-xs-3"></div>
	<div class="col-xs-6">
		<form class="form-horizontal" role="form" ng-submit="createCompany()">
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="email">Nombre:</label>
		    <div class="col-sm-10">
		    	<input type="text" class="form-control" name="name" ng-model="companyForm.name">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="trueName">Razón social:</label>
		  	<div class="col-sm-10">
		    	<input type="text" name="trueName" class="form-control" ng-model="companyForm.trueName">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="rut">Rut:</label>
		  	<div class="col-sm-10">
		    	<input type="text" name="rut" class="form-control" ng-model="companyForm.rut">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="phone">Teléfono:</label>
		  	<div class="col-sm-10">
		    	<input type="number" name="rut" class="form-control" ng-model="companyForm.phone">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="country">País:</label>
		    <div class="col-sm-10">
			    <select name="country" class="form-control" ng-model="companyForm.country" ng-options="country.id_pais as country.nombre for country in countries">
		            <option value="">Selecciones un país</option>
		        </select>
	        </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="address">Dirección:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="address" class="form-control" ng-model="companyForm.address">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="url">Url del sitio:</label>
	    	<div class="col-sm-10">
		    	<input type="text" name="url" class="form-control" ng-model="companyForm.tenantName">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="username">Usuario:</label>
		    <div class="col-sm-10">
		    	<input type="text" name="username" class="form-control" ng-model="companyForm.username">
	    	</div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="password">Contraseña:</label>
		    <div class="col-sm-10">
		    	<input type="password" name="password" class="form-control" ng-model="companyForm.password">
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
