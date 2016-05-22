goOnApp.controller('employeesController', function($scope, $http, uiGridConstants, i18nService) 
{
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar usuarios para el personal de la empresa.';
     
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
    
});