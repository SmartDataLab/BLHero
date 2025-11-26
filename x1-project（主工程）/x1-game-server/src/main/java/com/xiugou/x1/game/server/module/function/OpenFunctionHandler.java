package com.xiugou.x1.game.server.module.function;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.module.GuideCache;
import com.xiugou.x1.design.module.autogen.GuideAbstractCache.GuideCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.function.log.GuideDotLogger;
import com.xiugou.x1.game.server.module.function.model.OpenFunction;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
import com.xiugou.x1.game.server.module.function.struct.Guide;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.openfunction.OpenFunction.FinishGuideRequest;
import pb.xiugou.x1.protobuf.openfunction.OpenFunction.FinishGuideResponse;
import pb.xiugou.x1.protobuf.openfunction.OpenFunction.OpenFunctionInfoResponse;
import pb.xiugou.x1.protobuf.openfunction.OpenFunction.PbGuide;

/**
 * @author yh
 * @date 2023/6/8
 * @apiNote
 */
@Controller
public class OpenFunctionHandler extends AbstractModuleHandler {

    @Autowired
    private OpenFunctionService openFunctionService;
    @Autowired
    private GuideCache guideCache;
    @Autowired
    private GuideDotLogger guideDotLogger;
    
    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.BASE;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
        OpenFunction openFunction = openFunctionService.getEntity(playerContext.getId());
        
        OpenFunctionInfoResponse.Builder response = OpenFunctionInfoResponse.newBuilder();
        //已开启的功能
        List<Integer> openedFunctions = new ArrayList<>(openFunction.getFunctionIds());
        openedFunctions = openFunctionService.filterFunctionByDevice(playerContext.getDeviceType(), openedFunctions);
        response.addAllOpenFunctionIds(openedFunctions);
        //新开启的功能
        List<Integer> newFunctions = openFunctionService.checkAndGetNewFunctions(playerContext.getId());
        newFunctions = openFunctionService.filterFunctionByDevice(playerContext.getDeviceType(), newFunctions);
        response.addAllNewOpenFunctionIds(newFunctions);
        
        for(Guide guide : openFunction.getGuides().values()) {
        	response.addGuides(build(guide));
        }
        playerContextManager.push(playerContext.getId(), OpenFunctionInfoResponse.Proto.ID, response.build());
    }
    
    @PlayerCmd
    public FinishGuideResponse finishGuide(PlayerContext playerContext, FinishGuideRequest request) {
    	//验证是否有该引导
    	GuideCfg guideCfg = guideCache.findInGroupSubIdIndex(request.getGroup(), request.getStep());
    	if(guideCfg == null) {
    		return FinishGuideResponse.getDefaultInstance();
    	}
    	
    	OpenFunction openFunction = openFunctionService.getEntity(playerContext.getId());
    	Guide guide = openFunction.getGuides().get(request.getGroup());
    	if(guide == null) {
    		guide = new Guide();
    		guide.setGroup(request.getGroup());
    		openFunction.getGuides().put(guide.getGroup(), guide);
    	}
    	if(request.getStep() > guide.getStep()) {
    		guide.setStep(request.getStep());
    		openFunctionService.update(openFunction);
    		guideDotLogger.addGuideDot(playerContext.getId(), request.getGroup(), request.getStep());
    	}
    	
    	FinishGuideResponse.Builder response = FinishGuideResponse.newBuilder();
    	response.setGuide(build(guide));
    	return response.build();
    }
    
    public PbGuide build(Guide guide) {
    	PbGuide.Builder builder = PbGuide.newBuilder();
    	builder.setGroup(guide.getGroup());
    	builder.setStep(guide.getStep());
    	return builder.build();
    }
}
