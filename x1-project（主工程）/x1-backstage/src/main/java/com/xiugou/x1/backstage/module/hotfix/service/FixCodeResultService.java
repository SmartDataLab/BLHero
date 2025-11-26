/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.hotfix.model.FixCodeResult;

/**
 * @author YY
 *
 */
@Service
public class FixCodeResultService extends AbstractService<FixCodeResult> {

	public PageData<FixCodeResult> query(QuerySet querySet) {
		List<FixCodeResult> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<FixCodeResult> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public List<FixCodeResult> queryByFixId(long fixId) {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("fix_id = ?", fixId);
		querySet.formWhere();
		
		return this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
	}
}
