/**
 * 
 */
package com.xiugou.x1.game.server.module.function.conditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.DungeonCache;
import com.xiugou.x1.design.module.autogen.DungeonAbstractCache.DungeonCfg;
import com.xiugou.x1.game.server.module.dungeon.model.Dungeon;
import com.xiugou.x1.game.server.module.dungeon.service.DungeonService;
import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.service.MainlineSceneService;

/**
 * @author YY
 *
 */
@Component
public class DungeonCondition extends FunctionCondition {

	@Autowired
	private DungeonService dungeonService;
	@Autowired
	private DungeonCache dungeonCache;
	@Autowired
	private MainlineSceneService mainlineSceneService;
	
	@Override
	protected OpenfunctionType getOpenfunctionType() {
		return OpenfunctionType.DUNGEON;
	}

	@Override
	public boolean functionOpenOrNot(long pid, int condition) {
		DungeonCfg dungeonCfg = dungeonCache.getOrThrow(condition);
		if(dungeonCfg.getType() == 1) {
			Dungeon dungeon = dungeonService.getEntity(pid);
			return dungeon.getNormalDungeons().contains(condition);
		} else if(dungeonCfg.getType() == 2) {
			MainlineScene mainlineScene = mainlineSceneService.getOrThrow(pid, dungeonCfg.getMainlineId());
			return mainlineScene.getDungeons().contains(condition);
		} else {
			return true;
		}
	}

}
