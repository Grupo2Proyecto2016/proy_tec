goOnApp.controller('linesController', function($scope, $http, uiGridConstants, i18nService, $timeout)									 
{
	$scope.message = 'Genera nuevas lineas de manera cómoda.';
    $scope.error_message = '';
    $scope.custom_response = null;    
    $scope.txt_minutos = "";
    $scope.txt_km = "";
    $scope.txt_minutosV = ""; /*Todas las variables y funciones que tengan V al final, impactan sobre el viaje de vuelta*/
    $scope.txt_kmV = "";
    $scope.valor_sugerido = "";
    i18nService.setCurrentLang('es');
    
    $scope.lineForm = {};
    $scope.stops = {};
    $scope.terminals = null;
    $scope.lines = null;
    $scope.markers = [];    
    $scope.markersV = [];
    $scope.reajustes = [];
    $scope.reajustesV = [];
    $scope.letras = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $scope.stopDelay = 0;
    $scope.priceByTravelKm = 0;
    
    $scope.rutaTrazada = false;
    $scope.rutaTrazadaV = false;
    
    $scope.inicializoMarkers = function()
    {
    	for (var i = 0; i < $scope.markers.length; i++) 
    	{
    		$scope.markers[i].setMap(null);    		
    	}
    	$scope.markers = [];
    	$scope.markers.lenght = 0;
    }
    
    $scope.inicializoMarkersV = function()
    {
    	for (var i = 0; i < $scope.markersV.length; i++) 
    	{
    		$scope.markersV[i].setMap(null);    		
    	}
    	$scope.markersV = [];
    	$scope.markersV.lenght = 0;
    }
    
    $scope.getParameters = function(){
		$http.get(servicesUrl + 'getParams')
		.then(function(response) {
			if(response.status == 200)
			{
				angular.forEach(response.data, function(param, key) {
					  switch(param.id)
					  {
					  	/*case 1: $scope.priceByKg = param;
					  		break;
					  	case 2: $scope.priceByVolume = param;
					  		break;					  	
					  	case 4: $scope.priceByPackageKm = param;
					  		break;
					  	case 5: $scope.maxReservationDelay = param;					  	
					  		break;*/
					  	case 3: $scope.priceByTravelKm = param;
				  			break;
					  	case 6: $scope.stopDelay = param;
					  		break;
					  }
				});
			}
		});
	};
    
    $scope.showForm = function()
    {	    	
    	$scope.lineForm = {};
    	$scope.lineForm.generaVuelta = true;
    	$scope.inicializoMarkers();
    	$scope.inicializoMarkersV();    	
    	$("#divLineForm").removeClass('hidden');
    	$timeout(function () {            
    		google.maps.event.trigger($scope.map, 'resize');	//refresh map            
        }, 500);
    	$timeout(function () {            
            google.maps.event.trigger($scope.mapV, 'resize');	//refresh map            
        }, 500);
    };
    
    $scope.hideForm = function()
    {
    	$("#divLineForm").addClass('hidden');		
    };
    
    $scope.showFijoSugerido = function()
    {
    	$("#fijoSugerido").removeClass('hidden');	
    	$("#fijoSugeridoVariable").removeClass('hidden');	
    };
    
    $scope.getLines = function()
    {
    	$http.get(servicesUrl + 'getLines').success(function(data, status, headers, config)
    	{
    		$scope.lines = data;
    		$scope.linesGrid.data = $scope.lines;
    	});
    };   
    
    $scope.linesGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Numero', field: 'numero' },
          { name:'Origen', field: 'origen.descripcion' },
          { name:'Destino', field: 'destino.descripcion'},
          { name:'Tiempo Estimado (min)', field: 'tiempo_estimado' },
          { name: 'Pasajeros Parados', cellTemplate: '<div class="text-center ngCellText">{{row.entity.viaja_parado | SiNo}}</div>' },
          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<p align="center"><button style="" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button></p>'
            	/*'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getBusDetails(row)">Detalles</button>'+*/
            			  
    	  }
        ]
     };
    
    $scope.showDeleteDialog = function(row)
    {
    	$scope.lineToDelete = row.entity.id_linea;
    	$("#deleteModal").modal('show');
    };
    
    
    $scope.getTerminals = function()
    {
    	$http.get(servicesUrl + 'getTerminals').success(function(data, status, headers, config) 
    	{
        	$scope.terminals = data;        	
    	});
    };    
    
    $scope.getLines();
    $scope.getParameters();
    $scope.getTerminals();
    
    $scope.reajustaValores = function()
    {
    	//checkeos de integridad de los datos
    	if ($scope.rutaTrazada == false)
    	{
    		$scope.error_message = 'Debe trazar la ruta antes de calcular los reajustes'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	if (($scope.lineForm.costo_minimo <= 0) || ($scope.lineForm.costo_minimo === undefined))
    	{
    		$scope.error_message = 'Debe especificar costo mínimo del viaje para realizar el cálculo'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	if (($scope.lineForm.costo_maximo <= 0) || ($scope.lineForm.costo_maximo === undefined))    	
    	{          
    		$scope.error_message = 'Debe especificar costo máximo del viaje para realizar el cálculo'; 
			$("#errorModal").modal("toggle");
			return;
    	}    	
    	
    	if (($scope.lineForm.costo_maximo) < ($scope.lineForm.costo_minimo))    	
    	{          
    		$scope.error_message = 'El costo mínimo no puede superar el costo máximo'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	$scope.reajustes = []; //guarda los reajustes para saber si se modificaron sin reajustar
    	
    	var tot_km = 0;
    	
    	for (var i = 0; i < $scope.markers.length; i++)
    	{
    		tot_km = tot_km + $scope.markers[i].km;
    	}
    	
    	tot_km = tot_km / 1000; //viene en metros
    	var km_repartir = tot_km;
    	var pesos_repartir = $scope.lineForm.costo_maximo - $scope.lineForm.costo_minimo;
    	var distancia = 0;
    	for (var i = 0; i < $scope.markers.length; i++)
    	{    		
    		if ($scope.markers[i].reajusta)
    		{
    			if (distancia == 0)
    			{
    				distancia = $scope.markers[i].km / 1000;
    			}
    			$scope.markers[i].reajuste = Math.round((pesos_repartir / km_repartir) * (distancia + ($scope.markers[i].km / 1000)));
    			//km_repartir = km_repartir  - ($scope.markers[i].km / 1000);
    			distancia = 0;    			
    		}
    		else
    		{
    			distancia = distancia + ($scope.markers[i].km / 1000)
    		}
    		$scope.reajustes.push($scope.markers[i].reajuste);
    	}    	
    };
    
    $scope.reajustaValoresV = function()
    {
    	//checkeos de integridad de los datos
    	if ($scope.rutaTrazadaV == false)
    	{
    		$scope.error_message = 'Debe trazar la ruta antes de calcular los reajustes'; 
			$("#errorModal").modal("toggle");
    	}
    	
    	if (($scope.lineForm.costo_minimo <= 0) || ($scope.lineForm.costo_minimo === undefined))
    	{
    		$scope.error_message = 'Debe especificar costo mínimo del viaje para realizar el cálculo'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	if (($scope.lineForm.costo_maximo <= 0) || ($scope.lineForm.costo_maximo === undefined))    	
    	{          
    		$scope.error_message = 'Debe especificar costo máximo del viaje para realizar el cálculo'; 
			$("#errorModal").modal("toggle");
			return;
    	}  
    	
    	if (($scope.lineForm.costo_maximo) < ($scope.lineForm.costo_minimo))    	
    	{          
    		$scope.error_message = 'El costo mínimo no puede superar el costo máximo'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	$scope.reajustesV = []; //guarda los reajustes para saber si se modificaron sin reajustar
    	
    	var tot_km = 0;
    	
    	for (var i = 0; i < $scope.markersV.length; i++)
    	{
    		tot_km = tot_km + $scope.markersV[i].km;
    	}
    	
    	tot_km = tot_km / 1000; //viene en metros
    	var km_repartir = tot_km;
    	var pesos_repartir = $scope.lineForm.costo_maximo - $scope.lineForm.costo_minimo;
    	var distancia = 0;
    	for (var i = 0; i < $scope.markersV.length; i++)
    	{    		
    		if ($scope.markersV[i].reajusta)
    		{
    			if (distancia == 0)
    			{
    				distancia = $scope.markersV[i].km / 1000;
    			}
    			$scope.markersV[i].reajuste = Math.round((pesos_repartir / km_repartir) * (distancia + ($scope.markersV[i].km / 1000)));
    			//km_repartir = km_repartir  - ($scope.markers[i].km / 1000);
    			distancia = 0;
    		}
    		else
    		{
    			distancia = distancia + ($scope.markersV[i].km / 1000)
    		}
    		$scope.reajustesV.push($scope.markersV[i].reajuste);
    	}    	
    };
    
    $scope.createLine = function()
    {    	
    	//chequeo de reajustes
    	var preguntaReajuste = false;
		if ($scope.lineForm.calculo == 2) //costo variable
		{
			for (var i = 0; i < $scope.markers.length; i++)
			{
				if ($scope.reajustes[i] != $scope.markers[i].reajuste)
				{
					preguntaReajuste = true;
				}
			}    			
			
			for (var i = 0; i < $scope.markersV.length; i++)
			{
				if ($scope.reajustesV[i] != $scope.markersV[i].reajuste)
				{
					preguntaReajuste = true;
				}
			}
		}		    	
		if (preguntaReajuste)
		{
			$scope.showReajuste();	
		}
		else
		{
			$scope.persistLine();
		}
		
    };
    
    $scope.showReajuste = function(row)
    {    	
    	$("#reajusteModal").modal('show');
    };
    
    $scope.hideReajuste = function(row)
    {
    	$("#reajusteModal").modal("hide");
    }
    
    $scope.persistLine = function()
    {
    	$scope.hideReajuste();
    	if ($scope.rutaTrazada == false)
    	{
    		$scope.error_message = 'Debe trazar la ruta correspondiente al viaje.'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	if (($scope.rutaTrazadaV == false) && ($scope.lineForm.generaVuelta == true))
    	{
    		$scope.error_message = 'Debe trazar la ruta correspondiente al viaje de vuelta.'; 
			$("#errorModal").modal("toggle");
			return;
    	}
    	
    	if(!$scope.form.$invalid)
		{	
    		$scope.lineForm.paradas = [];
    		for (var i = 0; i < $scope.markers.length; i++)
    		{
    			var newP = {};
    			newP.latitud     = $scope.markers[i].position.lat();
    			newP.longitud    = $scope.markers[i].position.lng();
    			newP.reajuste    = $scope.markers[i].reajuste;
    			newP.id_parada   = $scope.markers[i].id_parada;
    			newP.descripcion = $scope.markers[i].descripcion;
    			newP.direccion   = $scope.markers[i].direccion;
    			$scope.lineForm.paradas.push(newP);
    		}
    		
    		$scope.lineForm.paradasV = [];
    		for (var i = 0; i < $scope.markersV.length; i++)
    		{
    			var newP = {};
    			newP.latitud     = $scope.markersV[i].position.lat();
    			newP.longitud    = $scope.markersV[i].position.lng();
    			newP.reajuste    = $scope.markersV[i].reajuste;
    			newP.id_parada   = $scope.markersV[i].id_parada;
    			newP.descripcion = $scope.markersV[i].descripcion;
    			newP.direccion   = $scope.markersV[i].direccion;
    			$scope.lineForm.paradasV.push(newP);
    		}
    		
    		$http.post(servicesUrl +'createLine', JSON.stringify($scope.lineForm))
			.success(function()
			{				
				$scope.hideForm();
		    	$scope.lineForm = {};	
		    	$scope.lineForm.generaVuelta = true;
		    	//$scope.inicializoMarkers();
		    	//$scope.inicializoMarkersV(); 
		        $scope.stops = {};	
		        $scope.getParameters();
		    	$scope.getLines();
		    	$scope.getTerminals();
		    	$.unblockUI();
				$scope.showSuccessAlert("Linea creada.");							
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear la sucursal. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			});  
		}
    }
    
    $scope.deleteLine = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteLine', JSON.stringify($scope.lineToDelete))
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
	        			$scope.getLines();	
		        		$scope.showSuccessAlert("La linea ha sido borrada.");	
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
	};
	
	$scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
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
    
    $scope.getTerminalById = function(id)
    {
    	for (var i = 0; i < $scope.terminals.length; i++) 
    	{
    		if($scope.terminals[i].id_parada == id)
    		{
    			return $scope.terminals[i];
    		}
    	}
    	return null;
    };
    
    $scope.updateTerminalDestino = function ()   
    {
    	var term = $scope.getTerminalById($scope.lineForm.destino);    	
    	var myLatlng = new google.maps.LatLng(term.latitud, term.longitud);    	
    	var esprimero = false;
    	
    	var marker = new google.maps.Marker(
    	{
    		position: myLatlng,
	  	    map: $scope.map,
	  	    animation: google.maps.Animation.DROP, //just for fun
	  	    es_terminal: true,
	  	    es_peaje: false,
	  	    es_origen: false,
	  	    descripcion: '',
	  	    reajusta: false,
	  	    reajuste: 0,
	  	    km: 0,
	  	    id_parada:$scope.lineForm.destino 
	  	});
    	
    	if($scope.markers.length == 0)
    	{
    		$scope.markers.splice(1, 0, marker);
    		esprimero = true;
    	}
    	
    	var tope = $scope.markers.length;
    	
    	if (($scope.markers[tope-1].es_terminal) && ($scope.markers[tope-1].es_origen === false))
    	{
    		if (esprimero === false){$scope.markers[tope-1].setMap(null)};
    		$scope.markers[tope-1] = marker;    
    	}
    	else
    	{
    		$scope.markers.push(marker);
    	}
    	
    	var l = myLatlng.lat();
		var g = myLatlng.lng();
		$scope.geocodePosition(marker);
		$scope.map.panTo(myLatlng);
		
		//Viaje de vuelta
		esprimero = false;
		
		var marker = new google.maps.Marker(
    	{
    		position: myLatlng,
	  	    map: $scope.mapV,
	  	    animation: google.maps.Animation.DROP, //just for fun
	  	    es_terminal: true,
	  	    es_peaje: false,
	  	    es_origen: true,
	  	    descripcion: '',
	  	    reajusta: false,
	  	    reajuste: 0,
	  	    km: 0,
	  	    id_parada:$scope.lineForm.destino
	  	});
		
		if($scope.markersV.length == 0)
    	{
    		$scope.markersV.splice(1, 0, marker);
    		esprimero = true;
    	}
		
		if(($scope.markersV[0].es_terminal) && ($scope.markersV[0].es_origen))
    	{  
    		if (esprimero === false){$scope.markersV[0].setMap(null)};
    		$scope.markersV[0] = marker;    		
    	}
    	else
    	{
    		$scope.markersV.splice(0, 0, marker);    		
    	}
    	
    	var l = myLatlng.lat();
		var g = myLatlng.lng();
		$scope.geocodePosition(marker);
		$scope.mapV.panTo(myLatlng);
    }
    
    $scope.updateTerminalOrigen = function ()   
    {
    	var term = $scope.getTerminalById($scope.lineForm.origen);    	
    	var myLatlng = new google.maps.LatLng(term.latitud, term.longitud);    	
    	var esprimero = false;
    	
    	var marker = new google.maps.Marker(
    	{
    		position: myLatlng,
	  	    map: $scope.map,
	  	    animation: google.maps.Animation.DROP,
	  	    es_terminal: true,
	  	    es_peaje: false,
	  	    es_origen: true,
	  	    descripcion: '',
	  	    reajusta: false,
	  	    reajuste: 0,
	  	    km: 0,
	  	    id_parada:$scope.lineForm.origen
	  	});
    	
    	if($scope.markers.length == 0)
    	{
    		$scope.markers.splice(1, 0, marker);
    		esprimero = true;
    	}    	
    	
    	if(($scope.markers[0].es_terminal) && ($scope.markers[0].es_origen))
    	{  
    		if (esprimero === false){$scope.markers[0].setMap(null)};
    		$scope.markers[0] = marker;    		
    	}
    	else
    	{
    		$scope.markers.splice(0, 0, marker);    		
    	}
    	
    	var l = myLatlng.lat();
		var g = myLatlng.lng();
		$scope.geocodePosition(marker);
		$scope.map.panTo(myLatlng);
		
		
		//ViajeVuelta
		esprimero = true;
		
		var marker = new google.maps.Marker(
    	{
    		position: myLatlng,
	  	    map: $scope.mapV,
	  	    animation: google.maps.Animation.DROP, //just for fun
	  	    es_terminal: true,
	  	    es_peaje: false,
	  	    es_origen: false,
	  	    descripcion: '',
	  	    reajusta: false,
	  	    reajuste: 0,
	  	    km: 0,
	  	    id_parada:$scope.lineForm.origen
	  	});
    	
    	if($scope.markersV.length == 0)
    	{
    		$scope.markersV.splice(1, 0, marker);
    		esprimero = true;
    	}
    	
    	var tope = $scope.markersV.length;
    	
    	if (($scope.markersV[tope-1].es_terminal) && ($scope.markersV[tope-1].es_origen === false))
    	{
    		if (esprimero === false){$scope.markersV[tope-1].setMap(null)};
    		$scope.markersV[tope-1] = marker;    
    	}
    	else
    	{
    		$scope.markersV.push(marker);
    	}
    	
    	var l = myLatlng.lat();
		var g = myLatlng.lng();
		$scope.geocodePosition(marker);
		$scope.mapV.panTo(myLatlng);
		
    };
    
    /*Mapa*/
    var directionsService = new google.maps.DirectionsService;
    
    $scope.map = new google.maps.Map(document.getElementById('map'), 
    {
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
    });
    
    $scope.mapV = new google.maps.Map(document.getElementById('mapV'), 
    {
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
    });
    
    
    //Create a renderer for directions and bind it to the map.    
    var directionsDisplay = new google.maps.DirectionsRenderer({map: $scope.map});
    var directionsDisplayV = new google.maps.DirectionsRenderer({map: $scope.mapV});
    //directionsDisplay.setMap($scope.map);
    
    //Instantiate an info window to hold step text.
    var stepDisplay = new google.maps.InfoWindow;
    
    $scope.calculateAndDisplayRoute = function(directionsDisplay, directionsService, markerArray, stepDisplay, map, vuelta)
    {
    	//elimina los markers anteriores
    	for (var i = 0; i < markerArray.length; i++) 
    	{
    	    markerArray[i].setMap(null);
    	}
    	
       	var waypts = [];
    	for (var i = 1; i < markerArray.length-1; i++) 
    	{
    	    waypts.push({location: markerArray[i].position,stopover: true});    	    
    	}
    	
    	
    	var ultimo = markerArray.length-1;
    	
    	directionsService.route({
    	    origin: markerArray[0].position,
    	    destination: markerArray[ultimo].position,
    	    waypoints: waypts,
    	    travelMode: google.maps.TravelMode.DRIVING
    	  }, function(response, status) {
    	    // Route the directions and pass the response to a function to create
    	    // markers for each step.
    	    if (status === google.maps.DirectionsStatus.OK) 
    	    {
    	    	//document.getElementById('warnings-panel').innerHTML = '<b>' + response.routes[0].warnings + '</b>';
    	    	directionsDisplay.setDirections(response);
    	    	$scope.actualizoKm(response.routes[0].legs, vuelta);
    	    	$scope.actualizoMinutos(response.routes[0].legs, vuelta);
    	    	$scope.$digest();
    	      	//$scope.showSteps(response, markerArray, stepDisplay, map);
    	    } 
    	    else 
    	    {
    	      window.alert('Falló el request de direcciones: ' + status);
    	    }
    	  });
    };
    
    $scope.geocodePosition = function (marker) 
    {
    	geocoder = new google.maps.Geocoder();
    	geocoder.geocode({
	    latLng: marker.getPosition()
		  }, function(responses) {
		    if (responses && responses.length > 0) 
		    {		      
		      var indice = responses[0].formatted_address.indexOf(",");
		      marker.descripcion = responses[0].formatted_address.substring(0, indice);
		      $scope.$digest();      
		    } 
		    else 
		    {
		      marker.descripcion = 'No se pudo obtener dirección.' 
		    }
		  });    	
    }
    
    $scope.showSteps = function(directionResult, markerArray, stepDisplay, map) 
    {
    	  // For each step, place a marker, and add the text to the marker's infowindow.
    	  // Also attach the marker to an array so we can keep track of it and remove it
    	  // when calculating new routes.
    	  var myRoute = directionResult.routes[0].legs[0];
    	  for (var i = 0; i < myRoute.steps.length; i++) {
    	    var marker = markerArray[i] = markerArray[i] || new google.maps.Marker;
    	    marker.setMap(map);
    	    marker.setPosition(myRoute.steps[i].start_location);
    	    $scope.attachInstructionText(
    	        stepDisplay, marker, myRoute.steps[i].instructions, map);
    	  }
    };
    
    $scope.attachInstructionText = function(stepDisplay, marker, text, map) 
    {
    	  google.maps.event.addListener(marker, 'click', function() {
    	    // Open an info window when the marker is clicked on, containing the text
    	    // of the step.
    	    stepDisplay.setContent(text);
    	    marker.descipcion = text;
    	    stepDisplay.open(map, marker);
    	  });
    };
    
    $scope.createRoute = function()
    {
    	$scope.calculateAndDisplayRoute(directionsDisplay, directionsService, $scope.markers, stepDisplay, $scope.map, false);
    	$scope.rutaTrazada = true;
    }
    
    $scope.createRouteV = function()
    {
    	$scope.calculateAndDisplayRoute(directionsDisplayV, directionsService, $scope.markersV, stepDisplay, $scope.mapV, true);
    	$scope.rutaTrazadaV = true;
    }
    
    // Create the search box and link it to the UI element.
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    $scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    
    searchBox.addListener('places_changed', function() 
    {
    	var places = searchBox.getPlaces();
    	
        if (places.length == 0) 
        {
        	return;
        }
        
        var bounds = new google.maps.LatLngBounds();
        //hacer for each porque puede devolver mas de un lugar       
        places.forEach(function(place) 
        {        	        
        	$scope.placeMarkerAndPanTo(place.geometry.location, $scope.map, false);        	
        });    
    });
    
    var inputV = document.getElementById('pac-inputV');
    var searchBoxV = new google.maps.places.SearchBox(inputV);
    $scope.mapV.controls[google.maps.ControlPosition.TOP_LEFT].push(inputV);
    
    searchBoxV.addListener('places_changed', function() 
    {
    	var places = searchBoxV.getPlaces();
    	
        if (places.length == 0) 
        {
        	return;
        }
        
        var bounds = new google.maps.LatLngBounds();
        //hacer for each porque puede devolver mas de un lugar       
        places.forEach(function(place) 
        {        	        
        	$scope.placeMarkerAndPanTo(place.geometry.location, $scope.mapV, true);        	
        });    
    });
    
    //Bias the SearchBox results towards current map's viewport.
    $scope.map.addListener('bounds_changed', function() 
    {
      searchBox.setBounds($scope.map.getBounds());
    });
    
    $scope.mapV.addListener('bounds_changed', function() 
    {
      searchBoxV.setBounds($scope.mapV.getBounds());
    });
    
    $scope.map.addListener('click', function(e) 
    {
    	//Clear out the old markers.
       /* $scope.markers.forEach(function(marker) 
        {
          marker.setMap(null);
        });
        $scope.markers = [];*/        
    	$scope.placeMarkerAndPanTo(e.latLng, $scope.map, false);
    	$scope.rutaTrazada = false;    	
    	$scope.$digest();
	});
    
    $scope.mapV.addListener('click', function(e) 
    {
    	$scope.placeMarkerAndPanTo(e.latLng, $scope.mapV, true);   
    	$scope.rutaTrazadaV = false;
    	$scope.$digest();
	});
    
    $scope.placeMarkerAndPanTo = function (latLng, map, vuelta) 
    {
  	  var marker = new google.maps.Marker({
  	    position: latLng,
  	    map: map, //map: $scope.map,
  	    animation: google.maps.Animation.DROP, //just for fun
  	    es_terminal: false,
  	    es_peaje: false,
  	    es_origen: false,  	    
  	    descripcion: '',
  	    reajusta: false,
  	    reajuste: 0,
  	    km: 0,
  	    id_parada:null
  	  });
  	  
  	  if (vuelta)
  	  {
  		  if($scope.markersV.length ==0)
    	  {
  			$scope.markersV.push(marker);
  			      		  
    	  }
    	  else
    	  {
    		  $scope.markersV.splice($scope.markersV.length-1, 0, marker);  
    	  }
  	  }
  	  else
  	  {
  		  if($scope.markers.length ==0)
    	  {
  			  $scope.markers.push(marker);  
    	  }
    	  else
    	  {
    		  $scope.markers.splice($scope.markers.length-1, 0, marker);  
    	  }
  	  }	 
  	    	    	  
  	  var l = latLng.lat();
  	  var g = latLng.lng();
  	  $scope.geocodePosition(marker);
  	  map.panTo(latLng);
  	  
   	}//fin de placemarker
    
    $scope.deleteMarker = function(indice, vuelta)
    {    	
    	if (vuelta)
    	{	
    		if ($scope.markersV[indice].es_terminal)
        	{
        		return;
        	}
        	$scope.markersV[indice].setMap(null);
        	$scope.markersV.splice(indice, 1);    	
        	$scope.createRouteV();
    	}
    	else
    	{
    		
    		if ($scope.markers[indice].es_terminal)
        	{
        		return;
        	}
        	$scope.markers[indice].setMap(null);
        	$scope.markers.splice(indice, 1);    	
        	$scope.createRoute();
    	}    	
    }
    
    $scope.changeIndex = function(old_index, new_index, vuelta)
    {
    	
    	if (vuelta)
    	{
    		var tope = $scope.markersV.length;
			
			if ((new_index == 0) || (new_index == tope-1) || (old_index == 0) || (old_index == tope-1))
			{
				return;
			}
			
			if ((new_index < 0) || (new_index >= tope))
			{
				return;
			}
			var auxMarker = $scope.markersV[old_index];
			$scope.markersV[old_index] = $scope.markersV[new_index];
			$scope.markersV[new_index] = auxMarker;
			$scope.createRouteV();	
    	}
    	else
    	{
			var tope = $scope.markers.length;
			
			if ((new_index == 0) || (new_index == tope-1) || (old_index == 0) || (old_index == tope-1))
			{
				return;
			}
			
			if ((new_index < 0) || (new_index >= tope))
			{
				return;
			}
			var auxMarker = $scope.markers[old_index];
			$scope.markers[old_index] = $scope.markers[new_index];
			$scope.markers[new_index] = auxMarker;
			$scope.createRoute();
    	}
    }
    
    $scope.actualizoKm = function (legs, vuelta)
    {
    	var km = 0; 
    	for (var i = 0; i < legs.length; i++) 
    	{
    		km = km + legs[i].distance.value;
    		if (vuelta)
        	{    			
    			$scope.markersV[i+1].km = legs[i].distance.value;
        	}
    		else
    		{
    			$scope.markers[i+1].km = legs[i].distance.value;	
    		}
    	}
    	km = km/1000
    	if (vuelta)
    	{
    		$scope.txt_kmV = Math.round(km * 100) / 100;    		
    	}
    	else
    	{
    		$scope.txt_km = Math.round(km * 100) / 100;
    		$scope.valor_sugerido = Math.round($scope.priceByTravelKm.valor * km);
    		$scope.showFijoSugerido();
    	}
    }
    
    $scope.actualizoMinutos = function (legs, vuelta)
    {
    	var min = 0; 
    	for (var i = 0; i < legs.length; i++) 
    	{
    		min = min + legs[i].duration.value + ($scope.stopDelay.valor * 60); //duracion de las paradas mas parametro de demora (esta en minutos la config)    		
    	}
    	min = min/60;
    	if (vuelta)
    	{
    		$scope.txt_minutosV = Math.round(min);
    		$scope.lineForm.tiempo_estimado_vuelta = $scope.txt_minutosV;
    	}
    	else
    	{
    		$scope.txt_minutos = Math.round(min);
    		$scope.lineForm.tiempo_estimado = $scope.txt_minutos;
    	}    	
    }
       
});