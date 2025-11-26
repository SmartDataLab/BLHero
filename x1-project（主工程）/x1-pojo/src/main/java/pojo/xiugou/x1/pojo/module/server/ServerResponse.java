/**
 * 
 */
package pojo.xiugou.x1.pojo.module.server;

import org.gaming.tool.GsonUtil;

/**
 * @author YY
 *
 */
public class ServerResponse {
	public static final ServerResponse SUCCESES = new ServerResponse(ServerResponseCode.SUCCESS);

	private int code;
	private String message;
	private String data;

	public ServerResponse(ServerResponseCode responseCode) {
		this.code = responseCode.getValue();
		this.message = responseCode.getDesc();
	}

	public ServerResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String result() {
		return GsonUtil.toJson(this);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = GsonUtil.toJson(data);
	}
}
