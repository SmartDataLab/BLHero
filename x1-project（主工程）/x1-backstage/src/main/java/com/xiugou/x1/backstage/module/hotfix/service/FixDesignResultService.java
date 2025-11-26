/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.hotfix.model.FixDesignResult;

/**
 * @author YY
 *
 */
@Service
public class FixDesignResultService extends AbstractService<FixDesignResult> {

	public PageData<FixDesignResult> query(QuerySet querySet) {
		List<FixDesignResult> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<FixDesignResult> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public List<FixDesignResult> queryByFixId(long fixId) {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("fix_id = ?", fixId);
		querySet.formWhere();
		
		return this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
	}
}
