// create the module and name it scotchApp
    var goOnApp = angular.module('goOnApp', ['ngRoute']);

    var tenantUrlPart = getTenant() + "/";
    // configure our routes
    goOnApp.config(function($routeProvider) {
    	$routeProvider
    	
    	// route for the home page
    	.when('/', {
    		templateUrl : tenantUrlPart + 'pages/home.html',
    		controller  : 'mainController'
    	})
    	
    	// route for the about page
    	.when('/travels', {
    		templateUrl : tenantUrlPart+ 'pages/travels.html',
    		controller  : 'aboutController'
    	})
    	
    	// route for the contact page
    	.when('/contact', {
    		templateUrl : tenantUrlPart + 'pages/contact.html',
    		controller  : 'contactController'
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
    goOnApp.controller('mainController', function($scope, $http) {
        $http.get(AppName + getTenant() + '/getCompany')
        	.then(function(response) {
        		$scope.company = response.data;
        	}
    	);
        	
        
        $scope.message = 'Everyone come and see how good I look!';
        $scope.signOut = function()
        {
        	removeJwtToken();
        	window.location = document.location.origin + document.location.pathname;
        };
    });
    
    goOnApp.controller('aboutController', function($scope) {
        $scope.message = 'Look! I am an about page.';
    });

    goOnApp.controller('contactController', function($scope) {
        $scope.message = 'Contact us! JK. This is just a demo.';
    });


