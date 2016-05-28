<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div ng-show="$parent.user != null">
	<div class="jumbotron text-center">
		<h2>Mi Cuenta</h2>
		
		<p>{{ message }}</p>
	</div>
	
	<div class="row">
		<div class="col-sm-7 col-sm-offset-1">
			<div class="row">
				<div class="container userPanel">
				  <div class="panel-group">
				    <div class="panel panel-default">
				      <div class="panel-heading">
				        <h4 class="panel-title text-center">
				          <a data-toggle="collapse" data-target="#myTravelsPanel"><b>Pasajes</b></a>
				        </h4>
				      </div>
				      <div id="myTravelsPanel" class="panel-collapse collapse">
				        <div class="panel-body">
				        
				        </div>
				      </div>
				    </div>
				  </div>
				</div>
				<div class="col-sm-1"></div>		
			</div>
			
			<div class="row">
				<div class="container userPanel">
				  <div class="panel-group">
				    <div class="panel panel-default">
				      <div class="panel-heading">
				        <h4 class="panel-title text-center">
				          <a data-toggle="collapse" data-target="#myTicketsPanel"><b>Encomiendas</b></a>
				        </h4>
				      </div>
				      <div id="myTicketsPanel" class="panel-collapse collapse">
				        <div class="panel-body">
				        
				        </div>
				      </div>
				    </div>
				  </div>
				</div>
				<div class="col-sm-1"></div>		
			</div>
		</div>
		
		<div class="col-sm-3">
			<div class="container userPanel">
			  <div class="panel-group">
			    <div class="panel panel-primary">
			      <div class="panel-heading">
			        <h4 class="panel-title">
			          <b>Datos personales</b>
			        </h4>
			      </div>
			      <div id="myDataPanel" class="panel">
			        <div class="panel-body">
			        	<div class="form-group row">
							<label class="control-label col-sm-4" for="name">Nombre:</label>
							<div class="col-sm-8">
								<p>{{$parent.user.nombre}}</p>
							</div>
						</div>
						<div class="form-group row">
							<label class="control-label col-sm-4" for="surname">Apellido:</label>
							<div class="col-sm-8">
								<p>{{$parent.user.apellido}}</p>
							</div>
						</div>
						<div class="form-group row">
							<label class="control-label col-sm-4" for="birth">Fecha de nacimiento:</label>
							<div class="col-sm-8">
								<p>{{$parent.user.fch_nacimiento | date:'dd/MM/yyyy' }}</p>
							</div>
						</div>
						<div class="form-group row">
							<label class="control-label col-sm-4" for="mail">Correo:</label>
							<div class="col-sm-8">
								<p>{{$parent.user.email}}</p>
							</div>
						</div>
						<div class="form-group row">
							<label class="control-label col-sm-4" for="address">Dirección:</label>
							<div class="col-sm-8">
								<p>{{$parent.user.direccion}}</p>
							</div>
						</div>
						<div class="form-group row">
							<label class="control-label col-sm-4" for="username">Nombre de usuario:</label>
							<div class="col-sm-8">
								<p>{{$parent.user.usrname}}</p>
							</div>
						</div>
			        </div>
			        <div class="panel-footer text-center">
			        	<button type="button" class="btn btn-primary">Editar</button>
			        	<button type="button" ng-click="showPasswordModal()" class="btn btn-primary">Cambiar contraseña</button>
			        	<button type="button" ng-click="showUserDeleteDialog()" class="btn btn-danger">Darme de baja</button>
			        </div>
			      </div>
			    </div>
			  </div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="passwordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content" style="width: 440px">
		      <div class="modal-header warning">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h3 class="modal-title" style="color:white" id="myModalLabel">Cambio de contraseña</h3>
		      </div>
		      <div class="modal-body">
	 	        <form class="form-signin" id="loginForm" name="passForm" role="form" ng-submit="changePassword()">
					<div class="form-group">
					   <label class="control-label col-sm-4" for="password">Contraseña:</label>
					   <div class="col-sm-8">
					   	<input type="password" name="password" class="form-control" ng-model="passwordModel.passwd" required>
					  	</div>
					 </div>
					 <div class="form-group">
					   <label class="control-label col-sm-4" for="passwordConfirm">Confirmación:</label>
					   <div class="col-sm-8">
					   	<input type="password" name="passwordConfirm" class="form-control" ng-model="passwordModel.passwordConfirm" required compare-to="passwordModel.passwd">
					  	</div>
					 </div>
					 <div class="form-group has-error">
						<div ng-messages="passForm.passwordConfirm.$error" role="alert" class="text-center">
							<span ng-message="compareTo" class="help-block">La contraseña y su confirmación no coinciden</span>
					   </div>
					 </div>
					<button class="btn btn-lg btn-warning btn-block"">
						Confirmar
					</button>
				</form>
		      </div>
		    </div>
		  </div>
	</div>
	
	<div class="modal fade" id="deleteUserModal" tabindex="-1" role="dialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header danger">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title">Solicitud de confirmación</h4>
	      </div>
	      <div class="modal-body">
	        <p>¿Está seguro que deseas dar de baja tu usuario?</p>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
	        <button type="button" class="btn btn-danger" ng-click="deleteUser()">Aceptar</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div>
	
</div>


<div ng-show="$parent.user == null">
	<div id="successAlert" class="row" style="display: none">
		<div class="col-xs-2"></div>
		<div class="col-xs-8">
			<div class="alert alert-success" style="">
			  <strong>Éxito! </strong> <p id="successMessage"></p>
			</div>
		</div>
		<div class="col-xs-2"></div>
	</div>
</div>