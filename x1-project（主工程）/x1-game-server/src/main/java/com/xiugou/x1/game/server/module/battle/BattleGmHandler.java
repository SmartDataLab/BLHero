/**
 * 
 */
package com.xiugou.x1.game.server.module.battle;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.IBattleProcessor;
import com.xiugou.x1.battle.sprite.Zone;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;

/**
 * @author YY
 *
 */
@Controller
public class BattleGmHandler {

	@PlayerGmCmd(command = "SCAN_BATTLE_SPRITE")
    public void scan(PlayerContext playerContext, String[] params) {
		BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        IBattleProcessor processor = context.getBattleProcessor();
        int monsterNum = 0;
        int harvestNum = 0;
        for(int i = 1; i <= 25; i++) {
        	Zone zone = processor.getZoneInfo(context, i);
        	monsterNum += zone.getSprites().size();
        	harvestNum += zone.getHarvests().size();
        }
        System.out.println("monsterNum " + monsterNum + " harvestNum " + harvestNum);
	}
}
