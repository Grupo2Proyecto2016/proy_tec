goOnApp.controller('branchController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Maneje sus sucursales de manera ágil.';
    $scope.error_message = '';
    $scope.custom_response = null;    
    
    i18nService.setCurrentLang('es');
    
    $scope.branchForm = {};
    
    $scope.initForm = function()
    {
	    $scope.branchForm.nombre = null;
	    $scope.branchForm.direccion = null;
	    $scope.branchForm.telefono = null;
	    $scope.branchForm.mail = null;
	    $scope.branchForm.latitud = null;
	    $scope.branchForm.longitud = null;
    };
    
    $scope.initForm();
    
    $scope.showForm = function()
    {
    	$("#divBranchForm").removeClass('hidden');    	
    };
    
    $scope.hideForm = function()
    {
    	$("#divBranchForm").addClass('hidden');		
    };
    
    $scope.actualizoMarker = function (lat, lng)
    {
    	$scope.branchForm.latitud = lat;
    	$scope.branchForm.longitud = lng; 
    };
    
    $scope.createBranch = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();			
			$http.post(servicesUrl +'createBranch', JSON.stringify($scope.branchForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
				$scope.initForm();
				$scope.showSuccessAlert("Sucursal creada.");			
				//$scope.getBranches();				
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear la sucursal. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			})
			;    			
		}
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
