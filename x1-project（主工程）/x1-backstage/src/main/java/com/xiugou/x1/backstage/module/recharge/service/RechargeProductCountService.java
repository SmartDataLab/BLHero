/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;

import java.util.List;

import org.gaming.backstage.service.AbstractService;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.recharge.model.RechargeProductCount;

/**
 * @author YY
 *
 */
@Service
public class RechargeProductCountService extends AbstractService<RechargeProductCount> {

	public <T> List<T> query(Class<T> clazz, String sql, Object... params) {
		return this.repository().getBaseDao().queryAliasObjects(clazz, sql, params);
	}
}
