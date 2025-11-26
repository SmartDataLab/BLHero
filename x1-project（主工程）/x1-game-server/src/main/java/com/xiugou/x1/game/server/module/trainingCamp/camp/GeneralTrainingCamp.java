package com.xiugou.x1.game.server.module.trainingCamp.camp;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.TrainingGeneralCampCache;
import com.xiugou.x1.design.module.autogen.TrainingGeneralCampAbstractCache.TrainingGeneralCampCfg;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.trainingCamp.AbsTrainingCamp;
import com.xiugou.x1.game.server.module.trainingCamp.constant.TrainingCampType;
import com.xiugou.x1.game.server.module.trainingCamp.model.TrainingCamp;
import com.xiugou.x1.game.server.module.trainingCamp.service.TrainingCampService;

/**
 * @author yh
 * @date 2023/6/15
 * @apiNote
 */
@Component
public class GeneralTrainingCamp extends AbsTrainingCamp {
    @Autowired
    private TrainingCampService trainingCampService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TrainingGeneralCampCache trainingGeneralCampCache;
    @Autowired
    private ThingService thingService;


    @Override
    protected TrainingCampType getTrainingCampType() {
        return TrainingCampType.GENERAL;
    }

    @Override
    public void handle(TrainingCamp trainingCamp) {
        long pid = trainingCamp.getPid();
        Player player = playerService.getEntity(pid);
        
        int trainingLevel = trainingCamp.getLevel();
        TrainingGeneralCampCfg trainingGeneralCampCfg = trainingGeneralCampCache.getOrThrow(trainingLevel + 1);
        Asserts.isTrue(player.getLevel() >= trainingGeneralCampCfg.getLevel(), TipsCode.LEVEL_NOT_ENOUGH);
        
        thingService.cost(pid, trainingGeneralCampCfg.getCostThing(), GameCause.CAMP_ACTIVATE_TALENT);
        
        trainingCamp.setLevel(trainingLevel + 1);
        trainingCampService.update(trainingCamp);
    }
}
