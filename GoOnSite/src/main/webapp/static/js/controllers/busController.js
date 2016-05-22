goOnApp.controller('busController', function($scope, $http, $location) 
{
    $scope.message = 'Maneje su flota de vehículos con facilidad.';
    
    /*$scope.busForm = {};
    $scope.companyForm.name = null;
    $scope.companyForm.trueName = null;
    $scope.companyForm.rut = null;
    $scope.companyForm.phone = null;
    $scope.companyForm.address = null;
    $scope.companyForm.tenantName = null;
    $scope.companyForm.username = null;
    $scope.companyForm.password = null;
    $scope.companyForm.countryId = null;
    $scope.companyForm.user = null;
    $scope.countries = null;*/
   
    $http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
	{
    	$scope.buses = data;
	});
    
    
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
    	
    };
});