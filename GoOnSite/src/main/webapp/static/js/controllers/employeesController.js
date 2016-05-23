goOnApp.controller('employeesController', function($scope, $http, uiGridConstants, i18nService) 
{
	$scope.userModel = null;
	
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar usuarios para el personal de la empresa.';
     
    $scope.showUserForm = function(bus)
    {
    	$scope.elBus = bus; 
    	$("#userForm").removeClass('hidden');
    };
    
    $scope.hideUserForm = function(bus)
    {
    	$scope.elBus = bus;
    	$("#userForm").addClass('hidden');		
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
           }
          }
        ]
     };
     
    $scope.getUsers();
    
    $scope.createUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'createUser', JSON.stringify($scope.userModel))
    		.then(function(response) {
	        	if(response.status == 201)
	        	{
	        		$scope.userModel = null;
	        		$scope.hideUserForm();
	        		$scope.getUsers();
	        	}
    		})
		;
    	$.unblockUI();
    };
});