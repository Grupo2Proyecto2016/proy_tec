<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="jumbotron text-center">
	<h2>
		Empresas registradas
		</h1>
		<p>{{ message }}</p>
</div>

<div class="row">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div class="panel panel-default">
			<div class="panel-heading"></div>
			<table class="table">
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Razón Social</th>
						<th>Url</th>
						<th>Teléfono</th>
						<th>País de origen</th>
						<th>Dirección</th>
						<th>Administrador</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="c in companies">
						<td>{{c.nombre}}</td>
						<td>{{c.razonSocial}}</td>
						<td>{{c.nombreTenant}}</td>
						<td>{{c.telefono}}</td>
						<td>{{c.pais.nombre}}</td>
						<td>{{c.direccion}}</td>
						<td>
							<button type="button" ng-click="getUserDetails(c.nombreTenant)"
								class="btn btn-primary btn-sm">Detalles</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
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