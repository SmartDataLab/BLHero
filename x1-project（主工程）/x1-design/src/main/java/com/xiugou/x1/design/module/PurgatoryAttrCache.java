package com.xiugou.x1.design.module;


import org.gaming.ruler.util.DropUtil;

import com.xiugou.x1.design.module.autogen.PurgatoryAttrAbstractCache;

@org.springframework.stereotype.Component
public class PurgatoryAttrCache extends PurgatoryAttrAbstractCache<PurgatoryAttrCache.PurgatoryAttrConfig>{
    public static class PurgatoryAttrConfig extends  PurgatoryAttrAbstractCache.PurgatoryAttrCfg implements DropUtil.IDrop{

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