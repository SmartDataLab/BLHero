/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.recharge.model.RechargeCallback;
import com.xiugou.x1.backstage.module.recharge.service.RechargeCallbackService;
import com.xiugou.x1.backstage.module.recharge.struct.RechargeQuery;

/**
 * @author YY
 *
 */
@Controller
public class RechargeCallbackController {
	
	@Autowired
	private RechargeCallbackService rechargeCallbackService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求充值订单数据")
	@RequestMapping(value = "/recharge/data.auth")
	@ResponseBody
	public PageData<RechargeCallback> data(RechargeQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", gameChannelService.currChannel());
		if(query.getPlayerId() > 0) {
			querySet.addCondition("player_id = ?", query.getPlayerId());
		}
		if(query.getNick() != null && !"".equals(query.getNick())) {
			querySet.addCondition("nick like ?", "%" + query.getNick() + "%");
		}
		if(query.getOpenId() != null && !"".equals(query.getOpenId())) {
			querySet.addCondition("open_id like ?", "%" + query.getOpenId() + "%");
		}
		if(query.getSdkOrderId() != null && !"".equals(query.getSdkOrderId())) {
			querySet.addCondition("sdk_order_id like ?", "%" + query.getSdkOrderId() + "%");
		}
		long startTime = query.getStartTime() * 1000L;
		if(startTime > 0) {
			String startStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime);
			querySet.addCondition("insert_time >= ?", startStr);
		}
		long endTime = query.getEndTime() * 1000L;
		if(endTime > 0) {
			endTime += DateTimeUtil.ONE_DAY_MILLIS;
			String endStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime);
			querySet.addCondition("insert_time <= ?", endStr);
		}
		if(query.getStatus() >= 0) {
			querySet.addCondition("give = ?", query.getStatus());
		}
		
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return rechargeCallbackService.query(querySet);
	}
	
	@ApiDocument("人工触发订单补发发货")
	@RequestMapping(value = "/recharge/manual.authw")
	@ResponseBody
	public void manual(@RequestParam("rechargeId") long rechargeId) {
		RechargeCallback rechargeCallback = rechargeCallbackService.getById(rechargeId);
		rechargeCallbackService.handleCallback(rechargeCallback);
	}
}
