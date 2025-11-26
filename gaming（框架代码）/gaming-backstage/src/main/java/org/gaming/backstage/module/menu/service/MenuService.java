/**
 * 
 */
package org.gaming.backstage.module.menu.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.backstage.module.menu.annotation.FunctionPage;
import org.gaming.backstage.module.menu.annotation.ModulePage;
import org.gaming.backstage.module.menu.annotation.SystemPage;
import org.gaming.backstage.module.menu.model.FunctionMenu;
import org.gaming.backstage.module.menu.model.ModuleMenu;
import org.gaming.backstage.module.menu.model.SystemMenu;
import org.gaming.backstage.module.menu.struct.UserMenuVo;
import org.gaming.backstage.module.user.model.UserFunction;
import org.gaming.backstage.module.user.service.UserAuthService;
import org.gaming.backstage.module.user.service.UserFunctionService;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.spring.Spring;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YY
 *
 */
@Service
public class MenuService implements Lifecycle {
	
	@Autowired
	private UserFunctionService userFunctionService;
	@Autowired
	private FunctionMenuService functionMenuService;
	@Autowired
	private SystemMenuService systemMenuService;
	@Autowired
	private ModuleMenuService moduleMenuService;

	@Override
	public void start() throws Exception {
		menuGenerate();
	}

	private void menuGenerate() {
		List<SystemMenu> systemMenus = new ArrayList<>();
		List<ModuleMenu> moduleMenus = new ArrayList<>();
		List<FunctionMenu> functionMenus = new ArrayList<>();

		Collection<Object> beans1 = Spring.getBeansWithAnnotation(Controller.class);
		Collection<Object> beans2 = Spring.getBeansWithAnnotation(RestController.class);

		Set<Object> beans = new HashSet<>();
		beans.addAll(beans1);
		beans.addAll(beans2);

		//构建菜单
		for (Object bean : beans) {
			SystemPage[] systemPages = bean.getClass().getAnnotationsByType(SystemPage.class);
			for (SystemPage systemPage : systemPages) {
				SystemMenu systemMenu = new SystemMenu();
				systemMenu.setId(systemPage.id());
				systemMenu.setTitle(systemPage.name());
				systemMenu.setSort(systemPage.sort());
				systemMenu.setIcon(systemPage.icon());
				systemMenu.setRouteName(systemPage.routeName());
				systemMenu.setRoutePath("/" + systemPage.routeName());
				systemMenu.setComponent(systemPage.routeComponent());
				systemMenus.add(systemMenu);
			}
			ModulePage[] modulePages = bean.getClass().getAnnotationsByType(ModulePage.class);
			for (ModulePage modulePage : modulePages) {
				ModuleMenu moduleMenu = new ModuleMenu();
				moduleMenu.setId(modulePage.id());
				moduleMenu.setTitle(modulePage.name());
				moduleMenu.setSystemId(modulePage.systemId());
				moduleMenu.setSort(modulePage.sort());
				moduleMenu.setIcon(modulePage.icon());
				moduleMenu.setRouteName(modulePage.routeName());
				moduleMenu.setRoutePath(modulePage.routeName());
				moduleMenu.setComponent(modulePage.routeComponent());
				moduleMenus.add(moduleMenu);
			}
			FunctionPage[] functionPages = bean.getClass().getAnnotationsByType(FunctionPage.class);
			for (FunctionPage functionPage : functionPages) {
				FunctionMenu functionMenu = new FunctionMenu();
				functionMenu.setModuleId(functionPage.moduleId());
				functionMenu.setTitle(functionPage.name());
				functionMenu.setSort(functionPage.sort());
				functionMenu.setIcon(functionPage.icon());
				functionMenu.setRouteName(functionPage.routeName());
				functionMenu.setRoutePath(functionPage.routeName());
				functionMenu.setAuthClazz(functionPage.authClass().getSimpleName());
				functionMenus.add(functionMenu);
			}
		}
	
		for (Object bean : beans) {
			// TODO 添加权限与页面的对应关系
			for (Method method : bean.getClass().getDeclaredMethods()) {
				RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
				if (requestMapping == null || requestMapping.value().length <= 0) {
					continue;
				}
				String authUrl = requestMapping.value()[0];
				UserAuthService.addAuth(authUrl, bean.getClass().getSimpleName());
			}
		}
		
		Map<Long, SystemMenu> currSystemMenus = ListMapUtil.listToMap(systemMenuService.getEntities(), SystemMenu::getId);
		for (SystemMenu systemMenu : systemMenus) {
			SystemMenu currMenu = currSystemMenus.get(systemMenu.getId());
			if (currMenu == null) {
				systemMenuService.insert(systemMenu);
			} else {
				if(!currMenu.sameWith(systemMenu)) {
					currMenu.setTitle(systemMenu.getTitle());
					currMenu.setRoutePath(systemMenu.getRoutePath());
					currMenu.setRouteName(systemMenu.getRouteName());
					currMenu.setComponent(systemMenu.getComponent());
					systemMenuService.update(currMenu);
				}
			}
		}
		Map<Long, ModuleMenu> currModuleMenus = ListMapUtil.listToMap(moduleMenuService.getEntities(), ModuleMenu::getId);
		for (ModuleMenu moduleMenu : moduleMenus) {
			ModuleMenu currMenu = currModuleMenus.get(moduleMenu.getId());
			if (currMenu == null) {
				moduleMenuService.insert(moduleMenu);
			} else {
				if(!currMenu.sameWith(moduleMenu)) {
					currMenu.setTitle(moduleMenu.getTitle());
					currMenu.setSystemId(moduleMenu.getSystemId());
					currMenu.setRoutePath(moduleMenu.getRoutePath());
					currMenu.setRouteName(moduleMenu.getRouteName());
					currMenu.setComponent(moduleMenu.getComponent());
					moduleMenuService.update(currMenu);
				}
			}
		}
		Map<String, FunctionMenu> currFunctionMenus = ListMapUtil.listToMap(functionMenuService.getEntities(), FunctionMenu::getTitle);
		for (FunctionMenu functionMenu : functionMenus) {
			FunctionMenu currMenu = currFunctionMenus.get(functionMenu.getTitle());
			if (currMenu == null) {
				functionMenuService.insert(functionMenu);
			} else {
				if(!currMenu.sameWith(functionMenu)) {
					currMenu.setTitle(functionMenu.getTitle());
					currMenu.setModuleId(functionMenu.getModuleId());
					currMenu.setRoutePath(functionMenu.getRoutePath());
					currMenu.setRouteName(functionMenu.getRouteName());
					currMenu.setAuthClazz(functionMenu.getAuthClazz());
					currMenu.setSort(functionMenu.getSort());
					functionMenuService.update(currMenu);
				}
			}
		}
	}

	public UserMenuVo getUserMenus(long userId) {
		List<UserFunction> userFunctions = userFunctionService.getEntityList(userId);

		Set<SystemMenu> systems = new HashSet<>();
		Set<ModuleMenu> modules = new HashSet<>();
		Set<FunctionMenu> functions = new HashSet<>();

		Map<Long, FunctionMenu> currFunctionMenus = ListMapUtil.listToMap(functionMenuService.getEntities(), FunctionMenu::getId);
		Map<Long, ModuleMenu> currModuleMenus = ListMapUtil.listToMap(moduleMenuService.getEntities(), ModuleMenu::getId);
		Map<Long, SystemMenu> currSystemMenus = ListMapUtil.listToMap(systemMenuService.getEntities(), SystemMenu::getId);
		
		for (UserFunction userFunction : userFunctions) {
			FunctionMenu functionMenu = currFunctionMenus.get(userFunction.getFunctionId());
			if (functionMenu == null) {
				continue;
			}
			ModuleMenu moduleMenu = currModuleMenus.get(functionMenu.getModuleId());
			if (moduleMenu == null) {
				continue;
			}
			SystemMenu systemMenu = currSystemMenus.get(moduleMenu.getSystemId());
			if (systemMenu == null) {
				continue;
			}
			systems.add(systemMenu);
			modules.add(moduleMenu);
			functions.add(functionMenu);
		}
		return new UserMenuVo(systems, modules, functions);
	}

	public UserMenuVo getAllMenus() {
		List<FunctionMenu> functionMenuList = functionMenuService.getEntities();
		SortUtil.sort(functionMenuList, FunctionMenu::getId);
		List<ModuleMenu> moduleMenuList = moduleMenuService.getEntities();
		SortUtil.sort(moduleMenuList, ModuleMenu::getId);
		List<SystemMenu> systemMenuList = systemMenuService.getEntities();
		SortUtil.sort(systemMenuList, SystemMenu::getId);
		return new UserMenuVo(systemMenuList, moduleMenuList, functionMenuList);
	}
}
