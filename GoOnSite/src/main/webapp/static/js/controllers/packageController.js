goOnApp.controller('packageController', function($scope, $http, uiGridConstants, i18nService, $timeout) 
{
    $scope.message = 'Desde este panel puedes dar de alta nuevas encomiendas y consulta las existentes';
    
    $scope.minDate = new Date();
    $scope.maxDate = new Date();
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	$scope.stations = {};
	$scope.nearbyDestinations = {};
    $scope.filteredOrigins = {};
    $scope.travelSearch = {};
    $scope.userMarkers = [];
    $scope.destinoMarkers = [];
    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');

    $scope.getPackageTerminals = function()
    {
    	$http.get(servicesUrl + 'getBranchesTerminals')
    		.then(function(result) 
        	{
            	$scope.originTerminals = result.data;    
            	$scope.destinationTerminals = result.data;
        	}
		);
    };  
    $scope.updateOrigins = function()
    {
    	if($scope.calcForm.destino !== undefined)
    	{
    		$http.post(servicesUrl + 'getPackageOriginTerminals', JSON.stringify($scope.calcForm.destino))
    			.then(function(result) 
				{
    				$scope.originTerminals = result.data;
				}
    		);
    	}
    	else
    	{
    		$scope.getPackageTerminals();
    	}
    };
    $scope.calcPackage = function()
    {
    	var origin = new google.maps.LatLng($scope.calcForm.origen.latitud, $scope.calcForm.origen.longitud);
    	var destination = new google.maps.LatLng($scope.calcForm.destino.latitud, $scope.calcForm.destino.longitud);
    	var result = gService.getDistanceMatrix({
    	    origins: [origin],
    	    destinations: [destination],
    	    travelMode: google.maps.TravelMode.DRIVING,
    	    unitSystem: google.maps.UnitSystem.METRIC,
    	    avoidHighways: false,
    	    avoidTolls: false
    		},
    		function(response, status) 
    		{
    			$scope.calcForm.distance = response.rows[0].elements[0]['distance']['value'] / 1000;
    			var volume = $scope.calcForm.alto * $scope.calcForm.ancho * $scope.calcForm.largo / 1000000; 
            	$http.post(servicesUrl + 'calcPackage', JSON.stringify({ distance: $scope.calcForm.distance, weigth: $scope.calcForm.peso, volume: volume  }))
            		.then(function(result){
            			$scope.packagePrice = result.data;
        		});
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
}); 	