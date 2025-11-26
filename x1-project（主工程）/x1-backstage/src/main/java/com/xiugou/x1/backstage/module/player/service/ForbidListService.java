/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.player.model.ForbidList;

/**
 * @author YY
 *
 */
@Service
public class ForbidListService extends AbstractService<ForbidList> {

	public PageData<ForbidList> query(QuerySet querySet) {
		List<ForbidList> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<ForbidList> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
}
