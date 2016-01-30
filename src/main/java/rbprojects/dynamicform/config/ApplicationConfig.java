package rbprojects.dynamicform.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="rbprojects.dynamicform.service")
public class ApplicationConfig {

	public static final String ERROR_PROPERTIES="errors.properties"; 
	
	@Bean
	@Qualifier(ERROR_PROPERTIES)
	public Properties error() throws IOException{
		InputStream inStream = ClassLoader.getSystemResourceAsStream(ERROR_PROPERTIES);
		try {
			Properties props = new Properties();
			props.load(inStream);
			return props;
		}finally {
			inStream.close();
		}
	}
	
	
	
	
}
