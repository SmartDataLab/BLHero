/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1009zhigou.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ZhiGouLiBaoCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.module.autogen.ZhiGouLiBaoAbstractCache.ZhiGouLiBaoCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.model.ZhiGou;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.struct.ZhiGouData;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.struct.ZhiGouLimit;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbKeyV;
import pb.xiugou.x1.protobuf.promotion.P1009ZhiGou.ZhiGouBuyMessage;

/**
 * @author YY
 *
 */
@Service
public class ZhiGouRechargeService extends RechargeOrderingService {

	@Autowired
	private ZhiGouService zhiGouService;
	@Autowired
	private ZhiGouLiBaoCache zhiGouLiBaoCache;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public ProductType productType() {
		return ProductType.ZHI_GOU;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		ZhiGouData zhiGouData = GsonUtil.parseJson(productData, ZhiGouData.class);
        int giftId = zhiGouData.getGiftId();
		
        ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(giftId);
        Asserts.isTrue(zhiGouLiBaoCfg.getChargeProductId() == rechargeProductCfg.getId(), TipsCode.ERROR_PARAM);
		promotionControlService.assertRunning(applicationSettings.getGameServerId(), zhiGouLiBaoCfg.getActivityId());
		
		ZhiGou entity = zhiGouService.getEntity(playerId, zhiGouLiBaoCfg.getActivityId());
		ZhiGouLimit zhiGouLimit = entity.getLimitBuys().get(zhiGouData.getGiftId());
		if(zhiGouLimit != null) {
			Asserts.isTrue(zhiGouLimit.getNum() < zhiGouLiBaoCfg.getLimitNum(), TipsCode.ZG_BUY_LIMIT);
		}
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		ZhiGouData zhiGouData = GsonUtil.parseJson(recharge.getProductData(), ZhiGouData.class);
		ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(zhiGouData.getGiftId());
		
		ZhiGou entity = zhiGouService.getEntity(playerId, zhiGouLiBaoCfg.getActivityId());
		ZhiGouLimit zhiGouLimit = entity.getLimitBuys().get(zhiGouLiBaoCfg.getId());
		if(zhiGouLimit == null) {
			zhiGouLimit = new ZhiGouLimit();
			zhiGouLimit.setId(zhiGouLiBaoCfg.getId());
			entity.getLimitBuys().put(zhiGouLimit.getId(), zhiGouLimit);
		}
		zhiGouLimit.setNum(zhiGouLimit.getNum() + 1);
		zhiGouService.update(entity);
		
		outRewards.addAll(zhiGouLiBaoCfg.getDiffReward());
		
		ZhiGouBuyMessage.Builder message = ZhiGouBuyMessage.newBuilder();
		message.setTypeId(entity.getTypeId());
		PbKeyV.Builder data = PbKeyV.newBuilder();
		data.setKey(zhiGouLimit.getId());
		data.setValue(zhiGouLimit.getNum());
		message.setKeyvs(data.build());
		playerContextManager.push(playerId, ZhiGouBuyMessage.Proto.ID, message.build());
	}
}
