goOnApp.controller('busController', function($scope, $http, uiGridConstants, i18nService)									 
{
    $scope.message = 'Maneje su flota de vehículos con facilidad.';
    $scope.error_message = '';
    $scope.custom_response = null;
    $scope.busToDelete = null;
    
    i18nService.setCurrentLang('es');
    
    $scope.busForm = {};
    
    $scope.initForm = function()
    {
	    $scope.busForm.matricula = null;
	    $scope.busForm.marca = null;
	    $scope.busForm.modelo = null;
	    $scope.busForm.ano = null;
	    $scope.busForm.cantAsientos = null;
	    $scope.busForm.cantParados = 0;
	    $scope.busForm.tieneBano = false;
	    $scope.busForm.cantAccesibles = 0;
	    $scope.busForm.cantAnimales = 0;
	    $scope.busForm.cantBultos = 0;
	    $scope.busForm.cantEncomiendas = 0;
    }
    
    $scope.initForm();
    
    $scope.getBuses = function(){
    	$http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
    	{
        	$scope.buses = data;
        	$scope.busesGrid.data = $scope.buses;
    	});
    };
    
    $scope.getBuses();
    
    $scope.getBusDetails = function(bus)
    {
    	$scope.elBus = bus; 
		$("#busDetailsModal").modal('toggle');
    };
    
    $scope.showForm = function()
    {
    	$("#divBusForm").removeClass('hidden');
    	$scope.hideSuccess();    	
    };
    
    $scope.hideForm = function()
    {
    	$("#divBusForm").addClass('hidden');		
    };

    $scope.hideSuccess = function()
    {
    	$("#successAlert").addClass('hidden');
    };
    
    $scope.createBus = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();			
			$http.post(servicesUrl +'createBus', JSON.stringify($scope.busForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
				$scope.initForm();
				$scope.showSuccessAlert('Vehiculo creado.');			
				$scope.getBuses();				
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear el vehículo. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			})
			;    			
		}
	};
	
	$scope.deleteBus = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteBus', JSON.stringify($scope.busToDelete))
		.then(function(response) 
			{
				$.unblockUI();		
	        	if(response.status == 200)
	        	{	       
	        		if (!response.data.success)
	    			{
	    				$scope.error_message = response.data.msg;
	    		    	$("#errorModal").modal("toggle");
	    			}
	        		else
	        		{
	        			$scope.getBuses();	
		        		$scope.showSuccessAlert("El vehiculo ha sido borrado.");	
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
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
	
	$scope.busesGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Matricula', field: 'matricula' },
          { name:'Marca', field: 'marca' },
          { name:'Modelo', field: 'modelo'},
          { name:'Año', field: 'ano' },
          { name: 'Asientos', field: 'cantAsientos' },
          { name: 'Lug. Parados', field: 'cantParados' },
          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>' 
    	  }
        ]
     };
	
	$scope.showDeleteDialog = function(row)
    {
    	$scope.busToDelete = row.entity.id_vehiculo;
    	$("#deleteModal").modal('show');
    };
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
});