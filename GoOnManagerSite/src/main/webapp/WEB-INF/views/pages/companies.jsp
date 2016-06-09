<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="jumbotron text-center">
	<h2>
		Empresas registradas
	</h2>
		<p>{{ message }}</p>
</div>


<div class="row" style="margin-top: 50px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div ui-grid="companyGrid" ui-grid-pagination class="genericGridHeader"></div>
	</div>
	<div class="col-xs-1"></div>
</div>


<div class="modal fade" id="adminDetailsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header" style="background-color: green">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" style="color: white" id="myModalLabel">Datos del Administrador</h3>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Nombre:</label>
						<div class="col-sm-8">
							<p>{{tenantAdmin.nombre}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="surname">Apellido:</label>
						<div class="col-sm-8">
							<p>{{tenantAdmin.apellido}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="birth">Fecha de
							nacimiento:</label>
						<div class="col-sm-8">
							<p>{{ tenantAdmin.fch_nacimiento | date:'dd/MM/yyyy' }}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="mail">Correo:</label>
						<div class="col-sm-8">
							<p>{{tenantAdmin.email}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="address">Dirección:</label>
						<div class="col-sm-8">
							<p>{{tenantAdmin.direccion}}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="username">Nombre
							de usuario:</label>
						<div class="col-sm-8">
							<p>{{ tenantAdmin.usrname}}</p>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>