/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.service;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.game.server.module.function.constant.FunctionEnum;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionModuleService;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.model.QiRiMuBiao;

/**
 * @author YY
 *
 */
@Service
public class QiRiMuBiaoOpenFunctionService extends OpenFunctionModuleService {

	@Autowired
	private QiRiMuBiaoService qiRiMuBiaoService;
	@Autowired
	private BattleConstCache battleConstCache;
	
	@Override
	protected FunctionEnum functionEnum() {
		return FunctionEnum.SEVEN_DAY_GOAL;
	}

	@Override
	protected void onFunctionOpen(long playerId) {
		QiRiMuBiao qiRiMuBiao = qiRiMuBiaoService.getEntity(playerId);
		if(qiRiMuBiao.isOpened()) {
			return;
		}
		qiRiMuBiao.setOpened(true);
		qiRiMuBiao.setEndTime(LocalDateTimeUtil.now().plusDays(battleConstCache.getQrmb_last_days()));
		qiRiMuBiaoService.update(qiRiMuBiao);
	}
}
