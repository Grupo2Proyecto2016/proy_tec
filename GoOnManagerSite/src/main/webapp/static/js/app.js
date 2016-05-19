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
        $scope.companyForm.user = null;
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
    			$http.post(AppName +'createCompany', JSON.stringify($scope.companyForm))
    			.success(function()
				{
    				$("#successModal").modal("toggle");
    			})
    			.error(function()
				{
    				$("#errorModal").modal("toggle");
    			})
    			;    			
    		}
    	};
    	
    	$scope.changeView = function(view)
    	{
    		$location.path(view);
    		$(".modal-backdrop").hide();
    		$("body").removeClass("modal-open");
    		$("body").addClass("modal-closed");
        };
    });

    goOnApp.controller('companiesController', function($scope) {
        $scope.message = 'A continuación se listan las empresas registradas en la plataforma';
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

