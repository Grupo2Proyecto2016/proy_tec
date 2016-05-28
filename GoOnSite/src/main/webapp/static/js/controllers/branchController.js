goOnApp.controller('branchController', function($scope, $http, uiGridConstants, i18nService)									 
{
	$scope.message = 'Maneje sus sucursales de manera ágil.';
    $scope.error_message = '';
    $scope.custom_response = null;    
    
    i18nService.setCurrentLang('es');
    
    $scope.branchForm = {};
    
//    $scope.initForm = function()
//    {
//	    $scope.branchForm.nombre = null;
//	    $scope.branchForm.direccion = null;
//	    $scope.branchForm.telefono = null;
//	    $scope.branchForm.mail = null;
//	    $scope.branchForm.latitud = 0;
//	    $scope.branchForm.longitud = 0;
//    };   
    
    
    $scope.showForm = function()
    {
    	$scope.branchForm = {};
    	//$scope.initForm();
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
			$http.post(servicesUrl +'createBranch', JSON.stringify($scope.branchForm))
			.success(function()
			{
				$.unblockUI();
				$scope.hideForm();
				$scope.initForm();
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
	
	$scope.closeSuccessAlert = function()
    {
    	$("#successAlert").hide();
    };
    
    $scope.showSuccessAlert = function(message)
    {
    	$('#successMessage').text(message);
		$("#successAlert").show();
    };
    
    //CONFIGURACIONES DEL MAPA\\\\
    var mapOptions = {
    		zoom:8,
    		center: new google.maps.LatLng(-34.893819, -56.166349),
    		mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    
    //$scope.map = new google.maps.map(document.getElementById('map'), mapOptions);
    
    $scope.map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: -34.893819, lng: -56.166349}, 
        zoom: 12
      });
    			 
    $scope.markers = [];
    
    $scope.map.addListener('click', function(e) 
    {
    	$scope.placeMarkerAndPanTo(e.latLng, $scope.map);
	});
    
    $scope.placeMarkerAndPanTo = function (latLng, map) 
    {
  	  var marker = new google.maps.Marker({
  	    position: latLng,
  	    map: map
  	  });
  	  var l = latLng.lat();
  	  var g = latLng.lng();
  	  $scope.actualizoMarker(l, g);
  	  $scope.map.panTo(latLng);
  	}
    
    $scope.actualizoMarker = function (lat, lng)
    {
    	$scope.branchForm.latitud = lat;
    	$scope.branchForm.longitud = lng;    	
    };
  
});
