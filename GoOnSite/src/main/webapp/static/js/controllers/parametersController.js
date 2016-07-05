goOnApp.controller('parametersController', function($scope, $http, $filter, uiGridConstants, i18nService) 
{
	$scope.message = 'La configuración de estos parámetros optimizará el uso de la plataforma para ti y para los clientes';
	
	$scope.priceByKg = null;
	$scope.priceByVolume = null;
	$scope.priceByTravelKm = null;
	$scope.priceByPackageKm = null;
	$scope.maxReservationDelay = null;
	$scope.stopDelay = null;
	
	$scope.getParameters = function(){
		$http.get(servicesUrl + 'getParams')
		.then(function(response) {
			if(response.status == 200)
			{
				angular.forEach(response.data, function(param, key) {
					  switch(param.id)
					  {
					  	case 1: $scope.priceByKg = param;
					  		break;
					  	case 2: $scope.priceByVolume = param;
					  		break;
					  	case 3: $scope.priceByTravelKm = param;
					  		break;
					  	case 4: $scope.priceByPackageKm = param;
					  		break;
					  	case 5: $scope.maxReservationDelay = param;					  	
					  		break;
					  	case 6: $scope.stopDelay = param;
					  		break;
					  	case 7: $scope.packageBasePrice = param;
					  }
				});
			}
		});
	};
	
	$scope.getParameters();
	
	$scope.updateParameters = function()
	{
		$.blockUI();
		var params = [];
		params.push($scope.priceByKg);
		params.push($scope.priceByVolume);
		params.push($scope.priceByTravelKm);
		params.push($scope.priceByPackageKm);
		params.push($scope.maxReservationDelay);
		params.push($scope.stopDelay);
		params.push($scope.packageBasePrice);
		
		$http.post(servicesUrl + 'updateParameters', JSON.stringify(params))
		.then(function(response) {
			if(response.status == 200)
			{
				$scope.getParameters();
				$scope.showSuccessAlert("Los parámetros de configuración han sido actualizados.");
			}
			$.unblockUI();
		})
		;
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
});