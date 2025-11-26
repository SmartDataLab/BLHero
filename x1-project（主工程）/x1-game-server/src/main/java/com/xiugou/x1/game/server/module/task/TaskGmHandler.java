/**
 * 
 */
package com.xiugou.x1.game.server.module.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.battle.event.KillMonsterEvent;

/**
 * @author YY
 *
 */
@Controller
public class TaskGmHandler {

	@PlayerGmCmd(command = "TEST_TASK")
	public void testTask(PlayerContext playerContext, String[] params) {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(7000001, 1);
		EventBus.post(KillMonsterEvent.of(playerContext.getId(), map, Collections.emptyList()));
	}
}
