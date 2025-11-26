/**
 * 
 */
package org.gaming.backstage.module.apidoc;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.module.apidoc.model.ApiDoc;
import org.gaming.backstage.module.apidoc.service.ApiDocService;
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
public class ApiDocController {

	@Autowired
	private ApiDocService apiDocService;
	
	@RequestMapping(value = "/apidoc/data.auth")
	@ResponseBody
	public PageData<ApiDoc> data(PageQuery query) {
		List<ApiDoc> list = apiDocService.getAll();
		SortUtil.sortStr(list, ApiDoc::getHrefUrl);
		
		PageData<ApiDoc> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
		return pageData;
	}
}
