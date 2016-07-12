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
    			$scope.confirmedSeats = $scope.payPalInfo.tickets; 
    			$scope.$digest();
        	}
		}
		);
	}	
	$scope.pagar();
	
	$scope.printDiv = function(idDiv)
    {
    	
    	
    	//var printSection = document.getElementById('printSection');

        // if there is no printing section, create one
//        if (!printSection) {
//            printSection = document.createElement('div');
//            printSection.id = 'printSection';
//            document.body.appendChild(printSection);
//        }
//        var elemToPrint = document.getElementById(idDiv);
//        
//        var domClone = elemToPrint.cloneNode(true);
//        
//        printSection.appendChild(domClone);
        
        window.print();
        
        //printSection.innerHTML = '';
    }
});