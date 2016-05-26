goOnApp.controller('userPanelController', function($scope, $http, uiGridConstants, i18nService) 
{
	$scope.userModel = {};
	
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar tus datos personales, pasajes y encomiendas';
    
    $scope.closeSuccessAlert = function()
    {
    	$("#successAlert").hide();
    }
    
    $scope.showSuccessAlert = function(message)
    {
    	$('#successMessage').text(message);
		$("#successAlert").show();
    };
    
    $scope.showDeleteDialog = function(row)
    {
    	$scope.userToDelete = row.entity.usrname;
    	$("#deleteModal").modal('show');
    };
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
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
     
    $scope.deleteUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'deleteUser', JSON.stringify({ 'usrname': $scope.userToDelete }))
    		.then(function(response) {
	        	if(response.status == 200)
	        	{
	        		$scope.userModel = {};
	        		$scope.getUsers();
	        		$scope.showSuccessAlert("El usuario ha sido borrado.");
	        		$scope.hideDeleteDialog();
	        	}
    		})
		;
    	$.unblockUI();
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