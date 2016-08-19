// create the module and name it scotchApp
    var goOnApp = angular.module('goOnApp', ['ngRoute', 'ngAnimate', 'ngMessages', 'ui.grid', 'ui.grid.pagination']);

    // configure our routes
    goOnApp.config(function($routeProvider) {
    	$routeProvider
    	
    	// route for the companies page
    	.when('/', {
    		templateUrl : 'pages/companies.html',
    		controller  : 'companiesController'
    	})
    	.when('/companies', {
    		templateUrl : 'pages/companies.html',
    		controller  : 'companiesController'
    	})
    	// route for the company form page
    	.when('/newCompany', {
    		templateUrl : 'pages/companyForm.html',
    		controller  : 'companyController'
    	});
    });

    goOnApp.service('authInterceptor', function($q) {
        var service = this;

        service.responseError = function(response) {
            if (response.status == 401){
                window.location = document.location.origin + document.location.pathname;
            }
            return $q.reject(response);
        };
    })
    
    //Add headers to every request
    goOnApp.service('tokenInterceptor', function($rootScope) { 
    	var service = this;
    	service.request = function(config) {
        	access_token = getJwtToken(); 
    		if (access_token) { 
    		     config.headers.authorization = access_token;
    		     config.headers.AppId = "MainAPP";
    		} 
            return config;
        }
    }); 

    //Add interceptors
    goOnApp.config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.interceptors.push('tokenInterceptor');
    }])
    
    
    // create the controller and inject Angular's $scope
    goOnApp.controller('mainController', function($scope) {
        // create a message to display in our view
        $scope.signOut = function()
        {
        	removeJwtToken();
        	window.location = document.location.origin + document.location.pathname;
        };
    });
    
    goOnApp.controller('companyController', function($scope, $http, $location) {
        $scope.message = 'Ingrese los siguientes datos para completar el registro de una nueva empresa';
        $scope.error_message = '';
        
        $scope.maxBirth = new Date();
        $scope.maxBirth.setDate($scope.maxBirth.getDate() - 6570);
        
        $scope.companyForm = {};
        $scope.companyForm.name = null;
        $scope.companyForm.trueName = null;
        $scope.companyForm.rut = null;
        $scope.companyForm.phone = null;
        $scope.companyForm.address = null;
        $scope.companyForm.tenantName = null;
        $scope.companyForm.username = null;
        $scope.companyForm.password = null;
        $scope.companyForm.countryId = null;
        $scope.companyForm.latitud = null;
        $scope.companyForm.longitud = null;
        $scope.companyForm.user = null;
        $scope.companyForm.css = null;
        $scope.companyForm.addTerminal = true;
        $scope.countries = null;
        
    	$http.get(AppName + 'countries').
	        success(function(data, status, headers, config) 
			{
	        	$scope.countries = data;
	        })
        ;
    	
    	$scope.createCompany = function()
    	{
    		
    		if(!$scope.form.$invalid)
    		{
    			$.blockUI();
    			if (($scope.companyForm.latitud == null) || ($scope.companyForm.longitud == null))
    			{
    				$.unblockUI();
    				$scope.error_message = 'Debe serleccionar un punto en el mapa.';    				
    				$("#errorModal").modal("toggle");
    				return;
    			}
    			$http.post(AppName +'createCompany', JSON.stringify($scope.companyForm))
    			.success(function()
				{
    				$.unblockUI();
    				$("#successModal").modal("toggle");
    			})
    			.error(function()
				{
    				$.unblockUI();
    				$scope.error_message = ' Ha ocurrido un error al crear la empresa. Intente de nuevo en unos instantes.';    				
    				$("#errorModal").modal("toggle");
    			})
    			;    			
    		}
    	};
    	
    	
    	
    	$scope.map = new google.maps.Map(document.getElementById('map'), 
	    {
	        center: {lat: -34.894418, lng: -56.165775},
	        zoom: 13
	    });
    	
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
	        $scope.companyForm.address = places[0].formatted_address;
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
      	        	$scope.companyForm.latitud = null;
      	        	$scope.companyForm.longitud = null;
      	        	$scope.$digest();
    				$("#errorModal").modal("toggle");
      	        }  	    	  
      	      }
      	    }
      	  });
       	}
        
        $scope.actualizoMarker = function (lat, lng)
        {
        	$scope.companyForm.latitud = lat;    	                  
        	$scope.companyForm.longitud = lng;
        };    
    	
    	$scope.changeView = function(view)
    	{
    		$location.path(view);
    		$(".modal-backdrop").hide();
    		$("body").removeClass("modal-open");
    		$("body").addClass("modal-closed");
        };
    });

    goOnApp.controller('companiesController', function($scope, $http, uiGridConstants) {
        $scope.message = 'A continuación se listan las empresas registradas en la plataforma';
        
        $http.get(AppName + 'getCompanies')
        	.success(function(data, status, headers, config) 
			{
	        	$scope.companies = data;
	        	$scope.companyGrid.data = $scope.companies;
		});
        
        $scope.getUserDetails = function(tenantName)
        {
        	$scope.tenantAdmin = null;
        	$http.get(AppName + 'getTenantAdmin?tenantId='+ tenantName)
        		.success(function(data, status, headers, config) 
				{
		        	$scope.tenantAdmin = data;
		        	$("#adminDetailsModal").modal('toggle');
		    });
        };
        
        $scope.companyGrid = 
        {
    		paginationPageSizes: [15, 30, 45],
    	    paginationPageSize: 15,
    		enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    		enableFiltering: true,
            columnDefs:
        	[
              { name:'Nombre', field: 'nombre' },
              { name:'Razón Social', field: 'razonSocial' },
              { name:'Url', field: 'nombreTenant'},
              { name:'Teléfono', field: 'telefono' },
              { name:'País de origen', field: 'pais.nombre' },
              { name:'Dirección', field: 'direccion' },
                           
              { name: 'Administrador',
            	enableFiltering: false,
            	enableSorting: false,
                cellTemplate:'<p align="center"><button style="" class="btn-xs btn-danger" ng-click="grid.appScope.getUserDetails(row.entity.nombreTenant)">Detalles</button></p>'
                	/*'<button style="width: 50%" class="btn-xs btn-primary" ng-click="grid.appScope.getBusDetails(row)">Detalles</button>'+*/
                			  
        	  }
            ]
         };
    });

    goOnApp.directive('tenantexists', function($http, $q) {
	  return {
	    require: 'ngModel',
	    link: function(scope, elm, attrs, ctrl)
	    {
	    	ctrl.$asyncValidators.tenantexists = function(modelValue, viewValue) 
	    	{
	    		if (ctrl.$isEmpty(modelValue)) 
	    		{
	  	          // consider empty model valid
	  	          return $q.when();
	    		}
	    		
	    		var def = $q.defer();
	    		
		    	$http.get(AppName + 'tenantExist?tenantId='+ modelValue)
		    	.success(function(data, status, headers, config) 
				{
		    		return def.reject();
		        })
		        .error(function(data, status, headers, config) 
				{
		        	return def.resolve();
		        });
		    	
		    	return def.promise;
	    	};
	    }
	  };
	});

    goOnApp.directive("compareTo", function() {
        return {
            require: "ngModel",
            scope: {
                otherModelValue: "=compareTo"
            },
            link: function(scope, element, attributes, ngModel) {
                 
                ngModel.$validators.compareTo = function(modelValue) {
                    return modelValue == scope.otherModelValue;
                };
     
                scope.$watch("otherModelValue", function() {
                    ngModel.$validate();
                });
            }
        };
    });
    
    $.blockUI.defaults.css.border = 'none'; 
    $.blockUI.defaults.css.padding = '15px';
    $.blockUI.defaults.css.backgroundColor = '#000'; 
    $.blockUI.defaults.css.opacity = 0.5; 
    $.blockUI.defaults.css.color = '#fff'; 
    $.blockUI.defaults.message = 'Espere por favor...'; 