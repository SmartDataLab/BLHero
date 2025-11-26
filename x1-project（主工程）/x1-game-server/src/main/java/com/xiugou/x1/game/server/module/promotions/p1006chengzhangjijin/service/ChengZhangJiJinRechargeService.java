/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.model.ChengZhangJiJin;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.promotion.P1006ChengZhangJiJin.CZJJRechargeMessage;

/**
 * @author YY
 *
 */
@Service
public class ChengZhangJiJinRechargeService extends RechargeOrderingService {

	@Autowired
	private ChengZhangJiJinService chengZhangJiJinService;
	
	@Override
	public ProductType productType() {
		return ProductType.CHENG_ZHANG_JI_JIN;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		Integer round = Integer.parseInt(rechargeProductCfg.getProductParam());
		
		ChengZhangJiJin entity = chengZhangJiJinService.getEntity(playerId);
		Asserts.isTrue(!entity.isOpen(), TipsCode.CZJJ_BOUGHT);
		Asserts.isTrue(entity.getRound() == round, TipsCode.CZJJ_ROUND_LOCK);
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		ChengZhangJiJin entity = chengZhangJiJinService.getEntity(playerId);
		entity.setOpen(true);
		chengZhangJiJinService.update(entity);
		
		CZJJRechargeMessage.Builder message = CZJJRechargeMessage.newBuilder();
		message.setOpen(entity.isOpen());
		playerContextManager.push(playerId, CZJJRechargeMessage.Proto.ID, message.build());
	}
}
