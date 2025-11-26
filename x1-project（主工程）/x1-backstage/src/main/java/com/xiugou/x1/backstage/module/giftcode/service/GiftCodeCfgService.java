/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.giftcode.model.GiftCodeCfg;

/**
 * @author YY
 *
 */
@Service
public class GiftCodeCfgService extends AbstractService<GiftCodeCfg> {

	public PageData<GiftCodeCfg> query(QuerySet querySet) {
		List<GiftCodeCfg> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<GiftCodeCfg> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void deleteAll() {
		this.repository().deleteInDb("where 1=1");
	}
	
	public void insertAll(List<GiftCodeCfg> list) {
		this.repository().insertAll(list);
	}
	
	public List<GiftCodeCfg> getAll() {
		return this.repository().getAllInDb();
	}
}
