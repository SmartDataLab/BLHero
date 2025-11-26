/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;

/**
 * @author YY
 * 数值资源类的储存器
 */
public abstract class ExpThingStorer<Log extends ExpThingLog> extends ThingStorer<ExpRewardReceipt, CostReceipt> {
	
	protected abstract Log newLog();
	protected abstract BaseRepository<Log> initRepository();
	
	private BaseRepository<Log> repository;
	private BaseRepository<Log> repository() {
		if(repository == null) {
			repository = initRepository();
		}
		return repository;
	}
	
	@Override
	protected final void afterAdd(long entityId, ExpRewardReceipt receipt, IGameCause cause, String remark) {
		String ownerName = this.getOwnerName(entityId);
		for(RewardDetail detail : receipt.getDetails()) {
			Log log = newLog();
			log.setOwnerId(entityId);
			log.setOwnerName(ownerName);
			log.setCurr(this.getCount(entityId, detail.getThingId()));
			log.setDelta(detail.getNum());
			log.setGameCause(cause);
			log.setThingId(detail.getThingId());
			log.setThingName(thingName(detail.getThingId()));
			log.setRemark(remark);
			log.setOldLevel(receipt.getOldLevel());
			log.setCurrLevel(receipt.getCurrLevel());
			repository().insert(log);
		}
	}
	
	@Override
	protected final void afterCost(long entityId, CostReceipt receipt, IGameCause cause, String remark) {
		throw new UnsupportedOperationException("exp thing " + thingType().getDesc() + " can't cost");
	}

	@Override
	protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
		throw new UnsupportedOperationException("exp thing " + thingType().getDesc() + " can't cost");
	}
	
	protected String thingName(int thingId) {
		return "";
	}
}
