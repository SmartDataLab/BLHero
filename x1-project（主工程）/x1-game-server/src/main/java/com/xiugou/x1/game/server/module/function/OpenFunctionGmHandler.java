/**
 * 
 */
package com.xiugou.x1.game.server.module.function;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.FunctionOpenCache;
import com.xiugou.x1.design.module.GuideCache;
import com.xiugou.x1.design.module.autogen.FunctionOpenAbstractCache.FunctionOpenCfg;
import com.xiugou.x1.design.module.autogen.GuideAbstractCache.GuideCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.function.model.OpenFunction;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionModuleService;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
import com.xiugou.x1.game.server.module.function.struct.Guide;

import pb.xiugou.x1.protobuf.openfunction.OpenFunction.OpenFunctionMessage;

/**
 * @author YY
 *
 */
@Controller
public class OpenFunctionGmHandler {
	
	@Autowired
    private OpenFunctionService openFunctionService;
	@Autowired
	private GuideCache guideCache;
	@Autowired
	private OpenFunctionHandler openFunctionHandler;
	@Autowired
	private FunctionOpenCache functionOpenCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	//移除某个已开启的功能，用于测试重登时的新功能开启
	@PlayerGmCmd(command = "FUNC_REMOVE")
    public void removeFunc(PlayerContext playerContext, String[] params) {
		Asserts.isTrue(params.length == 1, TipsCode.GM_PARAME_EMPTY);
		int functionId = Integer.parseInt(params[0]);
		
		OpenFunction openFunction = openFunctionService.getEntity(playerContext.getId());
		openFunction.getFunctionIds().remove(functionId);
		openFunctionService.update(openFunction);
    }
	
	@PlayerGmCmd(command = "FUNC_ALLOPEN")
    public void openAllFunc(PlayerContext playerContext, String[] params) {
		OpenFunction openFunction = openFunctionService.getEntity(playerContext.getId());
		List<Integer> newFunctions = new ArrayList<>();
		for(FunctionOpenCfg functionOpenCfg : functionOpenCache.all()) {
			boolean skip = false;
			for(Keyv keyv : functionOpenCfg.getOpenCondition()) {
				if(keyv.getId() == 1 && keyv.getValue() == 999) {
					skip = true;
					break;
				}
			}
			if(skip) {
				continue;
			}
			if(openFunction.getFunctionIds().contains(functionOpenCfg.getId())) {
				continue;
			}
			openFunction.getFunctionIds().add(functionOpenCfg.getId());
			newFunctions.add(functionOpenCfg.getId());
		}
		openFunctionService.update(openFunction);
		
		for(int functionId : newFunctions) {
			OpenFunctionModuleService.onFunctionOpen(playerContext.getId(), functionId);
			
			OpenFunctionMessage.Builder openFunctionMes = OpenFunctionMessage.newBuilder();
	        openFunctionMes.setOpenFunctionId(functionId);
	        playerContextManager.push(playerContext.getId(), OpenFunctionMessage.Proto.ID, openFunctionMes.build());
		}
    }
	
	//新手引导跳过
	@PlayerGmCmd(command = "GUIDE_SKIP")
	public void guideSkip(PlayerContext playerContext, String[] params) {
		OpenFunction openFunction = openFunctionService.getEntity(playerContext.getId());
		for(GuideCfg guideCfg : guideCache.all()) {
			Guide guide = openFunction.getGuides().get(guideCfg.getGroup());
			if(guide == null) {
				guide = new Guide();
				guide.setGroup(guideCfg.getGroup());
				openFunction.getGuides().put(guide.getGroup(), guide);
			}
			if(guideCfg.getSubId() > guide.getStep()) {
				guide.setStep(guideCfg.getSubId());
			}
		}
		openFunctionService.update(openFunction);
		
		openFunctionHandler.pushInfo(playerContext);
	}
}
