goOnApp.controller('ticketController', function($scope, $http, uiGridConstants, i18nService, $timeout) 
{
    $scope.message = 'Desde este panel puedes cancelar pasajes y confirmar reservas';
    
    $scope.minDate = new Date();
	$scope.minDate.setDate($scope.minDate.getDate() -10);
	$scope.filterMinDate = angular.copy($scope.minDate);
	
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
    
    $scope.ticketsGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
		enableColumnMenus: false,
        columnDefs:
    	[
          { name:'Cliente', field: 'ci_receptor', width: '80', enableSorting: false },
          { name:'Origen', field: 'viaje.linea.origen.descripcion', width: '*'},
          { name:'Destino', field: 'viaje.linea.destino.descripcion', width: '*' },
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText">{{ row.entity.viaje.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>', width: '*', enableFiltering: false, enableSorting: false },
          { name:'Nº Coche', field: 'viaje.vehiculo.numerov', width: '80' },
          { name:'Costo', field: 'costo', width: '*'},
          { name:'Estado', field: 'statusDes', width: '100'},
          { name: ' ',
          	enableFiltering: false,
          	enableSorting: false,
          	width: '180',
            cellTemplate:
            	'<button style="width: 50px" class="btn-xs btn-info" ng-click="grid.appScope.showTicket(row)">Ver</button>'
            	+ '<button style="width: 70px" class="btn-xs btn-success" ng-show="row.entity.estado == 1" ng-click="grid.appScope.confirmReservationDialog(row)">Confirmar</button>'
            	+ '<button style="width: 60px" class="btn-xs btn-warning" ng-click="grid.appScope.cancelTicketDialog(row)">Cancelar</button>'
      	  }
        ]
    };
    
    $scope.getActiveTickets = function()
    {
		$.blockUI();
    	$http.get(servicesUrl + 'getActiveTickets?from=' + $scope.filterMinDate)
		.then(function(result) 
    	{
			if(result.status == 200)
			{
				$scope.tickets = result.data;
				angular.forEach($scope.tickets, function(ticket)
				{
					switch(ticket.estado)
					{
						case 1: ticket.statusDes = "Reservado";
						break;
						case 2: ticket.statusDes = "Pago";
						break;
					}
				});
				$scope.ticketsGrid.data = $scope.tickets;
			}
			$.unblockUI();
    	});
    };
    
    $scope.getActiveTickets();
    
    $scope.showTicket = function(row)
    {
    	$scope.ticketToShow = row.entity;
    	$("#viewTicketModal").modal('show');
    };
    $scope.printDiv = function()
    {
        window.print();
    };
    
    $scope.confirmReservationDialog = function(row)
    {
    	$scope.ticketToConfirm = row.entity;
    	$("#reservationModal").modal('show');
    };
    $scope.confirmReservation = function()
    {
    	$http.post(servicesUrl + 'confirmReservation',  JSON.stringify($scope.ticketToConfirm))
		.then(function(result){
			if(result.status = 200)
			{
				$scope.showSuccessAlert("La reserva ha sido confirmada");
				$("#reservationModal").modal('hide');
				$scope.getActiveTickets();
			}
		});
    };
    
    $scope.cancelTicketDialog = function(row)
    {
    	$scope.ticketToCancel = row.entity;
    	$("#cancelTicketModal").modal('show');
    };
    $scope.cancelTicket = function()
    {
    	$http.post(servicesUrl + 'cancelTicket', JSON.stringify($scope.ticketToCancel))
		.then(function(result){
			if(result.status = 200)
			{
				$scope.showSuccessAlert(result.data.msg);
				$("#cancelTicketModal").modal('hide');
				$scope.getActiveTickets();
			}
		});
    };

}); 	