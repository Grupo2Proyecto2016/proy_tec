goOnApp.controller('companyController', function($scope, $http, $filter, uiGridConstants, i18nService) 
{
	$scope.message = 'Aquí puedes editar los datos de la empresa y añadir un poco de personalización';
	$scope.companyForm = angular.copy($scope.$parent.company);
	$scope.countries = null;
	
	$http.get(AppName + 'countries').
	    success(function(data, status, headers, config) 
		{
	    	$scope.countries = data;
	    })
    ;
	
	$scope.styles = [
		{ name: 'Cerulean',  value: 'cerulean' },
		{ name: 'Cosmo', value: 'cosmo' },
		{ name: 'Cyborg', value: 'cyborg' },
		{ name: 'Darkly', value: 'darkly' },
		{ name: 'Flatly', value: 'flatly' },
		{ name: 'Journal', value: 'journal' },
		{ name: 'Lumen', value: 'lumen' },
		{ name: 'Paper', value: 'paper' },
		{ name: 'Readable', value:'readable' },
		{ name: 'Simplex', value: 'simplex' },
		{ name: 'Slate', value: 'slate' },
		{ name: 'Superhero', value: 'superhero' },
		{ name: 'Yeti', value: 'yeti' }              
    ];
	
	$scope.showUpdateCompanyModal = function()
	{
		$("#updateCompanyModal").modal('show');
	};
	$scope.hideUpdateCompanyModal = function()
	{
		$("#updateCompanyModal").modal('hide');
	};
	$scope.updateCompany = function()
	{
		$.blockUI();
		$scope.companyForm.css = $scope.$parent.company.css;
		$http.post(servicesUrl + 'updateCompany', JSON.stringify($scope.companyForm))
		.then(function(response) {
			if(response.status == 200)
			{
				$scope.$parent.getCompany();
				$scope.$parent.company = angular.copy($scope.companyForm);
				$scope.hideUpdateCompanyModal();
				$scope.showSuccessAlert("El datos de la empresa han sido actualizados.");
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
       
    $("#stylec").on('change', function(){
    	$.blockUI({ overlayCSS:  { opacity: 0.93  }}); //ESCONDEMOS LA CHANCHADA
    	setTimeout(function(){ $.unblockUI(); }, 1000);
	});
});