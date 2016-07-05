<div id="divTitleOutBranches" class="jumbotron text-center">
	<h3>Sucursales</h3>	
	<p>{{ message }}</p>	
</div>

<div class="row">
	<div class="col-xs-7">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div><h4 class="modal-title">Ubicaciones</h4></div>
			</div>
			<div class="panel-body">
				<input id="pac-input" class="controls" type="text" placeholder="Especificá tu ubicación">
				<div id="map" style="height: 50%">
				
				</div>
			</div>
		</div>
	</div>
	<div class="col-xs-5">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div><h4 class="modal-title">Lista de Sucursales</h4></div>
			</div>
			<div class="panel-body" style="height: 54%; overflow: auto;">
				<table class="table table-striped">
   					<tr ng-repeat="b in branches track by $index">
						<td>
							<div><i class="fa fa-hashtag fa-lg" aria-hidden="true"></i><b>{{$index + 1}}-&nbsp; {{b.nombre}}</b></div>
							<div><i class="fa fa-building fa-lg" aria-hidden="true"></i>&nbsp;{{b.direccion}}</div>
							<div><i class="fa fa-skype fa-lg" aria-hidden="true"></i>&nbsp;<a ng-href="skype:{{b.telefono}}?call">>{{b.telefono}}</a></div>							
							<div><i class="fa fa-envelope fa-lg" aria-hidden="true"></i>&nbsp;<a ng-href="mailto:{{b.mail}}?Subject=Contacto desde GoOnSite">{{b.mail}}</a></div>
						</td>			        
					</tr>				
				</table>
			</div>
		</div>
	</div>
</div>