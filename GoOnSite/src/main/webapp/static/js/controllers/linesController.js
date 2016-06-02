goOnApp.controller('linesController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Genera nuevas lineas de manera cómoda.';
    $scope.error_message = '';
    $scope.custom_response = null;    
    $scope.txt_minutos = "";
    $scope.txt_km = "";
    i18nService.setCurrentLang('es');
    
    $scope.lineForm = {};
    $scope.stops = {};
    $scope.terminals = null;
    
    $scope.showForm = function()
    {
    	$scope.lineForm = {};
    	$("#divLineForm").removeClass('hidden');    	
    	google.maps.event.trigger($scope.map, 'resize');	//refresh map
    };
    
    $scope.getTerminals = function()
    {
    	$http.get(servicesUrl + 'getTerminals').success(function(data, status, headers, config) 
    	{
        	$scope.terminals = data;        	
    	});
    };    
    
    $scope.getTerminals();
    
    $scope.createLine = function()
    {
    	if(!$scope.form.$invalid)
		{
    		//$scope.markers
    		$scope.lineForm.paradas = $scope.markers;
		}
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
	  	    es_terminal: true,
	  	    es_peaje: false,
	  	    es_origen: false,
	  	    descripcion: '',
	  	    reajuste: 0, 
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
    	
    	//$scope.$digest();
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
	  	    es_terminal: true,
	  	    es_peaje: false,
	  	    es_origen: true,
	  	    descripcion: '',
	  	    reajuste: 0,
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
    	
    	//$scope.$digest();
    };
    
    /*Mapa*/
    var directionsService = new google.maps.DirectionsService;
    
    $scope.map = new google.maps.Map(document.getElementById('map'), 
    {
      zoom: 12,
      center: {lat: -34.894418, lng: -56.165775}
    });
    
    
    //Create a renderer for directions and bind it to the map.    
    var directionsDisplay = new google.maps.DirectionsRenderer({map: $scope.map});
    //directionsDisplay.setMap($scope.map);
    
    //Instantiate an info window to hold step text.
    var stepDisplay = new google.maps.InfoWindow;
    
    $scope.calculateAndDisplayRoute = function(directionsDisplay, directionsService, markerArray, stepDisplay, map)
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
    	    	$scope.actualizoKm(response.routes[0].legs);
    	    	$scope.actualizoMinutos(response.routes[0].legs);
    	    	$scope.$digest();
    	      	//$scope.showSteps(response, markerArray, stepDisplay, map);
    	    } 
    	    else 
    	    {
    	      window.alert('Directions request failed due to ' + status);
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
    	$scope.calculateAndDisplayRoute(directionsDisplay, directionsService, $scope.markers, stepDisplay, $scope.map);
    }
    
    // Create the search box and link it to the UI element.
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    $scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    
    //Bias the SearchBox results towards current map's viewport.
    $scope.map.addListener('bounds_changed', function() 
    {
      searchBox.setBounds($scope.map.getBounds());
    });
    
    $scope.markers = [];
    
    $scope.map.addListener('click', function(e) 
    {
    	//Clear out the old markers.
       /* $scope.markers.forEach(function(marker) 
        {
          marker.setMap(null);
        });
        $scope.markers = [];*/        
    	$scope.placeMarkerAndPanTo(e.latLng, $scope.map);    	
    	$scope.$digest();
	});
    
    $scope.placeMarkerAndPanTo = function (latLng, map) 
    {
  	  var marker = new google.maps.Marker({
  	    position: latLng,
  	    map: $scope.map,
  	    es_terminal: false,
  	    es_peaje: false,
  	    es_origen: false,  	    
  	    descripcion: '',
  	    reajuste: 0,
  	    id_parada:0
  	  });
  	  
  	  if($scope.markers.length ==0)
  	  {
  		$scope.markers.push(marker);  
  	  }
  	  else
  	  {
  		$scope.markers.splice($scope.markers.length-1, 0, marker);  
  	  }  	  
  	  var l = latLng.lat();
  	  var g = latLng.lng();
  	 // $scope.actualizoMarker(l, g);
  	  $scope.geocodePosition(marker);
  	  $scope.map.panTo(latLng);
  	  //
  	/* var elevationService = new google.maps.ElevationService();
  	  var requestElevation = 
  	  {
  		'locations': [marker.getPosition()]
  	  };  	  
   	 elevationService.getElevationForLocations(requestElevation, function(results, status) 
   	 {
  	    if (status == google.maps.ElevationStatus.OK) {
  	      if (results[0]) 
  	      {
  	        if (parseFloat(results[0].elevation) < 1)
  	        {
  	        	//Es Agua
  	        	marker.setMap(null);
  	        	$scope.markers = [];
  	        	$scope.error_message = 'Punto del mapa incorrecto, esta en el agua!';
  	        	$scope.branchForm.latitud = null;
  	        	$scope.branchForm.longitud = null;
  	        	$scope.$digest();
				$("#errorModal").modal("toggle");
  	        }  	    	  
  	      }
  	    }
  	  });*/
   	}//fin de placemarker
    
    $scope.deleteMarker = function(indice)
    {
    	if ((indice == 0) || (indice == $scope.markers.length -1))
    	{
    		return;
    	}
    	$scope.markers[indice].map = null;
    	$scope.markers.splice(indice, 1);    	
    	$scope.createRoute();
    }
    
    $scope.changeIndex = function(old_index, new_index)
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
    
    $scope.actualizoKm = function (legs)
    {
    	var km = 0; 
    	for (var i = 0; i < legs.length; i++) 
    	{
    		km = km + legs[i].distance.value;
    	}
    	km = km/1000
    	$scope.txt_km = Math.round(km * 100) / 100;
    }
    
    $scope.actualizoMinutos = function (legs)
    {
    	var min = 0; 
    	for (var i = 0; i < legs.length; i++) 
    	{
    		min = min + legs[i].duration.value;    		
    	}
    	min = min/60;
    	$scope.txt_minutos = Math.round(min * 100) / 100;
    }
    
});