package com.xiugou.x1.game.server.module.trainingCamp.camp;

import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.TrainingExpertCampCache;
import com.xiugou.x1.design.module.autogen.TrainingExpertCampAbstractCache.TrainingExpertCampCfg;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.function.service.OpenFunctionService;
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
public class ExpertTrainingCamp extends AbsTrainingCamp {
	
    @Autowired
    private TrainingCampService trainingCampService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TrainingExpertCampCache trainingExpertCampCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private OpenFunctionService openFunctionService;

    @Override
    protected TrainingCampType getTrainingCampType() {
        return TrainingCampType.EXPERT;
    }

    @Override
    public void handle(TrainingCamp trainingCamp) {
        Player player = playerService.getEntity(trainingCamp.getPid());
        
        int expertLevel = trainingCamp.getExpertLevel();
        TrainingExpertCampCfg campCfg = trainingExpertCampCache.getOrThrow(expertLevel + 1);
        Asserts.isTrue(player.getLevel() >= campCfg.getLevel(), TipsCode.LEVEL_NOT_ENOUGH);
        
        thingService.cost(player.getId(), campCfg.getCostThing(), GameCause.CAMP_ACTIVATE_TALENT);
        
        trainingCamp.setExpertLevel(expertLevel + 1);
        //1加成属性 2开启功能  3增加出战人数
        if (campCfg.getType() == 3) {
        	//就怕策划调来调去升级的数据，每次升级类型3的高级训练营都进行一次计算出战人数
        	//队伍出战人数改成通过角色等级、开服天数来控制
        	int peopleNum = 0;
        	for(TrainingExpertCampCfg tempCfg : trainingExpertCampCache.all()) {
        		if(tempCfg.getType() != 3) {
        			continue;
        		}
        		if(trainingCamp.getExpertLevel() >= tempCfg.getId()) {
        			peopleNum += tempCfg.getNum();
        		}
        	}
            trainingCamp.setMaxPeoples(peopleNum);
        }
        trainingCampService.update(trainingCamp);
        
        if(campCfg.getType() == 2) {
        	openFunctionService.checkAndPushNewFunctions(player.getId());
        }
    }
}
