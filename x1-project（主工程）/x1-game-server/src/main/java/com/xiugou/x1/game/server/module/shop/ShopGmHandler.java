/**
 * 
 */
package com.xiugou.x1.game.server.module.shop;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.model.ShopSystem;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;

/**
 * @author YY
 *
 */
@Controller
public class ShopGmHandler {

	@Autowired
	private ShopHandler shopHandler;
	
	
	@PlayerGmCmd(command = "SHOP_RESET")
	public void shopReset(PlayerContext playerContext, String[] params) {
		Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);
		String shopId = params[0];
		
		ShopEnum shopEnum = ShopEnum.valueOf(Integer.parseInt(shopId));
		Asserts.isTrue(shopEnum != null, TipsCode.GM_PARAM_ERROR);
		
		AbstractShopService abstractShopService = AbstractShopService.getService(shopEnum);
		ShopSystem shopSystem = abstractShopService.getShopSystem();
		shopSystem.setNextReset(LocalDateTimeUtil.now());
		
		shopHandler.pushInfo(playerContext);
	}
}
