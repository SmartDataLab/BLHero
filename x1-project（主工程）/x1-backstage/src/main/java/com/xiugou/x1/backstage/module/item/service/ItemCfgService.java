/**
 * 
 */
package com.xiugou.x1.backstage.module.item.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.item.model.ItemCfg;

/**
 * @author YY
 *
 */
@Service
public class ItemCfgService extends AbstractService<ItemCfg> {

	public PageData<ItemCfg> query(QuerySet querySet) {
		List<ItemCfg> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<ItemCfg> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void deleteAll() {
		this.repository().deleteInDb("where 1=1");
	}
	
	public void insertAll(List<ItemCfg> list) {
		this.repository().insertAll(list);
	}
	
	public List<ItemCfg> getAll() {
		return this.repository().getAllInDb();
	}
}
