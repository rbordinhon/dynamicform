package rbprojects.dynamicform.model;

import java.util.Arrays;

public class Field {
	private String label;
	private String type;
	private Boolean required;
	private Boolean readOnly;
	private String value;
	private Integer maxLength;
	private String placeholder;
	private Radio[] radios;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getRequired() {
		return required == null ? false : required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getReadOnly() {
		return readOnly == null ? false : readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getMaxLength() {
		return maxLength == null ? 0 :maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public Radio[] getRadios() {
		return radios == null ? new Radio[0] : radios;
	}

	public void setRadios(Radio[] radios) {
		this.radios = radios;
	}

	@Override
	public String toString() {
		return "Field [label=" + label + ", type=" + type + ", required=" + required + ", readOnly=" + readOnly
				+ ", value=" + value + ", maxLength=" + maxLength + ", placeHolder=" + placeholder + ", radios="
				+ Arrays.toString(radios) + "]";
	}

}
