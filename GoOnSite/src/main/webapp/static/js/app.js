// create the module and name it scotchApp
    var goOnApp = angular.module('goOnApp', ['ngRoute', 'ngAnimate', 'ngMessages', 'ui.grid', 'ui.grid.pagination']);

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
    	
    	// route for the user dashboard page
    	.when('/userPanel', {
    		templateUrl : tenantUrlPart + 'pages/userPanel.html',
    		controller  : 'userPanelController'
    	})
    	
    	// route for the employees page
    	.when('/employees', {
    		templateUrl : tenantUrlPart + 'pages/employees.html',
    		controller  : 'employeesController'
    	})
		.otherwise({
			redirectTo: '/'
    	});
    });

    goOnApp.service('authInterceptor', function($q) {
        var service = this;

        service.responseError = function(response) {
            if (response.status == 401)
            {
            	if($('#loginModal').hasClass('in'))
            	{
            		//do nothing
            	}
            	else
            	{
            		$("#loginModal").modal("toggle");
            	}
                //window.location = document.location.origin + document.location.pathname;
            }
            else if(response.status == 500)
            {
            	$("#errorModal").modal("toggle");
            }
            return $q.resolve(response);
        };
    })
    
    goOnApp.service('tokenInterceptor', function($rootScope) { 
    	var service = this;
    	service.request = function(config) {
    		//config.url = getTenant() + '/' + config.url;
        	access_token = getJwtToken(); 
    		if (access_token) { 
    		     config.headers.authorization = access_token; 
    		} 
            return config;
        }
    }); 

    
    goOnApp.config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.interceptors.push('tokenInterceptor');
    }])
    
    
    // create the controller and inject Angular's $scope
    goOnApp.controller('mainController', function($scope, $http, $location)
	{
    	$scope.user = null;
    	$scope.loginForm = null;
    	
    	$http.get(servicesUrl + 'getCompany')
        	.then(function(response) {
        		$scope.company = response.data;
        	}
    	);
    	
    	$http.get(servicesUrl + 'getUserInfo')
	    	.then(function(response) 
			{
	    		if(response.status == 200)
	    		{
	    			$scope.user = response.data;
	    		}
    			else
    			{
    				$scope.user = null;
    			}
	    	}
		);
        
        $scope.signIn = function()
        {
        	$http.post(servicesUrl + 'auth', JSON.stringify($scope.loginForm))
	        	.then(function(response) 
    			{
	        		if(response.status == 200)
	        		{
	        			$scope.user = response.data.user;
	        			setJwtToken(response.data.token, $scope.company.nombreTenant);
	        			$scope.loginForm = null;
	        			$("#loginModal").modal("toggle");
	        		}
	        		else
	        		{
	        			$scope.loginForm = null;
	        			$("#loginAlert").show();
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

    $.blockUI.defaults.css.border = 'none'; 
    $.blockUI.defaults.css.padding = '15px';
    $.blockUI.defaults.css.backgroundColor = '#000'; 
    $.blockUI.defaults.css.opacity = 0.5; 
    $.blockUI.defaults.css.color = '#fff'; 
    $.blockUI.defaults.message = 'Espere por favor...'; 