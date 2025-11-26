/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.RechargeProductCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToOneBaseService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;

import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeGivingMessage;
import pojo.xiugou.x1.pojo.module.recharge.RechargeExtra;

/**
 * @author YY
 *
 */
@Service
public class RechargeService extends OneToOneBaseService<Recharge> {
	
	private static Logger logger = LoggerFactory.getLogger(RechargeService.class);
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private RechargeProductCache rechargeProductCache;
	@Autowired
	private RechargePlayerService rechargePlayerService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private ApplicationSettings applicationSettings;
	
	
	private static Map<Integer, RechargeOrderingService> orderingServices = new HashMap<>();
	
	public static void register(RechargeOrderingService service) {
		orderingServices.put(service.productType().getValue(), service);
	}
	public static RechargeOrderingService getOrderService(ProductType productType) {
		return orderingServices.get(productType.getValue());
	}
	
	public void checkOrdering(long playerId, int productId, String productData) {
		RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(productId);
		RechargeOrderingService orderingService = orderingServices.get(rechargeProductCfg.getProductType());
		Asserts.isTrue(orderingService != null, TipsCode.RECHARGE_SERVICE_MISS, rechargeProductCfg.getId());
		orderingService.checkOrdering(playerId, rechargeProductCfg, productData);
	}
	
	/**
	 * 充值成功
	 */
	public void orderSuccess(Recharge recharge) {
		long playerId = recharge.getPlayerId();
		RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(recharge.getProductId());
		RechargeOrderingService orderingService = orderingServices.get(rechargeProductCfg.getProductType());
		
		RechargePlayer entity = rechargePlayerService.getEntity(playerId);
		
		//最终充值奖励=充值奖励+if（首充额外奖励）+贵族经验奖励+具体活动内设定奖励
		List<RewardThing> rewardThings = new ArrayList<>();
		rewardThings.addAll(rechargeProductCfg.getRewards());
		if(!entity.getBuyProducts().contains(rechargeProductCfg.getId())) {
			rewardThings.addAll(rechargeProductCfg.getFirstRewards());
		}
		if(rechargeProductCfg.getNoble().getThingId() > 0) {
			rewardThings.add(rechargeProductCfg.getNoble());
		}
		try {
			orderingService.buySuccess(playerId, recharge, rewardThings);

		} catch (Exception e) {
			String error = String.format("玩家%s购买商品%s订单%s时，充值逻辑%s发生异常", playerId, recharge.getProductId(),
					recharge.getId(), orderingService.getClass().getSimpleName());
            logger.error(error, e);
		}
		//记录充值信息
		rechargePlayerService.recordRecharge(playerId, recharge);
		for(RechargeOrderingService otherService : orderingServices.values()) {
			try {
                otherService.afterAnyBuySuccess(playerId, recharge);
            } catch (Exception e) {
				String error = String.format("充值逻辑%s触发的其它充值处理%s时发生异常", orderingService.getClass().getSimpleName(),
						otherService.getClass().getSimpleName());
				logger.error(error, e);
            }
		}
		//奖励发货
		if(!rewardThings.isEmpty()) {
			if(recharge.isVirtual()) {
				thingService.add(playerId, rewardThings, GameCause.RECHARGE_VIRTUAL);
			} else {
				thingService.add(playerId, rewardThings, GameCause.RECHARGE);
			}
		}
		//推送充值商品信息
		RechargeGivingMessage.Builder builder = RechargeGivingMessage.newBuilder();
		builder.setProductId(recharge.getProductId());
		playerContextManager.push(playerId, RechargeGivingMessage.Proto.ID, builder.build());
	}
	
	/**
	 * 
	 * @param playerId
	 * @param productId
	 * @param productData
	 * @param virtual 是否虚拟订单（GM订单）
	 * @return
	 */
	public Recharge createOrder(long playerId, int productId, String productData, boolean virtual, GameCause gameCause) {
		RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(productId);
		
		Player player = playerService.getEntity(playerId);
		
		Recharge recharge = null;
		while(true) {
			try {
				String timeStamp = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDDHHMMSSSSS, DateTimeUtil.currMillis());
				int randomNum = RandomUtil.closeClose(10000, 99999);
				long money = rechargeProductCfg.getMoney();
				recharge = new Recharge();
				recharge.setId("R" + timeStamp + "" + randomNum);
				recharge.setPlayerId(player.getId());
				recharge.setNick(player.getNick());
				recharge.setPlayerLv(player.getLevel());
				recharge.setServerId(player.getServerId());
				recharge.setProductId(rechargeProductCfg.getId());
				recharge.setProductName(rechargeProductCfg.getName());
				recharge.setProductCode(rechargeProductCfg.getProductCode());
				recharge.setPrePayMoney(money);
				recharge.setProductData(productData);
				recharge.setVirtual(virtual);
				recharge.setGameCause(gameCause.getCode());
				recharge.setGameCauseText(gameCause.getDesc());
				this.insert(recharge);
				break;
			} catch (Exception e) {
				//ID重复异常
			}
		}
		
		RechargeExtra rechargeExtra = new RechargeExtra();
		rechargeExtra.setOrderId(recharge.getId());
		rechargeExtra.setProductId(recharge.getProductId());
		rechargeExtra.setPlayerId(recharge.getPlayerId());
		rechargeExtra.setMoney(recharge.getPrePayMoney());
		rechargeExtra.setOpenId(player.getOpenId());
		rechargeExtra.setLevel(player.getLevel());
		rechargeExtra.setSign(rechargeExtra.buildSign(applicationSettings.getBackstageKey()));
		
        recharge.setExtraInfo(rechargeExtra.encode());
        this.update(recharge);
        return recharge;
	}
	
	@Deprecated
	@Override
	public Recharge getEntity(long entityId) {
		throw new UnsupportedOperationException("充值订单以字符串为ID");
	}
	
	public Recharge getOrder(String orderId) {
		return this.repository().getByMainKey(orderId);
	}
	
}
