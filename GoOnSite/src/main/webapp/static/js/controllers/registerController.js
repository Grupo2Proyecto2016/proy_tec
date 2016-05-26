goOnApp.controller('registerController', function($scope, $http) 
{
	$scope.message = 'Registrate para hacer uso de nuestros servicios de la forma más eficiente';
	$scope.userModel = {};
    
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
    
    $scope.hideUserForm = function()
    {
    	$("#userForm").addClass('hidden');		
    	$scope.userModel = {};
    };
     
    $scope.registerUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'registerUser', JSON.stringify($scope.userModel))
    		.then(function(response) {
	        	if(response.status == 201)
	        	{
	        		$scope.userModel = {};
	        		$scope.hideUserForm();
	        		$scope.showSuccessAlert("Tu usuario ha sido creado. Ya puedes ingresar haciendo uso de tus credenciales");
	        	}
    		})
		;
    	$.unblockUI();
    };
});