/**
 * 
 */
package org.gaming.tool;

/**
 * @author YY
 *
 */
public class CodeBuilder {

	public static final String NL = 	"\n";
	public static final String TAB = 	"\t";
	public static final String NT = 	"\n\t";
	public static final String NTT = 	"\n\t\t";
	public static final String NTTT = 	"\n\t\t\t";
	public static final String NTTTT = 	"\n\t\t\t\t";
	public static final String NTTTTT = "\n\t\t\t\t\t";
	public static final String NTTTTTT = "\n\t\t\t\t\t\t";
	public static final String T = 		"\t";
	public static final String TT = 	"\t\t";
	public static final String TTT = 	"\t\t\t";
	public static final String TTTT = 	"\t\t\t\t";
	public static final String TTTTT = 	"\t\t\t\t\t";
	public static final String TTTTTT = "\t\t\t\t\t\t";
	
	private final StringBuilder builder = new StringBuilder();
	private final String preIndent;
	
	public CodeBuilder() {
		this.preIndent = "";
	}
	
	public CodeBuilder(String preIndent) {
		this.preIndent = preIndent;
	}
	
	public CodeBuilder append(String s) {
		builder.append(s);
		return this;
	}
	
	public CodeBuilder format(String format, Object... args) {
		builder.append(String.format(format, args));
		return this;
	}
	
	public CodeBuilder NFormat(String format, Object... args) {
		builder.append(NL).append(preIndent).append(String.format(format, args));
		return this;
	}
	
	public CodeBuilder NTFormat(String format, Object... args) {
		builder.append(NT).append(preIndent).append(String.format(format, args));
		return this;
	}
	public CodeBuilder NTTFormat(String format, Object... args) {
		builder.append(NTT).append(preIndent).append(String.format(format, args));
		return this;
	}
	public CodeBuilder NTTTFormat(String format, Object... args) {
		builder.append(NTTT).append(preIndent).append(String.format(format, args));
		return this;
	}
	public CodeBuilder NTTTTFormat(String format, Object... args) {
		builder.append(NTTTT).append(preIndent).append(String.format(format, args));
		return this;
	}
	public CodeBuilder NTTTTTFormat(String format, Object... args) {
		builder.append(NTTTTT).append(preIndent).append(String.format(format, args));
		return this;
	}
	public CodeBuilder NTTTTTTFormat(String format, Object... args) {
		builder.append(NTTTTTT).append(preIndent).append(String.format(format, args));
		return this;
	}
	
	public String toString() {
		return builder.toString();
	}
	
	public static String upperFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
