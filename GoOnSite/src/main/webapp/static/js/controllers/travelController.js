goOnApp.controller('travelController', function($scope, $http, uiGridConstants, i18nService, $timeout, $rootScope, $routeParams) 
{
	$scope.message = 'Desde aquí podrás buscar el viaje que deseas y efectuar la compra o reserve de pasajes.';       
    $scope.minDate = new Date();
    $scope.maxDate = new Date();
	$scope.maxDate.setDate($scope.maxDate.getDate() + 30);
	$scope.stations = {};
	$scope.originStations = {};
	$scope.nearbyDestinations = {};
    $scope.filteredOrigins = {};
    $scope.travelSearch = {};
    
    $scope.userMarkers = [];
    $scope.userMarkersOrigin = [];
    $scope.destinoMarkers = [];
    $scope.origenMarkers = [];
    $scope.circle = null;
    $scope.circleOrigin = null;
    $scope.listaIDSeleccionados = [];
    $scope.listaIDSeleccionadosOrigin = [];
    $scope.routeLine = {};
    
    $scope.firstSearch = false;
    
    $scope.seatsForm = {};
    $scope.rOption = "1";    
    $scope.frmOpt = "0";    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');
    
    $(window).on("load", function() {
	    if($rootScope.user !== null)
	    {
	    	localStorage.removeItem($scope.$parent.getTicketStorageKey());
	    }
    });
      
    $scope.limpioOrigenes = function()
    {
    	$scope.origenMarkers.forEach(function(marker) 
        {
          marker.setMap(null);
        });
    	$scope.origenMarkers = [];  
    }
    //DESTINATION MAP
    $scope.destinationMap = new google.maps.Map(document.getElementById('destinationMap'), 
    {
    	center: {lat: -34.2, lng: -56.5},    	  
        zoom: 9
    });
    
    var destinationInput = document.getElementById('destination-pac-input');
    var destinationsearchBox = new google.maps.places.SearchBox(destinationInput);
    
    $scope.destinationMap.controls[google.maps.ControlPosition.TOP_LEFT].push(destinationInput);
    
    var infowindow = new google.maps.InfoWindow;
    
    destinationsearchBox.addListener('places_changed', function() 
    {
    	
    	$scope.userMarkers.forEach(function(marker) 
        {
          marker.setMap(null);
        });
    	
    	var places = destinationsearchBox.getPlaces();

        if (places.length == 0) 
        {
        	return;
        }
        
        var bounds = new google.maps.LatLngBounds();
        //hacer for each porque puede devolver mas de un lugar
        $scope.userMarkers.forEach(function(marker) 
        {
        	marker.setMap(null);
        });
        if ($scope.circle !== null)
  	  	{
  	  		$scope.circle.setMap(null);
  	  	}
        places.forEach(function(place) 
        {
        	$scope.userMarkers = [];        
        	$scope.placeMarkerAndPanTo(place.geometry.location, $scope.destinationMap, true);
        });         	        
    });
    
    $scope.destinationMap.addListener('click', function(e) 
    {
    	//Borra los anteriores, en este caso es uno solo, pero eventualmente podrian ser mas
    	
        $scope.userMarkers.forEach(function(marker) 
        {
        	marker.setMap(null);
        });
        
        if ($scope.circle !== null)
  	  	{
  	  		$scope.circle.setMap(null);
  	  	}
        
        $scope.userMarkers = [];        
    	$scope.placeMarkerAndPanTo(e.latLng, $scope.destinationMap, true);    	    	
    	//$scope.$digest();
	});
    
    $scope.creaRadio = function(marker, map, destino)
    {
    	var radius = 1000;
  	  	
    	if(destino == true)
    	{
	    	if ($scope.circle !== null)
	  	  	{
	  	  		$scope.circle.setMap(null);
	  	  	}
	  	  	$scope.circle = new google.maps.Circle({  	  		
	  	  			center:marker.getPosition(),
	  				strokeColor: '#678DEA',
	  				strokeOpacity: 0.35,
	  				strokeWeight: 1,
	  				radius: radius, //1km
	  				fillOpacity: 0.35,
	  				fillColor: "#678DEA",
	  				map: map}
	  			  );
    	}
    	else
    	{
    		if ($scope.circleOrigin !== null)
	  	  	{
	  	  		$scope.circleOrigin.setMap(null);
	  	  	}
	  	  	$scope.circleOrigin = new google.maps.Circle({  	  		
	  	  			center:marker.getPosition(),
	  				strokeColor: '#678DEA',
	  				strokeOpacity: 0.35,
	  				strokeWeight: 1,
	  				radius: radius, //1km
	  				fillOpacity: 0.35,
	  				fillColor: "#678DEA",
	  				map: map}
	  			  );
    	}
    	
	  	var bounds = new google.maps.LatLngBounds();
	  	var cant_paradas = 0;
	  	
	  	if(destino == true)
	  	{
		  	$scope.listaIDSeleccionados = [];
		    for (var i=0; i< $scope.destinoMarkers.length; i++) 
		    {
		    	if (google.maps.geometry.spherical.computeDistanceBetween($scope.destinoMarkers[i].getPosition(),marker.getPosition()) < radius) 
				{
				    //bounds.extend($scope.destinoMarkers[i].getPosition())
		    		$scope.listaIDSeleccionados.push($scope.destinoMarkers[i].id_parada);
		    		cant_paradas ++;
				} 
				else 
				{
					//$scope.destinoMarkers[i].setMap(null);
				}
		    }	    
	  	}
	  	else
  		{
	  		$scope.listaIDSeleccionadosOrigin = [];
		    for (var i=0; i< $scope.origenMarkers.length; i++) 
		    {
		    	if (google.maps.geometry.spherical.computeDistanceBetween($scope.origenMarkers[i].getPosition(),marker.getPosition()) < radius) 
				{
				    //bounds.extend($scope.destinoMarkers[i].getPosition())
		    		$scope.listaIDSeleccionadosOrigin.push($scope.origenMarkers[i].id_parada);
		    		cant_paradas ++;
				} 
				else 
				{
					//$scope.destinoMarkers[i].setMap(null);
				}
		    }	
  		}
	    map.panTo(marker.getPosition());
	    if(cant_paradas == 0)
    	{
	    	$scope.showWarning();
    	}
	    else
    	{
	    	$scope.closeWarning();
    	}
	    $scope.$digest();
    }
    
    $scope.placeMarkerAndPanTo = function (latLng, map, destino) 
    {
  	  
    	var marker = new google.maps.Marker({
    		position: latLng,
    		map: map, //map: $scope.map,
    		draggable:true,
    		icon: "static/images/marker_sm.png",
    		animation: google.maps.Animation.DROP, //just for fun
  	  	});	  
    	
    	marker.addListener('dragend',function(event) 
    	{
    		$scope.creaRadio(marker, map, destino);
    	});
    	
  	  	
    	if(destino == true)
    	{	    	
    		$scope.userMarkers.push(marker); 	
    	}
    	else
    	{
    		$scope.userMarkersOrigin.push(marker);
    	}
    	
    	$scope.creaRadio(marker, map, destino);
  	  	//var l = latLng.lat();
  	  	//var g = latLng.lng();
  	  	//$scope.geocodePosition(marker);
  	  	
   	}//fin de placeMarkerAndPanTo
       
    
    //ORIGIN MAP
    $scope.originMap = new google.maps.Map(document.getElementById('originMap'), 
    {
      //zoom: 12,
      //center: {lat: -34.894418, lng: -56.165775}
    	center: {lat: -34.4, lng: -56.5},    	  
        zoom: 9
    });
    var originInput = document.getElementById('origin-pac-input');
    var originSearchBox = new google.maps.places.SearchBox(originInput);
    $scope.originMap.controls[google.maps.ControlPosition.TOP_LEFT].push(originInput);
    //
    
    originSearchBox.addListener('places_changed', function() 
    {
    	
    	$scope.userMarkersOrigin.forEach(function(marker) 
        {
          marker.setMap(null);
        });
    	
    	var places = originSearchBox.getPlaces();

        if (places.length == 0) 
        {
        	return;
        }
        
        var bounds = new google.maps.LatLngBounds();
        //hacer for each porque puede devolver mas de un lugar
        $scope.userMarkersOrigin.forEach(function(marker) 
        {
        	marker.setMap(null);
        });
        if ($scope.circleOrigin !== null)
  	  	{
  	  		$scope.circleOrigin.setMap(null);
  	  	}
        places.forEach(function(place) 
        {
        	$scope.userMarkersOrigin = [];        
        	$scope.placeMarkerAndPanTo(place.geometry.location, $scope.originMap, false);
        });         	        
    });
    
    $scope.originMap.addListener('click', function(e) 
    {
    	//Borra los anteriores, en este caso es uno solo, pero eventualmente podrian ser mas
    	
        $scope.userMarkersOrigin.forEach(function(marker) 
        {
          marker.setMap(null);
        });
        if ($scope.circleOrigin !== null)
  	  	{
  	  		$scope.circleOrigin.setMap(null);
  	  	}
        $scope.userMarkersOrigin = [];        
    	$scope.placeMarkerAndPanTo(e.latLng, $scope.originMap);    	    	
    	//$scope.$digest();
	});
    
    $scope.getStations = function()
    {
    	$http.get(servicesUrl + 'getStations')
    		.then(function (result){
    			if(result.status == 200)
    			{
    				$scope.stations = result.data;
    				angular.forEach($scope.stations, function(station, key) {
    					var marker = new google.maps.Marker({
    						position: new google.maps.LatLng(station.latitud,station.longitud),
    						map: $scope.destinationMap,
    						title: station.descripcion,
    						id_parada: station.id_parada
    					});
    					if (station.es_terminal == true)
    					{
    						marker.setIcon("static/images/marker_blue.png");
    					}
    					else
    					{
    						marker.setIcon("static/images/marker_green.png"); 
    					}
    					
    					marker.addListener('click', function() 
    					{
    						//infowindow.setContent('<p>' + station.descripcion + '</p><p><button class="btn btn-sm btn-primary"><i class="fa fa-check-square fa-lg pull-left"></i>Seleccionar</button></p>');						
    						infowindow.setContent('<p>' + station.descripcion + '</p>');    						
    						infowindow.open($scope.destinationMap, marker);
    					});
    					
    					$scope.destinoMarkers.push(marker);
    				});
				}
		});
    };

    
    $scope.getFilteredOrigins = function()
    {
    	$http.get(servicesUrl + 'getFilteredStations', JSON.Stringlify($scope.nearbyDestinations))
    		.then(function (result){
    			if(result.status == 200)
    			{
    				$scope.filteredOrigins = result.data;
    				//aca hay que cargar todos los puntos en el mapa de origenes
    			}
    		}
		);
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
    
    $scope.showWarning = function()
    {
    	$("#warningRadio").show();    	
    };
    
    $scope.closeWarning = function()
    {
    	$("#warningRadio").hide();    	
    };
    
    $scope.searchTravels = function() 
    {
    	$scope.firstSearch = true;
    	
    	$("#originModal").modal('hide');
    	$.blockUI();
    	//pasar los destinos y sacar de ahi las lineas
    	$scope.travelSearch.origins = $scope.listaIDSeleccionadosOrigin;
    	$scope.travelSearch.destinations = $scope.listaIDSeleccionados;    	
    	$http.post(servicesUrl +'searchTravels', JSON.stringify($scope.travelSearch))
		.success(function(data, status, headers, config)
		{				
			$("#travelsSearchGrid").removeClass('hidden');
			$.unblockUI();
			//carga la grilla
			$scope.travels = data;
			$scope.travelsSearchGrid.data = $scope.travels;			  
			//hace scroll hasta la tabla
			element = document.getElementById("travelsSearchGrid");
			alignWithTop = true;
			//element.scrollIntoView(alignWithTop);   
			$scope.$apply;
		})
		.error(function()
		{
			$.unblockUI();
			//$scope.error_message = ''; 
			//$("#errorModal").modal("toggle");
		}); 	
    };
    
    $scope.travelsSearchGrid = 
    {
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: false,
		showGridFooter: false,
		enablePaginationControls: false,
	    showColumnFooter: false,
	    enableColumnMenus: false,
        columnDefs:
    	[
          { name:'Linea', field: 'numero' },
          { name:'Origen', field: 'origen_description', cellTooltip: true },
          { name:'Destino', field: 'destino_description', cellTooltip: true },
          { name:'Salida', cellTemplate: '<div style="padding-top:5px" class="text-center ngCellText">{{ row.entity.inicio | date:" h:mma"}}</div>' },
          { name:'Nº Coche', field: 'numerov' },
          { name:'Asientos Disp.', field: 'cantasientos' },
          { name:'Costo', field: 'valor' },
          { name: 'Acciones', width: '105',
          	enableSorting: false,
              cellTemplate:'<button style="width: 65px" class="btn-xs btn-primary" ng-click="grid.appScope.selectSeats(row.entity.id_viaje, row.entity.origen, row.entity.destino, row.entity.linea_id_linea, row.entity.id_vehiculo, row.entity.valor)">Comprar</button>'
            	  			+  '<button style="width: 32px; margin-left: 5px" class="btn-xs btn-primary" ng-click="grid.appScope.showRoute(row.entity.linea_id_linea, row.entity.origen, row.entity.destino)">Ver</button>'
      	  }	
        ]
     };
    
    $scope.showDestinationMap = function()
    {
     	$("#destinationModal").modal('show');
    	$timeout(function () {            
    		google.maps.event.trigger($scope.destinationMap, 'resize');
        }, 400);    	 
    	/*var bounds = new google.maps.LatLngBounds();
    	$scope.destinoMarkers;
    	angular.forEach($scope.stations, function(station, key) 
        {
    		position = new google.maps.LatLng(station.latitud,station.longitud);
    		bounds.extend(position);
    		
		});
    	window.alert(bounds.getCenter().lng());
    	window.alert(bounds.getCenter().lng()-0.5);    	  	
    	var micentro = new google.maps.LatLng(bounds.getCenter().lng()-1, bounds.getCenter().lat()-1);
    	//$scope.destinationMap.setCenter(bounds.getCenter());    	
    	$scope.destinationMap.setCenter(micentro);
    	window.alert(micentro);
    	$scope.destinationMap.fitBounds(bounds);    	
    	//$scope.destinationMap.setCenter(bounds.getCenter());   	 	
    	$scope.destinationMap.setZoom(9);
    	$timeout(function () {            
    		google.maps.event.trigger($scope.destinationMap, 'resize');
        }, 400);*/   	
    };
    
    $scope.showOriginMap = function()
    {
    	if($scope.origenMarkers.length > 0)
    	{
    		$("#originModal").modal('show');
    		$timeout(function () { 
    			google.maps.event.trigger($scope.originMap, 'resize');
    		}, 400);    		
    	}
    };
    
    $scope.searchOrigins = function()
    {
    	$("#destinationModal").modal('hide');
    	$.blockUI();
    	$scope.limpioOrigenes();
    	$http.post(servicesUrl +'getFilteredStations', JSON.stringify($scope.listaIDSeleccionados))
    	.then(function(response)
    			{
		    		$.unblockUI();		
		        	if(response.status == 200)
		        	{
		        		$scope.originStations = response.data;
		        		angular.forEach($scope.originStations, function(station, key) 
		        		{
	    					var marker = new google.maps.Marker({
	    						position: new google.maps.LatLng(station.latitud,station.longitud),
	    						map: $scope.originMap,
	    						title: station.descripcion,
	    						id_parada: station.id_parada
	    					});
	    					if (station.es_terminal == true)
	    					{
	    						marker.setIcon("static/images/marker_blue.png");
	    					}
	    					else
	    					{
	    						marker.setIcon("static/images/marker_green.png"); 
	    					}
	    					
	    					marker.addListener('click', function() 
	    					{
	    						infowindow.setContent('<p>' + station.descripcion + '</p><p><button class="btn btn-sm btn-primary" ng-click=""><i class="fa fa-check-square fa-lg pull-left"></i>Seleccionar</button></p>');   						
	    						
	    						infowindow.open($scope.originMap, marker);
	    					});
	    					
	    					$scope.origenMarkers.push(marker);
	    				});
		        	}
		        	else
	        		{
		        		
	        		}
    			}
    	);
    };
    
    $scope.selectSeats = function(id_viaje, origen, destino, id_linea, id_vehiculo, valor)
    {
    	$.blockUI();    	
    	$scope.seatsForm = {};
    	$scope.seatsForm.id_viaje = id_viaje;
    	$scope.seatsForm.origen = origen;
    	$scope.seatsForm.destino = destino;
    	$scope.seatsForm.id_linea = id_linea;
    	$scope.seatsForm.id_vehiculo = id_vehiculo;
    	$scope.seatsForm.valor = valor;
    	
    	
    	$http.post(servicesUrl +'getSeats', JSON.stringify($scope.seatsForm))
		.success(function(data, status, headers, config)
		{				
			$("#selectTicketsModal").modal('show');
			$.unblockUI();
			//cargar lista de asientos
			$scope.seats = {};
			$scope.seats = data;
			$scope.selected = {};
			//scripts para la seleccion de asientos.
			var lugares = 0;
			var txt_map = '';
			var array_asientos = [];
			$scope.reservados = [];
			$scope.noDisponibles = [];
			for (var i=0; i <$scope.seats.length; i++)
			{
				var accesibleKey = '';
				if($scope.seats[i].es_accesible == true)
				{
					accesibleKey = 'A';
				}
				else
				{
					accesibleKey = 'a';
				}
				if($scope.seats[i].reservado == true)
				{
					$scope.noDisponibles.push($scope.seats[i].id_asiento);
				}
				switch(lugares) 
				{
					case 0:
							txt_map = txt_map + accesibleKey + "[" + $scope.seats[i].id_asiento + "]";
							lugares++;
							break;
					case 1: 
							txt_map = txt_map + accesibleKey + "[" + $scope.seats[i].id_asiento +"]_";
							lugares++;
							break;
					case 2:
							txt_map = txt_map + accesibleKey + "[" + $scope.seats[i].id_asiento + "]";
							lugares++;
							break;
					case 3:
							txt_map = txt_map + accesibleKey + "[" + $scope.seats[i].id_asiento + "]";
							array_asientos.push(txt_map);
							txt_map = '';
							lugares = 0;
							break;					
				}
			}
			//si no queda de 4 asientos, agrega el pico
			if (txt_map != '')
			{
				array_asientos.push(txt_map);
			}
			//mete los espacios
			/*for(var i = 0; i < array_asientos.length; i++)
			{
				aux = array_asientos[i];
				aux = aux.substring(0, 2) + "_" + aux.substring(2, 4); 
				array_asientos[i] = aux;
			}*/
			var firstSeatLabel = 1;
			var $cart = $('#selected-seats');
			var $counter = $('#counter');
			var $total = $('#total');

			$scope.sc = $('#seat-map').seatCharts({
				map: array_asientos,
				seats: {
					a: {
						price   : valor,
						classes : 'first-class', //your custom CSS class
						category: 'First Class'
					},
					A: {
						price   : valor,
						classes : 'first-class', //your custom CSS class
						category: 'First Class'
					},
					/*,
					e: {
						price   : 40,
						classes : 'economy-class', //your custom CSS class
						category: 'Economy Class'
					}	*/				
				
				},
				naming : {
					top : false,
					getLabel : function (character, row, column) 
					{
						if(character == 'A')
						{
							return character + firstSeatLabel++;
						}
						else
						{
							return firstSeatLabel++;							
						}
					},
				},
				legend : {
					node : $('#legend'),
				    items : [
						[ 'a', 'available',   'Asiento Libre' ],
						[ 'a', 'available',   'Asiento Accesible (A)' ],
						/*[ 'e', 'available',   'Economy Class'],*/
						[ 'a', 'unavailable', 'Asiento Ocupado']
				    ]					
				},
				click: function () {
					if (this.status() == 'available') 
					{
						//let's create a new <li> which we'll add to the cart items
						/*$('<li>'+this.data().category+' Seat # '+this.settings.label+': <b>$'+this.data().price+'</b> <a href="#" class="cancel-cart-item">[cancel]</a></li>')
							.attr('id', 'cart-item-'+this.settings.id)
							.data('seatId', this.settings.id)
							.appendTo($cart);*/
						
						
						var sel_seat = [];
						sel_seat.txt = this.settings.label;
						sel_seat.price = this.data().price;
						sel_seat.id = this.settings.id;
						$scope.reservados.push(sel_seat);						
						/*
						 * Lets update the counter and total
						 *
						 * .find function will not find the current seat, because it will change its stauts only after return
						 * 'selected'. This is why we have to add 1 to the length and the current seat price to the total.
						 */
						$counter.text($scope.sc.find('selected').length+1);
						$total.text($scope.recalculateTotal($scope.sc)+this.data().price);
						$scope.precio_total = $scope.precio_total+this.data().price;
						$scope.$digest;
						$scope.$apply();
						return 'selected';
					} 
					else if (this.status() == 'selected') 
					{
						//update the counter
						$counter.text($scope.sc.find('selected').length-1);
						//and total
						$total.text($scope.recalculateTotal($scope.sc)-this.data().price);
						$scope.precio_total = $scope.precio_total-this.data().price;
						$scope.$digest;
						
						for(var i = 0; i < $scope.reservados.length; i++) 
						{
						    if($scope.reservados[i].id === this.settings.id) 
						    {
						    	$scope.reservados.splice(i, 1);
						    }
						}
						//remove the item from our cart
						//$('#cart-item-'+this.settings.id).remove();
					
						//seat has been vacated
						$scope.$apply();
						return 'available';
					} else if (this.status() == 'unavailable') {
						//seat has been already booked
						return 'unavailable';
					} else {
						return this.style();
					}
				}
			});

			//fin scripts
			$scope.sc.get($scope.noDisponibles).status('unavailable');

		})
		.error(function()
		{
			$.unblockUI();
		});
    }
    
    $scope.recalculateTotal = function(sc) 
    {
		var total = 0;	
		$scope.precio_total = 0;
		//basically find every selected seat and sum its price
		sc.find('selected').each(function () {
			total += this.data().price;
		});		
		$scope.precio_total = total;		
		return total;		
	}

    
    $scope.confirmSeats = function()
    {	
		$http.get(servicesUrl + 'getUserInfo')
		.then(function(response) 
		{
			if(response.status == 404)
			{
				$("#loginModal").modal("show");
				$.unblockUI();
			}
			else
			{
				$scope.seatsForm.seleccionados = [];
				for(var i = 0; i < $scope.reservados.length; i++) 
				{
					$scope.seatsForm.seleccionados.push($scope.reservados[i].id);			
				}
				localStorage.setItem($scope.$parent.getTicketStorageKey(), JSON.stringify($scope.seatsForm));				
				$("#selectTicketsModal").modal('hide');
		    	$("#divTravelForm").addClass('hidden');
		    	$("#travelsSearchGrid").addClass('hidden');
		    	$("#seatsConfirmForm").removeClass('hidden');	    
				
			}
		});    	
    }
    
    $scope.payTicket = function()
	{
    	$.blockUI();
    	$http.get(servicesUrl + 'getUserInfo')
		.then(function(response) 
		{
			if(response.status == 404)
			{
				$("#loginModal").modal("show");		
				$.unblockUI();
			}
			else
			{				
				//localStorage.setItem("userTickets", JSON.stringify($scope.seatsForm));
				$scope.seatsForm.seleccionados = [];
				for(var i = 0; i < $scope.reservados.length; i++) 
				{
					$scope.seatsForm.seleccionados.push($scope.reservados[i].id);			
				}  
				if($scope.rOption == "1")
				{
					$scope.seatsForm.rDoc = null;
				}
				else
				{
					$scope.seatsForm.rUser = null;
				}	
				if ($rootScope.user.rol_id_rol == "4")
				{
					$scope.seatsForm.rDoc = null;
					$scope.seatsForm.rUser = null;
				}	
				//$http.get(servicesUrl +'getPaypal')
				$scope.wrapper = {};
				$scope.wrapper.total = $scope.precio_total;
				$scope.wrapper.descripcion = "GoOn Services - Compra de Pasajes";	
				$http.post(servicesUrl +'getPaypal',  JSON.stringify($scope.wrapper))
				.then(function(response) 
				{							
		        	if(response.status == 200)
		        	{		
	        			$scope.payPalInfo = response.data;
	        			var link = ""
	        			for(var i=0; i < $scope.payPalInfo.links.length; i++)
	        			{
	        				var item = $scope.payPalInfo.links[i];
	        				if(item.rel == "approval_url")
	        				{
	        					link = item.href;
	        				}	
	        			}
	        			window.location = link;
	        			$.unblockUI();
		        	}
	    		}
				);				
			}
			
		}
		);
    	
						
	};
	//pago paypal
	//pago cash
	//reserva
	$scope.reserveTicket = function()
	{
		$.blockUI();
    	$http.get(servicesUrl + 'getUserInfo')
		.then(function(response) 
		{
			if(response.status == 404)
			{
				$("#loginModal").modal("show");
				$.unblockUI();
			}
			else
			{				
				//localStorage.setItem("userTickets", JSON.stringify($scope.seatsForm));
				$scope.seatsForm.seleccionados = [];
				for(var i = 0; i < $scope.reservados.length; i++) 
				{
					$scope.seatsForm.seleccionados.push($scope.reservados[i].id);			
				}  
				if($scope.rOption == "1")
				{
					$scope.seatsForm.rDoc = null;
				}
				else
				{
					$scope.seatsForm.rUser = null;
				}	
				if ($rootScope.user.rol_id_rol == "4")
				{
					$scope.seatsForm.rDoc = null;
					$scope.seatsForm.rUser = null;
				}
				
				$http.post(servicesUrl +'reserveTicket', JSON.stringify($scope.seatsForm))
				.then(function(response) 
					{
						$.unblockUI();		
			        	if(response.status == 200)
			        	{		
		        			$scope.confirmedSeats = response.data;
		        			$scope.showSuccessAlert("Los boletos han sido reservados..");
		        			$("#seatsInfo").removeClass('hidden');
		        	    	$("#divTravelForm").removeClass('hidden');
		        	    	$("#travelsSearchGrid").removeClass('hidden');
		        	    	$("#seatsConfirmForm").addClass('hidden');
			        	}
		    		}
				);
			}
			
		}
		);
	}
	
	$scope.buyTicket = function()
	{
		$.blockUI();
    	$http.get(servicesUrl + 'getUserInfo')
		.then(function(response) 
		{
			if(response.status == 404)
			{
				$("#loginModal").modal("show");
				$.unblockUI();
			}
			else
			{				
				//localStorage.setItem("userTickets", JSON.stringify($scope.seatsForm));
				$scope.seatsForm.seleccionados = [];
				for(var i = 0; i < $scope.reservados.length; i++) 
				{
					$scope.seatsForm.seleccionados.push($scope.reservados[i].id);			
				}  
				if($scope.rOption == "1")
				{
					$scope.seatsForm.rDoc = null;
				}
				else
				{
					$scope.seatsForm.rUser = null;
				}	
				if ($rootScope.user.rol_id_rol == "4")
				{
					$scope.seatsForm.rDoc = null;
					$scope.seatsForm.rUser = null;
				}
				$http.post(servicesUrl +'buyTicket', JSON.stringify($scope.seatsForm))
				.then(function(response) 
					{
						$.unblockUI();		
			        	if(response.status == 200)
			        	{		
		        			$scope.confirmedSeats = response.data;
		        			$scope.showSuccessAlert("Los boletos han sido reservados..");
		        			$("#seatsInfo").removeClass('hidden');
		        	    	$("#divTravelForm").removeClass('hidden');
		        	    	$("#travelsSearchGrid").removeClass('hidden');
		        	    	$("#seatsConfirmForm").addClass('hidden');
			        	}
		    		}
				);
			}
			
		}
		);
	}
	
	$scope.iniciarCheckout = function()
	{
		$http.get(servicesUrl + 'iniciarCheckout')
		.then(function(response) 
		{
			
		});
	};
	
	$scope.showBuyDialog = function(row)
    {
    	$("#buyModal").modal('show');
    };
    $scope.hideBuyDialog = function(row)
    {
    	$("#buyModal").modal('hide');
    };
    
    $scope.travelSubmit = function()
    {
    	if($scope.frmOpt == "1")
    	{
    		$scope.reserveTicket();
    	}
    	else if($scope.frmOpt == "2")
    	{
    		$scope.buyTicket();
    	}
    	else if($scope.frmOpt == "3")
    	{
    		$scope.payTicket();
    	}
    	
    }
    
    $scope.printDiv = function(idDiv)
    {
    	
    	
    	//var printSection = document.getElementById('printSection');

        // if there is no printing section, create one
//        if (!printSection) {
//            printSection = document.createElement('div');
//            printSection.id = 'printSection';
//            document.body.appendChild(printSection);
//        }
//        var elemToPrint = document.getElementById(idDiv);
//        
//        var domClone = elemToPrint.cloneNode(true);
//        
//        printSection.appendChild(domClone);
        
        window.print();
        
        //printSection.innerHTML = '';
    }
    
    $scope.getStations();
    
    $scope.routeMap = new google.maps.Map(document.getElementById('rutaMap'), 
    {
    	center: {lat: -34.2, lng: -56.5},
        zoom: 9
    });
    
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
				$scope.routeLine.selorigen = origen;
				$scope.routeLine.seldestino = destino;
				$scope.routeMap.setCenter(new google.maps.LatLng(-34.2, -56.5));
		   	    $scope.routeMap.setZoom(9);
		   	    $scope.routeMap.panTo(new google.maps.LatLng(-34.2, -56.5));		   	  
		   	    $("#rutaModal").modal('show');
		   	    $timeout(function () 
   	    		{            
   	    			$scope.routeMap.setZoom(9);		
   	    			google.maps.event.trigger($scope.routeMap, 'resize');
												
		        }, 400);	
		    	
		    	var directionsService = new google.maps.DirectionsService;
		    	var directionsDisplay = new google.maps.DirectionsRenderer({
		    	    														suppressPolylines: true,
		    	    														infoWindow: infowindow
		    	  															});
		    	directionsDisplay.setMap($scope.routeMap);
		   	    $scope.calculateAndDisplayRoute(directionsService, directionsDisplay);		   	    
		   	    $scope.routeMap.setCenter(new google.maps.LatLng(-34.2, -56.5));
		   	    $scope.routeMap.setZoom(9);
		   	    $scope.routeMap.panTo(new google.maps.LatLng(-34.2, -56.5));		   	  
		   	    $("#rutaModal").modal('show');
		   	    $timeout(function () 
   	    		{            
   	    			$scope.routeMap.setZoom(9);		
   	    			google.maps.event.trigger($scope.routeMap, 'resize');
												
		        }, 400);		   	    
				$scope.$digest();
			})
			.error(function()
			{
							
			});
		});
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
    		 							$scope.renderDirectionsPolylines(response, $scope.routeMap);
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
		        //bounds.extend(nextSegment[k]);
		      }
		      polylines.push(stepPolyline);
		      stepPolyline.setMap($scope.routeMap);
		      // route click listeners, different one on each step
		      /*google.maps.event.addListener(stepPolyline, 'click', function(evt) 
		      {
		    	infowindow.setContent("you clicked on the route<br>" + evt.latLng.toUrlValue(6));
		        infowindow.setPosition(evt.latLng);
		        infowindow.open($scope.routeMap);
		      })*/
		    }
		  }
		  //$scope.routeMap.fitBounds(bounds);
		  $scope.routeMap.setCenter(new google.maps.LatLng(-34.2, -56.5));
	   	  $scope.routeMap.setZoom(9);
	   	  $timeout(function () 
				{            
					google.maps.event.trigger($scope.routeMap, 'resize');
		        }, 400);
	}
	
	/*$marcadorCerca = function(lat, lng) 
	{
	    var R = 6371; // radius of earth in km
	    var distances = [];
	    var closest = -1;
	    for(i=0;i<map.markers.length; i++ ) 
	    {
	        var mlat = map.markers[i].position.lat();
	        var mlng = map.markers[i].position.lng();
	        var dLat  = rad(mlat - lat);
	        var dLong = rad(mlng - lng);
	        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	            Math.cos(rad(lat)) * Math.cos(rad(lat)) * Math.sin(dLong/2) * Math.sin(dLong/2);
	        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	        var d = R * c;
	        distances[i] = d;
	        if ( closest == -1 || d < distances[closest] ) {
	            closest = i;
	        }
	    }    
	}*/
    
}); 	