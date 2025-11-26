package com.xiugou.x1.design.module;


import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.design.module.TrainingGeneralCampCache.TrainingGeneralCampConfig;
import com.xiugou.x1.design.module.autogen.TrainingGeneralCampAbstractCache;

@org.springframework.stereotype.Component
public class TrainingGeneralCampCache extends TrainingGeneralCampAbstractCache<TrainingGeneralCampConfig> {

    public static class TrainingGeneralCampConfig extends TrainingGeneralCampAbstractCache.TrainingGeneralCampCfg {
        private BattleAttr levelAttr;

        public BattleAttr getLevelAttr() {
            return levelAttr;
        }
    }

    @Override
    protected void loadAfterReady() {
        for (TrainingGeneralCampConfig config1 : this.all()) {
            BattleAttr levelAttr = new BattleAttr();
            for (TrainingGeneralCampConfig config2 : this.all()) {
                if (config2.getId() <= config1.getId()) {
                    //累加
                    levelAttr.addById(config2.getAttr().getAttrId(), config2.getAttr().getValue());
                }
            }
            config1.levelAttr = levelAttr;
        }
    }
}