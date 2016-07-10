goOnApp.controller('payPalCheckoutController', function($scope, $http, uiGridConstants, i18nService, $timeout, $rootScope, $routeParams) 
{
	$scope.pagar = function()
	{		
		//sacar del storage los pasajes y mandarlos en el json
		$scope.seatsForm = JSON.parse(localStorage.getItem(/*getJwtToken() + */"userTickets"));
		$scope.seatsForm.paymentId = $routeParams.paymentId;
		$scope.seatsForm.token = $routeParams.token;
		$scope.seatsForm.PayerID = $routeParams.PayerID;		
		$http.post(servicesUrl +'payPaypal', JSON.stringify($scope.seatsForm))
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