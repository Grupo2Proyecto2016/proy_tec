<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
	<div class="jumbotron text-center">
		<h3>Parámetros de la plataforma</h3>

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
	
	<div class="row">
		<div class="col-xs-3"></div>
		<div class="col-sm-6">
			<form class="form-horizontal" role="form" name="form" ng-submit="updateParameters()">
				<div class="form-group">
					<label class="control-label col-sm-6" for="priceByTravelKm">Precio por kilómetro de viajes ($U):</label>
					<div class="col-sm-6">
						<input type="number" min="0.1" step="0.1" name="priceByTravelKm" class="form-control" ng-model="priceByTravelKm.valor" required>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-6" for="packageBasePrice">Precio mínimo de encomiendas ($U):</label>
					<div class="col-sm-6">
						<input type="number" min="1" step="1" name="packageBasePrice" class="form-control" ng-model="packageBasePrice.valor" required>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-6" for="priceByPackageKm">Precio por kilómetro de encomiendas ($U):</label>
					<div class="col-sm-6">
						<input type="number" min="0.1" step="0.1" name="priceByPackageKm" class="form-control" ng-model="priceByPackageKm.valor" required>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-6" for="priceByKg">Precio por kilo de paquete ($U):</label>
					<div class="col-sm-6">
						<input type="number" min="0.1" step="0.1" name="priceByKg" class="form-control" ng-model="priceByKg.valor" required>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-6" for="priceByVolume">Precio por metro cúbico de paquete ($U):</label>
					<div class="col-sm-6">
						<input type="number" min="0.1" step="0.1" name="priceByVolume" class="form-control" ng-model="priceByVolume.valor" required>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-6" for="maxReservationDelay">Plazo máximo para cancelación de reservas (min):</label>
					<div class="col-sm-6">
						<input type="number" min="1" step="1" name="maxReservationDelay" class="form-control" ng-model="maxReservationDelay.valor" required>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-6" for="stopDelay">Demora promedio por parada (min):</label>
					<div class="col-sm-6">
						<input type="number" min="1" step="1" name="stopDelay" class="form-control" ng-model="stopDelay.valor" required>
					</div>
				</div>
				<div class="form-group"> 
				    <div class="col-sm-offset-6 col-sm-6">
				      <button style="float: right" type="submit" class="btn btn-info">Actualizar</button>
				    </div>
				</div>
			</form>
		</div>
	</div>
