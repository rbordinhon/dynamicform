package rbprojects.dynamicform.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import rbprojects.dynamicform.model.Template;
import rbprojects.dynamicform.vo.DataVO;


public interface FormularioRepository extends MongoRepository<DataVO, String> {
	
	public DataVO findById(ObjectId id);

}
