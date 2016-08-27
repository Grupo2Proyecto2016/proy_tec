<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="jumbotron text-center">
	<h2>Registrar Empresa</h2>

	<p>{{ message }}</p>
</div>
<div class="row" style="margin-top: 50px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-6">
		<form class="form-horizontal" role="form" name="form" ng-submit="createCompany()">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div>
						<h4 class="modal-title">Datos de la Empresa:</h4>
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="control-label col-sm-4" for="email">Nombre:</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" name="name"
								ng-model="companyForm.name" required>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="trueName">Razón
							Social:</label>
						<div class="col-sm-8">
							<input type="text" name="trueName" class="form-control"
								ng-model="companyForm.trueName" required>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="rut">Rut:</label>
						<div class="col-sm-8">
							<input type="text" pattern="[0-9]+"
								title="Solo se aceptan números" name="rut"
								class="form-control" ng-model="companyForm.rut"
								required>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="phone">Teléfono:</label>
						<div class="col-sm-8">
							<input type="text" pattern="[0-9]+"
								title="Solo se aceptan números" name="telefono"
								class="form-control" ng-model="companyForm.phone"
								required>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="country">País de origen:</label>
						<div class="col-sm-8">
							<select name="country" class="form-control"
								ng-model="companyForm.countryId"
								ng-options="country.id_pais as country.nombre for country in countries"
								required>
								<option value="">Selecciones un país</option>
							</select>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="address">Dirección:</label>
						<div class="col-sm-8">
							<input type="text" name="address" class="form-control"
								ng-model="companyForm.address" required>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="addTerminal">Agregar terminal:</label>
						<div class="col-sm-8">
							<div class="checkbox">
								<label> <input type="checkbox" name="addTerminal" ng-model="companyForm.addTerminal">
								</label>
							</div>
						</div>
					</div>
	
					<div class="form-group">
						<label class="control-label col-sm-4" for="url">Url del sitio:</label>
						<div class="col-sm-8">
							<input type="text" autocomplete="off" name="url" pattern="[a-zA-Z]+" title="Solo se aceptan letras." class="form-control" ng-model="companyForm.tenantName" tenantexists required>
							<img src="<c:url value='/static/images/loader.gif' />" style="margin-top: -25px; margin-left: 80;" ng-show="form.url.$pending.tenantexists" /> 
							<span class="label label-danger" ng-show="form.url.$error.tenantexists">La Url está en uso</span>
						</div>
					</div>
					<div class="form-group has-error">
						<div ng-messages="form.url.$error" role="alert" class="col-sm-offset-4 col-sm-8 text-center">
						</div>
					</div>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<div>
						<h4 class="modal-title">Datos del Administrador:</h4>
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="control-label col-sm-4" for="name">Nombre:</label>
						<div class="col-sm-8">
							<input type="text" name="name" class="form-control"
								ng-model="companyForm.user.nombre" required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="surname">Apellido:</label>
						<div class="col-sm-8">
							<input type="text" name="surname" class="form-control"
								ng-model="companyForm.user.apellido" required>
						</div>
					</div>
					<div class="form-group">
					    <label class="control-label col-sm-4" for="ci">Nro Documento:</label>
					    <div class="col-sm-8">
					    	<input type="text" pattern="^[0-9]{6,7}$" title="Ingrese solo números sin el dígito verificador" name="ci" class="form-control" ng-model="companyForm.user.ci" required>
				    	</div>
				    </div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="birth">Fecha
							de nacimiento:</label>
						<div class="col-sm-8">
							<input type="date" name="birth" class="form-control" max="{{maxBirth | date:'yyyy-MM-dd'}}"
								ng-model="companyForm.user.fch_nacimiento" required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="mail">Correo:</label>
						<div class="col-sm-8">
							<input type="email" placeholder="Email" name="mail"
								class="form-control" ng-model="companyForm.user.email" required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="telefono">Teléfono:</label>
						<div class="col-sm-8">
							<input type="text" pattern="[0-9]+"
								title="Solo se aceptan números" name="telefono"
								class="form-control" ng-model="companyForm.user.telefono"
								required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="address">Dirección:</label>
						<div class="col-sm-8">
							<input type="text" name="address" class="form-control"
								ng-model="companyForm.user.direccion" required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="username">Nombre
							de usuario:</label>
						<div class="col-sm-8">
							<input type="text" autocomplete="off" name="username"
								class="form-control" ng-model="companyForm.user.usrname"
								required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="password">Contraseña:</label>
						<div class="col-sm-8">
							<input type="password" name="password" class="form-control"
								ng-model="companyForm.password" required>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-4" for="passwordConfirm">Confirmación:</label>
						<div class="col-sm-8">
							<input type="password" name="passwordConfirm"
								class="form-control" ng-model="companyForm.passwordConfirm"
								required compare-to="companyForm.password">
						</div>
					</div>
					<div class="form-group has-error">
						<div ng-messages="form.passwordConfirm.$error" role="alert"
							class="col-sm-offset-4 col-sm-8 text-center">
							<span ng-message="compareTo" class="help-block">La
								contraseña y su confirmación no coinciden</span>
						</div>
					</div>
	
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">
							<button style="float: right" type="submit" class="btn btn-info">Crear</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="col-xs-4" style="height: 600px;">
		<div class="panel panel-default">
			<div class="panel-body">
				<input id="pac-input" class="controls" type="text" placeholder="Search Box">
				<div id="map" style="height: 50%"></div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="successModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header" style="background-color: green">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" style="color: white" id="myModalLabel">Éxito</h3>
			</div>
			<div class="modal-body">La empresa ha sido creada.</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary"
					ng-click="changeView('companies')">Ok</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="errorModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header" style="background-color: lightcoral">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" id="myModalLabel">Error</h3>
			</div>
			<div class="modal-body">{{ error_message }}</div>
		</div>
	</div>
</div>