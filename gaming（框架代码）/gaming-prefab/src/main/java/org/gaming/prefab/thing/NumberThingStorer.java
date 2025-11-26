/**
 * 
 */
package org.gaming.prefab.thing;

import org.gaming.db.repository.BaseRepository;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.CostReceipt.CostDetail;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;

/**
 * @author YY
 * 数值资源类的储存器
 */
public abstract class NumberThingStorer<Log extends NumberThingLog> extends ThingStorer<RewardReceipt, CostReceipt> {
	
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
	protected final void afterAdd(long ownerId, RewardReceipt receipt, IGameCause cause, String remark) {
		String ownerName = this.getOwnerName(ownerId);
		for(RewardDetail detail : receipt.getDetails()) {
			Log log = newLog();
			log.setOwnerId(ownerId);
			log.setOwnerName(ownerName);
			log.setCurr(this.getCount(ownerId, detail.getThingId()));
			log.setDelta(detail.getNum());
			log.setGameCause(cause);
			log.setThingId(detail.getThingId());
			log.setThingName(thingName(detail.getThingId()));
			log.setRemark(remark + receipt.getRemark());
			repository().insert(log);
		}
	}
	
	@Override
	protected final void afterCost(long ownerId, CostReceipt receipt, IGameCause cause, String remark) {
		String ownerName = this.getOwnerName(ownerId);
		for(CostDetail detail : receipt.getDetails()) {
			Log log = newLog();
			log.setOwnerId(ownerId);
			log.setOwnerName(ownerName);
			log.setCurr(this.getCount(ownerId, detail.getThingId()));
			log.setDelta(-detail.getNum());
			log.setGameCause(cause);
			log.setThingId(detail.getThingId());
			log.setThingName(thingName(detail.getThingId()));
			log.setRemark(remark);
			repository().insert(log);
		}
	}
	
	protected String thingName(int thingId) {
		return this.thingType().getDesc();
	}
}
