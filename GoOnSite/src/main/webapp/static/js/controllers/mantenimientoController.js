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
	    $scope.mantenimientoForm.costo = null;
	    $scope.mantenimientoForm.estado = null;
	    $scope.mantenimientoForm.fecha = null;
	    $scope.mantenimientoForm.taller = null;
	    $scope.mantenimientoForm.vehiculo = null;
	    $scope.mantenimientoForm.user_crea = null;
    }
    
    $scope.initForm();
    
    $scope.getMantenimientos = function(){
    	$http.get(servicesUrl + 'getMantenimientos').success(function(data, status, headers, config) 
    	{
        	$scope.mantenimientos = data;
        	$scope.mantenimientosGrid.data = $scope.mantenimientos;
    	});
    };
    
    $scope.getMantenimientos();
    
    $scope.showMantenimientoForm = function()
    {
    	$("#divForm").removeClass('hidden');
    	$scope.hideSuccess();    	
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
          { name:'Fecha', field: 'fecha'},
          { name:'Taller', field: 'taller' },
          { name:'Vehículo', field: 'vehiculo' },
          { name:'Usuario encargado', field: 'user_crea' },
          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getMantenimientoDetails(row)">Detalles</button>'+
            			 '<button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>' 
            			  
    	  }
        ]
     };
	
	
	$scope.updateVehiculo = function ()   
    {
		
    }
	
	$scope.updateTaller = function ()   
    {
		
    }
		
    
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