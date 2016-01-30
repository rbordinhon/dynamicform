<!DOCTYPE html>
<html ng-app="dynaFormApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro Formulario</title>

    <!-- bower:css -->
    <link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.css" />
    <!-- endbower -->
    <link rel="stylesheet" Type="text/css" href="bower_components/jquery-ui/themes/black-tie/jquery-ui.css" />
    <link rel="stylesheet" Type="text/css" href="css/main.css" />
    <!-- bower:js -->
        <script src="bower_components/jquery/dist/jquery.js"></script>
       	<script src="bower_components/bootstrap/dist/js/bootstrap.js"></script>
        <script src="bower_components/angular/angular.js"></script>
   <!-- endbower -->
   <link rel="stylesheet" href="../componentes/css/commons.css">
   <script src="../componentes/js/commons.js" type="text/javascript"/>
   <script src="js/cadastro.js" charset="UTF-8" type="text/javascript"/>
   <link rel="stylesheet" href="css/cadastro.css">
</head>
<body ng-controller="formController">
	<div class="container">
		<div class="page-header">
			<h2 id="cadastoTitle" >Cadastro Formulario</h2>
			<form id="FormNovo"
							action="novo">
							<button type="button" ng-click="novo()" class="btn btn-default">Novo</button>
							<!--<button style="heigth:30px" class="btn btn-primary"        
							
			</form> 
		</div>
		
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>Descricao</th>
					<th>Registros</th>
					<th>Acoes</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="formulario in formularios">
					<td width="50%">{{formulario.title}}</a></td>
					<td width="40%">{{formulario.dataCount}}</a></td>
					<td>
						<form id="{{'FormEditar_'+ formulario.id}}"
							action="editarFormulario">
							<!--<button style="heigth:30px" class="btn btn-primary"        
							data-toggle="modal" ng-click="favorito(phone.idCelular)"
							id="addCompare" type="button" >OK</button>-->
							<a href="#" ng-click="editar(formulario.id)">Editar</a>
						</form>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- Modal -->
	<!-- Modal -->
	<div class="modal fade" id="alert" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div id="alert_dialog" class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Celular Comparação</h4>
				</div>
				<div class="modal-body">
					<h5>{{alertaMensagem}}</h5>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
				</div>
			</div>
		</div>
	</div>



</body>
</html>