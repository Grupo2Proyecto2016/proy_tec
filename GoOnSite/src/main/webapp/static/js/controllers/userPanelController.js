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
    	center: {lat: -34.2, lng: -56.5},
        zoom: 9
	});
    $scope.busMarker = new google.maps.Marker({
		map: $scope.travelMap,
		position: null,
		icon: "static/images/busMarker.png",
		zIndex: 1000
	});
    $scope.packMarker = new google.maps.Marker({
		map: $scope.travelMap,
		position: null,
		icon: "static/images/packMarker.png",
		zIndex: 1000
	});
    
    
    var directionsService = new google.maps.DirectionsService;
    
    var directionsDisplay = new google.maps.DirectionsRenderer({map: $scope.travelMap});
    
    $scope.showTravelLocationModal = function(travel, type)
    {
    	var isPackage = type == 'p';
		$scope.packMarker.setPosition(null);
		$scope.busMarker.setPosition(null);
    	$scope.travelToWatch = travel.id_viaje;
    	$scope.isPackage = isPackage;
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
    				var position = new google.maps.LatLng(result.data.latitud, result.data.longitud);
    				if($scope.isPackage)
    				{
    					$scope.packMarker.setPosition(position);
    				}
    				else
    				{
    					$scope.busMarker.setPosition(position);
    				}
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
    
    /*Pintado de ruta*/
    var infowindow = new google.maps.InfoWindow;
    $scope.routeLine = {};
    
    $scope.showRoute = function(line, origen, destino)
    {
    	$.blockUI();
    	$http.get(servicesUrl + 'getLine?id_linea=' +line)
		.success(function(data, status, headers, config) 
		{
			
			$scope.routeLine = data;
			
	    	$http.get(servicesUrl + 'FindNextStationsByOrigin?line=' + line+'&origin=' +$scope.routeLine.origen.id_parada)
			.success(function(data, status, headers, config) 
			{
				$scope.routeLine.paradas = data;
				
				$scope.routeLine.paradas.splice(0, 0, $scope.routeLine.origen);
				
				$scope.routeLine.selorigen = origen.id_parada;
				$scope.routeLine.seldestino = destino.id_parada;
				$scope.travelMap.setCenter(new google.maps.LatLng(-34.2, -56.5));
		   	    $scope.travelMap.setZoom(9);
		   	    $scope.travelMap.panTo(new google.maps.LatLng(-34.2, -56.5));	
		    	var directionsService = new google.maps.DirectionsService;
		    	var directionsDisplay = new google.maps.DirectionsRenderer({
		    	    														suppressPolylines: true,
		    	    														infoWindow: infowindow
		    	  															});
		    	directionsDisplay.setMap($scope.travelMap);		   	    
		    	$scope.travelMap.setCenter(new google.maps.LatLng(-34.2, -56.5));
			   	$scope.travelMap.setZoom(9);
			   	$scope.travelMap.panTo(new google.maps.LatLng(-34.2, -56.5));
			   	$("#travelLocationModal").modal('show');
		   	    $timeout(function () 
				{   $scope.travelMap.setZoom(9);	         
		   	    	google.maps.event.trigger($scope.travelMap, 'resize');
		   	    	$scope.calculateAndDisplayRoute(directionsService, directionsDisplay);
				}, 400);		   	    
				
			})
			.error(function()
			{
							
			});
		});
    	locateUser();
    	$.unblockUI();
    }
    
    $scope.calculateAndDisplayRoute = function(directionsService, directionsDisplay)
    {
    	/*var waypts = [{location: '41.062317, 28.899756',stopover: true }, 
    	              {location: '41.062276, 28.898866',  stopover: true }, 
    	              { location: '41.061993, 28.8982', stopover: true }];
    	*/
    	var waypts = [];
    	for (var i = 0; i < $scope.routeLine.paradas.length-1; i++) 
    	{
    		position = new google.maps.LatLng($scope.routeLine.paradas[i].latitud,$scope.routeLine.paradas[i].longitud);
    	    waypts.push({location: position, stopover: true});    	    
    	}
    	
    	$scope.routeLine.indOr = 0;
    	$scope.routeLine.indDe = 0;
    	
    	for (var i = 0; i < $scope.routeLine.paradas.length; i++) 
    	{
    		if($scope.routeLine.paradas[i].id_parada == $scope.routeLine.selorigen)
    		{
    			$scope.routeLine.indOr = i;
    		}
    		if($scope.routeLine.paradas[i].id_parada == $scope.routeLine.seldestino)
    		{
    			$scope.routeLine.indDe = i;
    		}
    	}
    	
    	 directionsService.route({ origin: {lat: $scope.routeLine.origen.latitud, lng: $scope.routeLine.origen.longitud}, 
    		 					   destination: {lat: $scope.routeLine.destino.latitud, lng: $scope.routeLine.destino.longitud},
    		 					waypoints: waypts,
    		 					optimizeWaypoints: false,
    		 					travelMode: google.maps.TravelMode.DRIVING}, 
    		 					function(response, status) {
    		 						if (status === google.maps.DirectionsStatus.OK) {
    		 							directionsDisplay.setOptions({directions: response,})
    		 							var route = response.routes[0];
    		 							$scope.renderDirectionsPolylines(response, $scope.travelMap);
    		 						} else {
    		 							window.alert('Directions request failed due to ' + status);
    		 							}
    		 						});
    }
    
    var polylineOptions = {
  		  strokeColor: '#C83939',
  		  strokeOpacity: 1,
  		  strokeWeight: 4
  		};
	var colors = ["#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF"];
	var polylines = [];
	
	$scope.renderDirectionsPolylines = function (response) 
	{
		 var bounds = new google.maps.LatLngBounds();
		  
		
		  var color = "#00FF00";
		  for (var i = 0; i < polylines.length; i++) 
		  {
		    polylines[i].setMap(null);
		  }
		  
		  		  
		  var legs = response.routes[0].legs;
		  for (i = 0; i < legs.length; i++) 
		  {
		    var steps = legs[i].steps;		    
		    if(i == $scope.routeLine.indOr+1)
		    {
		    	color = "#FF0000";
		    }
		    if(i == $scope.routeLine.indDe+1)
		    {
		    	color = "#00FF00";
		    }
		    for (j = 0; j < steps.length; j++) {
		      var nextSegment = steps[j].path;
		      var stepPolyline = new google.maps.Polyline(polylineOptions);
		      stepPolyline.setOptions({
		        strokeColor: color
		      })
		      for (k = 0; k < nextSegment.length; k++) {
		        stepPolyline.getPath().push(nextSegment[k]);
		        bounds.extend(nextSegment[k]);
		      }
		      polylines.push(stepPolyline);
		      stepPolyline.setMap($scope.travelMap);
		      // route click listeners, different one on each step
		    //  google.maps.event.addListener(stepPolyline, 'click', function(evt) 
		    //  {
		    	 /*infowindow.setContent("you clicked on the route<br>" + evt.latLng.toUrlValue(6));
		        infowindow.setPosition(evt.latLng);
		        infowindow.open($scope.routeMap);*/
		   //   })
		    }
		  }
		  $scope.travelMap.fitBounds(bounds);		  
		  //$("#travelLocationModal").modal('show');		  
	}
    
});