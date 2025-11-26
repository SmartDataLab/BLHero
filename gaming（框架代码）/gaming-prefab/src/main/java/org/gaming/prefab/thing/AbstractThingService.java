package org.gaming.prefab.thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.prefab.IGameCause;

public abstract class AbstractThingService<Reward extends IRewardThing, Cost extends ICostThing> {

	/**
	 * 加锁KEY前缀
	 */
	private static final String PREFIX = "THING";

	public ThingStorer<?, ?> getStorer(IThingType thingType) {
		ThingStorer<?, ?> thingStorer = ThingStorer.getStorer(thingType);
		if(thingStorer == null) {
			throw new RuntimeException(thingType + "没有找到该类型的储存器");
		}
		return thingStorer;
	}

	
	public abstract IThingType thingTypeOf(IThing thing);
	
	/**
	 * 物品是否足够
	 */
	public boolean isEnough(long entityId, Cost costThing) {
		IThingType thingType = thingTypeOf(costThing);
		ThingStorer<?, ?> thingStorer = getStorer(thingType);
		return thingStorer.isEnough(entityId, costThing);
	}

	/**
	 * 物品是否足够
	 */
	public boolean isEnough(long entityId, List<Cost> costThings) {
		for(Cost costThing : costThings) {
			if(!isEnough(entityId, costThing)) {
				return false;
			}
		}
		return true;
	}

	public CostReceipt cost(long entityId, Cost costThing, IGameCause cause, String remark) {
		return cost(entityId, Collections.singletonList(costThing), cause, remark);
	}
	
	public CostReceipt cost(long entityId, Cost costThing, IGameCause cause) {
		return cost(entityId, Collections.singletonList(costThing), cause, "");
	}

	public CostReceipt cost(long entityId, List<Cost> costThings, IGameCause cause) {
		return cost(entityId, costThings, cause, "");
	}
	
	protected abstract List<Cost> mergeCosts(List<Cost> costThings);
	
	public CostReceipt cost(long entityId, List<Cost> costThings, IGameCause cause, String remark) {
		List<Cost> costs = mergeCosts(costThings);
		Map<ThingStorer<?, ?>, List<Cost>> grouping = groupingThingByType(costs);
		
		CostReceipt costReceipt = null;
		synchronized (new String(PREFIX + entityId).intern()) {
			for (Entry<ThingStorer<?, ?>, List<Cost>> entry : grouping.entrySet()) {
				ThingStorer<?, ?> storer = entry.getKey();
				List<Cost> costing = entry.getValue();
				storer.prepareCost(entityId, costing, cause);
			}
			costReceipt = new CostReceipt();
			
			for (Entry<ThingStorer<?, ?>, List<Cost>> entry : grouping.entrySet()) {
				ThingStorer<?, ?> storer = entry.getKey();
				List<Cost> costing = entry.getValue();
				
				CostReceipt partReceipt = storer.cost(entityId, costing, cause, remark);
				costReceipt.merge(partReceipt);
			}
		}
		costFinished(entityId, costReceipt, remark);
		return costReceipt;
	}
	
	protected abstract void costFinished(long entityId, CostReceipt costReceipt, String remark);
	
	/**
	 * 尝试添加
	 * @param entityId
	 * @param rewardThings
	 * @param cause
	 * @param noticeType
	 * @param remark
	 * @return
	 */
	public final boolean tryAdd(long entityId, List<Reward> rewardThings, IGameCause cause) {
		List<Reward> rewards = this.mergeRewards(rewardThings);
		Map<ThingStorer<?, ?>, List<Reward>> grouping = groupingThingByType(rewards);
		
		boolean success = true;
		synchronized (new String(PREFIX + entityId).intern()) {
			for (Entry<ThingStorer<?, ?>, List<Reward>> entry : grouping.entrySet()) {
				ThingStorer<?, ?> storer = entry.getKey();
				List<Reward> rewarding = entry.getValue();
				storer.prepareAdd(entityId, rewarding);
				success = storer.tryAdd(entityId, rewarding) && success;
				if(!success) {
					break;
				}
			}
		}
		return success;
	}
	
	protected abstract List<Reward> mergeRewards(List<Reward> rewardThings);

	public final RewardReceipt add(long entityId, List<Reward> rewardThings, IGameCause cause, NoticeType noticeType, String remark) {
		List<Reward> rewards = this.mergeRewards(rewardThings);
		Map<ThingStorer<?, ?>, List<Reward>> grouping = groupingThingByType(rewards);

		RewardReceipt rewardReceipt = null;
		synchronized (new String(PREFIX + entityId).intern()) {
			for (Entry<ThingStorer<?, ?>, List<Reward>> entry : grouping.entrySet()) {
				ThingStorer<?, ?> storer = entry.getKey();
				List<Reward> rewarding = entry.getValue();
				storer.prepareAdd(entityId, rewarding);
			}
			rewardReceipt = new RewardReceipt();
			
			for (Entry<ThingStorer<?, ?>, List<Reward>> entry : grouping.entrySet()) {
				ThingStorer<?, ?> storer = entry.getKey();
				List<Reward> rewarding = entry.getValue();
				
				RewardReceipt partReceipt = storer.add(entityId, rewarding, cause, remark);
				rewardReceipt.merge(partReceipt);
			}
		}
		addFinished(entityId, rewardReceipt, noticeType, remark);
		return rewardReceipt;
	}
	
	protected abstract void addFinished(long entityId, RewardReceipt rewardReceipt, NoticeType noticeType, String remark);

	public RewardReceipt add(long entityId, List<Reward> rewardThings, IGameCause cause) {
		return add(entityId, rewardThings, cause, NoticeType.NORMAL, "");
	}
	
	public RewardReceipt add(long entityId, List<Reward> rewardThings, IGameCause cause, String remark) {
		return add(entityId, rewardThings, cause, NoticeType.NORMAL, remark);
	}
	
	public RewardReceipt add(long entityId, List<Reward> rewardThings, IGameCause cause, NoticeType noticeType) {
		return add(entityId, rewardThings, cause, noticeType, "");
	}
	

	public RewardReceipt add(long entityId, Reward rewardThing, IGameCause cause, NoticeType noticeType) {
		return add(entityId, Collections.singletonList(rewardThing), cause, noticeType, "");
	}
	
	public RewardReceipt add(long entityId, Reward rewardThing, IGameCause cause, NoticeType noticeType, String remark) {
		return add(entityId, Collections.singletonList(rewardThing), cause, noticeType, remark);
	}

	public RewardReceipt add(long entityId, Reward rewardThing, IGameCause cause, String remark) {
		return add(entityId, rewardThing, cause, NoticeType.NORMAL, remark);
	}
	
	public RewardReceipt add(long entityId, Reward rewardThing, IGameCause cause) {
		return add(entityId, rewardThing, cause, NoticeType.NORMAL, "");
	}
	

	private <T extends IThing> Map<ThingStorer<?, ?>, List<T>> groupingThingByType(List<T> things) {
		Map<ThingStorer<?, ?>, List<T>> map = new LinkedHashMap<>();
		for(T thing : things) {
			IThingType thingType = thingTypeOf(thing);
			ThingStorer<?, ?> storer = this.getStorer(thingType);
			List<T> groupList = map.get(storer);
			if(groupList == null) {
				groupList = new ArrayList<>();
				map.put(storer, groupList);
			}
			groupList.add(thing);
		}
		return map;
	}
}
