package com.xiugou.x1.game.server.module.function.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.FunctionOpenCache;
import com.xiugou.x1.design.module.autogen.FunctionOpenAbstractCache.FunctionOpenCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.function.conditions.FunctionCondition;
import com.xiugou.x1.game.server.module.function.constant.FunctionEnum;
import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import com.xiugou.x1.game.server.module.function.model.OpenFunction;

import pb.xiugou.x1.protobuf.openfunction.OpenFunction.OpenFunctionMessage;

/**
 * @author yh
 * @date 2023/6/7
 * @apiNote
 */
@Service
public class OpenFunctionService extends PlayerOneToOneService<OpenFunction> {
    @Autowired
    private FunctionOpenCache functionOpenCache;
    @Autowired
    private PlayerContextManager playerContextManager;
    @Autowired
    private ApplicationSettings applicationSettings;

    @Override
    protected OpenFunction createWhenNull(long entityId) {
        OpenFunction openFunction = new OpenFunction();
        openFunction.setPid(entityId);
        return openFunction;
    }

    /**
     * 判断单个功能是否开启
     */
    public boolean isFunctionEnabled(long entityId, FunctionEnum functionEnum) {
        OpenFunction openFunction = getEntity(entityId);
        return openFunction.getFunctionIds().contains(functionEnum.getFunctionId());
    }

    /**
     * @param playerId 玩家ID
     * @return 新解锁的功能
     */
    public List<Integer> checkAndGetNewFunctions(long playerId) {
    	OpenFunction openFunction = getEntity(playerId);
    	
        List<Integer> newFunctions = new ArrayList<>();
        Set<Integer> openFunctionIds = openFunction.getFunctionIds();
        for (FunctionOpenCfg functionOpenCfg : functionOpenCache.all()) {
            int functionId = functionOpenCfg.getId();
            if (openFunctionIds.contains(functionId)) {
                continue;
            }
            boolean open = true;
            for (Keyv keyv : functionOpenCfg.getOpenCondition()) {
                OpenfunctionType openfunctionType = OpenfunctionType.valueOf(keyv.getKey());
                FunctionCondition functionSystem = FunctionCondition.getFunctionSystem(openfunctionType);
                open = open && functionSystem.functionOpenOrNot(openFunction.getPid(), keyv.getValue());
                if (!open) {
                    break;
                }
            }
            if (open) {
                openFunctionIds.add(functionId);
                newFunctions.add(functionId);
            }
        }
        
        if (!newFunctions.isEmpty()) {
            this.update(openFunction);
            
            for(int functionId : newFunctions) {
            	OpenFunctionModuleService.onFunctionOpen(playerId, functionId);
            }
        }
        return newFunctions;
    }
    
    /**
     * 检查并推送新开启的功能
     * @param playerId
     */
    public void checkAndPushNewFunctions(long playerId) {
    	PlayerContext playerContext = playerContextManager.getContext(playerId);
    	if(playerContext == null) {
    		return;
    	}
    	List<Integer> newFunctions = checkAndGetNewFunctions(playerId);
    	newFunctions = filterFunctionByDevice(playerContext.getDeviceType(), newFunctions);
    	for(int functionId : newFunctions) {
    		OpenFunctionMessage.Builder builder = OpenFunctionMessage.newBuilder();
    		builder.setOpenFunctionId(functionId);
    		playerContextManager.push(playerId, OpenFunctionMessage.Proto.ID, builder.build());
    	}
    }
    
    /**
     * 过滤功能ID
     * @param deviceType
     * @param newFunctionIds
     * @return
     */
    public List<Integer> filterFunctionByDevice(String deviceType, List<Integer> newFunctionIds) {
    	if(applicationSettings.isGameArraignType() && "IOS".equals(deviceType)) {
    		List<Integer> filterList = new ArrayList<>();
    		for(int functionId : newFunctionIds) {
    			FunctionOpenCfg functionOpenCfg = functionOpenCache.getOrThrow(functionId);
    			if(functionOpenCfg.getHide() == 1) {
    				continue;
    			}
    			filterList.add(functionId);
    		}
    		return filterList;
    	} else {
    		return newFunctionIds;
    	}
    }
}
