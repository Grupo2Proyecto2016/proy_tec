goOnApp.controller('manageTravelsController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Gestiona viajes para las lineas asignando vehículos y choferes';
    $scope.travelForm = {};
	$scope.drivers = null;
    $scope.lines = null;
    $scope.buses = null;
    $scope.maxDate = new Date();
	$scope.minDate = new Date();
	$scope.minDate.setDate($scope.minDate.getDate() + 1);
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	
	$scope.isDayMissing = function()
	{
		return !$scope.travelForm.monday 
			&& !$scope.travelForm.tuesday 
			&& !$scope.travelForm.wednesday 
			&& !$scope.travelForm.thursday 
			&& !$scope.travelForm.friday 
			&& !$scope.travelForm.saturday 
			&& !$scope.travelForm.sunday
		;
	};
    
	$scope.custom_response = null;    
    $scope.txt_minutos = "";
    $scope.txt_km = "";
    i18nService.setCurrentLang('es');
    
    
    $scope.getDrivers = function()
    {
    	$http.get(servicesUrl + 'getDrivers').success(function(data, status, headers, config) {
    		$scope.drivers = data;        	
		});
    };    
    
    $scope.getBuses = function(){
    	$http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
    	{
        	$scope.buses = data;
    	});
    };
    
    $scope.getLines = function()
    {
    	$http.get(servicesUrl + 'getLines').success(function(data, status, headers, config){
    		$scope.lines = data;        	
		});
    };

    $scope.showForm = function()
    {
    	$scope.travelForm = {};
    	$scope.getBuses();
    	$scope.getDrivers();
    	$scope.getLines();
    	$("#divTravelForm").removeClass('hidden');    	
    };
    
    $scope.hideForm = function()
    {
    	$("#divTravelForm").addClass('hidden');		
    };
    
    $scope.createTravels = function()
    {
    	if(!$scope.form.$invalid && !$scope.isDayMissing())
		{
    		$.blockUI();
    		$http.post(servicesUrl +'createTravel', JSON.stringify($scope.travelForm))
			.success(function()
			{				
				//$scope.hideForm();
		    	//$scope.travelForm = {};
		    	$.unblockUI();
				$scope.showSuccessAlert("Los viajes han sido creados");							
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear los viajes. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			});  
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
});