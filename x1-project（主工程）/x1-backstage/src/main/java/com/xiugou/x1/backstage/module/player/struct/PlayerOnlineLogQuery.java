/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class PlayerOnlineLogQuery {
	private int time;
	private List<Integer> serverUids = new ArrayList<>();
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public List<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(List<Integer> serverUids) {
		this.serverUids = serverUids;
	}
}
