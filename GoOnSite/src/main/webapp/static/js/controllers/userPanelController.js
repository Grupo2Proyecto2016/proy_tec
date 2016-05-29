goOnApp.controller('userPanelController', function($scope, $http, $location, uiGridConstants, i18nService) 
{
	$scope.userModel = {};
	$scope.passwordModel = null; //Modelo para cambiar el password
	
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar tus datos personales, pasajes y encomiendas';
    
    $scope.showPasswordModal = function()
    {
    	$scope.passwordModel = null;
    	$("#passwordModal").modal("toggle");
    };
    $scope.hidePasswordModal = function()
    {
    	$("#passwordModal").modal("hide");
    };
    
    $scope.changePassword = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'changePassword', JSON.stringify({ 'passwd': $scope.passwordModel.passwd }))
    		.then(function(response) 
			{
    			$scope.passwordModel = null;
	        	if(response.status == 200)
	        	{
	        		$scope.showSuccessAlert("Tu contraseña ha sido cambiada. Tienes que volver a ingresar a la plataforma.");
	        		$scope.hidePasswordModal();
	        		$scope.$parent.user = null;
	        		shorSignInForm();
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
    
    $scope.showUserDeleteDialog = function()
    {
    	$("#deleteUserModal").modal('show');
    };
    $scope.hideUserDeleteDialog = function()
    {
    	$("#deleteUserModal").modal('hide');
    };
    $scope.deleteUser = function()
    {
    	$.blockUI();
    	$http({
    		method: 'POST',
    		url: servicesUrl + 'deleteSignedUser',
    		data: {},
    		headers: { 'Content-Type': 'application/json' }
    	})
		.then(function(response) {
			$.unblockUI();
        	if(response.status == 200)
        	{
        		$scope.hideUserDeleteDialog();
        		$scope.$parent.signOut();
        	}
		});
    };
    
    $scope.showUpdateUserModal = function()
    {
    	$scope.userModel = angular.copy($scope.$parent.user);
    	$scope.userModel.fch_nacimiento = new Date($scope.userModel.fch_nacimiento);
    	$("#updateUserModal").modal('show');
    };
    $scope.hideUpdateUserModal = function()
    {
    	$("#updateUserModal").modal('hide');		
    	$scope.userModel = {};
    };
    $scope.updateUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'updateClient', JSON.stringify($scope.userModel))
    		.then(function(response) {
	        	if(response.status == 200)
	        	{
	        		$scope.$parent.user = $scope.userModel;
	        		$scope.hideUpdateUserModal();
	        	}
	        	$.unblockUI();
    		})
		;
    };
});