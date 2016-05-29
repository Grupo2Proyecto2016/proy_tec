goOnApp.controller('outBranchesController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Encontrá la sucursal más cercana.';
    $scope.error_message = '';
    $scope.custom_response = null;   
    
    i18nService.setCurrentLang('es');
   
    //MAPA\\    
    $scope.map = new google.maps.Map(document.getElementById('map'), 
    {
        center: {lat: -34.894418, lng: -56.165775},
        zoom: 13
    });
    
    //Create the search box and link it to the UI element.
    var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    $scope.map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    
    //Bias the SearchBox results towards current map's viewport.
    $scope.map.addListener('bounds_changed', function() 
    {
      searchBox.setBounds($scope.map.getBounds());
    });
    
    $scope.markers = [];
    $scope.branchesMarkers = [];
    
    
    searchBox.addListener('places_changed', function() 
    {
    	var places = searchBox.getPlaces();

        if (places.length == 0) 
        {
          return;
        }
        
        //Clear out the old markers.
        $scope.markers.forEach(function(marker) 
        {
          marker.setMap(null);
        });
        $scope.markers = [];
        
        // For each place, get the icon, name and location.
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) 
        {
         
        	// Create a marker for each place.
		  $scope.markers.push(new google.maps.Marker(
		  {
		    map: $scope.map,
		    title: place.name,
		    position: place.geometry.location
		  }));

          if (place.geometry.viewport) {
            // Only geocodes have viewport.
            bounds.union(place.geometry.viewport);
          } else {
            bounds.extend(place.geometry.location);
          }
        });                 
        $scope.map.fitBounds(bounds);        
        $scope.$digest();
    });
    
    google.maps.event.trigger(map, 'resize');//refresh map
});