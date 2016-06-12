goOnApp.controller('travelController', function($scope, $http, uiGridConstants, i18nService) 
{
    $scope.message = 'Desde aquí podrás buscar el viaje que deseas y efectuar la compra o reserve de pasajes.';
    
    $scope.dateFrom = new Date();
    $scope.dateTo = new Date();
	$scope.dateFrom.setDate($scope.dateFrom.getDate() + 1);
	$scope.dateTo.setDate($scope.dateTo.getDate() + 30);
    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');
        
    $scope.closeSuccessAlert = function()
    {
    	$("#successAlert").hide();
    };
    
    $scope.showSuccessAlert = function(message)
    {
    	$('#successMessage').text(message);
		$("#successAlert").show();
    }; 
    
    $scope.getTravels = function()
    {
    	$http.get(servicesUrl + 'travels').success(function(data, status, headers, config)
    	{
    		$scope.travels = data;
    		angular.forEach($scope.travels, function(row){
    			row.getDriverName = function()
    			{
    				return row.conductor.nombre + " " + row.conductor.apellido;
    			};
			});
    		$scope.travelsGrid.data = $scope.travels;
    	});
    };
    
    $scope.travelsGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Linea', field: 'linea.numero' },
          { name:'Origen', field: 'linea.origen.descripcion' },
          { name:'Destino', field: 'linea.destino.descripcion'},
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText">{{ row.entity.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>' },
          { name:'Tiempo Estimado (min)', field: 'linea.tiempo_estimado' },
          { 
        	  name: 'Pasajeros Parados', 
        	  cellTemplate: '<div class="text-center ngCellText">{{row.entity.linea.viaja_parado | SiNo}}</div>'
          },
          { name:'Nº Coche', field: 'vehiculo.id_vehiculo' },
//          { name: 'Acciones',
//        	enableFiltering: false,
//        	enableSorting: false,
//            cellTemplate:'<p align="center"><button style="" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button></p>'
//    	  }
        ]
     };
    
    $scope.getTravels();
    
    $scope.buyTicket = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'buyTicket', JSON.stringify($scope.ticketForm))
		.then(function(response) 
			{
				$.unblockUI();		
	        	if(response.status == 200)
	        	{	       
	        		if (!response.data.success)
	    			{
//	    				$scope.error_message = response.data.msg;
//	    		    	$("#errorModal").modal("toggle");
	        			$scope.showSuccessAlert("Los viajes han sido creados");	
	    			}
	        		else
	        		{
//	        			$scope.getTravels();
//		        		$scope.showSuccessAlert("El viaje ha sido borrada.");	
	        		}	        		
	        	}
	        	$scope.hideBuyDialog();
    		}
		);				
	};
	$scope.showBuyDialog = function(row)
    {
    	$("#buyModal").modal('show');
    };
    $scope.hideBuyDialog = function(row)
    {
    	$("#buyModal").modal('hide');
    };
}); 	