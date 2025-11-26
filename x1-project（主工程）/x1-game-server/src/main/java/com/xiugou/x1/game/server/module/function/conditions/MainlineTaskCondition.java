/**
 * 
 */
package com.xiugou.x1.game.server.module.function.conditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.service.MainlinePlayerService;

/**
 * @author YY
 *
 */
@Component
public class MainlineTaskCondition extends FunctionCondition {

	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	
	@Override
	protected OpenfunctionType getOpenfunctionType() {
		return OpenfunctionType.MAINLINE_TASK;
	}

	@Override
	public boolean functionOpenOrNot(long pid, int condition) {
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(pid);
		return mainlinePlayer.getTask().getId() == condition || !mainlinePlayer.getParallelTasks().containsKey(condition);
	}

}
