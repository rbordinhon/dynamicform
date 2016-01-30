package rbprojects.dynamicform.config;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import rbprojects.dynamicform.repository.TemplateRepository;

@Configuration
@EnableMongoRepositories(basePackageClasses = TemplateRepository.class)
@ComponentScan(basePackages = "rbprojects.dynamicform.model")
public class PersitenceConfigs extends AbstractMongoConfiguration implements ApplicationContextAware {
	private static Logger logger = LoggerFactory.getLogger(PersitenceConfigs.class);
	@Value("${mongo.db.database:\"\"}")
	private String mongoDatabase;

	@Value("${server.env:\"\"}")
	private String ambiente;

	@Value("${mongo.db.host:\"localhost\"}")
	private String mongoHost;

	@Value("${mongo.db.port:\"27017\"}")
	private String mongoPort;

	@Value("${mongo.db.user:\"\"}")
	private String mongoUser;

	@Value("${mongo.db.password:\"\"}")
	private String mongoPassword;

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		if (ambiente.equals("teste")) {
			Class fongoClazz = Class.forName("com.github.fakemongo.Fongo");
			Constructor<Mongo> contruct = fongoClazz.getConstructor(new Class[] { String.class });
			Object fongo = contruct.newInstance("Embedded Mongo");
			return (Mongo) fongoClazz.getMethod("getMongo").invoke(fongo);
		} else {
			final String mongoDbUrlPattern = "mongodb://{0}:{1}@{2}:{3}";
			final String mongoDbUrl = MessageFormat.format(mongoDbUrlPattern, mongoUser, mongoPassword, mongoHost, mongoPort);
			logger.warn("Connectando no mongodb no endereco:"+mongoDbUrl);
			MongoClientURI uri = new MongoClientURI(mongoDbUrl);
			return new MongoClient(uri);
		}
	}

	@Override
	public String getDatabaseName() {
		return mongoDatabase;
	}

	@Override
	public String getMappingBasePackage() {
		return "rbprojects.dynamicform.repository";
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub

	}

}
