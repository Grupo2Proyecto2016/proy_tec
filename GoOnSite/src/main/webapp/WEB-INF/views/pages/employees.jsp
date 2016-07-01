<div class="jumbotron text-center">
	<h3>Manejo del Personal</h2>
	
	<p>{{ message }}</p>
	<p><button class="btn btn-sm btn-primary" ng-click="showUserForm()"><i class="fa fa-plus fa-md pull-left"></i>Agregar</button></p>
</div>

<div id="userForm" class="hidden">
	<div class="row"">
		<div class="col-xs-3"></div>
		<div class="col-xs-6">
			<form class="form-horizontal" role="form" name="userForm" ng-submit="createUser()">
				<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideUserForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h5 class="modal-title">Datos de Usuario</h5>
					</div>
				</div>
				<div class="panel-body">
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="name">Nombre:</label>
				    <div class="col-sm-6">
				    	<input type="text" name="name" class="form-control" ng-model="userModel.nombre" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="surname">Apellido:</label>
				    <div class="col-sm-6">
				    	<input type="text" name="surname" class="form-control" ng-model="userModel.apellido" required>
			    	</div>
				  </div>
				  <div class="form-group">
				     <label class="control-label col-sm-4" for="ci">Nro Documento:</label>
				     <div class="col-sm-6">
				    	<input type="text" pattern="^[0-9]{6,7}$" title="Ingrese solo números sin el dígito verificador" name="ci" class="form-control" ng-model="userModel.ci" required>
			     	 </div>
			     </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="birth">Fecha de nacimiento:</label>
				    <div class="col-sm-6">
				    	<input type="date" name="birth" class="form-control" ng-model="userModel.fch_nacimiento" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="mail">Correo:</label>
				    <div class="col-sm-6">
				    	<input type="email" placeholder="Email" name="mail" class="form-control" ng-model="userModel.email" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="address">Dirección:</label>
				    <div class="col-sm-6">
				    	<input type="text" name="address" class="form-control" ng-model="userModel.direccion" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="telefono">Teléfono:</label>
				    <div class="col-sm-6">
				    	<input type="text" pattern="[0-9]+" title="Solo se aceptan números" name="telefono" class="form-control" ng-model="userModel.telefono" required>
			    	</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="username">Nombre de usuario:</label>
				    <div class="col-sm-6">
				    	<input type="text" autocomplete="off" name="username" placeholder="username" class="form-control" ng-model="userModel.usrname" required userexists>
			    	</div>
				  </div>
				  <div class="form-group has-error">
					<div ng-messages="userForm.username.$error" role="alert" class="col-sm-offset-4 col-sm-6 text-center">
						<span ng-message="userexists" class="help-block">El nombre de usuario ya está en uso</span>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="rol">Rol:</label>
				    <div class="col-sm-6">
				    	<select name="rol" ng-model="userModel.rol_id_rol" class="form-control" required>
				    		<option value="" ng-selected="selected">Seleccione un rol</option>
				    		<option value="2">Ventas</option>
      						<option value="3">Guarda/Conductor</option>
      						<option value="5">Largador</option>
				    	</select>
			    	</div>
				  </div>
				  
				  <div class="form-group" ng-show="userModel.rol_id_rol == 2">
				    <label class="control-label col-sm-4" for="rol">Sucursal:</label>
				    <div class="col-sm-6">
				    	<select name="sucursal" ng-model="userModel.id_sucursal" ng-options="branch.id_sucursal as branch.nombre for branch in branches" class="form-control" ng-required='userModel.rol_id_rol == 2'>
				    		<option value="">Selecciones una sucursal</option>
				    	</select>
			    	</div>
				  </div>
				  
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="password">Contraseña:</label>
				    <div class="col-sm-6">
				    	<input type="password" name="password" class="form-control" ng-model="userModel.passwd" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="passwordConfirm">Confirmación:</label>
				    <div class="col-sm-6">
				    	<input type="password" name="passwordConfirm" class="form-control" ng-model="userModel.passwordConfirm" required compare-to="userModel.passwd">
			    	</div>
				  </div>
				  <div class="form-group has-error">
					<div ng-messages="userForm.passwordConfirm.$error" role="alert" class="col-sm-offset-4 col-sm-6 text-center">
						<span ng-message="compareTo" class="help-block">La contraseña y su confirmación no coinciden</span>
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

<div id="userEditForm" class="hidden">
	<div class="row" style="margin-top: 50px;">
		<div class="col-xs-3"></div>
		<div class="col-xs-6">
			<form class="form-horizontal" role="form" name="userEditForm" ng-submit="updateUser()">
				<div class="panel panel-default">				
				<div class="panel-heading">
					<div> 
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="hideUserUpdateForm()">
						<span aria-hidden="true">×</span>
						</button> 
						<h5 class="modal-title">Datos de Usuario</h5>
					</div>
				</div>
				<div class="panel-body">
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="name">Nombre:</label>
				    <div class="col-sm-6">
				    	<input type="text" name="name" class="form-control" ng-model="userModel.nombre" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="surname">Apellido:</label>
				    <div class="col-sm-6">
				    	<input type="text" name="surname" class="form-control" ng-model="userModel.apellido" required>
			    	</div>
				  </div>
				  <div class="form-group">
				     <label class="control-label col-sm-4" for="ci">Nro Documento:</label>
				     <div class="col-sm-6">
				    	<input type="text" pattern="^[0-9]{6,7}$" title="Ingrese solo números sin el dígito verificador" name="ci" class="form-control" ng-model="userModel.ci" required>
			     	 </div>
			     </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="birth">Fecha de nacimiento:</label>
				    <div class="col-sm-6">
				    	<input type="date" name="birth" class="form-control" ng-model="userModel.fch_nacimiento" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="mail">Correo:</label>
				    <div class="col-sm-6">
				    	<input type="email" placeholder="Email" name="mail" class="form-control" ng-model="userModel.email" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="address">Dirección:</label>
				    <div class="col-sm-6">
				    	<input type="text" name="address" class="form-control" ng-model="userModel.direccion" required>
			    	</div>
				  </div>
				  <div class="form-group">
				    <label class="control-label col-sm-4" for="telefono">Teléfono:</label>
				    <div class="col-sm-6">
				    	<input type="text" pattern="[0-9]+" title="Solo se aceptan números" name="telefono" class="form-control" ng-model="userModel.telefono" required>
			    	</div>
				  </div>
				  
				  <div class="form-group" ng-show="userModel.rol_id_rol == 2">
				    <label class="control-label col-sm-4" for="rol">Sucursal:</label>
				    <div class="col-sm-6">
				    	<select name="sucursal" ng-model="userModel.id_sucursal" ng-options="branch.id_sucursal as branch.nombre for branch in branches" class="form-control" ng-required='userModel.rol_id_rol == 2'>
<!-- 				    		<option value="">Selecciones una sucursal</option> -->
				    	</select>
			    	</div>
				  </div>
				  
			  	  <div class="form-group"> 
		    		<div class="col-sm-10">
		      			<button style="float: right" type="submit" class="btn btn-info">Actualizar</button>
		      		</div>
		  		  </div>
		  		</div>
		  		</div>
			</form>	
		</div>
	</div>
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

<div class="row" style="margin-top: 50px;">
	<div class="col-xs-2"></div>
	<div class="col-xs-8">
		<div ui-grid="usersGrid" ui-grid-pagination class="genericGridHeader"></div>
	</div>
</div>

<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title">Solicitud de confirmación</h4>
      </div>
      <div class="modal-body">
        <p>¿Está seguro que desea eliminar este usuario del sistema?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-danger" ng-click="deleteUser()">Aceptar</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>