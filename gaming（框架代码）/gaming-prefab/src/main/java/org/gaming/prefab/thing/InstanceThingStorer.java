/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;

/**
 * @author YY
 * 实物
 */
public abstract class InstanceThingStorer<Log extends InstanceThingLog, R extends RewardReceipt> extends ThingStorer<R, CostReceipt> {
	
	
	protected abstract BaseRepository<Log> initRepository();
	
	private BaseRepository<Log> repository;
	protected final BaseRepository<Log> repository() {
		if(repository == null) {
			repository = initRepository();
		}
		return repository;
	}
	
	@Override
	protected final void afterCost(long entityId, CostReceipt receipt, IGameCause cause, String remark) {
		throw new UnsupportedOperationException("instance thing " + thingType().getDesc() + " can't cost");
	}

	@Override
	protected final CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
		throw new UnsupportedOperationException("instance thing " + thingType().getDesc() + " can't cost");
	}
	
	protected abstract void afterAdd(long entityId, R receipt, IGameCause cause, String remark);

	@Override
	protected final ITipCause lackCode() {
		throw new UnsupportedOperationException("instance thing " + thingType().getDesc() + " can't cost");
	}

	@Override
	public final long getCount(long entityId, int thingId) {
		throw new UnsupportedOperationException("instance thing " + thingType().getDesc() + " can't cost");
	}
}
