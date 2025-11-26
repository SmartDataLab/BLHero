package com.xiugou.x1.game.server.module.trainingCamp;


import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.trainingCamp.constant.TrainingCampType;
import com.xiugou.x1.game.server.module.trainingCamp.event.TrainingCampUpLevelEvent;
import com.xiugou.x1.game.server.module.trainingCamp.model.TrainingCamp;
import com.xiugou.x1.game.server.module.trainingCamp.service.TrainingCampService;

import pb.xiugou.x1.protobuf.trainingcamp.TrainingCamp.TrainingCampActivateTalentRequest;
import pb.xiugou.x1.protobuf.trainingcamp.TrainingCamp.TrainingCampActivateTalentResponse;
import pb.xiugou.x1.protobuf.trainingcamp.TrainingCamp.TrainingCampInfoResponse;

/**
 * @author yh
 * @date 2023/6/5
 * @apiNote
 */
@Controller
public class TrainingCampHandler extends AbstractModuleHandler {

    @Autowired
    private TrainingCampService trainingCampService;
    @Autowired
    private HeroService heroService;
    @Autowired
    private BattleConstCache battleConstCache;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        TrainingCamp trainingCamp = trainingCampService.getEntity(playerId);
        
        TrainingCampInfoResponse.Builder response = TrainingCampInfoResponse.newBuilder();
        response.setTrainLevel(trainingCamp.getLevel());
        response.setExpertTrainLevel(trainingCamp.getExpertLevel());
        response.setMaxPeoples(trainingCamp.getMaxPeoples() + battleConstCache.getTroop_init_num());
        playerContextManager.push(playerId, TrainingCampInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public TrainingCampActivateTalentResponse activate(PlayerContext playerContext, TrainingCampActivateTalentRequest request) {
        long pid = playerContext.getId();
        TrainingCamp trainingCamp = trainingCampService.getEntity(pid);

        TrainingCampType trainingCampType = TrainingCampType.valueOf(request.getType());
        AbsTrainingCamp absTrainingCamp = AbsTrainingCamp.TRAININGCAMP.get(trainingCampType);
        absTrainingCamp.handle(trainingCamp);

        //刷新属性
        heroService.calculateAllHeroAttr(pid, GameCause.CAMP_ACTIVATE_TALENT);
        //训练营升级事件
        EventBus.post(TrainingCampUpLevelEvent.of(pid));

        TrainingCampActivateTalentResponse.Builder response = TrainingCampActivateTalentResponse.newBuilder();
        response.setTrainLevel(trainingCamp.getLevel());
        response.setExpertTrainLevel(trainingCamp.getExpertLevel());
        response.setMaxPeoples(trainingCamp.getMaxPeoples() + battleConstCache.getTroop_init_num());
        return response.build();
    }
}
