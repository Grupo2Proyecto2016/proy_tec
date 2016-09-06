goOnApp.controller('registerController', function($scope, $location, $rootScope, $http) 
{
	$scope.message = 'Regístrate para hacer uso de nuestros servicios de la forma más eficiente';
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
    	if(!$scope.userForm.$invalid)
    	{
	    	$.blockUI();
	    	$http.post(servicesUrl + 'registerUser', JSON.stringify($scope.userModel))
	    		.then(function(response) {
		        	if(response.status == 201)
		        	{
		        		$scope.signIn($scope.userModel.usrname, $scope.userModel.passwd);
		        		$scope.userModel = {};
		        		//$location.path('home');
		        		$scope.hideUserForm();
		        		$scope.showSuccessAlert("Tu usuario ha sido creado. Ya puedes hacer uso de nuestros servicios");
		        	}
	    		})
			;
	    	$.unblockUI();
    	}
    };
    
    $scope.signIn = function(username, password)
    {
    	$http.post(servicesUrl + 'auth', JSON.stringify({ 'username' : username, 'password' : password }))
        	.then(function(response) 
			{
        		if(response.status == 200)
        		{
        			$rootScope.user = response.data.user;
        			setJwtToken(response.data.token);
        			//$scope.loginForm = null;
        			//$("#loginModal").modal("toggle");
        		}
//        		else
//        		{
//        			$scope.loginForm = null;
//        			$("#loginAlert").show();
//        			$('#username').focus();
//        		}
        	}
    	);
    };
});