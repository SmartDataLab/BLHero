/**
 * 
 */
package com.xiugou.x1.game.server.module.rank.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class RankRewardData {
	private int rankType;
	private List<Integer> rewards = new ArrayList<>();
	public int getRankType() {
		return rankType;
	}
	public void setRankType(int rankType) {
		this.rankType = rankType;
	}
	public List<Integer> getRewards() {
		return rewards;
	}
	public void setRewards(List<Integer> rewards) {
		this.rewards = rewards;
	}
	
}
