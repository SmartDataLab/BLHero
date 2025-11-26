/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.recharge.model.RechargeCallback;
import com.xiugou.x1.backstage.module.recharge.model.RechargeProductCount;
import com.xiugou.x1.backstage.module.recharge.model.RechargeToday;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Service
public class RechargeCallbackService extends AbstractService<RechargeCallback> {

	@Autowired
	private RechargeTodayService rechargeTodayService;
	@Autowired
	private RechargeProductCountService rechargeProductCountService;
	@Autowired
	private GameServerService gameServerService;
	
	public PageData<RechargeCallback> query(QuerySet querySet) {
		List<RechargeCallback> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<RechargeCallback> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	/**
	 * 处理充值发货
	 * @param recharge
	 * @return
	 */
	public RechargeStatus handleCallback(RechargeCallback recharge) {
		if(!recharge.isAllCheck()) {
			return RechargeStatus.NOT_CHECK;
		}
		RechargeStatus rechargeStatus = RechargeStatus.NONE;
		GameServer gameServer = null;
		try {
			//通知游戏服进行发货
			gameServer = gameServerService.getByPlatformAndServer(recharge.getPlatformId(), recharge.getServerId());
			Map<String, Object> map = new HashMap<>();
			map.put("orderId", recharge.getGameOrderId());
			map.put("payMoney", recharge.getMoney());
			map.put("test", recharge.getTest());
			map.put("backstageOrderId", recharge.getId());
			
			logger.info("充值通知发货参数{}", GsonUtil.toJson(map));
			logger.info("充值通知发货服务器{}，{}，{}，{}，{}", gameServer.getId(), gameServer.getServerId(), gameServer.getName(), gameServer.getInternalIp(), gameServer.getHttpPort());
			ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.rechargeCallback, map);
			logger.info("充值通知发货响应{}", GsonUtil.toJson(response));
			if(response == null) {
				recharge.setRemark("服务器未响应");
				rechargeStatus = RechargeStatus.FAIL;
			} else {
				recharge.setGameResponse(GsonUtil.toJson(response));
				if(response.getCode() == 0) {
					//发货成功
					recharge.setGive(1);
					recharge.setRemark("发货成功");
					rechargeStatus = RechargeStatus.SUCCESS;
				} else if(response.getCode() == 1900001) {
					recharge.setGive(2);
					recharge.setRemark(response.getCode() + "-" + response.getMessage());
					rechargeStatus = RechargeStatus.REPEATED;
				} else {
					recharge.setGive(2);
					recharge.setRemark(response.getCode() + "-" + response.getMessage());
					rechargeStatus = RechargeStatus.FAIL;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			rechargeStatus = RechargeStatus.FAIL;
		} finally {
			this.update(recharge);
		}
		
		if(recharge.getGive() == 1) {
			//成功发货才记录商品购买
			RechargeToday rechargeToday = new RechargeToday();
			rechargeToday.setChannelId(recharge.getChannelId());
			rechargeToday.setPlayerId(recharge.getPlayerId());
			rechargeToday.setMoney(recharge.getMoney());
			rechargeToday.setProductId(recharge.getProductId());
			rechargeToday.setProductName(recharge.getProductName());
			rechargeTodayService.insert(rechargeToday);
			
			RechargeProductCount rechargeProductCount = new RechargeProductCount();
			rechargeProductCount.setChannelId(recharge.getChannelId());
			rechargeProductCount.setPayMoney(recharge.getMoney());
			rechargeProductCount.setProductId(recharge.getProductId());
			rechargeProductCount.setProductName(recharge.getProductName());
			rechargeProductCount.setPlayerId(recharge.getPlayerId());
			rechargeProductCount.setCallbackId(recharge.getId());
			rechargeProductCount.setServerUid(gameServer.getId());
			rechargeProductCountService.insert(rechargeProductCount);
		} else {
			//未能正常发货需要走人工补单流程
		}
		return rechargeStatus;
	}
}
