#Dynamic Form#

###Detalhes da Implementa��o
   1. Estou utilizando o banco de dados NoSQL Mongodb 
   2. Para implenta��o do servidor Rest, "back-end", 
   utilizei a framework Spring para fazer a inje��o de depend�ncia.
   3. O container web � Jetty.
   4. Para inicializar o container web e configurar o Banco de Dados,estou utilizando a framework SpringBoot.
   5. Para a implenta��o "Front-End" , estou utilizando a framework AngularJs com jQuery.    

###Configura��o do Banco de Dados e host do servidor.
   1. Para configurar o banco de dados MongoDB basta alterar as propriedades ,"mongo.db.*",do arquivo src/main/resources/application.properties.
   2. Para configurar o host do servidor adicionar os param�tros "server.host" e "server.port", por padr�o o servidor sobe 
   no host localhost porta "8080".

###Teste
   1. Os testes unit�rios est�o na pasta rbprojects.dynamicform.FormularioServiceTeste.
   2. Para realiza��o dos testes unit�rios estou utilizando uma implementa��o do banco de dados MongoDb, que roda em mem�ria, chamada Fongo.
   3. Os dados de inicializa��o dos testes unit�rios s�o carregados atrav�s do arquivo "src/test/resources/data.json".
   4. As configura��es do banco de dados e do servidor , inicializado nos testes, est�o no arquivo "src/test/resources/application-teste.properties"     
   
###Execu��o
   1. A aplica��o est� dispon�vel na nuvem,Openshift, atrav�s do link http://dynamicform-rbprojects.rhcloud.com/
   2. Podemos executar a aplica��o compilando ela e depois executando o comando "java -jar target/dynamicform-0.0.1-SNAPSHOT.jar"
   3. Execu��o no modo de teste atrav�s da classe rbprojects.dynamicform.AppTesteRun

###Acesso Banco de Dados Openshift
    Link: https://dynamicform-rbprojects.rhcloud.com/rockmongo
    Usuario: visitorUser,
    Senha: aapd5942,
    Banco de Dados :dynamicform

  
