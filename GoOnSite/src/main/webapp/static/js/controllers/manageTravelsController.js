goOnApp.controller('manageTravelsController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Gestiona viajes para las lineas asignando vehículos y choferes';
    $scope.travelForm = {};
	$scope.drivers = null;
    $scope.lines = null;
    $scope.buses = null;
    $scope.maxDate = new Date();
	$scope.minDate = new Date();
	$scope.minDate.setDate($scope.minDate.getDate() + 1);
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	
	$scope.filterMinDate = new Date();
	$scope.filterMinDate.setDate($scope.filterMinDate.getDate() - 15);
	$scope.filterDate = angular.copy($scope.filterMinDate);
	
	$scope.isDayMissing = function()
	{
		return !$scope.travelForm.monday 
			&& !$scope.travelForm.tuesday 
			&& !$scope.travelForm.wednesday 
			&& !$scope.travelForm.thursday 
			&& !$scope.travelForm.friday 
			&& !$scope.travelForm.saturday 
			&& !$scope.travelForm.sunday
		;
	};
    
	$scope.custom_response = null;    
    $scope.txt_minutos = "";
    $scope.txt_km = "";
    i18nService.setCurrentLang('es');
    
    
    $scope.getDrivers = function()
    {
    	$http.get(servicesUrl + 'getDrivers').success(function(data, status, headers, config) {
    		$scope.drivers = data;
    		angular.forEach($scope.drivers, function(d){
    			d.customDes = d.usrname + ": " + d.nombre + " " + d.apellido;
    		});
		});
    };    
    
    $scope.getBuses = function(){
    	$http.get(servicesUrl + 'getBuses').success(function(data, status, headers, config) 
    	{
        	$scope.buses = data;
        	angular.forEach($scope.buses, function(b){
    			b.customDes = b.numerov + ": " + b.matricula;
    		});
    	});
    };
    
    $scope.getLines = function()
    {
    	$http.get(servicesUrl + 'getLines').success(function(data, status, headers, config){
    		$scope.lines = data;        	
    		angular.forEach($scope.lines, function(l){
    			l.customDes = l.numero + ": " + l.origen.descripcion + " -> " + l.destino.descripcion;
    		});
		});
    };

    $scope.showForm = function()
    {
    	$scope.travelForm = {};
    	$scope.getBuses();
    	$scope.getDrivers();
    	$scope.getLines();
    	$("#divTravelForm").removeClass('hidden');    	
    };
    
    $scope.hideForm = function()
    {
    	$("#divTravelForm").addClass('hidden');		
    };
    
    $scope.createTravels = function()
    {
    	if(!$scope.form.$invalid && !$scope.isDayMissing())
		{
    		$.blockUI();
    		$http.post(servicesUrl +'createTravel', JSON.stringify($scope.travelForm))
			.then(function(result)
			{	
				$.unblockUI();
				if(result.data.success)
				{
					$scope.showSuccessAlert(result.data.msg);		
					$scope.getTravels();
				}
				else
				{
					$scope.error_message = 'Ha ocurrido un error al crear los viajes. ' + result.data.msg; 
					$("#errorModal").modal("toggle");
				}
			});
		}
    };
    
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
    	
    	$http.get(servicesUrl + 'getTravels?filterDate='+ $scope.filterDate).success(function(data, status, headers, config)
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
          { name:'Origen', field: 'linea.origen.descripcion', cellTooltip: true },
          { name:'Destino', field: 'linea.destino.descripcion', cellTooltip: true },
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText" style="width: 180px">{{ row.entity.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>', width: 180, cellTooltip: true },
          { name:'Tiempo Estimado (min)', field: 'linea.tiempo_estimado' },
          { 
        	  name: 'Pasajeros Parados', 
        	  cellTemplate: '<div class="text-center ngCellText">{{row.entity.linea.viaja_parado | SiNo}}</div>'
          },
          { name:'Nº Coche', field: 'vehiculo.numerov' },
          { name:'Conductor', field: 'getDriverName()', cellTooltip: true },
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<p align="center"><button style="" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button></p>'
    	  }
        ]
     };
    
    $scope.getTravels();
    
    $scope.showDeleteDialog = function(row)
    {
    	$scope.travelToDelete = row.entity.id_viaje;
    	$("#deleteModal").modal('show');
    };
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
    $scope.deleteTravel = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteTravel', JSON.stringify($scope.travelToDelete))
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
	        			$scope.getTravels();
		        		$scope.showSuccessAlert("El viaje ha sido borrada.");	
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
	};
	
});