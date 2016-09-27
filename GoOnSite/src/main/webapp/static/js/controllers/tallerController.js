goOnApp.controller('tallerController', function($scope, $http, uiGridConstants, i18nService)									 
{
    $scope.message = 'Maneje sus talleres con facilidad.';
    $scope.error_message = '';
    $scope.custom_response = null;
    $scope.tallerToDelete = null;
    
    i18nService.setCurrentLang('es');
    
    $scope.tallerForm = {};
    
    $scope.initForm = function()
    {
	    $scope.tallerForm.direccion = null;
	    $scope.tallerForm.telefono = null;
	    $scope.tallerForm.ciudad = 0;
	    $scope.tallerForm.departamento = 0;
	    $scope.tallerForm.nombre = null;
    }
    
    $scope.initForm();
    
    $scope.getTalleres = function(){
    	$http.get(servicesUrl + 'getTalleres').success(function(data, status, headers, config) 
    	{
        	$scope.talleres = data;
        	$scope.talleresGrid.data = $scope.talleres;
    	});
    };
    
    $scope.getTalleres();
    
    $scope.showForm = function()
    {
    	$scope.tallerForm = {};
    	$("#divTallerForm").removeClass('hidden');
    	$scope.hideSuccess();
    	google.maps.event.trigger(map, 'resize');//refresh map
    };
    
    $scope.hideForm = function()
    {
    	$("#divTallerForm").addClass('hidden');		
    };

    $scope.hideSuccess = function()
    {
    	//$("#successAlert").addClass('hidden');
    };
    
    $scope.createTaller = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();
			if (($scope.tallerForm.latitud == null) || ($scope.tallerForm.longitud == null))
			{
				$.unblockUI();
				$scope.error_message = 'Debe serleccionar un punto en el mapa.'; 
				$("#errorModal").modal("toggle");
				return;
			}
			$http.post(servicesUrl +'createTaller', JSON.stringify($scope.tallerForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
				$scope.initForm();
				$scope.showSuccessAlert("Taller creado.");			
				$scope.getTalleres();				
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear el taller. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			})
			;    			
		}
	};
	
	$scope.deleteTaller = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteTaller', JSON.stringify($scope.tallerToDelete))
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
	        			$scope.getTalleres();	
		        		$scope.showSuccessAlert("El taller ha sido borrado.");	
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
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
	
	$scope.talleresGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Nombre', field: 'nombre', cellTooltip: true },
          { name:'Dirección', field: 'direccion', cellTooltip: true },
          { name:'Teléfono', field: 'telefono', cellTooltip: true},
          
//          { name: 'Acciones',
//        	enableFiltering: false,
//        	enableSorting: false,
//            cellTemplate:'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getTallerDetails(row)">Detalles</button>'+
//            			 '<button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>' 
//            			  
//    	  }
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
        $scope.tallerForm.direccion = places[0].formatted_address;
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
  	        	$scope.tallerForm.latitud = null;
  	        	$scope.tallerForm.longitud = null;
  	        	$scope.$digest();
				$("#errorModal").modal("toggle");
  	        }  	    	  
  	      }
  	    }
  	  });
   	}
    
    $scope.actualizoMarker = function (lat, lng)
    {
    	$scope.tallerForm.latitud = lat;    	                  
    	$scope.tallerForm.longitud = lng;
    };
	
	$scope.showDeleteDialog = function(row)
    {
    	$scope.tallerToDelete = row.entity.id_taller;
    	$("#deleteModal").modal('show');
    };
    
    $scope.getTallerDetails = function(row)
    {
    	$scope.elTaller = row.entity; 
		$("#tallerDetailsModal").modal('toggle');
    };
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
});