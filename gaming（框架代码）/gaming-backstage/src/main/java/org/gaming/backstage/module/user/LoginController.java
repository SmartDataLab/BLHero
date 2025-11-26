/**
 * 
 */
package org.gaming.backstage.module.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaming.backstage.WebCode;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.menu.service.MenuService;
import org.gaming.backstage.module.menu.struct.UserMenuVo;
import org.gaming.backstage.module.user.auth.RoleToken;
import org.gaming.backstage.module.user.auth.Token;
import org.gaming.backstage.module.user.auth.UserType;
import org.gaming.backstage.module.user.event.UserLoginEvent;
import org.gaming.backstage.module.user.form.ChangePasswordForm;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.service.UserLogService;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author YY
 *
 */
@Controller
public class LoginController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserLogService userLogService;
	
	public static void main(String[] args) {
		System.out.println(MD5Util.getMD5("123"));		//202cb962ac59075b964b07152d234b70
		System.out.println(MD5Util.getMD5("123456"));	//e10adc3949ba59abbe56e057f20f883e
		System.out.println(MD5Util.getMD5("xgcd2023qazwsx"));//78174cf7e518c7c7ac7cf2d207493b81
	}
	//202cb962ac59075b964b07152d234b70
//	测试http://127.0.0.1:8080/login.gate?username=A&password=1234
	@ApiDocument("请求登录系统")
	@RequestMapping(value = "/login.gate")
	@ResponseBody
	public RoleToken loginDo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("username") String username, @RequestParam("password") String password) {
		logger.info("用户{}登录", username);
		Asserts.isTrue(username != null && password != null, WebCode.USERNAME_EMPTY);
		User user = userService.getByName(username);
		Asserts.isTrue(user != null, WebCode.USERNAME_EMPTY);
		String pwd = MD5Util.getMD5(password);
		Asserts.isTrue(user.getPassword().equals(pwd), WebCode.USER_PWD_ERROR);
		Asserts.isTrue(user.isUsable(), WebCode.USER_UN_USABLE);
		
		RoleContext userContext = new RoleContext();
		userContext.setRoleType(UserType.USER1);
		userContext.setId(user.getId());
		userContext.setName(user.getName());
		userContext.setToken(Token.gen(user.getId()));
		request.getSession().setAttribute("ROLECONTEXT", userContext);
		userService.addRoleContext(userContext);
		userLogService.addLog(userContext, request.getRequestURI(), GsonUtil.toJson(request.getParameterMap()));
		
		EventBus.post(UserLoginEvent.of(user.getId()));
		
		RoleToken roleToken = new RoleToken();
		roleToken.setToken(userContext.getToken());
		return roleToken;
	}
	
	@ApiDocument("获取用户可用菜单")
	@RequestMapping(value = "/menu.do")
	@ResponseBody
	public UserMenuVo menuJson() {
		RoleContext userContext = userService.getCurrUser();
		User user = userService.getById(userContext.getId());
		
		UserMenuVo vo = null;
		if(user.isSuperUser()) {
			vo = menuService.getAllMenus();
		} else {
			vo = menuService.getUserMenus(user.getId());
		}
		vo.setUsername(user.getName());
		return vo;
	}
	
	@ApiDocument("修改密码")
	@RequestMapping(value = "/changePasssword.do")
	@ResponseBody
	public void changePassword(ChangePasswordForm form) {
		RoleContext userContext = userService.getCurrUser();
		User user = userService.getById(userContext.getId());
		
		Asserts.isTrue(user.getPassword().equals(MD5Util.getMD5(form.getOldPassword())), WebCode.USER_OLDPWD_WRONG);
		Asserts.isTrue(form.getNewPassword() != null && !"".equals(form.getNewPassword().trim()), WebCode.USER_NEWPWD_EMPTY);
		Asserts.isTrue(form.getNewPassword().trim().equals(form.getConfirmPassword().trim()), WebCode.USER_CONFIRMPWD_NOT_MATCH);
		
		user.setPassword(MD5Util.getMD5(form.getNewPassword().trim()));
		userService.update(user);
	}
}
