package rbprojects.dynamicform.web.dto;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import rbprojects.dynamicform.model.Field;

public class TemplateDTO {
	public TemplateDTO() {
	}

	public String _id;

	public String title;

	public Object fields;
	
	public Map<String, Object>[] data;
	
	public Integer dataCount;

}
