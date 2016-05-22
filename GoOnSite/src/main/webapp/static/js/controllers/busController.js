goOnApp.controller('busController', function($scope, $http, $location) 
{
    $scope.message = 'Maneje su flota de vehículos con facilidad.';
    
    $scope.busForm = {};
    $scope.busForm.matricula = null;
    $scope.busForm.marca = null;
    $scope.busForm.modelo = null;
    $scope.busForm.ano = null;
    $scope.busForm.cantAsientos = null;
    $scope.busForm.cantParados = null;
    $scope.busForm.tieneBano = false;
    $scope.busForm.cantAccesibles = null;
    $scope.busForm.cantAnimales = null;
    $scope.busForm.cantBultos = null;
    $scope.busForm.cantEncomiendas = null;
    
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
    
    $scope.showForm = function(bus)
    {
    	$scope.elBus = bus; 
    	$("#divBusForm").removeClass('hidden');
    };
    
    $scope.hideForm = function(bus)
    {
    	$scope.elBus = bus;
    	$("#divBusForm").addClass('hidden');		
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
				$("#successAlert").removeClass('hidden');
				$scope.getBuses();
				$scope.hideForm();
			})
			.error(function()
			{
				$.unblockUI();
				$("#errorModal").modal("toggle");
			})
			;    			
		}
	};
});