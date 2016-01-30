package rbprojects.dynamicform.model;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Template {
	public Template() {
	}

	@Id
	private ObjectId id;

	private String title;

	private Field[] fields;

	private Map<String, Object>[] data;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Field[] getFields() {
		return fields == null ? new Field[0] : fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public Map<String, Field> getMappedFields() {
		Map<String, Field> mappedFields = new HashMap();
		for (Field field : fields) {
			mappedFields.put(field.getLabel(), field);
		}
		return mappedFields;
	}

	public Map<String, Object>[] getData() {
		return data == null ? new HashMap[0] : data;
	}

	public void addData(Map<String, Object> data) {
		final Map[] mappedToAdd = new HashMap[getData().length + 1];
		for (int i = 0; i < getData().length; i++) {
			mappedToAdd[i] = getData()[i];
		}
		mappedToAdd[getData().length] = data;
		this.data = mappedToAdd;
	}

	@Override
	public String toString() {
		return String.format("Template[id=%s, title='%s',fields=%s]", id, title, fields);
	}

}
