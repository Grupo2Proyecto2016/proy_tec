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
        		$('div.modal-backdrop.fade.in').remove();
        		$scope.$parent.signOut();
        	}
		});
    };
    
    $scope.showUserForm = function()
    {
    	$scope.userModel = {}; 
    	$("#userEditForm").addClass('hidden');
    	$("#userForm").removeClass('hidden');
    };
    
    $scope.hideUserForm = function()
    {
    	$("#userForm").addClass('hidden');		
    	$scope.userModel = {};
    };
    
    $scope.hideUserUpdateForm = function()
    {
    	$("#userEditForm").addClass('hidden');		
    	$scope.userModel = {};
    };
    
    $scope.showUserUpdateForm = function(userRow)
    {
    	$scope.userModel.usrname = userRow.entity.usrname;
    	$scope.userModel.nombre = userRow.entity.nombre;
    	$scope.userModel.apellido = userRow.entity.apellido;
    	$scope.userModel.telefono = userRow.entity.telefono;
    	$scope.userModel.email = userRow.entity.email;
    	$scope.userModel.direccion = userRow.entity.direccion;
    	$scope.userModel.telefono = userRow.entity.telefono;
    	$scope.userModel.fch_nacimiento = new Date(userRow.entity.fch_nacimiento);
    	$scope.userModel.rol_id_rol = 2;
    	//$("select[name=rol]").val($scope.userModel.rol_id_rol); //SELECT DE MIERDA!
    	
    	$("#userForm").addClass('hidden');
    	$("#userEditForm").removeClass('hidden');
    };
     
    $scope.updateUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'updateUser', JSON.stringify($scope.userModel))
    		.then(function(response) {
	        	if(response.status == 200)
	        	{
	        		$scope.userModel = {};
	        		$scope.hideUserUpdateForm();
	        		$scope.getUsers();
	        		$scope.showSuccessAlert("El usuario ha sido actualizado.");
	        	}
    		})
		;
    	$.unblockUI();
    };
});