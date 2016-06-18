goOnApp.controller('mantenimientoController', function($scope, $http, uiGridConstants, i18nService)									 
{
    $scope.message = 'Maneje el mantenimiento de sus vehículos con facilidad.';
    $scope.error_message = '';
    $scope.custom_response = null;
    $scope.mantenimientoToDelete = null;
    
    i18nService.setCurrentLang('es');
    
    $scope.mantenimientoForm = {};
    
    $scope.initForm = function()
    {
	    $scope.mantenimientoForm.costo = 0;
	    $scope.mantenimientoForm.estado = 0;
	    $scope.mantenimientoForm.fecha = null;
	    $scope.mantenimientoForm.taller = 0;
	    $scope.mantenimientoForm.vehiculo = 0;
	    $scope.mantenimientoForm.user_crea = 0;
	    $scope.user = null;
    }
    
    $scope.initForm();
    
    $scope.getMantenimientos = function(){
    	$http.get(servicesUrl + 'getMantenimientos').success(function(data, status, headers, config) 
    	{
        	$scope.mantenimientos = data;
        	$scope.mantenimientosGrid.data = $scope.mantenimientos;
    	});
    };
    
    $scope.getBuses = function(){
    	$http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
    	{
        	$scope.buses = data;
    	});
    };
    
    $scope.getTalleres = function(){
    	$http.get(servicesUrl + 'getTalleres').success(function(data, status, headers, config) 
    	{
        	$scope.talleres = data;
    	});
    };
    
    $scope.getMantenimientos();
    
    $scope.showForm = function()
    {
    	$("#divMantenimientoForm").removeClass('hidden');
    	$scope.hideSuccess(); 
    	$scope.getBuses();
    	$scope.getTalleres();
    };
    
    $scope.hideForm = function()
    {
    	$("#divMantenimientoForm").addClass('hidden');		
    };

    $scope.hideSuccess = function()
    {
    	//$("#successAlert").addClass('hidden');
    };
    
    $scope.createMantenimiento = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();
			
			$http.get(servicesUrl + 'getUserInfo')
			.then(function(response) 
			{
				if(response.status == 200)
				{
					$scope.user = response.data;
				}
				else
				{
					$scope.user = null;
					removeJwtToken();
				}
				$scope.userInfoReady = true;
			}
			);
			
			$scope.mantenimientoForm.user_crea = $scope.user
			$http.post(servicesUrl +'createMantenimiento', JSON.stringify($scope.mantenimientoForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
				$scope.initForm();
				$scope.showSuccessAlert("Vehiculo enviado al mantenimiento.");	
				$scope.getMantenimientos();				
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al enviar el vehículo. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			})
			;    			
		}
	};
	
	$scope.closeSuccessAlert = function()
    {
    	$("#successAlert").hide();
    }
    
    $scope.showSuccessAlert = function(message)
    {
    	$('#successMessage').text(message);
		$("#successAlert").show();
    };
	
	$scope.mantenimientosGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Costo', field: 'costo' },
          { name:'Estado', field: 'estado' },
          { name:'fecha', field: 'fecha'},
          { name:'Taller', field: 'taller' },
          { name: 'Usuario', field: 'user_crea' },
          { name:'Vehículo', field: 'vehiculo'},
          
          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getMantenimientosDetails(row)">Detalles</button>'+
            			 '<button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>' 
            			  
    	  }
        ]
     };
	
	
	$scope.showUpdateVehiculo = function()
	{
		$("#updateVehiculoModal").modal('show');
	};
	$scope.hideUpdateTaller = function()
	{
		$("#updateTallerModal").modal('hide');
	};
	
	
//	$scope.updateVehiculo = function ()   
//    {
//		$scope.getVehiculoById($scope.mantenimientoForm.vehiculo);
//    }
//	
//	$scope.updateTaller = function ()   
//    {
//		
//    }
		
    
	$scope.showDeleteDialog = function(row)
    {
    	$scope.mantenimientoToDelete = row.entity.id_vehiculo;
    	$("#deleteModal").modal('show');
    };
    
    $scope.getMantenimientoDetails = function(row)
    {
    	$scope.elMantenimiento = row.entity; 
		$("#mantenimientoDetailsModal").modal('toggle');
    };
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
});