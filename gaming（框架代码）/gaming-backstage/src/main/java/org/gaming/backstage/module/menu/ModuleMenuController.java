/**
 * 
 */
package org.gaming.backstage.module.menu;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.WebCode;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.menu.form.ModuleMenuForm;
import org.gaming.backstage.module.menu.model.ModuleMenu;
import org.gaming.backstage.module.menu.service.ModuleMenuService;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author YY
 *
 */
@Controller
public class ModuleMenuController {

	@Autowired
	private ModuleMenuService moduleMenuService;
	
	@ApiDocument("请求模块菜单数据")
	@RequestMapping(value = "/modulemenu/data.auth")
	@ResponseBody
	public PageData<ModuleMenu> moduleMenuJson(PageQuery query) {
		List<ModuleMenu> list = moduleMenuService.getEntities();
		SortUtil.sort(list, ModuleMenu::getId);
		
		PageData<ModuleMenu> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
		return pageData;
	}
	
	@ApiDocument("更新模块菜单数据")
	@RequestMapping(value = "/modulemenu/update.authw")
	@ResponseBody
	public ModuleMenu updateModuleMenu(ModuleMenuForm form) {
		Asserts.isTrue(form.getId() != 0, WebCode.MENU_MODULE_MISS, form.getId());
		ModuleMenu currMenu = moduleMenuService.getMenu(form.getId());
		Asserts.isTrue(currMenu != null, WebCode.MENU_MODULE_MISS, form.getId());
		
		currMenu.setIcon(form.getIcon());
		currMenu.setSort(form.getSort());
		moduleMenuService.update(currMenu);
		return currMenu;
	}
}
