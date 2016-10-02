goOnApp.controller('terminalController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Agregue las terminales donde arribarán los vehículos';
    $scope.error_message = '';
    
    $scope.custom_response = null;    
    
    i18nService.setCurrentLang('es');
    
    $scope.terminalForm = {};
    
    $scope.getTerminals = function()
    {
    	$http.get(servicesUrl + 'getTerminals')
    		.success(function(data, status, headers, config) 
	    	{
	        	$scope.terminals = data;
	        	$scope.terminalsGrid.data = $scope.terminals;
    	});
    };
    
    $scope.getTerminals();
    
    $scope.showForm = function()
    {
    	$scope.terminalForm = {};
    	$("#divTerminalForm").removeClass('hidden');    	
    	google.maps.event.trigger(map, 'resize');//refresh map
    };
    
    $scope.hideForm = function()
    {
    	$("#divTerminalForm").addClass('hidden');		
    };
    
        
    $scope.createTerminal = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();		
			if (($scope.terminalForm.latitud == null) || ($scope.terminalForm.longitud == null))
			{
				$.unblockUI();
				$scope.error_message = 'Debe serleccionar un punto en el mapa.'; 
				$("#errorModal").modal("toggle");
				return;
			}
			$http.post(servicesUrl +'createTerminal', JSON.stringify($scope.terminalForm))
				.then(function(response){				
					$scope.hideForm();
			    	$scope.terminalForm = {};
			    	$scope.getTerminals();
			    	$.unblockUI();
					$scope.showSuccessAlert("Terminal creada.");							
			});    			
		}
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
    
    $scope.terminalsGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
		enableColumnMenus: false,
        columnDefs:
    	[
          { name:'Nombre', field: 'descripcion', cellTooltip: true },
          { name:'Dirección', field: 'direccion', cellTooltip: true },
          { name:'Es sucursal', cellTemplate: '<div class="text-center ngCellText">{{row.entity.sucursal != null | SiNo}}</div>' }
        ]
     };
    
    //MAPA\\
    
    $scope.map = new google.maps.Map(document.getElementById('map'), 
    {
        center: {lat: -34.894418, lng: -56.165775},
        zoom: 13
    });
    			 
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
          $scope.markers.push(new google.maps.Marker({
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
        $scope.terminalForm.direccion = places[0].formatted_address;
        $scope.actualizoMarker(places[0].geometry.location.lat(), places[0].geometry.location.lng());
        $scope.$digest();
    });
	     
    
    $scope.map.addListener('click', function(e) 
    {
    	//Clear out the old markers.
        $scope.markers.forEach(function(marker) 
        {
          marker.setMap(null);
        });
        $scope.markers = [];
        
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
  	  $scope.actualizoMarker(l, g);
  	  $scope.map.panTo(latLng);
  	  //
  	  var elevationService = new google.maps.ElevationService();
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
  	        	$scope.terminalForm.latitud = null;
  	        	$scope.terminalForm.longitud = null;
  	        	$scope.$digest();
				$("#errorModal").modal("toggle");
  	        }  	    	  
  	      }
  	    }
  	  });
   	}
    
    $scope.actualizoMarker = function (lat, lng)
    {
    	$scope.terminalForm.latitud = lat;    	                  
    	$scope.terminalForm.longitud = lng;
    };    
    
  
});
