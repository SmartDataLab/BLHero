package com.xiugou.x1.game.server.module.trainingCamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.trainingCamp.model.TrainingCamp;

/**
 * @author yh
 * @date 2023/6/5
 * @apiNote
 */
@Service
public class TrainingCampService extends PlayerOneToOneResetableService<TrainingCamp> {

//	@Autowired
//    private PlayerService playerService;
//    @Autowired
//    private ServerInfoService serverInfoService;
//    @Autowired
//    private TroopPositionCache troopPositionCache;
//    @Autowired
//    private PlayerContextManager playerContextManager;
    @Autowired
    private BattleConstCache battleConstCache;
	
    @Override
    protected TrainingCamp createWhenNull(long entityId) {
        TrainingCamp trainingCamp = new TrainingCamp();
        trainingCamp.setPid(entityId);
//        trainingCamp.setMaxPeoples(checkTroopNum(entityId));
        return trainingCamp;
    }
    
    public int getMaxTroopNum(long entityId) {
    	return this.getEntity(entityId).getMaxPeoples() + battleConstCache.getTroop_init_num();
    }

//	@Override
//	protected void doDailyReset(TrainingCamp entity) {
//		entity.setMaxPeoples(checkTroopNum(entity.getPid()));
//	}
//    
//    public int checkTroopNum(long playerId) {
//    	Player player = playerService.getEntity(playerId);
//    	int openDay = serverInfoService.getOpenedDay();
//        int troopNum = 0;
//        for(TroopPositionCfg troopPositionCfg : troopPositionCache.all()) {
//        	if(player.getLevel() >= troopPositionCfg.getLevel() || openDay >= troopPositionCfg.getOpenDay()) {
//        		if(troopPositionCfg.getId() > troopNum) {
//            		troopNum = troopPositionCfg.getId();
//            	}
//        	}
//        }
//        return troopNum;
//    }
//    
//    @Subscribe
//    private void listen(PlayerUpLevelEvent event) {
//    	TrainingCamp trainingCamp = this.getEntity(event.getPlayer().getId());
//    	int troopNum = checkTroopNum(event.getPlayer().getId());
//    	if(trainingCamp.getMaxPeoples() == troopNum) {
//    		return;
//    	}
//    	trainingCamp.setMaxPeoples(troopNum);
//		this.update(trainingCamp);
//		
//		TrainingCampTroopNumMessage.Builder message = TrainingCampTroopNumMessage.newBuilder();
//		message.setMaxPeoples(troopNum);
//		playerContextManager.push(event.getPlayer().getId(), TrainingCampTroopNumMessage.Proto.ID, message.build());
//    }
//
//	@Override
//	protected boolean doSpecialReset(TrainingCamp entity) {
//		//兼容老数据
//		if(entity.getMaxPeoples() > 0) {
//			return false;
//		}
//		entity.setMaxPeoples(this.checkTroopNum(entity.getPid()));
//		return true;
//	}
    
    
}
