goOnApp.controller('packageController', function($scope, $http, uiGridConstants, i18nService, $timeout) 
{
    $scope.message = 'Desde este panel puedes dar de alta nuevas encomiendas y consulta las existentes';
    $scope.error_message = null;
    $scope.minDate = new Date();
    $scope.maxDate = new Date();
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	
    $scope.travelSearch = {};
    $scope.userMarkers = [];
    $scope.destinoMarkers = [];
    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');
    var gService = null;

    $scope.showPackageForm = function()
    {  	
    	$scope.packageForm = {};   	
    	gService = new google.maps.DistanceMatrixService();
    	$("#divPackageForm").removeClass('hidden');
    	$("#packageFormLink").addClass('hidden');
    };
    $scope.hidePackageForm = function()
    {  	  	
    	$("#divPackageForm").addClass('hidden');
    	$("#packageFormLink").removeClass('hidden');
    };
    $scope.showErrorPopup = function(message)
    {
    	$scope.error_message = message;
    	$("#errorModal").modal("show");
    };
    $scope.hideErrorPopup = function()
    {
    	$("#errorModal").modal("hide");
    	$scope.error_message = null;
    };
    
    $scope.getPackageOrigin = function()
    {
    	$http.get(servicesUrl + 'getPackageOrigin')
    		.then(function(result) 
        	{
    			if(result.status == 200)
    			{
    				if(result.data == "")
    				{
    					$timeout(function () {            
    						$scope.showErrorPopup("Esta sucursal no está habilitada para recibir encomiendas.");    
    			        }, 500);
    					$scope.packageOrigin = null;
    				}
    				else
    				{
    					$scope.packageOrigin = result.data;
    					$scope.getPackageDestinations();
    				}
    			}
        	}
		);
    };
    
    $scope.getPackageDestinations = function()
    {
    	$http.get(servicesUrl + 'GetPackageDestinationsByLocalBranch')
    		.then(function(result) 
        	{
    			if(result.status == 200)
    			{
    				$scope.destinationTerminals = result.data;
    				if(result.data.length == 0)
    				{
    					$scope.packageOrigin = null;
    					$timeout(function() {   
    						$scope.showErrorPopup("Actualmente no hay ningún destino de encomiendas disponible."); 
    					}, 500);
					}
    			}
        	}
		);
    };  
   
    $scope.getPackageOrigin();
    
    $scope.calcPackage = function()
    {
    	if($scope.packageForm.peso != null || $scope.volumeInputsFilled())
    	{
    		$scope.calc_error = null;
	    	var origin = new google.maps.LatLng($scope.packageOrigin.latitud, $scope.packageOrigin.longitud);
	    	var destination = new google.maps.LatLng($scope.packageForm.destino.latitud, $scope.packageForm.destino.longitud);
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
	    			$scope.packageForm.distance = response.rows[0].elements[0]['distance']['value'] / 1000;
	    			var volume = $scope.packageForm.alto * $scope.packageForm.ancho * $scope.packageForm.largo / 1000000; 
	            	$http.post(servicesUrl + 'calcPackage', JSON.stringify({ distance: $scope.packageForm.distance, weigth: $scope.packageForm.peso, volume: volume  }))
	            		.then(function(result){
	            			$scope.packagePrice = result.data;
	        		});
	    		}
	    	);
    	}
    	else
    	{
    		$scope.calc_error = "Debe completar los datos de peso o volumen para realizar el cálculo.";
    	}
    };
    $scope.volumeInputsFilled = function()
    {
    	return $scope.packageForm.alto != null && $scope.packageForm.largo != null && $scope.packageForm.ancho != null;
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