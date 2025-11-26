/**
 * 
 */
package org.gaming.design.meta;

/**
 * @author YY
 *
 */
public class DesignConstData {
	private String key;
	private String type;
	private String comment;
	
	public DesignConstData(String key, String type, String comment) {
		this.key = key;
		this.type = type;
		this.comment = comment;
	}
	
	public String getKey() {
		return key;
	}
	public String getType() {
		return type;
	}
	public String getComment() {
		return comment;
	}
}
