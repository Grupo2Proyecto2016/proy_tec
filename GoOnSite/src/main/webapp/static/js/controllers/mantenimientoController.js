goOnApp.controller('mantenimientoController', function($scope, $http, uiGridConstants, i18nService)									 
{
    $scope.message = 'Maneje el mantenimiento de sus vehículos con facilidad.';
    $scope.error_message = '';
    $scope.custom_response = null;
    $scope.minDate = new Date();
    $scope.maxDate = new Date();
    $scope.minDate.setDate($scope.minDate.getDate() + 1);
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);


	
    i18nService.setCurrentLang('es');
    
    $scope.mantenimientoForm = {};
    $scope.mantenimientoToDelete = {};

    
    $scope.initForm = function()
    {

	    $scope.mantenimientoForm.taller = 0;
	    $scope.mantenimientoForm.vehiculo = 0;

		$scope.mantenimientoToDelete.id_mantenimiento = null;
		$scope.mantenimientoToDelete.costo = null;
		$scope.mantenimientoToDelete.factura = null;

    }
    
    $scope.initForm();
    
    $scope.getMantenimientos = function(){
    	$http.get(servicesUrl + 'getMantenimientos').success(function(data, status, headers, config) 
    	{
        	$scope.mantenimientos = data;
        	$scope.mantenimientosGrid.data = null;
        	$scope.mantenimientosGrid.data = $scope.mantenimientos;


    	});
    };
    
    $scope.getBuses = function(){
    	$http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
    	{
        	$scope.buses = data;
    	});
    };
    
    $scope.getTalleres = function(){
    	$http.get(servicesUrl + 'getTalleres').success(function(data, status, headers, config) 
    	{
        	$scope.talleres = data;
    	});
    };
    
    $scope.getMantenimientos();
    
    $scope.showForm = function()
    {
    	$("#divMantenimientoForm").removeClass('hidden');
    	$scope.hideSuccess(); 
    	$scope.getBuses();
    	$scope.getTalleres();
    };
    
    $scope.hideForm = function()
    {
    	$("#divMantenimientoForm").addClass('hidden');		
    };

    $scope.hideSuccess = function()
    {
    	//$("#successAlert").addClass('hidden');
    };
    
    $scope.createMantenimiento = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();
			
			$http.post(servicesUrl +'createMantenimiento', JSON.stringify($scope.mantenimientoForm))
			.then(function(result)
			{	
				$.unblockUI();
				if(result.data.success)
				{
					$scope.hideForm();
					$scope.showSuccessAlert(result.data.msg);		
					$scope.getMantenimientos();	
				}
				else
				{
					$scope.error_message = 'Ha ocurrido un error al enviar el vehículo. ' + result.data.msg; 
					$("#errorModal").modal("toggle");
				}
			});
			
		}
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
	
	$scope.mantenimientosGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Costo', field: 'costo' },
          {field: 'inicio', displayName: 'Fecha de Inicio',type: 'date', cellFilter: 'date:\'dd-MM-yyyy\'' },
          {field: 'fin', displayName: 'Fecha de Fin',type: 'date', cellFilter: 'date:\'dd-MM-yyyy\'' },
//          {field: 'inicio', displayName: 'Fecha de Inicio',type: 'date', cellFilter: 'date:\'dd-MM-yyyy H:mm\'' },
//          {field: 'fin', displayName: 'Fecha de Fin',type: 'date', cellFilter: 'date:\'dd-MM-yyyy H:mm\'' },
          { name:'Nro Vehículo', field: 'vehiculo.id_vehiculo'},
          { name:'Taller', field: 'taller.nombre' },
          { name: 'Nombre Usuario', field: 'user_crea.nombre' },
          { name: 'Apellido Usuario', field: 'user_crea.apellido' },
          
          
          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate://'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getMantenimientoDetails(row)">Detalles</button>'
            	//'<button style="width: 50%" style="" class="btn-xs btn-danger" ng-show="{{row.entity.factura != null}} " ng-click="verFactura(row)">Ver Factura</button>' +
            	//'<button style="width: 50%" style="" class="btn-xs btn-danger" ng-show="{{row.entity.factura == null}} "  ng-href="{{row.entity.factura)}}">Ver Factura</button>' +
            			 '<button style="width: 50%" style="" class="btn-xs btn-danger" ng-show="{{row.entity.costo == null}} " ng-click="grid.appScope.showDeleteDialog(row)">Salida</button>' 
            			  
    	  }
        ]
     };
	
		
    
	$scope.showDeleteDialog = function(row)
    {
		$scope.mantenimientoToDelete.costo = null;
		$scope.mantenimientoToDelete.id_mantenimiento = row.entity.id_mantenimiento;
		$scope.mantenimientoToDelete.factura = null;
//		$scope.mantenimientoToDelete = null;
		
    	$("#deleteModal").modal('toggle');
    };
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
    
	$scope.deleteMantenimiento = function()
	{
		$.blockUI();
		
		$http.post(servicesUrl +'deleteMantenimiento', JSON.stringify($scope.mantenimientoToDelete))
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
	    				$.unblockUI();
	    				$scope.hideForm();
	    				$scope.initForm();
	    				$scope.showSuccessAlert("Se ha completado el mantenimiento para el vehiculo seleccionado.");
	    				$scope.getMantenimientos();

		        			
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
	};

	
//    $scope.getMantenimientoDetails = function(row)
//    {
//    	$scope.elMantenimiento = row.entity; 
//		$("#mantenimientoDetailsModal").modal('toggle');
//    };
	

    

});