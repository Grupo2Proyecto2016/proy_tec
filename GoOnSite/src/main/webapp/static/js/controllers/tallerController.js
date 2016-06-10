goOnApp.controller('tallerController', function($scope, $http, uiGridConstants, i18nService)									 
{
    $scope.message = 'Maneje sus talleres con facilidad.';
    $scope.error_message = '';
    $scope.custom_response = null;
    $scope.tallerToDelete = null;
    
    i18nService.setCurrentLang('es');
    
    $scope.tallerForm = {};
    
    $scope.initForm = function()
    {
	    $scope.tallerForm.direccion = null;
	    $scope.tallerForm.telefono = null;
	    $scope.tallerForm.ciudad = 0;
	    $scope.tallerForm.departamento = 0;
	    $scope.tallerForm.nombre = null;
    }
    
    $scope.initForm();
    
    $scope.getTalleres = function(){
    	$http.get(servicesUrl + 'getTalleres').success(function(data, status, headers, config) 
    	{
        	$scope.talleres = data;
        	$scope.talleresGrid.data = $scope.talleres;
    	});
    };
    
    $scope.getTalleres();
    
    $scope.showForm = function()
    {
    	$("#divTallerForm").removeClass('hidden');
    	$scope.hideSuccess();    	
    };
    
    $scope.hideForm = function()
    {
    	$("#divTallerForm").addClass('hidden');		
    };

    $scope.hideSuccess = function()
    {
    	//$("#successAlert").addClass('hidden');
    };
    
    $scope.createTaller = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();			
			$http.post(servicesUrl +'createTaller', JSON.stringify($scope.tallerForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
				$scope.initForm();
				$scope.showSuccessAlert("Taller creado.");			
				$scope.getTalleres();				
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear el taller. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			})
			;    			
		}
	};
	
	$scope.deleteTaller = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteTaller', JSON.stringify($scope.tallerToDelete))
		.then(function(response) 
			{
				$.unblockUI();		
	        	if(response.status == 200)
	        	{	       
	        		if (!response.data.success)
	    			{
	    				$scope.error_message = response.data.msg;
	    		    	$("#errorModal").modal("toggle");
	    			}
	        		else
	        		{
	        			$scope.getTalleres();	
		        		$scope.showSuccessAlert("El taller ha sido borrado.");	
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
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
	
	$scope.talleresGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Dirección', field: 'direccion' },
          { name:'Teléfono', field: 'telefono' },
          { name:'Ciudad', field: 'cuidad'},
          { name:'Departamento', field: 'departamento' },
          { name: 'Nombre', field: 'nombre' },
          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getTallerDetails(row)">Detalles</button>'+
            			 '<button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>' 
            			  
    	  }
        ]
     };
	
	$scope.showDeleteDialog = function(row)
    {
    	$scope.tallerToDelete = row.entity.id_taller;
    	$("#deleteModal").modal('show');
    };
    
    $scope.getTallerDetails = function(row)
    {
    	$scope.elTaller = row.entity; 
		$("#tallerDetailsModal").modal('toggle');
    };
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
});