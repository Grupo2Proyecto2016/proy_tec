goOnApp.controller('busController', function($scope, $http, $location) 
{
    $scope.message = 'Maneje su flota de vehículos con facilidad.';
    $scope.error_message = '';
    $scope.custom_response = null;
    
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
				$("#successAlert").removeClass('hidden');				
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
	
	$scope.deleteBus = function(bus)
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteBus', JSON.stringify(bus.id_vehiculo))
		.success(function(data, status, headers, config)
		{
			$.unblockUI();
			$scope.custom_response = data;
			if (!$scope.custom_response.success)
			{
				$scope.error_message = $scope.custom_response.msg;
		    	$("#errorModal").modal("toggle");
			}
		});				
	};
});