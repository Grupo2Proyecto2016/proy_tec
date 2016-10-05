goOnApp.controller('employeesController', function($scope, $http, $filter, uiGridConstants, i18nService) 
{
	$scope.userToDelete = null; //Variable temporal para almacenar nombre de usuario que se desea borrar
	$scope.roles = [{id: 2, name: "Ventas"}, { id:3, name: "Guarda/Conductor"}, { id:5, name: "Largador"}];
	$scope.branches = {};
	$scope.branchesToGrid = [];
	$scope.userModel = {};
	
	//GET BRANCHES
	$http.get(servicesUrl + 'getBranches')
		.then(function(response) 
		{
	    	if(response.status == 200)
	    	{
	    		$scope.branches = response.data;
	    		angular.forEach(response.data, function(b)
				{
	    			$scope.branchesToGrid.push({ label: b.nombre, value: b.nombre });
				});
	    	}
	});
	
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar usuarios para el personal de la empresa.';
    
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
    	$scope.userModel.ci = userRow.entity.ci;
    	$scope.userModel.telefono = userRow.entity.telefono;
    	$scope.userModel.email = userRow.entity.email;
    	$scope.userModel.direccion = userRow.entity.direccion;
    	$scope.userModel.telefono = userRow.entity.telefono;
    	$scope.userModel.fch_nacimiento = new Date(userRow.entity.fch_nacimiento);
    	$scope.userModel.rol_id_rol = userRow.entity.rol_id_rol;
    	if(userRow.entity.rol_id_rol == 2)
    	{
    		$scope.userModel.id_sucursal = userRow.entity.sucursal.id_sucursal;
    	}
    	
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
		    					break;
		    					case 5: return 'Largador';
		    					break;
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
		enableColumnMenus: false,
        columnDefs:
    	[
          { 
        	  name:'Nombre',
    		  cellTemplate:'<p align="center" title="{{row.entity.nombre}}">{{row.entity.nombre}}</p>',
    		  cellTooltip: true
		  },
          { 
			  name:'Apellido', 
			  cellTemplate:'<p align="center" title="{{row.entity.apellido}}">{{row.entity.apellido}}</p>',
			  cellTooltip: true
		  },
          { name:'CI', field: 'ci' },
          { name:'Nombre de Usuario', field: 'usrname'},
          {
        	  name:'Correo', 
        	  cellTemplate:'<p align="center" title="{{row.entity.email}}">{{row.entity.email}}</p>',
        	  cellTooltip: true
		  },
          {
			  name: 'Dirección', 
			  cellTemplate:'<p align="center" title="{{row.entity.direccion}}">{{row.entity.direccion}}</p>',
			  cellTooltip: true
		  },
          { name: 'Rol', field: 'getRole()'
        	  , filter: {
              type: uiGridConstants.filter.SELECT,
              selectOptions: [ { value: 'Administrador', label: 'Administrador' }, { value: 'Ventas', label: 'Ventas' }, { value: 'Guarda/Conductor', label: 'Guarda/Conductor'}, { value: 'Largador', label: 'Largador'} ]
          }},
          { name: 'Sucursal', field: 'sucursal.nombre', cellTooltip: true
        	  , filter: {
              type: uiGridConstants.filter.SELECT,
              selectOptions: $scope.branchesToGrid
          }},
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
        	width: 120,
            cellTemplate:'<button class="btn-xs btn-warning" style="width: 50%" ng-click="grid.appScope.showUserUpdateForm(row)">Editar</button><button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>' 
    	  }
        ]
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
     
    $scope.getUsers();
    
    $scope.createUser = function()
    {
    	if(!$scope.userForm.$invalid)
    	{
	    	$.blockUI();
	    	$http.post(servicesUrl + 'createUser', JSON.stringify($scope.userModel))
	    		.then(function(response) {
		        	if(response.status == 201)
		        	{
		        		$scope.userModel = {};
		        		$scope.hideUserForm();
		        		$scope.getUsers();
		        		$scope.showSuccessAlert("El usuario ha sido creado.");
		        	}
	    		})
			;
	    	$.unblockUI();
    	}
    };
    
    $scope.updateUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'updateEmployee', JSON.stringify($scope.userModel))
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