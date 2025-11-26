package com.xiugou.x1.design.module;


import org.gaming.ruler.util.DropUtil;

import com.xiugou.x1.design.module.autogen.EquipattrStashAbstractCache;

@org.springframework.stereotype.Component
public class EquipattrStashCache extends EquipattrStashAbstractCache<EquipattrStashCache.EquipattrStashConfig> {

    public static class EquipattrStashConfig extends EquipattrStashAbstractCache.EquipattrStashCfg implements DropUtil.IDrop {

        @Override
        public int getDropId() {
            return this.getId();
        }

        @Override
        public int getDropRate() {
            return this.getWeight();
        }
    }
}