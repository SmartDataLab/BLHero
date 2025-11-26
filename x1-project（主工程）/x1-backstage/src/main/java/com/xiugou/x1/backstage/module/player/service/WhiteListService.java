/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.player.model.WhiteList;

/**
 * @author YY
 *
 */
@Service
public class WhiteListService extends AbstractService<WhiteList> {

	public PageData<WhiteList> query(QuerySet querySet) {
		List<WhiteList> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<WhiteList> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	//TODO 优化查询
	public WhiteList query(QueryOptions options) {
		return this.repository().get(options);
	}
}
