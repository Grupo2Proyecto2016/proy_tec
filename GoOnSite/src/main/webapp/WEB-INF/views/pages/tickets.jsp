<div class="jumbotron text-center">
	<h3>Boletos activos</h3>
	
	<p>{{ message }}</p>
</div>

<div id="successAlert" class="row" style="display: none">
	<div class="col-xs-1"></div>
	<div class="col-xs-10">
		<div class="alert alert-success" style="">
		  <button type="button" class="close" ng-click="closeSuccessAlert()" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Éxito! </strong> <p id="successMessage"></p>
		</div>
	</div>
	<div class="col-xs-1"></div>
</div>


<div id="ticketsGridDiv" class="row" style="margin-top: 50px;">
	<div class="row">
		<div class="col-xs-3"></div>
		<div class="col-xs-6">
			<div class="alert alert-warning text-center">
			  <p>Se muestran pasajes en un rango de 30 días</p>
			</div>
		</div>
		<div class="col-xs-3"></div>
	</div>
	
	<div class="col-xs-1"></div>	
	<div class="col-xs-10">
		<form class="form-horizontal" name="packsForm" role="form" ng-submit="getActiveTickets()">
			<div class="form-group"> 
				<div class="col-sm-1"></div>
				<div class="col-sm-6">
					<label class="control-label col-sm-3">Desde:</label>
					<div class="col-sm-9">
						<input type="date" class="form-control"  ng-model="filterMinDate" required>
				    </div>
				</div>
	    		<div class="col-sm-1">
	      			<button style="float: right; margin-top: 10px;" type="submit"  class="btn btn-sm btn-info">Filtrar</button>
	    		</div>
	    		<div class="col-sm-1"></div>
		    </div>
	    </form>
		<div ui-grid="ticketsGrid" ui-grid-pagination></div>
	</div>
	<div class="col-xs-1"></div>
</div>

<div class="modal fade" id="cancelTicketModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" id="myModalLabel">Cancelación de pasaje</h3>
      </div>
      <div class="modal-body">
	  	<h5 class="text-center">¿Comfirma la cancelación del pasaje?</h5>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
			<button type="button" class="btn btn-warning" ng-click="cancelTicket()">Eliminar</button>
		</div>        
      </div>
    </div>
  </div>
 </div>
 
 <div class="modal fade" id="reservationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header warning">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" id="myModalLabel">Confirmación de reserva</h3>
      </div>
      <div class="modal-body">
	  	<h5 class="text-center">¿Desea confirmar la reserva?</h5>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
			<button type="button" class="btn btn-warning" ng-click="confirmReservation()">Confirmar</button>
		</div>        
      </div>
    </div>
  </div>
 </div>
 
 <div class="modal fade" id="viewTicketModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header text-center form">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 class="modal-title" id="myModalLabel">Boleto</h3>
        <h5 class="text-center" ng-click="printDiv()"> &nbsp;<i class="fa fa-print" aria-hidden="true"></i> Imprimir</h5>
      </div>
      <div class="modal-body">
	        <div class="panel-body" id="printSection">	
				<div id="forPrint">
				    <div>
				        <div>
				            <div class="well well-sm">
				                <div class="row">
				                    <div class="col-sm-6 col-md-4">
				                    	<qrcode version="3" error-correction-level="M" size="150" data="{{ticketToShow.numero}}"></qrcode>
				                    </div>
				                    <div class="col-sm-6 col-md-8">
				                    	<h4><i class="fa fa-bus" aria-hidden="true"></i> Línea {{ticketToShow.viaje.linea.id_linea}} - Coche: {{ticketToShow.viaje.vehiculo.id_vehiculo}} - <i class="fa fa-hashtag" aria-hidden="true"> Asiento</i> {{ticketToShow.asiento.numero}}</h4>
				                        <small> <i class="fa fa-map-marker"></i>&nbsp;Desde:<cite title="Montevideo">{{ticketToShow.parada_sube.descripcion}}</cite></small>
				                        <small> <i class="fa fa-map-marker"></i>&nbsp;Hasta:<cite title="Piriapolis">{{ticketToShow.parada_baja.descripcion}}</cite></small>
				                        <p>
				                        	<i class="fa fa-calendar"></i>&nbsp;Fecha: {{ticketToShow.viaje.inicio | date:'dd-MM-yyyy'}}
				                            <br />
				                            <i class="fa fa-clock-o"></i>&nbsp;Salida de Terminal:{{ticketToShow.viaje.inicio | date:'HH:nn'}} 
				                            <br />
				                        </p>		                        
				                    </div>
				                </div>
				            </div>
				        </div>
				    </div>
				</div>
			</div>
      </div>
    </div>
  </div>
</div>