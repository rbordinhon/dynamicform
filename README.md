#Dynamic Form#

###Detalhes da Implementação
   1. Estou utilizando o banco de dados NoSQL Mongodb 
   2. Para implentação do servidor Rest, "back-end", 
   utilizei a framework Spring para fazer a injeção de dependência.
   3. Para inicializar o container web e configurar o Banco de Dados, estou utilizando a framework SpringBoot.
   4. Para a implentação "Front-End", estou utilizando as frameworks AngularJs e jQuery.    
   5. A Ide utilizada durante o desenvolvimento é o Eclipse.

###Configuração do banco de dados e host do servidor.
   1. Para configurar o banco de dados MongoDB basta alterar as propriedades "mongo.db.*" no arquivo "src/main/resources/application.properties".
   2. Para configurar o host do servidor basta adicionar as propriedados "server.host" e "server.port" no arquivo "src/main/resources/application.properties", por padrão o servidor sobe 
   no host "localhost" na porta "8080".

###Teste
   1. Os testes unitários estão no pasta "src/test/java/rbprojects/dynamicform".
   2. Para realização dos testes unitários estou utilizando uma implementação do banco de dados MongoDb, que roda em memória, chamada Fongo.
   3. Os dados de inicialização dos testes unitários são carregados através do arquivo "src/test/resources/data.json".
   4. As configurações do banco de dados e do servidor , inicializado nos testes, estão no arquivo "src/test/resources/application-teste.properties"     
   
###Execução
   1. A aplicação está disponível na nuvem,Openshift, através do link http://dynamicform-rbprojects.rhcloud.com/
   2. Podemos executar a aplicação compilando ela e depois executando o comando "java -jar target/dynamicform-0.0.1-SNAPSHOT.jar"
   3. Podemos executar no modo de teste através da classe rbprojects.dynamicform.AppTesteRun

###Acesso ao banco de dados do servidor Openshift
    Link: https://dynamicform-rbprojects.rhcloud.com/rockmongo
    Usuario: visitorUser,
    Senha: aapd5942,
    Banco de Dados :dynamicform

  
