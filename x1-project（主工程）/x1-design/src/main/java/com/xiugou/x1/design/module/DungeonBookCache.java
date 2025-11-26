package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.ruler.util.DropUtil;
import org.gaming.ruler.util.DropUtil.IDrop;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.autogen.DungeonBookAbstractCache;

@org.springframework.stereotype.Component
public class DungeonBookCache extends DungeonBookAbstractCache<DungeonBookCache.DungeonBookConfig> {
	
	@Autowired
	private BattleConstCache battleConstCache;
	
	public static class DungeonBookConfig extends DungeonBookAbstractCache.DungeonBookCfg implements IDrop {
		@Override
		public int getDropId() {
			return this.id;
		}

		@Override
		public int getDropRate() {
			return this.weight;
		}
	}
	
	public int dropSkillBook() {
        //技能书
		int dropRate = RandomUtil.closeClose(1, 10000);
		if(dropRate > battleConstCache.getDrop_book_rate()) {
			return 0;
		}
        List<DungeonBookConfig> all = this.all();
        return DropUtil.randomDrop(all).getDropId();
    }
}