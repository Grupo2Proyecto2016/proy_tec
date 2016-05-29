goOnApp.controller('branchController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Maneja las sucursales de manera ágil.';
    $scope.error_message = '';
    $scope.custom_response = null;    
    $scope.branchToDelete = null;
    
    i18nService.setCurrentLang('es');
    
    $scope.branchForm = {};
    
    $scope.getBranches = function()
    {
    	$http.get(servicesUrl + 'getBranches').success(function(data, status, headers, config) 
    	{
        	$scope.branches = data;
        	$scope.branchesGrid.data = $scope.branches;
    	});
    };
    
    $scope.getBranches();
    
    $scope.showForm = function()
    {
    	$scope.branchForm = {};
    	$("#divBranchForm").removeClass('hidden');    	
    	google.maps.event.trigger(map, 'resize');//refresh map
    };
    
    $scope.hideForm = function()
    {
    	$("#divBranchForm").addClass('hidden');		
    };
    
        
    $scope.createBranch = function()
	{		
		if(!$scope.form.$invalid)
		{
			$.blockUI();		
			if (($scope.branchForm.latitud == null) || ($scope.branchForm.longitud == null))
			{
				$.unblockUI();
				$scope.error_message = 'Debe serleccionar un punto en el mapa.'; 
				$("#errorModal").modal("toggle");
				return;
			}
			$http.post(servicesUrl +'createBranch', JSON.stringify($scope.branchForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
		    	$scope.branchForm = {};
				$scope.showSuccessAlert("Sucursal creada.");			
				//$scope.getBranches();				
			})
			.error(function()
			{
				$.unblockUI();
				$scope.error_message = 'Ha ocurrido un error al crear la sucursal. Intente de nuevo en unos instantes.'; 
				$("#errorModal").modal("toggle");
			})
			;    			
		}
	};
	
	$scope.deleteBranch = function()
	{
		$.blockUI();
		$http.post(servicesUrl +'deleteBranch', JSON.stringify($scope.branchToDelete))
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
	        			$scope.getBranches();	
		        		$scope.showSuccessAlert("La sucursal ha sido borrada.");	
	        		}	        		
	        	}
	        	$scope.hideDeleteDialog();
    		});				
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
    
    $scope.hideDeleteDialog = function(row)
    {
    	$("#deleteModal").modal('hide');
    };
    
    $scope.branchesGrid = 
    {
		paginationPageSizes: [15, 30, 45],
	    paginationPageSize: 15,
		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
		enableFiltering: true,
        columnDefs:
    	[
          { name:'Nombre', field: 'nombre' },
          { name:'Dirección', field: 'direccion' },
          { name:'Teléfono', field: 'telefono'},
          { name:'Correo', field: 'mail' },          
          { name: 'Acciones',
        	enableFiltering: false,
        	enableSorting: false,
            cellTemplate:'<button style="width: 50%" class="btn-xs btn-danger" ng-click="grid.appScope.showDeleteDialog(row)">Eliminar</button>'
            	/*'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getBusDetails(row)"></button>'+
            			 */ 
            			  
    	  }
        ]
     };
    
    $scope.showDeleteDialog = function(row)
    {
    	$scope.branchToDelete = row.entity.id_sucursal;
    	$("#deleteModal").modal('show');
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
        $scope.branchForm.direccion = places[0].formatted_address;
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
  	        	$scope.branchForm.latitud = null;
  	        	$scope.branchForm.longitud = null;
  	        	$scope.$digest();
				$("#errorModal").modal("toggle");
  	        }  	    	  
  	      }
  	    }
  	  });
   	}
    
    $scope.actualizoMarker = function (lat, lng)
    {
    	$scope.branchForm.latitud = lat;    	                  
    	$scope.branchForm.longitud = lng;
    };    
    
  
});
