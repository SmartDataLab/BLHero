/**
 * 
 */
package pojo.xiugou.x1.pojo.module.player.form;

import org.gaming.tool.CodeBuilder;

/**
 * @author YY
 *
 */
public enum LoginDotTiming {
	REQ_RES_URL_BEGIN(1, "根据版本号请求资源路径"),
    REQ_RES_URL_SUCESS(2, "根据版本号请求资源路径成功"),
    REQ_RES_URL_FAIL(3, "根据版本号请求资源路径成功"),
	
    SDK_LOGIN_BEGIN(4, "SDK开始登陆"),
    SDK_LOGIN_SUCESS(5, "SDK的登录成功"),
    SDK_LOGIN_FAIL(6, "SDK的登录失败"),
    REQ_SERVER_VERIFY_BEGIN(7, "请求服务器账号验证"),
    REQ_SERVER_VERIFY_SUCESS(8, "请求服务器账号验证成功"),
    REQ_SERVER_VERIFY_FAIL(9, "请求服务器账号验证失败"),

    REQ_SERVER_LIST_BEGIN(10, "请求服务器列表"),
    REQ_SERVER_LIST_SUCESS(11, "请求服务器列表成功"),
    REQ_SERVER_LIST_FAIL(12, "请求服务器列表失败"),
    START_GAME(13, "点击开始游戏"),
    REQ_SOCKET_CONNECT_SUCESS(14, "向服务器socket连接成功"),

    REQ_SOCKET_LOGIN_BEGIN(15, "向服务器发送登陆请求协议"),
    REQ_SOCKET_LOGIN_SUCESS(16, "向服务器发送登陆请求协议成功"),
    REQ_SOCKET_LOGIN_FAIL(17, "向服务器发送登陆请求协议失败"),
    
    CREATE_SUCC(18, "创号成功"),
    REQ_SERVER_PUSH_DATA_END(19, "服务器推送数据结束"),
    IN_GAME(20, "进入游戏"),
    
	;
	
	private final int value;
	private final String desc;
	private LoginDotTiming(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	public int getValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static void main(String[] args) {
//		genSetValue();
//		genFields();
		genVueFields();
	}
	
	protected static void genSetValue() {
		CodeBuilder builder = new CodeBuilder();
		boolean first = true;
		for(LoginDotTiming timing : LoginDotTiming.values()) {
			if(first) {
				builder.NFormat("if(vo.getTiming() == LoginDotTiming.%s.getValue()) {", timing.name());
			} else {
				builder.format(" else if(vo.getTiming() == LoginDotTiming.%s.getValue()) {", timing.name());
			}
			builder.NTFormat("statistic.set");
			String[] parts = timing.name().toLowerCase().split("_");
			for(int i = 0; i < parts.length; i++) {
				builder.append(CodeBuilder.upperFirst(parts[i]));
			}
			builder.append("((int)vo.getNum());");
			builder.NFormat("}");
			first = false;
		}
		System.out.println(builder.toString());
	}
	
	protected static void genFields() {
		CodeBuilder builder = new CodeBuilder();
		for(LoginDotTiming timing : LoginDotTiming.values()) {
			builder.NFormat("@Column(name = \"%s\", comment = \"%s\")", timing.name().toLowerCase(), timing.getDesc());
			
			builder.NFormat("private int ");
			String[] parts = timing.name().toLowerCase().split("_");
			for(int i = 0; i < parts.length; i++) {
				if(i == 0) {
					builder.append(parts[i]);
				} else {
					builder.append(CodeBuilder.upperFirst(parts[i]));
				}
			}
			builder.append(";");
		}
		System.out.println(builder.toString());
	}
	
	protected static void genVueFields() {
		CodeBuilder builder = new CodeBuilder();
		for(LoginDotTiming timing : LoginDotTiming.values()) {
			String fieldName = "";
			String[] parts = timing.name().toLowerCase().split("_");
			for(int i = 0; i < parts.length; i++) {
				if(i == 0) {
					fieldName += parts[i];
				} else {
					fieldName += CodeBuilder.upperFirst(parts[i]);
				}
			}
			builder.NFormat("<el-table-column prop=\"%s\" label=\"%s\" min-width=\"120\" />", fieldName, timing.getDesc());
		}
		System.out.println(builder.toString());
	}
}
