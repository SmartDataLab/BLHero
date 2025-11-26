/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

/**
 * @author YY
 *
 */
public class GameServerMaintainVo {
	private int serverId;
	private String name;
	private boolean maintain;
	private boolean maintainResponse;
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMaintain() {
		return maintain;
	}
	public void setMaintain(boolean maintain) {
		this.maintain = maintain;
	}
	public boolean isMaintainResponse() {
		return maintainResponse;
	}
	public void setMaintainResponse(boolean maintainResponse) {
		this.maintainResponse = maintainResponse;
	}
}
