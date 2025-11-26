/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.backstage.service.AbstractService;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.recharge.model.RechargeToday;

/**
 * @author YY
 *
 */
@Service
public class RechargeTodayService extends AbstractService<RechargeToday> {

	@Autowired
	private RechargeOneDateService rechargeOneDateService;
	
	public void runInSchedule() {
		List<RechargeToday> todayList = this.repository().getAllInDb();
		Map<Long, List<RechargeToday>> channelTodayMap = ListMapUtil.fillListMap(todayList, RechargeToday::getChannelId);
		
		for(Entry<Long, List<RechargeToday>> entry : channelTodayMap.entrySet()) {
			rechargeOneDateService.updateRank(entry.getKey(), entry.getValue());
		}
		//删除已经统计的数据
		this.repository().deleteAll(todayList);
		logger.info("充值统计，已处理数据数量{}", todayList.size());
	}
}
