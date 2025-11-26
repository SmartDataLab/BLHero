/**
 * 
 */
package com.xiugou.x1.game.server.module.player.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

import pb.xiugou.x1.protobuf.player.Player.PlayerInfoChangeMessage;

/**
 * @author YY
 *
 */
@Service
public class AdvPrivilegeOrderingService extends RechargeOrderingService {

	@Autowired
	private PlayerService playerService;
	
	@Override
	public ProductType productType() {
		return ProductType.ADV_PRIVILEGE;
	}

	@Override
	public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String productData) {
		Player player = playerService.getEntity(playerId);
		Asserts.isTrue(!player.isBuyPrivilege(), TipsCode.PLAYER_PRIVILEGE_BUY);
	}

	@Override
	public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
		Player player = playerService.getEntity(playerId);
		player.setBuyPrivilege(true);
		playerService.update(player);
		
		PlayerInfoChangeMessage.Builder message = PlayerInfoChangeMessage.newBuilder();
		message.setBuyPrivilege(player.isBuyPrivilege());
		playerContextManager.push(playerId, PlayerInfoChangeMessage.Proto.ID, message.build());
	}

}
