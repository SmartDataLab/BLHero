/**
 * 
 */
package org.gaming.design.meta;

/**
 * @author YY
 *
 */
public class DesignColumnMeta {
	private String name;
	private String type;
	private String comment;
	private ExportType readType;
	
	public DesignColumnMeta(String name, String type, String comment, String readType) {
		this.name = name;
		this.type = type;
		this.comment = comment;
		this.readType = ExportType.vof(readType);
	}
	
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getComment() {
		return comment;
	}
	public ExportType getReadType() {
		return readType;
	}
}
