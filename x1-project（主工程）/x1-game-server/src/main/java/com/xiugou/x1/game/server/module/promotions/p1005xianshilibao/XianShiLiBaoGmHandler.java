/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.service.XianShiLiBaoService;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.strcut.XianShiLiBaoType;

/**
 * @author YY
 *
 */
@Controller
public class XianShiLiBaoGmHandler {

	@Autowired
	private XianShiLiBaoService xianShiLiBaoService;
	
	@PlayerGmCmd(command = "XIAN_SHI_LI_BAO")
	public void xianShiLiBao(PlayerContext playerContext, String[] params) {
		int type = Integer.parseInt(params[0]);
		XianShiLiBaoType xslbType = null;
		for(XianShiLiBaoType enumType : XianShiLiBaoType.values()) {
			if(enumType.getValue() == type) {
				xslbType = enumType;
				break;
			}
		}
		xianShiLiBaoService.checkGiftConditions(playerContext.getId(), xslbType);
	}
}
