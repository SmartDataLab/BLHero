/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge;

import java.time.LocalDateTime;

import org.gaming.db.usecase.SlimDao;
import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.Common;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.RechargeProductCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.difchannel.DifChannelManager;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.recharge.message.RechargeInternalMessage;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;
import com.xiugou.x1.game.server.module.recharge.service.RechargePlayerService;
import com.xiugou.x1.game.server.module.recharge.service.RechargeService;

import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeInfoResponse;
import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeOrderRequest;
import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeOrderResponse;
import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeVoucherOrderRequest;
import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeVoucherOrderResponse;
import pojo.xiugou.x1.pojo.log.player.PlayerRechargeLog;

/**
 * @author YY
 *
 */
@Controller
public class RechargeHandler extends AbstractModuleHandler {

	@Autowired
	private RechargeService rechargeService;
	@Autowired
	private RechargePlayerService rechargePlayerService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private RechargeProductCache rechargeProductCache;
	@Autowired
	private DifChannelManager difChannelManager;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public boolean needDailyPush() {
		return false;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		RechargePlayer entity = rechargePlayerService.getEntity(playerId);

		RechargeInfoResponse.Builder response = RechargeInfoResponse.newBuilder();
		response.addAllProductIds(entity.getBuyProducts());
		playerContextManager.push(playerId, RechargeInfoResponse.Proto.ID, response.build());
	}
	
	@PlayerCmd
	public RechargeOrderResponse order(PlayerContext playerContext, RechargeOrderRequest request) {
		rechargeService.checkOrdering(playerContext.getId(), request.getProductId(), request.getProductData());
		
		Recharge recharge = rechargeService.createOrder(playerContext.getId(), request.getProductId(),
				request.getProductData(), false, GameCause.RECHARGE);
        
        RechargeOrderResponse.Builder response = RechargeOrderResponse.newBuilder();
        response.setOrderId(recharge.getId());
        response.setProductId(recharge.getProductId());
        response.setExtraInfo(recharge.getExtraInfo());
        difChannelManager.onRecharge(recharge, request.getRechargeData(), response);
        return response.build();
	}
	
	@InternalCmd
	public void rechargeCallback(RechargeInternalMessage message) {
		rechargeService.orderSuccess(message.getRecharge());
		
		Player player = playerService.getEntity(message.getPlayerId());
		PlayerRechargeLog log = new PlayerRechargeLog();
		log.setPid(player.getId());
		log.setOpenId(player.getOpenId());
		log.setLevel(player.getLevel());
		log.setNick(player.getNick());
		log.setRechargeTime(LocalDateTime.now());
		log.setBornTime(player.getInsertTime());
		log.setChannelId(player.getCreateChannel());
		log.setMoney(message.getRecharge().getPayMoney());
		SlimDao.getRepository(PlayerRechargeLog.class).insert(log);
	}
	
	@PlayerCmd
	public RechargeVoucherOrderResponse voucherOrder(PlayerContext playerContext, RechargeVoucherOrderRequest request) {
		rechargeService.checkOrdering(playerContext.getId(), request.getProductId(), request.getProductData());
		
		RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrThrow(request.getProductId());
		thingService.cost(playerContext.getId(), CostThing.of(Common.VOUCHER, rechargeProductCfg.getMoney()), GameCause.RECHARGE_VOUCHER);
		Recharge recharge = rechargeService.createOrder(playerContext.getId(), request.getProductId(), request.getProductData(), true, GameCause.RECHARGE_VOUCHER);
		rechargeService.orderSuccess(recharge);
		
        return RechargeVoucherOrderResponse.getDefaultInstance();
	}


}
