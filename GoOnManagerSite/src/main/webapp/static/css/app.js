// create the module and name it scotchApp
    var goOnApp = angular.module('goOnApp', ['ngRoute', 'ngAnimate']);

    var tenantUrlPart =  urlTenant  + "/";
    var servicesUrl = AppName + tenantUrlPart;
    
    // configure our routes
    goOnApp.config(function($routeProvider) {
    	$routeProvider
    	
    	// route for the home page
    	.when('/', {
    		templateUrl : 'pages/home.html',
    		controller  : 'mainController'
    	})
    	
    	.when('/home', {
    		templateUrl : 'pages/home.html',
    		controller  : 'mainController'
    	})
    	
    	// route for the about page
    	.when('/travels', {
    		templateUrl : 'pages/travels.html',
    		controller  : 'travelController'
    	})
    	
    	// route for the contact page
    	.when('/contact', {
    		templateUrl : 'pages/contact.html',
    		controller  : 'contactController'
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
	    		if(response.data != null && response.data != "")
	    		{
	    			$scope.user = response.data;
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
        	$location.path('/');
        };
    });
    
    goOnApp.controller('travelController', function($scope) {
        $scope.message = 'Desde aquí podrás buscar el viaje que deseas y efectuar la compra o reserve de pasajes.';
    });

    goOnApp.controller('contactController', function($scope) {
        $scope.message = '¿Tienes alguna duda? Comunícate con nosotros para despejarla.';
    });


    $.blockUI.defaults.css.border = 'none'; 
    $.blockUI.defaults.css.padding = '15px';
    $.blockUI.defaults.css.backgroundColor = '#000'; 
    $.blockUI.defaults.css.opacity = 0.5; 
    $.blockUI.defaults.css.color = '#fff'; 
    $.blockUI.defaults.message = 'Espere por favor...'; 