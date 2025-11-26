/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.event;

/**
 * @author hyy
 *
 */
public class HeroAwakenEvent {
	private long pid;

	private int identity;

	public static HeroAwakenEvent of(long pid, int identity) {
		HeroAwakenEvent event = new HeroAwakenEvent();
		event.pid = pid;
		event.identity = identity;
		return event;
	}

	public long getPid() {
		return pid;
	}

	public int getIdentity() {
		return identity;
	}
}
