// Encoding: ISO-8859-1

jQuery(function() {
    function reposition() {
        var modal =  jQuery(this),
            dialog = jQuery("#alert_dialog");
        modal.css('display', 'block');
        
        // Dividing by two centers the modal exactly, but dividing by three 
        // or four works better for larger screens.
        dialog.css("margin-top", Math.max(0, (jQuery(window).height() - dialog.height()) / 2));
    }
    // Reposition when a modal is shown
    jQuery('.modal').on('show.bs.modal', reposition);
    // Reposition when the window is resized
    jQuery(window).on('resize', function() {
    	jQuery('.modal:visible').each(reposition);
    });
});

var errorCallback = function(scope){
	return function(response){
		alert(response.status);
		if(response.status == 412){
			 for (var i = 0; i < response.data.errors.length; i++) {
				 var errorData = response.data.errors[i];
				 alert("#"+errorData.category.replace(/\s/g,'_')+"_Warn");
				 jQuery("#"+errorData.category.replace(/\s/g,'_')+"_Warn").text(errorData.message);
			 }
		} else {
			alert(response.status);
			alert('Erro'+response.data.errors[0].message);
			jQuery('#modalTitle').text(scope.title);
			jQuery("#alertMessage").text(response.data.errors[0].message);
			jQuery("#alert").modal('show');
		}
	}
}

var dynaFormApp = angular.module('dynaFormApp', ['ngRoute','appControllers'])
 


dynaFormApp.config(function($routeProvider) {
        $routeProvider

            // route for the home page
            .when('/editar/:formId', {
                templateUrl : 'editar.html',
                controller  : 'EditarController'
            })
            .when('/adicionar/:formId', {
                templateUrl : 'adicionar.html',
                controller  : 'AdicionarController'
            })
            .when('/dadosFormulario/:formId/:formTitle', {
                templateUrl : 'dadosFormulario.html',
                controller  : 'DadosFormularioController'
            })
            .when('/novo', {
                templateUrl : 'adicionar.html',
                controller  : 'NovoController'
            })
            .otherwise({
		      templateUrl: 'lista.html',
		      controller: 'formController'
		    });
            

           

    });

var appControllers = angular.module('appControllers', []);
appControllers.service('dynaFormAppService',['$http',function($http){
	this.alertMessage = function($scope,message){
		$scope.alertaMensagem=message;
		jQuery("#alert").modal('show');
	}
	this.findAll = function(callBack,error){
		$http({
			  method: 'GET',
			  url: '/colector/templates'
			}).then(function (response) {
		        	callBack(response.data);
		         },function(response){
		        	 error(response);
		         });
		
	}
	this.findOne = function(formId,callBack,error){
		$http({
			  method: 'GET',
			  url: "/colector/templates/"+formId
			})
		        .then(function (response) {
		        	callBack(response.data);
		         },function(response){
		        	 error(response);
		         });
		
	}
	this.findData = function(formId,callBack,error){
		$http({
			  method: 'GET',
			  url: "/colector/templates/"+formId+"/data"
			})
		        .then(function (response) {
		        	callBack(response.data);
		         },function(response){
		        	 error(response);
		         });
		
	}
	
	this.update = function(formId, dataValue,callBack,error){
		$http({
			  method: 'POST',
			  url: "/colector/templates/"+formId+"/data",
			  data: dataValue
			})
		        .then(function (response) {
		        	callBack(response.data);
		         },function(response){
		        	 error(response);
		         });
		
	}
	this.deletar = function(formId,callBack,error){
		$http({
			  method: 'DELETE',
			  url: "/colector/templates/"+formId
			})
		        .then(function (response) {
		        	 
		         },function(response){
		        	 error(response);
		         });
		
	}
	
	
	
}]);

dynaFormApp.directive("dynamicform",['$compile',"dynaFormAppService", function($compile,dynaFormAppService){
    return{
        link: function(scope, element){
        	 this.element = element;
        	 dynaFormAppService.findOne(scope.formId,function(response){
        		 scope.title=response.title; 
        		 scope.formulario =  response;
	             var fields = scope.formulario.fields;
	             for (i = 0; i < fields.length; i++) {
	   	    		 var functionValue = fieldMethodFactory[fields[i].type];
	   	    		 if(functionValue == null){
	   	    			 functionValue = fieldMethodFactory['default'];
	   	    		 }
	   	    		 var config = new FactoryConfig(fields[i],$compile,scope,element);
	   	    		 functionValue(config);   
	   	    		 
	   	 	    	    	
	   	    	 }
            },errorCallback(scope))
   	     }
    }
}]);

appControllers.controller('DadosFormularioController',['$scope','$routeParams','$location','dynaFormAppService', function ($scope,$routeParams,$location,dynaFormAppService){
	 $scope.title =  $routeParams.formTitle;
	 $scope.formId =  $routeParams.formId;
	 dynaFormAppService.findData($scope.formId,function(response){
		 $scope.formulario =  response;
     },errorCallback($scope));
	 $scope.adicionar = function(idForm){
		  $location.path('/adicionar/'+ idForm);
	 };
}]);

appControllers.controller('formController',['$scope','$location','dynaFormAppService', function ($scope,$location,dynaFormAppService) {
	 $scope.title="Lista Formularios"; 
	 dynaFormAppService.findAll(function(response){
		 $scope.formularios =  response;  
		   
	  },errorCallback($scope));
	  $scope.editar = function(idForm){
		  $location.path('/editar/'+ idForm);
	  };
	  $scope.dadosFormulario = function(idForm,title){
		  $location.path('/dadosFormulario/'+ idForm+'/'+title);
	  };
	  $scope.adicionar = function(idForm){
		  $location.path('/adicionar/'+ idForm);
	  };
	  $scope.excluir = function(formulario){
		  dynaFormAppService.deletar(formlario._id,function(response){
			  $scope.formularios.remove(formulario);
		  },errorCallback($scope));
	  };
	  
	}]);




dynaFormApp.controller('EditarController',['$scope','$routeParams','$location','dynaFormAppService', function ($scope,$routeParams,$location,dynaFormAppService) {
	
	$scope.formId=$routeParams.formId;
	  
	}]);

dynaFormApp.controller('AdicionarController',['$scope','$routeParams','$location','dynaFormAppService', function ($scope,$routeParams,$location,dynaFormAppService) {
	$scope.formId=$routeParams.formId;  
	$scope.salvar = function(){
		var json='{';
		var i = 1;
		jQuery("#form").find('input').each(function () {
			 jQuery("#"+this.name.replace(/\s/g,'_')+"_Warn").text("");
			if(this.type == 'checkbox'){
				json+= (i>1) ? ',"'+this.name +'"':'"'+this.name +'"';
				if(this.value == 'check'){
					json+=':true'
				} else {
					json+=':false'
				}
			} else if(this.type == 'number'){
				json+= (i>1) ? ',"'+this.name +'"':'"'+this.name +'"';
			} else if(this.type == 'radio'){
			   if(this.checked){
				   json+= (i>1) ? ',"'+this.name +'"':'"'+this.name +'"';
			   	   json+=':"'+this.value+'"';
			   }	
			} else {
				json+= (i>1) ? ',"'+this.name +'"':'"'+this.name +'"';
				json+=':"'+this.value+'"';
			}
			i++;
		});
		json+='}';
		alert(angular.toJson(json));
		dynaFormAppService.update($scope.formId,json,function(response){
			$location.path("/listaFormulario");
		},errorCallback($scope));
	}
	
	/*  
	  dynaFormAppService.findOne($routeParams.formId,function(response){
	    	 $scope.formulario =  response;
	    	 var fields = $scope.formulario.fields;
	    	 for (i = 0; i < fields.length; i++) {
	    		 var functionValue = fieldMethodFactory[fields[i].type];
	    		 if(functionValue == null){
	    			 functionValue = fieldMethodFactory['default'];
	    		 }
	    		 var config = new FactoryConfig('#form',fields[i],(i+1));
	    		 functionValue(config);   ~kl~klç
	    		 
	 	    	    	llçççÇÇÇÇÇ~~~~~gt;;c;x;çççççllllçççç
	    	 }
	    },errorCallback($scope))
	    */
	   
	  
	}]);
dynaFormApp.controller('NovoController',['$scope','$location','dynaFormAppService', function ($scope,$location,dynaFormAppService) {

	  
	}]);










