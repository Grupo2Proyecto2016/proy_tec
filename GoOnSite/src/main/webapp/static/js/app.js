// create the module and name it scotchApp
    var goOnApp = angular.module('goOnApp', ['ngRoute', 'ngAnimate', 'ngMessages', 'ui.grid', 'ui.grid.pagination']);
    
    goOnApp.run(function($rootScope) 
	{
        $rootScope.user = null;
    });
    
    goOnApp.filter('SiNo', function() {
        return function(input) {
            return input ? 'Si' : 'No';
        }
    });
    
    var tenantUrlPart =  urlTenant  + "/";
    var servicesUrl = AppName + tenantUrlPart;

    // configure our routes
    goOnApp.config(function($routeProvider) {
    	$routeProvider
    	
    	// route for the home page
    	.when('/', {
    		templateUrl : tenantUrlPart + 'pages/home.html',
    	})
    	
    	.when('/home', {
    		templateUrl : tenantUrlPart + 'pages/home.html',
    	})
    	
    	.when('/register', {
    		templateUrl : tenantUrlPart + 'pages/register.html',
    		controller  : 'registerController'
    	})
    	
    	// route for the about page
    	.when('/travels', {
    		templateUrl :  tenantUrlPart + 'pages/travels.html',
    		controller  : 'travelController'
    	})
    	
    	// route for the contact page
    	.when('/contact', {
    		templateUrl : tenantUrlPart + 'pages/contact.html',
    		controller  : 'contactController'
    	})
    	
    	// route for the buses page
    	.when('/bus', {
    		templateUrl : tenantUrlPart + 'pages/buses.html',
    		controller  : 'busController'
    	})
    	
    	// route for the talleres page
    	.when('/taller', {
    		templateUrl : tenantUrlPart + 'pages/talleres.html',
    		controller  : 'tallerController'
    	})
    	
    	// route for the mantenimientos page
    	.when('/mantenimiento', {
    		templateUrl : tenantUrlPart + 'pages/mantenimientos.html',
    		controller  : 'mantenimientoController'
    	})
    	
    	// route for the user dashboard page
    	.when('/userPanel', {
    		templateUrl : tenantUrlPart + 'pages/userPanel.html',
    		controller  : 'userPanelController'
    	})
    	
    	// route for the branch office page
    	.when('/branches', {
    		templateUrl : tenantUrlPart + 'pages/branches.html',
    		controller  : 'branchController'
    	})
    	
    	// route for the employees page
    	.when('/employees', {
    		templateUrl : tenantUrlPart + 'pages/employees.html',
    		controller  : 'employeesController'
    	})
    	
    	// route for the employees page
    	.when('/company', {
    		templateUrl : tenantUrlPart + 'pages/company.html',
    		controller  : 'companyController'
    	})
    	
    	// route rot the outBranches page
    	.when('/outbranches', {
    		templateUrl : tenantUrlPart + 'pages/outbranches.html',
    		controller  : 'outBranchesController'
    	})
    	
    	// route rot the parameters page
    	.when('/parameters', {
    		templateUrl : tenantUrlPart + 'pages/parameters.html',
    		controller  : 'parametersController'
    	})
    	
    	.when('/lines', {
    		templateUrl : tenantUrlPart + 'pages/lines.html',
    		controller  : 'linesController'
    	})
    	
    	.when('/terminals', {
    		templateUrl : tenantUrlPart + 'pages/terminals.html',
    		controller  : 'terminalController'
    	})
    	
    	.when('/manageTravels', {
    		templateUrl : tenantUrlPart + 'pages/manageTravels.html',
    		controller  : 'manageTravelsController'
    	})
    	
		.otherwise({
			redirectTo: '/'
    	});
    });

    goOnApp.service('authInterceptor', function($q, $location, $rootScope) {
        var service = this;
        
        service.responseError = function(response) {
            if (response.status == 401)
            {
            	$rootScope.user = null;
            	$location.path('home');
            	if($('#loginModal').hasClass('in'))
            	{
            		//do nothing
            	}
            	else
            	{
    				$("#loginModal").modal("show");
            	}
            }
            else if(response.status == 500)
            {
            	if(!$('#errorModal').hasClass('in'))
            	{
            		$("#errorModal").modal("show");
            	}
            }
            else if(response.status == -1)
            {
            	if(!$('#connectErrorModal').hasClass('in'))
            	{
            		$("#connectErrorModal").modal("show");
            	}
            }
            else if(response.status == 403)
            {
            	$location.path('home');
            	$("div.modal-backdrop").remove();
            }
            return $q.resolve(response);
        };
    })
    
    goOnApp.service('tokenInterceptor', function($rootScope) { 
    	var service = this;
    	service.request = function(config) {
        	access_token = getJwtToken(); 
    		if (access_token) { 
    		     config.headers.authorization = access_token; 
    		} 
            return config;
        };
        service.response = function(response) {
    		var headers = response.headers();
    		var authToken = headers['authorization'];
    		if(authToken != undefined)
    		{
    			setJwtToken(authToken);
    		}
            return response;
        }
    }); 
                                 
    goOnApp.config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.interceptors.push('tokenInterceptor');
    }])
    
     goOnApp.config(['$compileProvider',function($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|skype):/);
    }])
    
    
    // create the controller and inject Angular's $scope
    goOnApp.controller('mainController', function($scope, $http, $location, $rootScope)
	{
    	$scope.userInfoReady = false;
    	$scope.companyInfoReady = false;
    	$scope.user = null;
    	$scope.company = null;
    	$scope.loginForm = null;
    	$scope.terminals = null;
    	$scope.calcForm = null;
    	var gService = null;
    	
    	$(window).on("load", function(){
    		gService = new google.maps.DistanceMatrixService();
		});
    	
    	$rootScope.$watch('user', function(user) {
    		  $scope.user = user;
		});
    	
    	$scope.getTerminals = function()
        {
        	$http.get(servicesUrl + 'getTerminals').success(function(data, status, headers, config) 
        	{
            	$scope.terminals = data;        	
        	});
        };    
        
        $scope.calcPackage = function()
        {
        	var origin = new google.maps.LatLng($scope.calcForm.origen.latitud, $scope.calcForm.origen.longitud);
        	var destination = new google.maps.LatLng($scope.calcForm.destino.latitud, $scope.calcForm.destino.longitud);
        	var result = gService.getDistanceMatrix({
        	    origins: [origin],
        	    destinations: [destination],
        	    travelMode: google.maps.TravelMode.DRIVING,
        	    unitSystem: google.maps.UnitSystem.METRIC,
        	    avoidHighways: false,
        	    avoidTolls: false
        		},
        		function(response, status) 
        		{
        			$scope.calcForm.distance = response.rows[0].elements[0]['distance']['value'] / 1000;
        			var volume = $scope.calcForm.alto * $scope.calcForm.ancho * $scope.calcForm.largo / 1000000; 
                	$http.post(servicesUrl + 'calcPackage', JSON.stringify({ distance: $scope.calcForm.distance, weigth: $scope.calcForm.peso, volume: volume  }))
                		.then(function(result){
                			$scope.packagePrice = result.data;
            		});
        		}
        	);
        };

        $scope.getTerminals();
        
    	$scope.showPackageCalc = function()
    	{
    		$scope.packagePrice = null;
    		$("#packageCalcModal").modal("toggle");
    	};
    	
    	$scope.getCompany = function()
    	{
    		$http.get(servicesUrl + 'getCompany')
	        	.then(function(response) {
	        		$scope.company = response.data;
	        		$("#logo").attr("src", $scope.company.logo);
	        		
	        		$scope.companyInfoReady = true;
        	});
    	};
    	
    	$scope.getCompany();
    	
    	$http.get(servicesUrl + 'getUserInfo')
	    	.then(function(response) 
			{
	    		if(response.status == 200)
	    		{
	    			$rootScope.user = response.data;
	    		}
    			else
    			{
    				$rootScope.user = null;
    				removeJwtToken();
    			}
	    		$scope.userInfoReady = true;
	    	}
		);
        
        $scope.signIn = function()
        {
        	$http.post(servicesUrl + 'auth', JSON.stringify($scope.loginForm))
	        	.then(function(response) 
    			{
	        		if(response.status == 200)
	        		{
	        			$rootScope.user = response.data.user;
	        			setJwtToken(response.data.token);
	        			$scope.loginForm = null;
	        			$("#loginModal").modal("toggle");
	        		}
	        		else
	        		{
	        			$scope.loginForm = null;
	        			$("#loginAlert").show();
	        			$('#username').focus();
	        		}
	        	}
	    	);
        };
        
        $scope.signOut = function()
        {
        	removeJwtToken();
        	$scope.user = null;
        	$location.path('home');
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

    goOnApp.directive('userexists', function($http, $q) {
  	  return {
  	    require: 'ngModel',
  	    link: function(scope, elm, attrs, ctrl)
  	    {
  	    	ctrl.$asyncValidators.userexists = function(modelValue, viewValue) 
  	    	{
  	    		if (ctrl.$isEmpty(modelValue)) 
  	    		{
  	  	          // consider empty model valid
  	  	          return $q.when();
  	    		}
  	    		
  	    		var def = $q.defer();
  	    		
  		    	$http.get(servicesUrl + 'userExists?username='+ modelValue)
	  		    	.then(function(response) 
	    			{
	  		    		if(response.status == 200)
		        		{
	  		    			return def.reject();
		        		}
	  		    		else if (response.status == 404)
	  		    		{
	  		    			return def.resolve();
	  		    		}
    			});
  		    	
  		    	return def.promise;
  	    	};
  	    }
  	  };
  	});
    
    goOnApp.directive("fileread", [function () {
        return {
            scope: {
                fileread: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    var reader = new FileReader();
                    reader.onload = function (loadEvent) {
                        scope.$apply(function () {
                            scope.fileread = loadEvent.target.result;
                        });
                    }
                    reader.readAsDataURL(changeEvent.target.files[0]);
                });
            }
        }
    }]);

    $.blockUI.defaults.css.border = 'none'; 
    $.blockUI.defaults.css.padding = '15px';
    $.blockUI.defaults.css.backgroundColor = '#000'; 
    $.blockUI.defaults.css.opacity = 0.5; 
    $.blockUI.defaults.css.color = '#fff'; 
    $.blockUI.defaults.message = 'Espere por favor...'; 