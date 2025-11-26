/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.recharge.model.RechargeVirtual;

/**
 * @author YY
 *
 */
@Service
public class RechargeVirtualService extends AbstractService<RechargeVirtual> {

	public PageData<RechargeVirtual> query(QuerySet querySet) {
		List<RechargeVirtual> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<RechargeVirtual> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
}
