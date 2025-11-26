/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.mail.model.MailSystemResult;

/**
 * @author YY
 *
 */
@Service
public class MailSystemResultService extends AbstractService<MailSystemResult> {

	public PageData<MailSystemResult> query(QuerySet querySet) {
		List<MailSystemResult> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<MailSystemResult> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public MailSystemResult getById(long id) {
		return this.repository().getByMainKey(id);
	}
	
	public void insert(MailSystemResult result) {
		this.repository().insert(result);
	}
	
	public void update(MailSystemResult result) {
		this.repository().update(result);
	}
}
