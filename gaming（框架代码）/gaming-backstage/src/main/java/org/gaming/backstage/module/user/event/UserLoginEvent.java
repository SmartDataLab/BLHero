/**
 * 
 */
package org.gaming.backstage.module.user.event;

/**
 * @author YY
 *
 */
public class UserLoginEvent {
	private long userId;

	public static UserLoginEvent of(long userId) {
		UserLoginEvent event = new UserLoginEvent();
		event.userId = userId;
		return event;
	}

	public long getUserId() {
		return userId;
	}
}
