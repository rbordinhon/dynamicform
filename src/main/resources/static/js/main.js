/*
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
*/
var errorCallback = function(scope){
	return function(response){
		if(response.status == 412){
			 for (var i = 0; i < response.data.errors.length; i++) {
				 var errorData = response.data.errors[i];
				 var categoryId = errorData.category.replace(/\s/g,'_');
				 categoryId = categoryId.replace(/:/g,'_');
				 jQuery("#"+categoryId+"_Warn").text(errorData.message);
			 }
			 jQuery('#modalTitle').text(scope.title);
		 	 jQuery("#alertMessage").text("Erro na validação");
			 jQuery("#alert").modal('show');
		} else {
			jQuery('#modalTitle').text(scope.title);
			jQuery("#alertMessage").text(response.data.errors[0].message);
			jQuery("#alert").modal('show');
		}
	}
}

var dynaFormApp = angular.module('dynaFormApp', ['ngRoute','appControllers'])
 


dynaFormApp.config(function($routeProvider) {
        $routeProvider

            .when('/dadosFormulario/adicionar/:formId', {
                templateUrl : 'views/adicionarFormulario.html',
                controller  : 'AdicionarController'
            })
            .when('/dadosFormulario/:formId/:formTitle', {
                templateUrl : 'views/dadosFormulario.html',
                controller  : 'DadosFormularioController'
            })
            .when('/template/novo', {
                templateUrl : 'views/cadastroTemplate.html',
                controller  : 'CadastroController'
            })
            .when('/template/editar/:formId', {
                templateUrl : 'views/cadastroTemplate.html',
                controller  : 'CadastroController'
            })
            .otherwise({
		      templateUrl: 'views/listaTemplate.html',
		      controller: 'FormController'
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
	
	this.novoFormulario = function(dataValue,callBack,error){
		$http({
			  method: 'POST',
			  url: "/colector/templates",
			  data: dataValue
			})
		        .then(function (response) {
		        	callBack(response.data);
		         },function(response){
		        	 error(response);
		         });
		
	}
	
	this.atualizaFormulario = function(formId,dataValue,callBack,error){
		$http({
			  method: 'PUT',
			  url: "/colector/templates/"+formId,
			  data: dataValue
			})
		        .then(function (response) {
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
		        	callBack(response.data);
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
		  $location.path('/dadosFormulario/adicionar/'+ idForm);
	 };
	 $scope.voltar = function(){
		  $location.path('');
	 };
}]);

appControllers.controller('FormController',['$scope','$location','dynaFormAppService', function ($scope,$location,dynaFormAppService) {
	 $scope.title="Lista Formularios"; 
	 dynaFormAppService.findAll(function(response){
		 $scope.formularios =  response;  
		   
	  },errorCallback($scope));
	  $scope.editar = function(idForm){
		  $location.path('/template/editar/'+ idForm);
	  };
	  $scope.dadosFormulario = function(idForm,title){
		  $location.path('/dadosFormulario/'+ idForm+'/'+title);
	  };
	  $scope.adicionar = function(idForm){
		  $location.path('/dadosFormulario/adicionar/'+ idForm);
	  };
	  $scope.excluir = function(index){
		  var formulario = $scope.formularios[index];
		  dynaFormAppService.deletar(formulario._id,function(response){
		  $scope.formularios.splice(index,1);
		  $scope.$apply();
		  },errorCallback($scope));
	  };
	  $scope.novo = function(index){
		  $location.path('/template/novo');
	  };
	  
	
	  
	  
	  
	}]);

appControllers.controller('AdicionarController',['$scope','$routeParams','$location','dynaFormAppService', function ($scope,$routeParams,$location,dynaFormAppService) {
	$scope.formId=$routeParams.formId;  
	$scope.voltar = function(){
	   $location.path('');
	};
	
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
		dynaFormAppService.update($scope.formId,json,function(response){
			$location.path("");
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
appControllers.controller('CadastroController',['$scope','$routeParams','$location','dynaFormAppService', function ($scope,$routeParams,$location,dynaFormAppService) {
	    $scope.voltar = function(){
		   $location.path('');
		};
	   if($routeParams.formId != null){
		   dynaFormAppService.findOne($routeParams.formId,function(response){
			   $scope.formulario = response;
			   $scope.title = response.title;
		   },errorCallback($scope));
	   } else {
		   $scope.formulario = {title:"", fields:[{label:"",type:""}]};
		   $scope.title="Novo Formulario";
	   }
	   
	   $scope.adicionarCampo = function(){
		   $scope.formulario.fields.push({label:"",type:""});   
	   }
	   $scope.adicionarRadio = function(index){
		   if($scope.formulario.fields[index].radios == null){
			   $scope.formulario.fields[index].radios=[]; 
		   }
		   $scope.formulario.fields[index].radios.push({label:"",value:""}); 
	   }
	   $scope.removerRadio = function(index, radioIndex){
		   $scope.formulario.fields[index].radios.splice(radioIndex,1); 
	   }
	   $scope.removerCampo = function(index){
		   $scope.formulario.fields.splice(index,1); 
	   }
	   $scope.salvar = function(){
		   if($scope.formulario._id == null){
			   dynaFormAppService.novoFormulario($scope.formulario,function(response){
				   $location.path('');
			   },errorCallback($scope));
		   } else {
			   dynaFormAppService.atualizaFormulario($scope.formulario._id,$scope.formulario,function(response){
				   $location.path('');
			   },errorCallback($scope));
		   }
		   
	   }
	   
}]);










