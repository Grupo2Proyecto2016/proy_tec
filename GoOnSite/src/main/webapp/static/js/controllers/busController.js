goOnApp.controller('busController', function($scope, $http, $location) 
{
    $scope.message = 'Maneje su flota de vehículos con facilidad.';
   
    $http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
	{
    	$scope.buses = data;
	});
    
    
    $scope.getBusDetails = function(bus)
    {
    	$scope.elBus = bus; 
		$("#busDetailsModal").modal('toggle');
    };
    
    $scope.muestraForm = function(bus)
    {
    	$scope.elBus = bus; 
		$("#divBusForm").toggleClass('hidden');
    };

});