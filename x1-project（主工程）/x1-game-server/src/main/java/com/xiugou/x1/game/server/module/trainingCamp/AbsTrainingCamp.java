package com.xiugou.x1.game.server.module.trainingCamp;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.game.server.module.trainingCamp.constant.TrainingCampType;
import com.xiugou.x1.game.server.module.trainingCamp.model.TrainingCamp;

/**
 * @author yh
 * @date 2023/6/15
 * @apiNote
 */
public abstract class AbsTrainingCamp {
    public static final Map<TrainingCampType, AbsTrainingCamp> TRAININGCAMP = new HashMap<>();

    public AbsTrainingCamp(){
        TRAININGCAMP.put(getTrainingCampType(),this);
    }

    protected abstract TrainingCampType getTrainingCampType();

    public abstract void handle(TrainingCamp trainingCamp);
}
