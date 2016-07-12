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
    
    $scope.seatsForm = {};
    $scope.rOption = "1";    
    $scope.frmOpt = "0";    
	$scope.custom_response = null;    
    i18nService.setCurrentLang('es');
    
    if($rootScope.user !== null)
    {
    	localStorage.removeItem($scope.$parent.getTicketStorageKey());
    }
      
    //DESTINATION MAP
    $scope.destinationMap = new google.maps.Map(document.getElementById('destinationMap'), 
    {
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
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
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
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
    						infowindow.setContent('<p>' + station.descripcion + '</p><p><button class="btn btn-sm btn-primary" ng-click=""><i class="fa fa-check-square fa-lg pull-left"></i>Seleccionar</button></p>');   						
    						
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
			element.scrollIntoView(alignWithTop);    										
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
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Linea', field: 'numero' },
          { name:'Origen', field: 'origen_description' },
          { name:'Destino', field: 'destino_description'},
          { name:'Salida', cellTemplate: '<div class="text-center ngCellText">{{ row.entity.inicio | date:"dd/MM/yyyy @ h:mma"}}</div>' },
          //{ name:'Tiempo Estimado (min)', field: 'linea.tiempo_estimado' },
          /*{ 
        	  name: 'Pasajeros Parados', 
        	  cellTemplate: '<div class="text-center ngCellText">{{row.entity.linea.viaja_parado | SiNo}}</div>'
          },*/
          { name:'Nº Coche', field: 'id_vehiculo' },
          { name:'Asientos Disp.', field: 'cantasientos' },
          { name:'Costo', field: 'valor' },
          { name: 'Acciones',
          	enableFiltering: false,
          	enableSorting: false,
              cellTemplate:'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.selectSeats(row.entity.id_viaje, row.entity.origen, row.entity.destino, row.entity.linea_id_linea, row.entity.id_vehiculo, row.entity.valor)">Comprar</button>'
      	  }
        ]
     };
    
    $scope.showDestinationMap = function()
    {
    	$("#destinationModal").modal('show');
    	$timeout(function () {            
    		google.maps.event.trigger($scope.destinationMap, 'resize');
        }, 400);
    };
    
    $scope.showOriginMap = function()
    {
    	$("#originModal").modal('show');
    	$timeout(function () { 
    		google.maps.event.trigger($scope.originMap, 'resize');
    	}, 400);
    };
    
    $scope.searchOrigins = function()
    {
    	$("#destinationModal").modal('hide');
    	$.blockUI();
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
				if($scope.seats[i].reservado == true)
				{
					$scope.noDisponibles.push($scope.seats[i].id_asiento);
				}
				switch(lugares) 
				{
					case 0:
							txt_map = txt_map + "a[" + $scope.seats[i].id_asiento + "]";
							lugares++;
							break;
					case 1: 
							txt_map = txt_map + "a[" + $scope.seats[i].id_asiento + "]_";
							lugares++;
							break;
					case 2:
							txt_map = txt_map + "a[" + $scope.seats[i].id_asiento + "]";
							lugares++;
							break;
					case 3:
							txt_map = txt_map + "a[" + $scope.seats[i].id_asiento + "]";
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
					}/*,
					e: {
						price   : 40,
						classes : 'economy-class', //your custom CSS class
						category: 'Economy Class'
					}	*/				
				
				},
				naming : {
					top : false,
					getLabel : function (character, row, column) {
						return firstSeatLabel++;
					},
				},
				legend : {
					node : $('#legend'),
				    items : [
						[ 'a', 'available',   'Asiento Libre' ],
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
}); 	