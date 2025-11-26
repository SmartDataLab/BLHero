/**
 * 
 */
package org.gaming.backstage.module.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.backstage.PageData;
import org.gaming.backstage.WebCode;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.menu.model.FunctionMenu;
import org.gaming.backstage.module.menu.model.ModuleMenu;
import org.gaming.backstage.module.menu.service.FunctionMenuService;
import org.gaming.backstage.module.menu.service.ModuleMenuService;
import org.gaming.backstage.module.user.form.UserFunctionForm;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.model.UserFunction;
import org.gaming.backstage.module.user.service.UserFunctionService;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.backstage.module.user.struct.FunctionGranting;
import org.gaming.backstage.module.user.struct.UserFunctionQuery;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
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
public class UserFunctionController {

	@Autowired
	private UserFunctionService userFunctionService;
	@Autowired
	private UserService userService;
	@Autowired
	private FunctionMenuService functionMenuService;
	@Autowired
	private ModuleMenuService moduleMenuService;
	
	@ApiDocument("请求用户权限数据")
	@RequestMapping(value = "/userfunction/data.auth")
	@ResponseBody
	public PageData<UserFunction> data(UserFunctionQuery query) {
		int page = query.getPage();
		if(page == 0) {
			page = 1;
		}
		int limit = query.getLimit();
		if(limit == 0) {
			limit = 200;
		}
		QuerySet querySet = new QuerySet();
		if(query.getUserName() != null && !"".equals(query.getUserName())) {
			querySet.addCondition("user_name like ?", "%" + query.getUserName() + "%");
		}
		querySet.limit(page, limit);
		querySet.formWhere();
		
		PageData<UserFunction> pageData = userFunctionService.queryUserFunction(querySet);
		return pageData;
	}
	
	@ApiDocument("保存用户权限数据")
	@RequestMapping(value = "/userfunction/save.authw")
	@ResponseBody
	public PageData<FunctionGranting> save(UserFunctionForm form) {
		RoleContext roleContext = userService.getCurrUser();
		User currUser = userService.getById(roleContext.getId());
		User targetUser = userService.getById(form.getUserId());
		Asserts.isTrue(currUser.getLevel() < targetUser.getLevel(), WebCode.USER_LEVEL_LACK);
		Asserts.isTrue(!targetUser.isSuperUser(), WebCode.USER_SUPER_DONT_GRANT);
		
		List<UserFunction> targetFunctions = userFunctionService.getEntityList(targetUser.getId());
		Map<Long, UserFunction> targetFunctionMap = ListMapUtil.listToMap(targetFunctions, UserFunction::getFunctionId);
		
		List<UserFunction> insertList = new ArrayList<>();
		List<UserFunction> updateList = new ArrayList<>();
		Set<Long> functionIds = new HashSet<>();
		
		if(!currUser.isSuperUser()) {
			List<UserFunction> userFunctions = userFunctionService.getEntityList(currUser.getId());
			Map<Long, UserFunction> userFunctionMap = ListMapUtil.listToMap(userFunctions, UserFunction::getFunctionId);
			
			for(String functionAndWrite : form.getFunctionAndWrite()) {
				String[] parts = functionAndWrite.split("#");
				Long functionId = Long.parseLong(parts[0]);
				boolean writee = Boolean.parseBoolean(parts[1]);
				UserFunction userFunction = userFunctionMap.get(functionId);
				if(userFunction == null) {
					//当前用户没有的权限，不能授权给下级用户
					continue;
				}
				if(writee && !userFunction.isWritee()) {
					//当前用户没有写权限时，新授权的功能也不能是写权限
					writee = false;
				}
				if(functionIds.contains(functionId)) {
					continue;
				}
				FunctionMenu menu = functionMenuService.getMenu(functionId);
				if(menu == null) {
					continue;
				}
				functionIds.add(functionId);
				
				UserFunction targetFunction = targetFunctionMap.get(functionId);
				if(targetFunction == null) {
					targetFunction = new UserFunction();
					targetFunction.setUserId(targetUser.getId());
					targetFunction.setUserName(targetUser.getName());
					targetFunction.setFunctionId(menu.getId());
					targetFunction.setFunctionName(menu.getTitle());
					targetFunction.setGrantUserId(currUser.getId());
					targetFunction.setGrantUserName(currUser.getName());
					targetFunction.setWritee(writee);
					insertList.add(targetFunction);
				} else {
					if(targetFunction.isWritee() != writee) {
						targetFunction.setWritee(writee);
						updateList.add(targetFunction);
					}
				}
			}
		} else {
			for(String functionAndWrite : form.getFunctionAndWrite()) {
				String[] parts = functionAndWrite.split("#");
				Long functionId = Long.parseLong(parts[0]);
				boolean writee = Boolean.parseBoolean(parts[1]);
				if(functionIds.contains(functionId)) {
					continue;
				}
				FunctionMenu menu = functionMenuService.getMenu(functionId);
				if(menu == null) {
					continue;
				}
				functionIds.add(functionId);
				UserFunction targetFunction = targetFunctionMap.get(functionId);
				if(targetFunction == null) {
					targetFunction = new UserFunction();
					targetFunction.setUserId(targetUser.getId());
					targetFunction.setUserName(targetUser.getName());
					targetFunction.setFunctionId(menu.getId());
					targetFunction.setFunctionName(menu.getTitle());
					targetFunction.setGrantUserId(currUser.getId());
					targetFunction.setGrantUserName(currUser.getName());
					targetFunction.setWritee(writee);
					insertList.add(targetFunction);
				} else {
					if(targetFunction.isWritee() != writee) {
						targetFunction.setWritee(writee);
						updateList.add(targetFunction);
					}
				}
			}
		}
		//被移除的权限
		List<UserFunction> deleteList = new ArrayList<>();
		for(UserFunction targetFunction : targetFunctions) {
			if(functionIds.contains(targetFunction.getFunctionId())) {
				continue;
			}
			deleteList.add(targetFunction);
		}
		if(!deleteList.isEmpty()) {
			userFunctionService.deleteAll(deleteList);
		}
		if(!insertList.isEmpty()) {
			userFunctionService.insertAll(insertList);
		}
		if(!updateList.isEmpty()) {
			userFunctionService.updateAll(updateList);
		}
		return targetUserFunctionOption(roleContext.getId(), form.getUserId());
	}
	
	@ApiDocument("获取可授权的菜单数据")
	@RequestMapping(value = "/userfunction/options.auth")
	@ResponseBody
	public PageData<FunctionGranting> options(@RequestParam("userId") long userId) {
		RoleContext userContext = userService.getCurrUser();
		return targetUserFunctionOption(userContext.getId(), userId);
	}
	
	private PageData<FunctionGranting> targetUserFunctionOption(long userId, long targetUserId) {
		//当前用户
		User user = userService.getById(userId);
		User targetUser = userService.getById(targetUserId);
		//目标用户当前用的权限
		List<UserFunction> targetHavingFunctions = userFunctionService.getEntityList(targetUser.getId());
		Map<Long, UserFunction> targetHavingFunctionMap = ListMapUtil.listToMap(targetHavingFunctions, UserFunction::getFunctionId);
		
		Map<Long, ModuleMenu> moduleMenuMap = ListMapUtil.listToMap(moduleMenuService.getEntities(), ModuleMenu::getId);
		Map<Long, FunctionMenu> functionMenuMap = ListMapUtil.listToMap(functionMenuService.getEntities(), FunctionMenu::getId);
		
		PageData<FunctionGranting> pageData = new PageData<>();
		if(user.isSuperUser()) {
			List<FunctionMenu> menus = functionMenuService.getEntities();
			for(FunctionMenu menu : menus) {
				ModuleMenu moduleMenu = moduleMenuMap.get(menu.getModuleId());
				
				FunctionGranting granting = new FunctionGranting();
				granting.setId(menu.getId());
				granting.setName(menu.getTitle());
				granting.setModule(moduleMenu.getTitle());
				
				UserFunction havingFunction = targetHavingFunctionMap.get(menu.getId());
				if(havingFunction != null) {
					granting.setHas(true);
					granting.setWritee(havingFunction.isWritee());
				}
				pageData.getData().add(granting);
			}
		} else {
			//当前用户可以授权的菜单数据
			List<UserFunction> userFunctions = userFunctionService.getEntityList(user.getId());
			for(UserFunction userFunction : userFunctions) {
				FunctionMenu functionMenu = functionMenuMap.get(userFunction.getFunctionId());
				ModuleMenu moduleMenu = moduleMenuMap.get(functionMenu.getModuleId());
				
				FunctionGranting granting = new FunctionGranting();
				granting.setId(functionMenu.getId());
				granting.setName(functionMenu.getTitle());
				granting.setModule(moduleMenu.getTitle());
				
				UserFunction havingFunction = targetHavingFunctionMap.get(functionMenu.getId());
				if(havingFunction != null) {
					granting.setHas(true);
					granting.setWritee(havingFunction.isWritee());
				}
				pageData.getData().add(granting);
			}
		}
		pageData.setCount(pageData.getData().size());
		SortUtil.sort(pageData.getData(), FunctionGranting::getId);
		return pageData;
	}
}
