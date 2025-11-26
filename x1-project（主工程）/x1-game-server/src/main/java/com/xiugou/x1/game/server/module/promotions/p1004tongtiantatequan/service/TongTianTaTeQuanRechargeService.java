/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.TongTianTaTeQuanProductCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.module.autogen.TongTianTaTeQuanProductAbstractCache.TongTianTaTeQuanProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.model.TongTianTaTeQuan;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.struct.TTTTQDetail;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.promotion.P1004TongTianTaTeQuan.TTTTQRechargeMessage;

/**
 * @author YY
 *
 */
@Service
public class TongTianTaTeQuanRechargeService extends RechargeOrderingService {

	@Autowired
	private TongTianTaTeQuanService tongTianTaTeQuanService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private TongTianTaTeQuanProductCache tongTianTaTeQuanProductCache;
	
	@Override
	public ProductType productType() {
		return ProductType.TONG_TIAN_TA_TE_QUAN;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		Player player = playerService.getEntity(playerId);
		
		TongTianTaTeQuanProductCfg tongTianTaTeQuanProductCfg = tongTianTaTeQuanProductCache.getOrThrow(rechargeProductCfg.getId());
		Asserts.isTrue(player.getVipLevel() >= tongTianTaTeQuanProductCfg.getVipLevel(), TipsCode.TTTTQ_VIP_LACK);
		
		TongTianTaTeQuan entity = tongTianTaTeQuanService.getEntity(playerId);
		TTTTQDetail detail = entity.getTowerDetails().get(tongTianTaTeQuanProductCfg.getTowerType());
		Asserts.isTrue(detail.getRound() == tongTianTaTeQuanProductCfg.getRound(), TipsCode.TTTTQ_ROUND_LOCK);
		Asserts.isTrue(!detail.isOpenHigh(), TipsCode.TTTTQ_BOUGHT);
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		TongTianTaTeQuanProductCfg tongTianTaTeQuanProductCfg = tongTianTaTeQuanProductCache.getOrThrow(recharge.getProductId());
		
		TongTianTaTeQuan entity = tongTianTaTeQuanService.getEntity(playerId);
		TTTTQDetail detail = entity.getTowerDetails().get(tongTianTaTeQuanProductCfg.getTowerType());
		detail.setOpenHigh(true);
		tongTianTaTeQuanService.update(entity);
		
		TTTTQRechargeMessage.Builder message = TTTTQRechargeMessage.newBuilder();
		message.setDetail(detail.build());
		playerContextManager.push(playerId, TTTTQRechargeMessage.Proto.ID, message.build());
	}
}
