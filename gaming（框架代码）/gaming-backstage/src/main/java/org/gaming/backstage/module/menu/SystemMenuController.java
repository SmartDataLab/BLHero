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
import org.gaming.backstage.module.menu.form.SystemMenuForm;
import org.gaming.backstage.module.menu.model.SystemMenu;
import org.gaming.backstage.module.menu.service.SystemMenuService;
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
public class SystemMenuController {

	@Autowired
	private SystemMenuService systemMenuService;
	
	@ApiDocument("请求系统菜单数据")
	@RequestMapping(value = "/systemmenu/data.auth")
	@ResponseBody
	public PageData<SystemMenu> systemMenuJson(PageQuery query) {
		List<SystemMenu> list = systemMenuService.getEntities();
		SortUtil.sort(list, SystemMenu::getId);
		
		PageData<SystemMenu> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
		return pageData;
	}
	
	@ApiDocument("更新系统菜单数据")
	@RequestMapping(value = "/systemmenu/update.authw")
	@ResponseBody
	public SystemMenu updateSystemMenu(SystemMenuForm form) {
		Asserts.isTrue(form.getId() != 0, WebCode.MENU_SYSTEM_MISS, form.getId());
		SystemMenu currMenu = systemMenuService.getMenu(form.getId());
		Asserts.isTrue(currMenu != null, WebCode.MENU_SYSTEM_MISS, form.getId());
		
		currMenu.setIcon(form.getIcon());
		currMenu.setSort(form.getSort());
		systemMenuService.update(currMenu);
		return currMenu;
	}
}
