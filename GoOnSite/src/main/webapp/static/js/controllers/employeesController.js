goOnApp.controller('employeesController', function($scope, $http, uiGridConstants) 
{
    $scope.message = 'Desde aquí puedes gestionar los usuarios para el personal de la empresa.';
    
    $scope.usersGrid = 
    {
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Nombre', field: 'nombre' },
          { name:'Apellido', field: 'apellido' },
          { name:'Nombre de Usuario', field: 'usrname'},
          { name:'Correo', field: 'email' },
          { name: 'Dirección', field: 'direccion' },
          { name: 'Rol', field: 'rol_id_rol', filter: {
              term: '',
              type: uiGridConstants.filter.SELECT,
              selectOptions: [ { value: '1', label: 'Administrador' }, { value: '2', label: 'Ventas' }, { value: '3', label: 'Guarda/Conductor'} ]
           }}
        ]
     };
    
    $http.get(servicesUrl + 'getUsers')
    	.then(function(response) 
		{
	    	if(response.status == 200)
	    	{
	    		$scope.usersGrid.data = response.data;
	    	}
		}
    );
});