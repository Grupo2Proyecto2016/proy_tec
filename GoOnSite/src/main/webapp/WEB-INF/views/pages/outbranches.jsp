<div id="divTitleOutBranches" class="jumbotron text-center">
	<h3>Sucursales</h3>	
	<p>{{ message }}</p>	
</div>

<div class="row">
	<div class="col-xs-7">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div><h4 class="modal-title">Mapa</h4></div>
			</div>
			<div class="panel-body">
				<input id="pac-input" class="controls" type="text" placeholder="Especific� tu ubicaci�n">
				<div id="map" style="height: 50%">
				
				</div>
			</div>
		</div>
	</div>
	<div class="col-xs-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div><h4 class="modal-title">Datos</h4></div>
			</div>
			<div class="panel-body">
				<table class="table table-striped">
   					<tr ng-repeat="b in branches track by $index">
						<td>
							<p><span class="badge pull-left">{{$index + 1}}</span>&nbsp; <b>{{b.nombre}}</b></p>
							<p>{{b.direccion}}</p>
						</td>			        
					</tr>				
				</table>
			</div>
		</div>
	</div>
</div>