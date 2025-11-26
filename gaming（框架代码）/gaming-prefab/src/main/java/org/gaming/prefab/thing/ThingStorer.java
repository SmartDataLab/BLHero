package org.gaming.prefab.thing;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.exception.ThingLackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 物品处理
 */
public abstract class ThingStorer<R extends RewardReceipt, C extends CostReceipt> {
	
	protected Logger logger = LoggerFactory.getLogger(ThingStorer.class);
	
	private static final Map<IThingType, ThingStorer<?, ?>> STORERS = new HashMap<>();
	
	public ThingStorer() {
		STORERS.put(thingType(), this);
	}
	
	public static ThingStorer<?, ?> getStorer(IThingType thingType) {
		return STORERS.get(thingType);
	}
	
    /**
     * 资源类型
     */
	protected abstract IThingType thingType();
    /**
     * 物品不足编码
     */
	protected abstract ITipCause lackCode();
    /**
     * 获取拥有的数量
     */
	public abstract long getCount(long ownerId, int thingId);
	/**
	 * 尝试添加
	 * @param ownerId 物品的拥有者ID
	 * @param rewardThings
	 * @return
	 */
	protected boolean tryAdd(long ownerId, List<? extends IRewardThing> rewardThings) {
		return true;
	}
    /**
     * 添加之前
     */
	protected final void prepareAdd(long ownerId, List<? extends IRewardThing> rewardThings) {
    	for(IThing thing : rewardThings) {
			requirePositive(thing);
		}
    }
	
	protected final R add(long ownerId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
		R receipt = this.doAdd(ownerId, rewardThings, cause, remark);
		this.afterAdd(ownerId, receipt, cause, remark);
		return receipt;
	}
	
    /**
     * 执行添加
     * @param ownerId 物品的拥有者ID
     * @param rewardThings
     * @param cause
     * @param remark
     * @return
     */
    protected abstract R doAdd(long ownerId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark);
    /**
     * 执行添加后
     * @param entityId
     * @param receipt
     */
    protected void afterAdd(long ownerId, R receipt, IGameCause cause, String remark) {}
    /**
     * 物品是否足够
     * @param rid
     * @param thing
     * @return
     */
    protected final boolean isEnough(long ownerId, ICostThing thing) {
    	requirePositive(thing);
        long remainCount = getCount(ownerId, thing.getThingId()) - thing.getNum();
        return remainCount >= 0;
    }
    /**
     * 消耗之前
     * @param rid
     * @param costSet
     * @param gameEvent
     * @return
     */
    protected final void prepareCost(long ownerId, List<? extends ICostThing> costThings, IGameCause cause) {
    	for(ICostThing thing : costThings) {
    		if(!isEnough(ownerId, thing)) {
    			throw new ThingLackException(cause, lackCode());
    		}
    	}
    }
    
    protected final C cost(long ownerId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
    	C receipt = doCost(ownerId, costThings, cause, remark);
    	afterCost(ownerId, receipt, cause, remark);
    	return receipt;
    }
    
    /**
     * 执行消耗
     * @param rid
     * @param costSet
     * @param gameEvent
     */
    protected abstract C doCost(long ownerId, List<? extends ICostThing> costThings, IGameCause cause, String remark);
    /**
     * 消耗后执行的处理
     * @param entityId
     * @param costReceipt
     * @param cause
     * @param remark
     */
    protected void afterCost(long ownerId, C receipt, IGameCause cause, String remark) {}
    /**
     * 验证非负数
     * @param value
     * @param thingType
     */
    private final void requirePositive(IThing thing) {
    	if(thing.getNum() <= 0) {
    		throw new RuntimeException("物品数量必须大于0，实际：" + thing.getNum());
    	}
    }
    /**
     * 获取拥有者的名字
     * @param ownerId
     * @return
     */
    protected abstract String getOwnerName(long ownerId);
}
