/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.api;

import java.time.LocalDateTime;

import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.game.server.foundation.player.PlayerActorPool;
import com.xiugou.x1.game.server.module.recharge.constant.RechargeStatus;
import com.xiugou.x1.game.server.module.recharge.message.RechargeInternalMessage;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class RechargeApi {

	private static Logger logger = LoggerFactory.getLogger(RechargeApi.class);
	
	@Autowired
	private RechargeService rechargeService;
	
	/**
	 * http://120.79.34.46:21001/rechargeApi/rechargeCallback?orderId=100000106&payMoney=100&test=true&backstageOrderId=100000010
	 * http://127.0.0.1:20004/rechargeApi/rechargeCallback?orderId=400000022&payMoney=100&test=true&backstageOrderId=100000010
	 * 充值回调
	 * @param orderId		字符串，订单ID
	 * @param payMoney 		长整形，实付金额
	 * @param test 			布尔，是否为测试订单
	 * @param backstageOrderId 字符串，后台充值订单ID
	 * @return
	 */
	@RequestMapping(value = GameApi.rechargeCallback)
	@ResponseBody
	public String rechargeCallback(@RequestParam("orderId") String orderId, @RequestParam("payMoney") long payMoney,
			@RequestParam("test") boolean test, @RequestParam("backstageOrderId") String backstageOrderId) {
		logger.info("收到充值回调，订单号{}，支付金额{}，是否测试订单{}，后台订单ID{}", orderId, payMoney, test, backstageOrderId);
		synchronized (("RECHARGE" + orderId).intern()) {
			Recharge recharge = null;
			try {
				recharge = rechargeService.getOrder(orderId);
				
				if (recharge.getStatus() == RechargeStatus.SUCCESS.getType()) {
                    logger.error("充值订单{}已发货", orderId);
                    return GsonUtil.toJson(new ServerResponse(ServerResponseCode.RECHARGE_REPEAT));
                }
				
				recharge.setBackstageOrderId(backstageOrderId);
				recharge.setTest(test);
				recharge.setPayMoney(payMoney);
				recharge.setGiveTime(LocalDateTime.now());
				recharge.setStatus(RechargeStatus.SUCCESS.getType());
				
				PlayerActorPool.tell(RechargeInternalMessage.of(recharge.getPlayerId(), recharge));
				
				return GsonUtil.toJson(ServerResponse.SUCCESES);
			} catch (Exception e) {
				logger.error(String.format("充值订单%s处理异常", orderId), e);
				return GsonUtil.toJson(new ServerResponse(ServerResponseCode.FAIL.getValue(), e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
			} finally {
				if(recharge != null) {
					rechargeService.update(recharge);
				}
			}
		}
	}
	
	/**
	 * 虚拟充值
	 * @param playerId
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = GameApi.virtualRecharge)
	@ResponseBody
	public String virtualRecharge(@RequestParam("playerId") long playerId, @RequestParam("productId") int productId) {
		
		rechargeService.checkOrdering(playerId, productId, "");
		Recharge recharge = rechargeService.createOrder(playerId, productId, "", true, GameCause.RECHARGE_VIRTUAL);
		rechargeService.orderSuccess(recharge);
		
		return GsonUtil.toJson(ServerResponse.SUCCESES);
	}
}
