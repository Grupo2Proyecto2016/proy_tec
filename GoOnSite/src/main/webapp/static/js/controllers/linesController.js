goOnApp.controller('linesController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Genera nuevas lineas de manera cómoda.';
    $scope.error_message = '';
    $scope.custom_response = null;    
    
    i18nService.setCurrentLang('es');
    
    $scope.lineForm = {};
    
    $scope.showForm = function()
    {
    	$scope.lineForm = {};
    	$("#divLineForm").removeClass('hidden');    	
    	google.maps.event.trigger($scope.map, 'resize');	//refresh map
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
    	      showSteps(response, markerArray, stepDisplay, map);
    	    } 
    	    else 
    	    {
    	      window.alert('Directions request failed due to ' + status);
    	    }
    	  });
    };
    
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
	});
    
    $scope.placeMarkerAndPanTo = function (latLng, map) 
    {
  	  var marker = new google.maps.Marker({
  	    position: latLng,
  	    map: $scope.map
  	  });
  	  $scope.markers.push(marker);
  	  var l = latLng.lat();
  	  var g = latLng.lng();
  	 // $scope.actualizoMarker(l, g);
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
});