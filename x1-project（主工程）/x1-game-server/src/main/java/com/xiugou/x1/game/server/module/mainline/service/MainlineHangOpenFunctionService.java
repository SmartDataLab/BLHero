/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.function.constant.FunctionEnum;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionModuleService;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;

/**
 * @author YY
 *
 */
@Service
public class MainlineHangOpenFunctionService extends OpenFunctionModuleService {

	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	
	@Override
	protected FunctionEnum functionEnum() {
		return FunctionEnum.OFFLINE_BENEFITS;
	}

	@Override
	protected void onFunctionOpen(long playerId) {
		MainlinePlayer entity = mainlinePlayerService.getEntity(playerId);
		entity.setHangOpened(true);
		mainlinePlayerService.update(entity);
	}
}
