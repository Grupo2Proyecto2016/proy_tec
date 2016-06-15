goOnApp.controller('travelController', function($scope, $http, uiGridConstants, i18nService, $timeout) 
{
    $scope.message = 'Desde aquí podrás buscar el viaje que deseas y efectuar la compra o reserve de pasajes.';
    
    $scope.minDate = new Date();
    $scope.maxDate = new Date();
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	$scope.stations = {};
	$scope.nearbyDestinations = {};
    $scope.filteredOrigins = {};
    $scope.travelSearch = {};
    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');

    //DESTINATION MAP
    $scope.destinationMap = new google.maps.Map(document.getElementById('destinationMap'), 
    {
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
    });
    var destinationInput = document.getElementById('destination-pac-input');
    var destinationsearchBox = new google.maps.places.SearchBox(destinationInput);
    $scope.destinationMap.controls[google.maps.ControlPosition.TOP_LEFT].push(destinationInput);
    //
    //ORIGIN MAP
    $scope.originMap = new google.maps.Map(document.getElementById('originMap'), 
    {
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
    });
    var originInput = document.getElementById('origin-pac-input');
    var originSearchBox = new google.maps.places.SearchBox(originInput);
    $scope.originMap.controls[google.maps.ControlPosition.TOP_LEFT].push(originInput);
    //
    
    $scope.getStations = function()
    {
    	$http.get(servicesUrl + 'getStations')
    		.then(function (result){
    			if(result.status == 200)
    			{
    				$scope.stations = result.data;
    				angular.forEach($scope.stations, function(station, key) {
    					var marker = new google.maps.Marker({
    						position: new google.maps.LatLng(station.latitud,station.longitud),
    						map: $scope.destinationMap,
    						title: station.descripcion
    					});
					  
    				});
				}
		});
    };
    
    $scope.getFilteredOrigins = function()
    {
    	$http.get(servicesUrl + 'getFilteredOrigins', JSON.Stringlify($scope.nearbyDestinations))
    		.then(function (result){
    			if(result.status == 200)
    			{
    				$scope.filteredOrigins = result.data;
    				//aca hay que cargar todos los puntos en el mapa de origenes
    			}
    		}
		);
    };
    
    $scope.closeSuccessAlert = function()
    {
    	$("#successAlert").hide();
    };
    
    $scope.showSuccessAlert = function(message)
    {
    	$('#successMessage').text(message);
		$("#successAlert").show();
    }; 
    
    $scope.searchTravels = function()
    {
    	$http.get(servicesUrl + 'searchtravels')
    		.then(function(result) {
    			if(result.status == 200)
    			{
    				$scope.travels = data;
    				$scope.travelsGrid.data = $scope.travels;    				
    			}
			}
		);
    };
    
    $scope.travelsGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Linea', field: 'linea.numero' },
          { name:'Origen', field: 'linea.origen.descripcion' },
          { name:'Destino', field: 'linea.destino.descripcion'},
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText">{{ row.entity.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>' },
          { name:'Tiempo Estimado (min)', field: 'linea.tiempo_estimado' },
          { 
        	  name: 'Pasajeros Parados', 
        	  cellTemplate: '<div class="text-center ngCellText">{{row.entity.linea.viaja_parado | SiNo}}</div>'
          },
          { name:'Nº Coche', field: 'vehiculo.id_vehiculo' },
//          { name: 'Acciones',
//        	enableFiltering: false,
//        	enableSorting: false,
//            cellTemplate:'<p align="center"><button style="" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button></p>'
//    	  }
        ]
    };
    
    $scope.showDestinationMap = function()
    {
    	$("#destinationModal").modal('show');
    	$timeout(function () {            
    		google.maps.event.trigger($scope.destinationMap, 'resize');
        }, 400);
    };
    
    $scope.showOriginMap = function()
    {
    	$("#originModal").modal('show');
    	$timeout(function () { 
    		google.maps.event.trigger($scope.originMap, 'resize');
    	}, 400);
    };
    
    $scope.buyTicket = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'buyTicket', JSON.stringify($scope.ticketForm))
		.then(function(response) 
			{
				$.unblockUI();		
	        	if(response.status == 200)
	        	{	       
	        		if (!response.data.success)
	    			{
//	    				$scope.error_message = response.data.msg;
//	    		    	$("#errorModal").modal("toggle");
	        			$scope.showSuccessAlert("Los boletos han sido acreditados. Accede a tu panel para descargarlos.");	
	    			}
	        		else
	        		{
//	        			$scope.getTravels();
//		        		$scope.showSuccessAlert("El viaje ha sido borrada.");	
	        		}	        		
	        	}
	        	$scope.hideBuyDialog();
    		}
		);				
	};
	$scope.showBuyDialog = function(row)
    {
    	$("#buyModal").modal('show');
    };
    $scope.hideBuyDialog = function(row)
    {
    	$("#buyModal").modal('hide');
    };
    
    
    $scope.getStations();
}); 	