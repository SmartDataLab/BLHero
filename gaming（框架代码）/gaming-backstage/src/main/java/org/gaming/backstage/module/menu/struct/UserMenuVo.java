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
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;

/**
 * @author YY
 *
 */
public class UserMenuVo {
	private String username;
	private List<SystemMenuVo> systemMenus = new ArrayList<>();
	
	public UserMenuVo() {}
	
	public UserMenuVo(Collection<SystemMenu> systemMenus, Collection<ModuleMenu> moduleMenus,
			Collection<FunctionMenu> functions) {
		
		List<SystemMenu> systemList = new ArrayList<>(systemMenus);
		SortUtil.sort(systemList, SystemMenu::getSort);
		
		Map<Long, List<ModuleMenu>> systemModuleMap = ListMapUtil.fillListMap(new ArrayList<>(moduleMenus), ModuleMenu::getSystemId);
		Map<Long, List<FunctionMenu>> moduleFunctionMap = ListMapUtil.fillListMap(new ArrayList<>(functions), FunctionMenu::getModuleId);
		
		for(SystemMenu systemMenu : systemList) {
			SystemMenuVo systemMenuVo = new SystemMenuVo(systemMenu);
			this.systemMenus.add(systemMenuVo);
			List<ModuleMenu> moduleList = systemModuleMap.get(systemMenu.getId());
			if(moduleList == null) {
				continue;
			}
			SortUtil.sort(moduleList, ModuleMenu::getSort);
			
			for(ModuleMenu moduleMenu : moduleList) {
				ModuleMenuVo moduleMenuVo = new ModuleMenuVo(moduleMenu);
				systemMenuVo.getChildren().add(moduleMenuVo);
				List<FunctionMenu> functionList = moduleFunctionMap.get(moduleMenu.getId());
				if(functionList == null) {
					continue;
				}
				SortUtil.sort(functionList, FunctionMenu::getSort);
				
				for(FunctionMenu functionMenu : functionList) {
					MenuVo menuVo = new MenuVo();
					menuVo.setName(functionMenu.getRouteName() == null ? "" : functionMenu.getRouteName());
					menuVo.setPath(functionMenu.getRoutePath() == null ? "" : functionMenu.getRoutePath());
					Meta meta = new Meta();
					meta.title = functionMenu.getTitle();
					meta.icon = functionMenu.getIcon() == null ? "" : functionMenu.getIcon();
					menuVo.setMeta(meta);
					menuVo.setComponent(systemMenu.getRouteName() + "/" + moduleMenu.getRouteName() + "/" + functionMenu.getRouteName());
					moduleMenuVo.getChildren().add(menuVo);
				}
			}
		}
	}
	
	public static class SystemMenuVo extends MenuVo {
		private List<ModuleMenuVo> children = new ArrayList<>();
		
		public SystemMenuVo() {}
		public SystemMenuVo(SystemMenu systemMenu) {
			this.name = systemMenu.getRouteName() == null ? "" : systemMenu.getRouteName();
			this.path = systemMenu.getRoutePath() == null ? "" : systemMenu.getRoutePath();
			this.meta = new Meta();
			this.meta.title = systemMenu.getTitle();
			this.meta.icon = systemMenu.getIcon() == null ? "" : systemMenu.getIcon();
			this.component = systemMenu.getComponent() == null ? "" : systemMenu.getComponent();
		}
		public List<ModuleMenuVo> getChildren() {
			return children;
		}
		public void setChildren(List<ModuleMenuVo> children) {
			this.children = children;
		}
	}
	
	public static class ModuleMenuVo extends MenuVo {
		private boolean alwaysShow = true;
		private List<MenuVo> children = new ArrayList<>();
		
		public ModuleMenuVo() {}
		public ModuleMenuVo(ModuleMenu moduleMenu) {
			this.name = moduleMenu.getRouteName() == null ? "" : moduleMenu.getRouteName();
			this.path = moduleMenu.getRoutePath() == null ? "" : moduleMenu.getRoutePath();
			this.meta = new Meta();
			this.meta.title = moduleMenu.getTitle();
			this.meta.icon = moduleMenu.getIcon() == null ? "" : moduleMenu.getIcon();
			this.component = moduleMenu.getComponent() == null ? "" : moduleMenu.getComponent();
		}
		public List<MenuVo> getChildren() {
			return children;
		}
		public void setChildren(List<MenuVo> children) {
			this.children = children;
		}
		public boolean isAlwaysShow() {
			return alwaysShow;
		}
		public void setAlwaysShow(boolean alwaysShow) {
			this.alwaysShow = alwaysShow;
		}
	}
	
	public static class MenuVo {
		protected String name;
		protected String path;
		protected Meta meta;
		protected String component;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public Meta getMeta() {
			return meta;
		}
		public void setMeta(Meta meta) {
			this.meta = meta;
		}
		public String getComponent() {
			return component;
		}
		public void setComponent(String component) {
			this.component = component;
		}
	}
	
	public static class Meta {
		private String title;
		private String icon;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<SystemMenuVo> getSystemMenus() {
		return systemMenus;
	}

	public void setSystemMenus(List<SystemMenuVo> systemMenus) {
		this.systemMenus = systemMenus;
	}
}
