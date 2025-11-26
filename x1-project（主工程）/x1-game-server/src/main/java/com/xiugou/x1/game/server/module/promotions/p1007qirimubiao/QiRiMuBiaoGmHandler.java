/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1007qirimubiao;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.model.QiRiMuBiao;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.service.QiRiMuBiaoService;

/**
 * @author YY
 *
 */
@Controller
public class QiRiMuBiaoGmHandler {

	@Autowired
	private QiRiMuBiaoService qiRiMuBiaoService;
	@Autowired
	private QiRiMuBiaoHandler qiRiMuBiaoHandler;
	
	@PlayerGmCmd(command = "QRMB_NEXT_DAY")
	public void nextDay(PlayerContext playerContext, String[] params) {
		QiRiMuBiao entity = qiRiMuBiaoService.getEntity(playerContext.getId());
		entity.setDay(entity.getDay() + 1);
		qiRiMuBiaoService.update(entity);
		
		qiRiMuBiaoHandler.pushInfo(playerContext);
	}
}
