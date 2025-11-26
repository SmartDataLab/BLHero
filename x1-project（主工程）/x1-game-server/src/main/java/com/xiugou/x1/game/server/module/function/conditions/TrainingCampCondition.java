package com.xiugou.x1.game.server.module.function.conditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.function.constant.OpenfunctionType;
import com.xiugou.x1.game.server.module.trainingCamp.model.TrainingCamp;
import com.xiugou.x1.game.server.module.trainingCamp.service.TrainingCampService;

/**
 * @author yh
 * @date 2023/6/14
 * @apiNote
 */
@Component
public class TrainingCampCondition extends FunctionCondition {
    @Autowired
    private TrainingCampService trainingCampService;
    
    @Override
    protected OpenfunctionType getOpenfunctionType() {
        return OpenfunctionType.TRAINING_ADVANCED_CAMP_LEVEL;
    }

    @Override
    public boolean functionOpenOrNot(long pid, int condition) {
        TrainingCamp entity = trainingCampService.getEntity(pid);
        return entity.getExpertLevel() >= condition;
    }
}
