/**
 * 
 */
package org.gaming.backstage.module.menu.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.module.menu.model.FunctionMenu;
import org.gaming.backstage.module.menu.model.ModuleMenu;
import org.gaming.backstage.module.menu.model.SystemMenu;
import org.gaming.backstage.module.user.model.UserFunction;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;

/**
 * @author YY
 *
 */
public class UserMenuOption {
	private List<SystemMenuOption> options = new ArrayList<>();
	
	public UserMenuOption() {}
	
	public UserMenuOption(Collection<SystemMenu> systemMenus, Collection<ModuleMenu> moduleMenus,
			Collection<FunctionMenu> functions, Collection<UserFunction> userFunctions) {
		
		List<SystemMenu> systemList = new ArrayList<>(systemMenus);
		SortUtil.sort(systemList, SystemMenu::getSort);
		
		Map<Long, List<ModuleMenu>> systemModuleMap = ListMapUtil.fillListMap(new ArrayList<>(moduleMenus), ModuleMenu::getSystemId);
		Map<Long, List<FunctionMenu>> moduleFunctionMap = ListMapUtil.fillListMap(new ArrayList<>(functions), FunctionMenu::getModuleId);
		
		Map<Long, UserFunction> userFunctionMap = ListMapUtil.listToMap(new ArrayList<>(userFunctions), UserFunction::getFunctionId);
		
		for(SystemMenu systemMenu : systemList) {
			SystemMenuOption systemOption = new SystemMenuOption(systemMenu);
			options.add(systemOption);
			
			List<ModuleMenu> moduleList = systemModuleMap.get(systemMenu.getId());
			if(moduleList == null) {
				continue;
			}
			SortUtil.sort(moduleList, ModuleMenu::getSort);
			
			for(ModuleMenu moduleMenu : moduleList) {
				ModuleMenuOption moduleOption = new ModuleMenuOption(moduleMenu);
				systemOption.options.add(moduleOption);
				
				List<FunctionMenu> functionList = moduleFunctionMap.get(moduleMenu.getId());
				if(functionList == null) {
					continue;
				}
				SortUtil.sort(functionList, FunctionMenu::getSort);
				
				for(FunctionMenu functionMenu : functionList) {
					FunctionMenuOption functionOption = new FunctionMenuOption(functionMenu);
				
					UserFunction userFunction = userFunctionMap.get(functionMenu.getId());
					if(userFunction == null) {
						functionOption.has = false;
					} else {
						functionOption.has = true;
					}
					moduleOption.options.add(functionOption);
				}
			}
		}
	}
	
	public static class SystemMenuOption {
		private long id;
		private String name;
		private List<ModuleMenuOption> options = new ArrayList<>();
		public SystemMenuOption() {}
		public SystemMenuOption(SystemMenu menu) {
			this.id = menu.getId();
			this.name = menu.getTitle();
		}
		public long getId() {
			return id;
		}
		public String getName() {
			return name;
		}
	}
	
	public static class ModuleMenuOption {
		private long id;
		private String name;
		private List<FunctionMenuOption> options = new ArrayList<>();
		public ModuleMenuOption() {}
		public ModuleMenuOption(ModuleMenu menu) {
			this.id = menu.getId();
			this.name = menu.getTitle();
		}
		public long getId() {
			return id;
		}
		public String getName() {
			return name;
		}
	}
	
	public static class FunctionMenuOption {
		private long id;
		private String name;
		private boolean has;
		public FunctionMenuOption() {}
		public FunctionMenuOption(FunctionMenu menu) {
			this.id = menu.getId();
			this.name = menu.getTitle();
		}
		public long getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public boolean isHas() {
			return has;
		}
	}
	
}
