goOnApp.controller('userPanelController', function($scope, $http, $location, uiGridConstants, $timeout, i18nService, $rootScope) 
{
	$scope.userModel = {};
	$scope.passwordModel = null; //Modelo para cambiar el password
	$scope.myPackages = [];
	$scope.myTickets = [];
	
	i18nService.setCurrentLang('es');
    $scope.message = 'Desde aquí puedes gestionar tus datos personales, pasajes y encomiendas';
    
    $scope.getPackages = function()
    {
    	$.blockUI();
    	$http.get(servicesUrl + 'userPackages')
    		.then(function(response) 
			{
	        	if(response.status == 200)
	        	{
	        		$scope.myPackages = response.data;
	        	}
	        	$.unblockUI();
    		})
		;
    };
    $scope.getPackages();
    
    $scope.getTickets = function()
    {
    	$.blockUI();
    	$http.get(servicesUrl + 'userTickets')
    		.then(function(response) 
			{
	        	if(response.status == 200)
	        	{
	        		$scope.myTickets = response.data;
	        	}
	        	$.unblockUI();
    		})
		;
    };
    $scope.getTickets();
    
    $scope.getPackageStatus = function(status)
    {
    	switch (status) 
    	{
		case 1:
			return "Ingresada"
			break;
		case 2:
			return "En camino"
			break;
		case 3:
			return "Transportada"
			break;
		case 4:
			return "Entregada"
			break;
		default:
			return "";
			break;
		}
    };
    
    $scope.getTicketStatus = function(status)
    {
    	switch (status) 
    	{
		case 1:
			return "Reservado"
			break;
		case 2:
			return "Comprado"
			break;
		case 3:
			return "En viaje"
			break;
		case 4:
			return "Cobrado"
			break;
		case 5:
			return "Cancelado"
			break;
		default:
			return "";
			break;
		}
    };
    
    $scope.showPasswordModal = function()
    {
    	$scope.passwordModel = null;
    	$("#passwordModal").modal("toggle");
    };
    $scope.hidePasswordModal = function()
    {
    	$("#passwordModal").modal("hide");
    };
    
    $scope.changePassword = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'changePassword', JSON.stringify({ 'passwd': $scope.passwordModel.passwd }))
    		.then(function(response) 
			{
    			$scope.passwordModel = null;
	        	if(response.status == 200)
	        	{
	        		$scope.showSuccessAlert("Tu contraseña ha sido cambiada. Tienes que volver a ingresar a la plataforma.");
	        		$scope.hidePasswordModal();
	        		$rootScope.user = null;
	        		shorSignInForm();
	        	}
	        	$.unblockUI();
    		})
		;
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
    
    $scope.showUserDeleteDialog = function()
    {
    	$("#deleteUserModal").modal('show');
    };
    $scope.hideUserDeleteDialog = function()
    {
    	$("#deleteUserModal").modal('hide');
    };
    $scope.deleteUser = function()
    {
    	$.blockUI();
    	$http({
    		method: 'POST',
    		url: servicesUrl + 'deleteSignedUser',
    		data: {},
    		headers: { 'Content-Type': 'application/json' }
    	})
		.then(function(response) {
			$.unblockUI();
        	if(response.status == 200)
        	{
        		$scope.hideUserDeleteDialog();
        		$scope.$parent.signOut();
        		$("div.modal-backdrop").remove();
        	}
		});
    };
    
    $scope.showUpdateUserModal = function()
    {
    	$scope.userModel = angular.copy($rootScope.user);
    	$scope.userModel.fch_nacimiento = new Date($scope.userModel.fch_nacimiento);
    	$("#updateUserModal").modal('show');
    };
    $scope.hideUpdateUserModal = function()
    {
    	$("#updateUserModal").modal('hide');		
    	$scope.userModel = {};
    };
    $scope.updateUser = function()
    {
    	$.blockUI();
    	$http.post(servicesUrl + 'updateClient', JSON.stringify($scope.userModel))
    		.then(function(response) {
	        	if(response.status == 200)
	        	{
	        		$scope.$parent.getUserInfo();
	        		$scope.getPackages();
	        		$scope.hideUpdateUserModal();
	        	}
	        	$.unblockUI();
    		})
		;
    };
    
    $scope.showTicket = function(ticket)
    {
    	$scope.ticketToShow = ticket;
    	$("#viewTicketModal").modal('show');
    };
    $scope.printDiv = function()
    {
        window.print();
    };
    
    //MONITOREO
    $scope.travelToWatch = null;
    $scope.travelMap = new google.maps.Map(document.getElementById('travelMap'), 
	{
    	zoom: 12,
    	center: {lat: -34.894418, lng: -56.165775}
	});
    $scope.busMarker = new google.maps.Marker({
		map: $scope.travelMap,
		position: null,
		icon: "static/images/busMarker.png",
		zIndex: 1000
	});
    var directionsService = new google.maps.DirectionsService;
    
    var directionsDisplay = new google.maps.DirectionsRenderer({map: $scope.travelMap});
    
    $scope.showTravelLocationModal = function(travel)
    {
    	$scope.busMarker.setPosition(null);
    	$scope.travelToWatch = travel.id_viaje;
    	$("#travelLocationModal").modal('show');	
    	$timeout(function () {            
    		google.maps.event.trigger($scope.travelMap, 'resize');
    		$scope.displayTravel(travel.linea, directionsDisplay);
        }, 400);
    };
    
    
    $scope.displayTravel = function(line, directionsDisplay)
    {
    	//ordeno paradas
    	line.paradas.sort(function(a, b){return a.id_parada - b.id_parada});

    	var origin = null;
    	var destination = null;
    	
       	var waypts = [];
    	for (var i = 0; i < line.paradas.length; i++) 
    	{
    		var position = new google.maps.LatLng(line.paradas[i].latitud, line.paradas[i].longitud);
    	    if(line.paradas[i].id_parada == line.origen.id_parada)
    	    {
    	    	origin = position;
    	    }
    	    else if(line.paradas[i].id_parada == line.destino.id_parada)
    	    {
    	    	destination = position;
    	    }
    	    else
    	    {
    	    	waypts.push({location: position, stopover: true});    	        	    	
    	    }
    	}

    	directionsService.route(
    		{
	    	    origin: origin,
	    	    destination: destination,
	    	    waypoints: waypts,
	    	    travelMode: google.maps.TravelMode.DRIVING
    	    },
    	    function(response, status) 
    	    {
	    	    if (status === google.maps.DirectionsStatus.OK) 
	    	    {
	    	    	directionsDisplay.setDirections(response);
	    	    	$scope.$digest();
	    	    } 
	    	    else 
	    	    {
	    	      window.alert('Falló el request de direcciones: ' + status);
	    	    }
    	    }
	    );
    	
    	locateUser();
    };
    
    function locateBus()
    {
    	if($scope.travelToWatch != null && $('#travelLocationModal').hasClass('in'))
    	{
    		$http.get(servicesUrl + 'getLastTravelLocation?travelId=' + $scope.travelToWatch)
    		.then(function(result) 
	    	{
    			if(result.status == 200 && result.data != null)
    			{
    				var busPosition = new google.maps.LatLng(result.data.latitud, result.data.longitud);
    				$scope.busMarker.setPosition(busPosition);
    			}
	    	});
    	}
    	setTimeout(locateBus, 5000 );
    }
    
    function locateUser()
    {
    	if(!!navigator.geolocation) 
    	{
	    	navigator.geolocation.getCurrentPosition(function(position) {
	    		
	    		var geolocate = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
	    		
	    		var userMarker = new google.maps.Marker({
	    			map: $scope.travelMap,
	    			position: geolocate,
	    			icon: "static/images/marker_sm.png"
	    		});
	    	});
    	}
    }
    
    locateBus();
});