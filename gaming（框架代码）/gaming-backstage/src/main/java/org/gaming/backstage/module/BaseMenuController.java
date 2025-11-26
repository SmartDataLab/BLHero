/**
 * 
 */
package org.gaming.backstage.module;

import org.gaming.backstage.module.apidoc.ApiDocController;
import org.gaming.backstage.module.menu.FunctionMenuController;
import org.gaming.backstage.module.menu.ModuleMenuController;
import org.gaming.backstage.module.menu.SystemMenuController;
import org.gaming.backstage.module.menu.annotation.FunctionPage;
import org.gaming.backstage.module.menu.annotation.ModulePage;
import org.gaming.backstage.module.menu.annotation.SystemPage;
import org.gaming.backstage.module.user.UserController;
import org.gaming.backstage.module.user.UserFunctionController;
import org.gaming.backstage.module.user.UserLogController;
import org.springframework.stereotype.Controller;

/**
 * @author YY
 *
 */
@SystemPage(id = 1, name = "系统设置", sort = 1, routeName = "systemSettings", routeComponent = "Layout")
@ModulePage(id = 1, name = "菜单设置", systemId = 1, routeName = "menuSettings", routeComponent = "layout/secondaryLayout", sort = 1)
	@FunctionPage(moduleId = 1, name = "系统菜单", routeName = "systemMenu", sort = 1, authClass = SystemMenuController.class)
	@FunctionPage(moduleId = 1, name = "模块菜单", routeName = "moduleMenu", sort = 2, authClass = ModuleMenuController.class)
	@FunctionPage(moduleId = 1, name = "功能菜单", routeName = "functionMenu", sort = 3, authClass = FunctionMenuController.class)
@ModulePage(id = 2, name = "用户设置", systemId = 1, routeName = "userSettings", routeComponent = "layout/secondaryLayout", sort = 2)
	@FunctionPage(moduleId = 2, name = "用户管理", routeName = "user", sort = 1, authClass = UserController.class)
	@FunctionPage(moduleId = 2, name = "用户权限管理", routeName = "userFunction", sort = 2, authClass = UserFunctionController.class)
	@FunctionPage(moduleId = 2, name = "用户操作日志", routeName = "userLog", sort = 3, authClass = UserLogController.class)
@ModulePage(id = 3, name = "接口说明", systemId = 1, routeName = "apiSettings", routeComponent = "layout/secondaryLayout", sort = 3)
	@FunctionPage(moduleId = 3, name = "接口说明", routeName = "apidoc", sort = 1, authClass = ApiDocController.class)
	@FunctionPage(moduleId = 3, name = "空白主页", routeName = "home", sort = 1, authClass = ApiDocController.class)
@Controller
public class BaseMenuController {

}
