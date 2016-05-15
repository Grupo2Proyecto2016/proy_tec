// create the module and name it scotchApp
    var goOnApp = angular.module('goOnApp', ['ngRoute']);

    // configure our routes
    goOnApp.config(function($routeProvider) {
    	$routeProvider
    	
    	// route for the home page
    	.when('/', {
    		templateUrl : 'pages/home.html',
    		controller  : 'mainController'
    	})
    	
    	// route for the about page
    	.when('/companies', {
    		templateUrl : 'pages/companies.html',
    		controller  : 'companiesController'
    	})
    	
    	// route for the contact page
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
    
    goOnApp.service('tokenInterceptor', function($rootScope) { 
    	var service = this;
    	service.request = function(config) {
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
    goOnApp.controller('mainController', function($scope) {
        $scope.signOut = function()
        {
        	removeJwtToken();
        	window.location = document.location.origin + document.location.pathname;
        };
    });
    
    goOnApp.controller('companiesController', function($scope) {
        $scope.message = 'Empresas que hacen uso de la plataforma';
    });

    goOnApp.controller('companyController', function($scope) {
        $scope.message = 'Complete los datos para dar de alta una nueva empresa en la plataforma.';
    });


