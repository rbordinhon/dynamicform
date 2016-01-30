package rbprojects.dynamicform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import rbprojects.dynamicform.config.ApplicationConfig;
import rbprojects.dynamicform.config.PersitenceConfigs;
import rbprojects.dynamicform.repository.TemplateRepository;
import rbprojects.dynamicform.service.TemplateService;

public class AppTesteRun {
	public static void main(String[] args) throws DataAccessException, Exception {
		SpringApplicationBuilder build = new SpringApplicationBuilder();
		build.profiles("teste");
		build.sources(Application.class);
		ApplicationContext appCtx = build.run(new String[0]);
		TemplateRepository repo = appCtx.getBean(TemplateRepository.class);

		repo.deleteAll();
		PersitenceConfigs persist = appCtx.getBean(PersitenceConfigs.class);
		DB db = persist.mongoDbFactory().getDb(persist.getDatabaseName());
		DBCollection table = db.getCollection("template");
		BasicDBList list = (BasicDBList) JSON.parse(loadJsonData());
		for (Object object : list) {
			DBObject obdb = (DBObject) object;
			obdb.put("_id", new ObjectId((String) obdb.get("_id")));
			table.insert((DBObject) object);
		}
	}

	public static String loadJsonData() throws IOException {
		StringBuilder build = new StringBuilder();
		InputStream data = null;
		try {
			data = ClassLoader.getSystemResourceAsStream("data.json");
			byte[] buffer = new byte[1024];
			for (int len = data.read(buffer); len > -1; len = data.read(buffer)) {
				build.append(new String(Arrays.copyOfRange(buffer, 0, len)));
			}
			return build.toString();

		} finally {
			try {
				data.close();
			} catch (Exception e) {

			}
		}
	}
}
