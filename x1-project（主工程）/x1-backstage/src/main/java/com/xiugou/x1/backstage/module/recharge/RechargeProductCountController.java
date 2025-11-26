/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge;

import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.recharge.service.RechargeProductCountService;
import com.xiugou.x1.backstage.module.recharge.struct.RechargeProductCountQuery;
import com.xiugou.x1.backstage.module.recharge.vo.RechargeProductCountVo;
import com.xiugou.x1.backstage.module.rechargeproduct.model.RechargeProductCfg;
import com.xiugou.x1.backstage.module.rechargeproduct.service.RechargeProductCfgService;

/**
 * @author YY
 *
 */
@Controller
public class RechargeProductCountController {

	@Autowired
	private RechargeProductCountService rechargeProductCountService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private RechargeProductCfgService rechargeProductCfgService;
	
	@ApiDocument("请求充值商品项目统计数据")
	@RequestMapping(value = "/rechargeProductCount/data.auth")
	@ResponseBody
	public PageData<RechargeProductCountVo> data(RechargeProductCountQuery query) {
		long channelId = gameChannelService.currChannel();
		
		long startTime = query.getStartTime() * 1000L;
		if(startTime <= 0) {
			startTime = DateTimeUtil.currMillis();
		}
		String startStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, startTime);
		long endTime = query.getEndTime() * 1000L;
		if(endTime <= 0) {
			endTime = DateTimeUtil.currMillis() + DateTimeUtil.ONE_DAY_MILLIS;
		}
		String endStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, endTime);
		
		List<RechargeProductCfg> cfgList = rechargeProductCfgService.getEntities();
		Map<Integer, RechargeProductCfg> cfgMap = ListMapUtil.listToMap(cfgList, RechargeProductCfg::getId);
		
		String sql = "SELECT product_id AS productId,count(distinct player_id) AS playerCount,count(1) AS buyCount,sum(pay_money) AS totalPay "
				+ "FROM recharge_product_count WHERE channel_id = ? AND insert_time >= ? AND insert_time <= ? GROUP BY product_id";
		List<RechargeProductCountVo> voList = rechargeProductCountService.query(RechargeProductCountVo.class, sql, channelId, startStr, endStr);
		
		long totalPay = 0;
		for(RechargeProductCountVo vo : voList) {
			totalPay += vo.getTotalPay();
		}
		for(RechargeProductCountVo vo : voList) {
			RechargeProductCfg cfg = cfgMap.get(vo.getProductId());
			if(cfg != null) {
				vo.setProductName(cfg.getName());
			}
			vo.setRate((int)(vo.getTotalPay() * 10000.0d / totalPay));
		}
		
		PageData<RechargeProductCountVo> pageData = new PageData<>();
		pageData.setCount(voList.size());
		pageData.setData(voList);
		return pageData;
	}
}
