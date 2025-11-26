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
import org.gaming.backstage.module.menu.form.FunctionMenuForm;
import org.gaming.backstage.module.menu.model.FunctionMenu;
import org.gaming.backstage.module.menu.service.FunctionMenuService;
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
public class FunctionMenuController {

	@Autowired
	private FunctionMenuService functionMenuService;
	
	@ApiDocument("请求功能菜单数据")
	@RequestMapping(value = "/functionmenu/data.auth")
	@ResponseBody
	public PageData<FunctionMenu> functionMenuJson(PageQuery query) {
		List<FunctionMenu> list = functionMenuService.getEntities();
		SortUtil.sort(list, FunctionMenu::getId);
		
		PageData<FunctionMenu> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
		return pageData;
	}
	
	@ApiDocument("更新功能菜单数据")
	@RequestMapping(value = "/functionmenu/update.authw")
	@ResponseBody
	public FunctionMenu updateFunctionMenu(FunctionMenuForm form) {
		Asserts.isTrue(form.getId() != 0, WebCode.MENU_FUNCTION_MISS, form.getId());
		FunctionMenu currMenu = functionMenuService.getMenu(form.getId());
		Asserts.isTrue(currMenu != null, WebCode.MENU_FUNCTION_MISS, form.getId());
		
		currMenu.setIcon(form.getIcon());
		currMenu.setSort(form.getSort());
		functionMenuService.update(currMenu);
		return currMenu;
	}
}
