goOnApp.controller('packageController', function($scope, $http, uiGridConstants, i18nService, $timeout) 
{
    $scope.message = 'Desde este panel puedes dar de alta nuevas encomiendas y consulta las existentes';
    $scope.error_message = null;
    $scope.minDate = new Date();
    $scope.maxDate = new Date();
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	
	$scope.packageForm = {};
	$scope.packagePrice = null;
	$scope.packageForm.rDoc = null;
	$scope.packageForm.eDoc = null;
	$scope.rOption = "1";
	$scope.eOption = "1";
	
    $scope.userMarkers = [];
    $scope.destinoMarkers = [];
    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');
    var gService = null;

    $scope.showPackageForm = function()
    {  	
    	$scope.packagePrice = null;
    	$scope.travelsGrid.data = [];
    	$scope.packageForm = {};   	
    	gService = new google.maps.DistanceMatrixService();
    	$("#divPackageForm").removeClass('hidden');
    	$("#packageFormLink").addClass('hidden');
    	$("#packagesGridDiv").addClass('hidden');
    };
    $scope.hidePackageForm = function()
    {  	  	
    	$("#divPackageForm").addClass('hidden');
    	$("#packageFormLink").removeClass('hidden');
    	$("#packagesGridDiv").removeClass('hidden');
    	$scope.getBranchPackages();
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
    
    $scope.getPackageTravels = function()
    {
    	if($scope.packageForm.destino !== undefined)
    	{
    		$.blockUI();
	    	$http.post(servicesUrl + 'getPackageTravels', JSON.stringify($scope.packageForm.destino))
			.then(function(result) 
	    	{
				if(result.status == 200)
				{
					$scope.travels = result.data;
					if(result.data.length == 0)
					{
						$timeout(function () {            
							$scope.showErrorPopup("Actualmente no existen viajes disponibles para el traslado de encomiendas.");    
				        }, 500);
					}
					else
					{
						$scope.travelsGrid.data = $scope.travels;
					}
				}
				$.unblockUI();
	    	});
    	}
    	else
    	{
    		$scope.travels = [];
    	}
    }
    
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
    			$scope.packageForm.volume = $scope.packageForm.alto * $scope.packageForm.ancho * $scope.packageForm.largo / 1000000; 
            	$http.post(servicesUrl + 'calcPackage', JSON.stringify({ distance: $scope.packageForm.distance, weigth: $scope.packageForm.peso, volume: $scope.packageForm.volume  }))
            		.then(function(result){
            			$scope.packagePrice = result.data;
        		});
    		}
    	);
    };
    
    
    $scope.createPackage = function()
    {
    	if($scope.rOption == "1")
		{
			$scope.packageForm.rDoc = null;
		}
		else
		{
			$scope.packageForm.rUser = null;
		}
		
		if($scope.eOption == "1")
		{
			$scope.packageForm.eDoc = null;
		}
		else
		{
			$scope.packageForm.eUser = null;
		}
		
    	if($scope.pForm.$valid && $scope.cForm.$valid)
    	{
    		var rows = $scope.travelGridApi.selection.getSelectedRows();
    		if(rows.length == 0)
    		{
    			$scope.showErrorPopup("Debe seleccionar un viaje para asignar la encomienda.");
    		}
    		else
    		{
    			$scope.packageForm.travel_id = rows[0].id_viaje;
    			
    			$http.post(servicesUrl + 'createPackage', JSON.stringify($scope.packageForm))
        		.then(function(result){
        			if(result.data.success)
        			{
        				$scope.showSuccessAlert("La encomienda ha sido agendada.");
        				$scope.hidePackageForm();
        			}
        			else
        			{
        				$scope.showErrorPopup(result.msg);
        			}
        		});
    		}
    	}
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
		enableRowSelection: true,
		enableRowHeaderSelection: false,
		multiSelect: false,
		modifierKeysToMultiSelect: false,
		noUnselect: true,
		onRegisterApi: function( gridApi ) 
		{
		    $scope.travelGridApi = gridApi;
		},
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: false,
        columnDefs:
    	[
          { name:'Linea', field: 'linea.numero' },
          { name:'Origen', field: 'linea.origen.descripcion' },
          { name:'Destino', field: 'linea.destino.descripcion'},
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText">{{ row.entity.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>' },
          { name:'Tiempo Estimado (min)', field: 'linea.tiempo_estimado' },
          { name:'Nº Coche', field: 'vehiculo.id_vehiculo' },
        ]
    };
    
    $scope.packagesGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
		enableColumnMenus: false,
        columnDefs:
    	[
          { name:'CI emisor', field: 'ci_emisor', width: '80', enableSorting: false },
          { name:'CI receptor', field: 'ci_receptor', width: '90', enableSorting: false },
          { name:'Origen', field: 'viaje.linea.origen.descripcion', width: '*' },
          { name:'Destino', field: 'viaje.linea.destino.descripcion', width: '*' },
          { name:'Peso(kg)', field: 'peso', width: '80'},
          { name:'Volumen(m3)', field: 'volumen', width: '105'},
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText">{{ row.entity.viaje.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>', width: '*' },
          { name:'Nº Coche', field: 'viaje.vehiculo.id_vehiculo', width: '80' },
          { name:'Estado', field: 'statusDes', width: '100'},
          { name: ' ',
          	enableFiltering: false,
          	enableSorting: false,
          	width: '60',
            cellTemplate:'<button style="width: 60px" class="btn-xs btn-warning" ng-show="row.entity.ShowDeliverButton" ng-click="grid.appScope.showDeliverDialog(row)">Entregar</button>'
      	  }
        ]
    };
    $scope.showDeliverDialog = function(row)
    {
    	$scope.packageToDeliver = row.entity.id_encomienda;
    	$("#deliverModal").modal('show');
    };
    $scope.deliverPackage = function()
    {
    	alert("entregado!");
    };
    
    $scope.getBranchPackages = function()
    {
		$.blockUI();
    	$http.get(servicesUrl + 'getBranchPackages')
		.then(function(result) 
    	{
			if(result.status == 200)
			{
				$scope.packages = result.data;
				angular.forEach($scope.packages, function(pack)
				{
					switch(pack.status)
					{
						case 1: pack.statusDes = "Ingresada";
						break;
						case 2: pack.statusDes = "En camino";
						break;
						case 3: pack.statusDes = "Transportada";
						break;
						case 4: pack.statusDes = "Entregada";
						break;
					}
				});
				$scope.packagesGrid.data = $scope.packages;
			}
			$.unblockUI();
    	});
    };
    
    $scope.getBranchPackages();
}); 	