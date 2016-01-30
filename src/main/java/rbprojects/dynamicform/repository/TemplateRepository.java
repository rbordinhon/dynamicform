package rbprojects.dynamicform.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import rbprojects.dynamicform.model.Template;


public interface TemplateRepository extends MongoRepository<Template, String> {
	public Template findByTitle(String title);
	public Template findById(ObjectId id);
	
}
