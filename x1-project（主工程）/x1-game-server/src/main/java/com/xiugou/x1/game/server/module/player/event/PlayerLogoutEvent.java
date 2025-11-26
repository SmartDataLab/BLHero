/**
 * 
 */
package com.xiugou.x1.game.server.module.player.event;

/**
 * @author YY
 *
 */
public class PlayerLogoutEvent {
	private long pid;
	public PlayerLogoutEvent(long pid) {
		this.pid = pid;
	}
	public long getPid() {
		return pid;
	}
}
