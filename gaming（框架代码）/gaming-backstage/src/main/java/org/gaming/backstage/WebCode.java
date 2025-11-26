/**
 * 
 */
package org.gaming.backstage;

import org.gaming.backstage.advice.IResultTips;

/**
 * @author YY
 *
 */
public enum WebCode implements IResultTips {
	USER_NOT_FOUND(1001, "未找到该用户"),
	USER_PWD_ERROR(1002, "密码不正确"),
	USERNAME_EMPTY(1003, "用户名不能为空"),
	USER_LEVEL_LACK(1004, "你的用户级别不能对该用户进行操作"),
	USER_FUNCTION_NOT_EXIST(1005, "未找到该权限"),
	USER_FUNCTION_LACK(1006, "你没有该功能的分配权"),
	USER_CONTEXT_MISS(1007, "还没有完成登录验证"),
	USER_UN_USABLE(1008, "该用户已被停用"),
	USER_SUPER_DONT_GRANT(1009, "超级用户不需要进行功能授权"),
	USER_OLDPWD_WRONG(1010, "原密码不正确"),
	USER_NEWPWD_EMPTY(1011, "新密码不能为空"),
	USER_CONFIRMPWD_NOT_MATCH(1012, "确认密码跟新密码不一致"),
	
	MENU_SYSTEM_MISS(2001, "未找到ID为%s的系统菜单"),
	MENU_MODULE_MISS(2002, "未找到ID为%s的模块菜单"),
	MENU_FUNCTION_MISS(2003, "未找到ID为%s的功能菜单"),
	;
	
	private final int code;
	private final String message;
	
	private WebCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
