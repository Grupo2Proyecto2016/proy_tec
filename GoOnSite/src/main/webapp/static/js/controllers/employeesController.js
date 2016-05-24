goOnApp.controller('employeesController', function($scope, $http, $filter, uiGridConstants, i18nService) 
{
	$scope.roles = [{id: 2, name: "Ventas"}, { id:3, name: "Guarda/Conductor"}];
	
	$scope.userModel = {};
	
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar usuarios para el personal de la empresa.';
     
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
    
    $scope.getUsers = function(){
    	$http.get(servicesUrl + 'getUsers')
		    .then(function(response) {
		    	if(response.status == 200)
		    	{
		    		$scope.users = response.data;
		    		angular.forEach($scope.users, function(row){
		    			row.getRole = function(){
		    				switch(this.rol_id_rol){
		    					case 1: return 'Administrador';
		    					break;
		    					case 2: return 'Ventas'
		    					break;
		    					case 3: return 'Guarda/Conductor';
		    					default: return "";
		    				}
		    			};
					});
		    		$scope.usersGrid.data = $scope.users;
		    	}
    	});
    };
    
    $scope.usersGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Nombre', field: 'nombre' },
          { name:'Apellido', field: 'apellido' },
          { name:'Nombre de Usuario', field: 'usrname'},
          { name:'Correo', field: 'email' },
          { name: 'Dirección', field: 'direccion' },
          { name: 'Rol', field: 'getRole()'
        	  , filter: {
              type: uiGridConstants.filter.SELECT,
              selectOptions: [ { value: 'Administrador', label: 'Administrador' }, { value: 'Ventas', label: 'Ventas' }, { value: 'Guarda/Conductor', label: 'Guarda/Conductor'} ]
          }},
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<button class="btn-xs btn-warning" style="width: 50%" ng-click="grid.appScope.showUserUpdateForm(row)">Editar</button><button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showMe()">Eliminar</button>' 
    	  }
        ]
     };
     
    $scope.someProp = 'abc';
    
    $scope.showMe = function(){
        alert($scope.someProp);
     };
     
    $scope.getUsers();
    
    $scope.createUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'createUser', JSON.stringify($scope.userModel))
    		.then(function(response) {
	        	if(response.status == 201)
	        	{
	        		$scope.userModel = {};
	        		$scope.hideUserForm();
	        		$scope.getUsers();
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
	        	if(response.status == 201)
	        	{
	        		$scope.userModel = {};
	        		$scope.hideUserUpdateForm();
	        		$scope.getUsers();
	        	}
    		})
		;
    	$.unblockUI();
    };
});