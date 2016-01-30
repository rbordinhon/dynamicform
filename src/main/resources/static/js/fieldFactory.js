 var fieldMethodFactory = {};

var Formater = function(field){
			this.field = field;
			this.getId = function(){
				 return this.field.label.replace(/\s/g,'_');
			};
			this.getWarnId = function(){
				 return this.getId()+"_Warn";
			};
			 
			this.getInputId = function(){
				 return this.getId()+"_Input";
			};
 }

var FactoryConfig = function(field, $compile, scope, element){
	this.field = field;
	this.formater = new Formater(field);
	this.compiler = $compile;
	this.scope = scope;
	this.element = element;
	
}

 
 fieldMethodFactory['default'] = function(config){
	 var returned = "";
	     returned += '<div class="form-group">\n';
		 returned += ' <label for="'+config.formater.getInputId()+'">'+config.field.label+':</label>\n';
		 returned += "<h8 style='float:right' class='text-danger' id='"+ config.formater.getWarnId()+"' ></h8>\n";
		 returned += " <input id='"+config.formater.getInputId()+"' name='"+config.field.label+"' type='"+config.field.type+ "'class='form-control'";
		 
		 //var data = angular.fromJson('{"'+config.field.label.replace(/\s/g,'\s')+'":{"value":"","label":""}}');
		 
		 //alert(angular.toJson(data));
		 //scope.dados = data;
		 //alert(angular.toJson(data));
	 if(config.field.readOnly){
		 returned += ' readonly';
	 }
	 if(config.field.value != null ){
			returned += ' value="'+ config.field.value+'"';
	 }
	 if(config.field.placeholder != null){
		 returned += ' placeholder="'+config.field.placeholder+'"';
	 }
	 if(config.field.maxLength > 0 ){
		 returned += ' SIZE="'+config.field.maxLength+'" MAXLENGTH="'+config.field.maxLength+'"';
	 }
	 returned += '></input>\n';
	 returned += '</div>\n';
	 var linkFn = config.compiler(returned);
	 var content = linkFn(config.scope);
     config.element.append(content);
	
 };
 
 fieldMethodFactory['text'] = function(config){
    fieldMethodFactory['default'](config);
   
 };
 
 fieldMethodFactory['email'] = function(config){
	 fieldMethodFactory['default'](config);
 };
 fieldMethodFactory['color'] = function(config){
	 fieldMethodFactory['default'](config);
 };
 fieldMethodFactory['radio'] = function(config){
	 var radios  = config.field.radios;
	 var returned =  "";
		
	     returned += '<div class="form-group">\n'
		 returned += ' <label for="usr">'+config.field.label+':</label>\n'
		 returned += "<h8 style='float:right' class='text-danger' id='"+ config.formater.getWarnId()+"' ></h8>\n";
		 returned += '<div class="form-control">\n'
		 
     for (var int = 0; int < radios.length; int++) {
		 returned += '<label class="radio-inline"><input   type="'+config.field.type+'" name="'+config.field.label+'" value="'+radios[int].value+'">'+radios[int].label+'</label>\n'
	 }
	 returned += '</div>\n';
	 returned += '</div>\n';
	 var linkFn = config.compiler(returned);
	 var content = linkFn(config.scope);
     config.element.append(content);
 };
 fieldMethodFactory['checkbox'] = function(config){
	 var radios  = config.field.radios;
	 var returned =  "";
		
	     returned += '<div class="form-group">\n';
	    	 returned += '<div class="checkbox">\n';
	    	 returned += "<h8 style='float:right' class='text-danger' id='"+ config.formater.getWarnId()+"' ></h8>\n";
		     returned += '<label for="'+config.formater.getInputId()+'"><input class="checkbox" name="'+config.field.label+'" id="'+config.formater.getInputId()+'" type="checkbox" value="check">'+config.field.label+'</label>\n'
	    	 returned += '</div>\n'
	    	 returned += '</div>\n';
		     var linkFn = config.compiler(returned);
			 var content = linkFn(config.scope);
		     config.element.append(content);

 
 };
 fieldMethodFactory['date'] = function(config){
	 fieldMethodFactory['default'](config);
	 jQuery("#"+config.formater.getInputId()).mask("99/99/9999");
 };
 
 fieldMethodFactory['number'] = function(config){
	 var returned = "";
     returned += '<div class="form-group">\n';
	 returned += ' <label for="'+config.formater.getInputId()+'">'+config.field.label+':</label>\n';
	 returned += "<h8 style='float:right' class='text-danger' id='"+ config.formater.getWarnId()+"' ></h8>\n";
	 returned+= " <input id='"+config.formater.getInputId()+"' name='"+config.field.label+"' type='text' class='form-control'";
	 if(config.field.readOnly){
		 returned += ' readonly';
	 }
	 if(config.field.value != null){
		returned += ' value="'+ config.field.value+'"';
	 }
	 if(config.field.placeholder != null){
		 returned += ' placeholder="'+config.field.placeholder+'"';
	 }
	 if(config.field.maxLength > 0 ){
		 var max ='';
		 for (var i = 0; i < config.field.maxLength; i++){
			 max+='9'
		 }
		 returned += ' min="0" max="'+max+'" maxLength="'+config.field.maxLength+'"';
	 }
	 returned += '></input>\n';
	 returned += '</div>\n';
	 var linkFn = config.compiler(returned);
	 var content = linkFn(config.scope);
     config.element.append(content);
	 //jQuery(config.contentId).append(returned);
	 
	 
	 jQuery("#"+config.formater.getInputId()).keydown(function (e) {
	       if (jQuery.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
	            (e.keyCode == 65 && ( e.ctrlKey === true || e.metaKey === true ) ) || 
	            (e.keyCode >= 35 && e.keyCode <= 40)) {
	            return;
	        }
	        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
	            e.preventDefault();
	        }
	  });
	
	
 };
 
 fieldMethodFactory['datetime'] = function(field,index){
	 fieldMethodFactory['default'](config);
	 jQuery("#"+config.formater.getInputId()).mask("99/99/9999 99:99:99");
 };
 
 fieldMethodFactory['datetime-local'] = function(field,index){
	 fieldMethodFactory['default'](config);
	 jQuery("#"+config.formater.getInputId()).mask("99/99/9999 99:99:99");
 };
 
 fieldMethodFactory['time'] = function(field,index){
	 fieldMethodFactory['default'](config);
	 jQuery("#"+config.formater.getInputId()).mask("99:99:99");

 };
 
 fieldMethodFactory['month'] = function(field,index){
	 fieldMethodFactory['default'](config);
	
 };
 
 
 