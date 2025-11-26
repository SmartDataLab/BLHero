/**
 * 
 */
package pojo.xiugou.x1.pojo.module.server;

/**
 * @author YY
 *
 */
public enum ServerResponseCode {
	SUCCESS(0, "SUCCESS"),
	FAIL(1, "FAIL"),
	
	SERVER_OPENED(100001, "服务器已开，无法修改开服时间"),
	
	PLAYER_NOT_FOUND(200001, "未找到玩家"),
	PLAYER_NOT_THIS_SERVER(200002, "不是本服玩家"),
	
	MAIL_REPEAT(600001 ,"系统邮件重复发送"),
	
	RECHARGE_REPEAT(1900001, "订单重复"),
	
	
	
	;
	
	private final int value;
	private final String desc;
	private ServerResponseCode(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	public int getValue() {
		return value;
	}
	public String getDesc() {
		return desc;
	}
}
