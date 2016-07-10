goOnApp.controller('payPalCheckoutController', function($scope, $http, uiGridConstants, i18nService, $timeout, $rootScope, $routeParams) 
{
	$scope.pagar = function()
	{		
		//sacar del storage los pasajes y mandarlos en el json
		$http.post(servicesUrl +'payPaypal', JSON.stringify($routeParams))
		.then(function(response) 
		{
			$.unblockUI();		
        	if(response.status == 200)
        	{		
    			$scope.payPalInfo = response.data;    			
        	}
		}
		);
	}
	
	$scope.pagar();
});