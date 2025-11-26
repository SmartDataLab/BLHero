/**
 * 
 */
package org.gaming.backstage.module.user.auth;

import org.gaming.backstage.interceptor.IRoleType;
import org.gaming.backstage.interceptor.RequestInterceptor;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.menu.model.FunctionMenu;
import org.gaming.backstage.module.menu.service.FunctionMenuService;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.service.UserAuthService;
import org.gaming.backstage.module.user.service.UserFunctionService;
import org.gaming.backstage.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class UserInterceptor extends RequestInterceptor {

	@Autowired
	private UserService userService;
	@Autowired
	private UserFunctionService userFunctionService;
	@Autowired
	private FunctionMenuService functionMenuService;
	
	@Override
	public IRoleType roleType() {
		return UserType.USER1;
	}

	@Override
	public boolean beforeDo(RoleContext roleContext, String requestURI) {
		userService.setCurrUser(roleContext);
		return true;
	}

	@Override
	public boolean beforeAuth(RoleContext roleContext, String requestURI) {
		userService.setCurrUser(roleContext);
		User user = userService.getById(roleContext.getId());
		if(user.isSuperUser()) {
			return true;
		}
		//验证权限
		String authClazz = UserAuthService.getAuthClazzPage(requestURI);
		FunctionMenu functionMenu = functionMenuService.getByAuthClazz(authClazz);
		if(functionMenu == null) {
			return false;
		}
		return userFunctionService.hasAuth(roleContext.getId(), functionMenu.getId());
	}

	@Override
	public boolean beforeAuthw(RoleContext roleContext, String requestURI) {
		userService.setCurrUser(roleContext);
		User user = userService.getById(roleContext.getId());
		if(user.isSuperUser()) {
			return true;
		}
		String authClazz = UserAuthService.getAuthClazzPage(requestURI);
		FunctionMenu functionMenu = functionMenuService.getByAuthClazz(authClazz);
		if(functionMenu == null) {
			return false;
		}
		return userFunctionService.hasAuthw(roleContext.getId(), functionMenu.getId());
	}

	@Override
	public void afterDo(RoleContext roleContext) {
		userService.cleanCurrUser();
	}

	@Override
	public void afterAuth(RoleContext roleContext) {
		userService.cleanCurrUser();
	}

	@Override
	public void afterAuthw(RoleContext roleContext) {
		userService.cleanCurrUser();
	}
}
