/**
 * 
 */
package com.xiugou.x1.backstage.module.rechargeproduct.service;

import java.util.List;

import org.gaming.backstage.service.SystemOneToManyService;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.rechargeproduct.model.RechargeProductCfg;

/**
 * @author YY
 *
 */
@Service
public class RechargeProductCfgService extends SystemOneToManyService<RechargeProductCfg> {

	public void deleteAll() {
		this.repository().deleteInDb("where 1=1");
	}
	
	public List<RechargeProductCfg> getAll() {
		return this.repository().getAllInDb();
	}
}
