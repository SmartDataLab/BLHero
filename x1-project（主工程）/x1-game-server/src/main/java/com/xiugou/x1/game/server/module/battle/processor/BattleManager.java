/**
 *
 */
package com.xiugou.x1.game.server.module.battle.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xiugou.x1.battle.IBattleType;

/**
 * @author YY
 *
 */
public class BattleManager {

    private static Map<IBattleType, BaseBattleProcessor<?>> battleProcessorMap = new ConcurrentHashMap<>();

    public static BaseBattleProcessor<?> getProcessor(IBattleType battleType) {
        return battleProcessorMap.get(battleType);
    }

    public static void register(BaseBattleProcessor<?> processor) {
        battleProcessorMap.put(processor.battleType(), processor);
    }
    
    /**
     * 当前各战斗类型当前的战斗数据
     * @return
     */
    public static int getTotalBattleNum() {
    	int totalNum = 0;
    	for(BaseBattleProcessor<?> battleProcessor : battleProcessorMap.values()) {
    		totalNum += battleProcessor.getBattleNum();
    	}
    	return totalNum;
    }
    
    public static int getCurrBattleNum() {
    	return BaseBattleProcessor.getCurrBattleNum();
    }
}
