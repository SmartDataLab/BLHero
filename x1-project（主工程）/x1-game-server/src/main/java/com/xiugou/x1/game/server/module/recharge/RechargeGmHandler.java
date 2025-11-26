/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeService;

/**
 * @author YY
 *
 */
@Controller
public class RechargeGmHandler {

	@Autowired
	private RechargeService rechargeService;
	
	@PlayerGmCmd(command = "GM_RECHARGE")
	public void gmRecharge(PlayerContext playerContext, String[] params) {
		int productId = Integer.parseInt(params[0]);
		String productData = params[1];
		
		rechargeService.checkOrdering(playerContext.getId(), productId, productData);
		Recharge recharge = rechargeService.createOrder(playerContext.getId(), productId, productData, true, GameCause.GM);
		rechargeService.orderSuccess(recharge);
	}
}
