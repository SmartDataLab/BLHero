/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.gaming.tool.GsonUtil;

/**
 * @author YY
 *
 */
public class TestHeroStorer extends InstanceThingStorer<TestHeroLog, TestHeroReceipt> {

	@Override
	protected IThingType thingType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TestHeroReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
		
		List<TestHero> heroes = new ArrayList<>();
		
		TestHeroReceipt receipt = new TestHeroReceipt(heroes);
		for(TestHero testHero : heroes) {
			//TODO 将hero进行序列化
			receipt.append(new RewardDetail(testHero.getIdentity(), 1, testHero));
		}
		return receipt;
	}

	@Override
	protected void afterAdd(long entityId, TestHeroReceipt receipt, IGameCause cause, String remark) {
		for(TestHero testHero : receipt.getHeroes()) {
			TestHeroLog log = new TestHeroLog();
			log.setOwnerId(entityId);
			log.setCurr(1);
			log.setDelta(1);
			log.setGameCause(cause);
			log.setThingId(testHero.getIdentity());
			log.setThingName(testHero.getName());
			log.setRemark(remark);
			log.setInstanceId(testHero.getId());
			log.setInstanceData(GsonUtil.toJson(testHero));
			//TODO 日志插入到数据库
		}
	}

	@Override
	protected BaseRepository<TestHeroLog> initRepository() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOwnerName(long entityId) {
		// TODO Auto-generated method stub
		return null;
	}
}
