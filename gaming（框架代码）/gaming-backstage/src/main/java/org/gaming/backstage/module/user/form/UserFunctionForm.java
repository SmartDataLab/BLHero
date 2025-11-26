/**
 * 
 */
package org.gaming.backstage.module.user.form;

import java.util.List;

/**
 * @author YY
 *
 */
public class UserFunctionForm {
	private long userId;
	private List<String> functionAndWrite;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public List<String> getFunctionAndWrite() {
		return functionAndWrite;
	}
	public void setFunctionAndWrite(List<String> functionAndWrite) {
		this.functionAndWrite = functionAndWrite;
	}
}
