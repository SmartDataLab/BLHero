/**
 * 
 */
package org.gaming.design.loader;

/**
 * @author YY
 *
 */
public class DesignField {
	private String name;
	private String fieldType;
	private String readBy;
	private String comment;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReadBy() {
		return readBy;
	}
	public void setReadBy(String readBy) {
		this.readBy = readBy;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
}
