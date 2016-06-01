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
    var directionsDisplay = new google.maps.DirectionsRenderer;
    $scope.map = new google.maps.Map(document.getElementById('map'), 
    {
      zoom: 6,
      center: {lat: -34.894418, lng: -56.165775}
    });
    directionsDisplay.setMap($scope.map);

    // Create the search box and link it to the UI element.
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    $scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    
 // Bias the SearchBox results towards current map's viewport.
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