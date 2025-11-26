/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode.form;

/**
 * @author YY
 *
 */
public class GiftCodeForm {
	private int type;
	private String code;
	private int configId;
	private int num;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
