/**
 * 
 */
package org.gaming.db.annotation.enuma;

/**
 * @author YY
 * 索引方式
 */
public enum IndexWay {
	BTREE("BTREE"),
	HASH("HASH");
	
	private String v;
	
	private IndexWay(String value) {
		this.v = value;
	}
	public String value() {
		return this.v;
	}
}
