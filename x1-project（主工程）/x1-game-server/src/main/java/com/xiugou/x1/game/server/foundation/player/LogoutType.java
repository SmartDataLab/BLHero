/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

/**
 * @author YY
 *
 */
public enum LogoutType {
	ANOTHER_LOGIN(1, "异地登录"),
	IDLE_TOO_LONG(2, "闲时过长"),
	CONNECT_ERROR(3, "连接异常"),
	PLAYER_LOGOUT(4, "玩家登出"),
	CONNECT_BREAK(5, "连接断开"),
	FORBID(6, "账号封禁"),
	FORCED_OFFLINE(7, "后台强制玩家下线"),
	MAINTAIN(8, "服务器维护"),
	;
	
	private final int code;
	private final String desc;
	
	private LogoutType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}
