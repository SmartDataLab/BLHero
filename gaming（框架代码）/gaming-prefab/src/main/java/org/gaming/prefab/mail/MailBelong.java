/**
 * 
 */
package org.gaming.prefab.mail;

/**
 * @author YY
 * 邮件归属者
 */
public enum MailBelong {
	//每个人
	EVERYONE(1),
	//某些人
	SOMEONE(2),
	;
	private final int value;
	private MailBelong(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
