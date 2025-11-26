/**
 * 
 */
package com.xiugou.x1.game.server.module.goldenpig.service;

import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.function.constant.FunctionEnum;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionModuleService;
import com.xiugou.x1.game.server.module.goldenpig.model.GoldenPig;

/**
 * @author YY
 *
 */
@Service
public class GoldenPigFunctionOpenService extends OpenFunctionModuleService {

	@Autowired
	private GoldenPigService goldenPigService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private BattleTypeCache battleTypeCache;
	
	@Override
	public FunctionEnum functionEnum() {
		return FunctionEnum.GOLDEN_PIG;
	}

	@Override
	protected void onFunctionOpen(long playerId) {
		GoldenPig goldenPig = goldenPigService.getEntity(playerId);
		if(!goldenPig.isInitTicket()) {
			goldenPig.setInitTicket(true);
			goldenPigService.update(goldenPig);
			
			BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(BattleType.GOLDEN_PIG.getValue());
	        thingService.add(playerId, battleTypeCfg.getFreeTicket(), GameCause.GOLDENPIG_TICKET, NoticeType.TIPS);
		}
	}

}
