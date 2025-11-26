/**
 * 
 */
package com.xiugou.x1.game.server.module.function.service;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.game.server.module.function.constant.FunctionEnum;

/**
 * @author YY
 *
 */
public abstract class OpenFunctionModuleService {

	
	private static Map<Integer, OpenFunctionModuleService> moduleServices = new HashMap<>();
	
	public OpenFunctionModuleService() {
		moduleServices.put(functionEnum().getFunctionId(), this);
	}
	
	public static void onFunctionOpen(long playerId, int functionId) {
		OpenFunctionModuleService moduleService = moduleServices.get(functionId);
		if(moduleService == null) {
			return;
		}
		moduleService.onFunctionOpen(playerId);
	}
	
	
	protected abstract FunctionEnum functionEnum();
	
	protected abstract void onFunctionOpen(long playerId);
}
