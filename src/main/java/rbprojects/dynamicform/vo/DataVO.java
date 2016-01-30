package rbprojects.dynamicform.vo;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "template")
public class DataVO {

	
	public DataVO() {
		
	}
	@Id
	public String id;
	
	public LabelVO[] fields;
	
	public Map<String, String>[] data;

}
