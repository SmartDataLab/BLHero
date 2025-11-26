package com.xiugou.x1.design.struct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品工具类
 */
public class ThingUtil {

	public static List<CostThing> mergeCost(List<? extends CostThing> things) {
		Map<Integer, CostThing> map = new LinkedHashMap<>();
		for (CostThing thing : things) {
			CostThing mergeThing = map.get(thing.getThingId());
			if (mergeThing == null) {
				mergeThing = CostThing.of(thing.getThingId(), thing.getNum());
			} else {
				mergeThing = CostThing.of(thing.getThingId(), mergeThing.getNum() + thing.getNum());
			}
			map.put(thing.getThingId(), mergeThing);
		}
		List<CostThing> resultList = new ArrayList<>();
		resultList.addAll(map.values());
		return resultList;
	}

	/**
	 * 按类型合并奖励列表
	 */
	public static List<RewardThing> mergeReward(List<? extends RewardThing> things) {
		Map<Integer, RewardThing> map = new LinkedHashMap<>();
		for (RewardThing thing : things) {
			RewardThing mergeThing = map.get(thing.getThingId());
			if (mergeThing == null) {
				mergeThing = RewardThing.of(thing.getThingId(), thing.getNum());
			} else {
				mergeThing = RewardThing.of(thing.getThingId(), mergeThing.getNum() + thing.getNum());
			}
			map.put(thing.getThingId(), mergeThing);
		}
		List<RewardThing> resultList = new ArrayList<>();
		resultList.addAll(map.values());
		return resultList;
	}

	public static RewardThing multiplyReward(RewardThing oldThing, int multiply) {
		return RewardThing.of(oldThing.getThingId(), oldThing.getNum() * multiply);
	}
	
	public static RewardThing multiplyReward(RewardThing oldThing, float multiply) {
		return RewardThing.of(oldThing.getThingId(), (int)(oldThing.getNum() * multiply));
	}

	public static List<RewardThing> multiplyReward(List<RewardThing> things, int multiply) {
		List<RewardThing> resultList = new ArrayList<>();
		for (RewardThing oldThing : things) {
			resultList.add(multiplyReward(oldThing, multiply));
		}
		return resultList;
	}

	public static CostThing multiplyCost(CostThing oldThing, int multiply) {
		return CostThing.of(oldThing.getThingId(), oldThing.getNum() * multiply);
	}

	public static List<CostThing> multiplyCost(List<CostThing> things, int multiply) {
		List<CostThing> resultList = new ArrayList<>();
		for (CostThing oldThing : things) {
			resultList.add(multiplyCost(oldThing, multiply));
		}
		return resultList;
	}

	/***
	 * 乘以小数，向上取整
	 * 
	 * @param things
	 * @param multiply
	 * @return
	 */
	public static List<RewardThing> multiplyReward(List<RewardThing> things, float multiply) {
		List<RewardThing> resultList = new ArrayList<>();
		for (RewardThing oldThing : things) {
			resultList.add(RewardThing.of(oldThing.getThingId(), (long) Math.ceil(oldThing.getNum() * multiply)));
		}
		return resultList;
	}

	/**
	 * 消耗转奖励
	 *
	 * @param costs
	 * @return
	 */
	public static List<RewardThing> costToReward(List<CostThing> costs) {
		List<RewardThing> rewards = new ArrayList<>();
		for (CostThing cost : costs) {
			rewards.add(RewardThing.of(cost.getThingId(), cost.getNum()));
		}
		return rewards;
	}
}
