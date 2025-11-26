/**
 * 
 */
package org.gaming.design.meta;

/**
 * @author YY
 *
 */
public enum ExportType {
	BOTH("*"),
	NEITHER("-"),
	S("S"),
	C("C"),
	;
	
	private final String value;
	private ExportType(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	public static ExportType vof(String value) {
		if("*".equals(value)) {
			return BOTH;
		} else if("S".equals(value)) {
			return S;
		} else if("C".equals(value)) {
			return C;
		} else {
			return NEITHER;
		}
	}
}
