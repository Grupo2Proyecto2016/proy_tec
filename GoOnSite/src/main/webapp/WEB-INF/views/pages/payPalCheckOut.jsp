<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">

</div>
<br/>
<div class="row">
	<div class="col-xs-2"></div>
	<div class="col-xs-8">
		<div class="panel panel-default" id="seatsInfo">
			<div class="panel-heading">
				<h4 class="modal-title">Asientos Confirmados</h4>
			</div>
			<div class="panel-body">	
				<div class="container">
				    <div class="row" ng-repeat="cs in confirmedSeats track by $index">
				        <div class="col-xs-8">
				            <div class="well well-sm">
				                <div class="row">
				                    <div class="col-sm-6 col-md-4">
				                        	<qrcode version="3" error-correction-level="M" size="150" data="{{cs.numero}}"></qrcode>		                  		
				                    </div>
				                    <div class="col-sm-6 col-md-8">
				                    	<h4><i class="fa fa-bus" aria-hidden="true"></i> Línea {{cs.viaje.linea.id_linea}} - Coche: {{cs.viaje.vehiculo.id_vehiculo}} - <i class="fa fa-hashtag" aria-hidden="true"> Asiento</i> {{cs.asiento.numero}}</h4>
				                        <small> <i class="fa fa-map-marker"></i>&nbsp;Desde:<cite title="Montevideo">Montevideo</cite></small>
				                        <small> <i class="fa fa-map-marker"></i>&nbsp;Hasta:<cite title="Piriapolis">Piriapolis </cite></small>
				                        <p>
				                        	<i class="fa fa-calendar"></i>&nbsp;Fecha: 10/07/2016
				                            <br />
				                            <i class="fa fa-clock-o"></i>&nbsp;Salida de Terminal: 20:30
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
	<div class="col-xs-2"></div>
</div>	
<script>
window.onload = function(){
	if(window.opener){
		window.close();
	} 
	else{
		if(top.dg.isOpen() == true){
			top.dg.closeFlow();
			return true;
		}
	}                              
};                             
</script>