/**
 * 
 */
package org.gaming.prefab.mail;

/**
 * @author YY
 *
 */
public class MailArgs {
	
	private Object[] args;
	
	public static MailArgs build(Object... args) {
		MailArgs params = new MailArgs();
		params.args = args;
		return params;
	}

	public Object[] getArgs() {
		return args;
	}
}
