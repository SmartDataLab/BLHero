/**
 * 
 */
package com.xiugou.x1.game.server.module.bag.service;

import java.util.List;

import org.gaming.prefab.thing.AbstractThingService;
import org.gaming.prefab.thing.CostReceipt;
import org.gaming.prefab.thing.CostReceipt.CostDetail;
import org.gaming.prefab.thing.IThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;

import pb.xiugou.x1.protobuf.bag.Bag.ThingChangeMessage;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThing;

/**
 * @author YY
 *
 */
@Service
public class ThingService extends AbstractThingService<RewardThing, CostThing> {

	@Autowired
	private ItemCache itemCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	@Override
	public IThingType thingTypeOf(IThing thing) {
		return ItemType.valueOf(itemCache.getOrThrow(thing.getThingId()).getKind());
	}

	@Override
	protected void costFinished(long entityId, CostReceipt costReceipt, String remark) {
		ThingChangeMessage.Builder builder = ThingChangeMessage.newBuilder();
		for(CostDetail cost : costReceipt.getDetails()) {
			PbThing.Builder data = PbThing.newBuilder();
			data.setIdentity(cost.getThingId());
			data.setNum(-cost.getNum());
			builder.addThings(data.build());
		}
		builder.setShowType(NoticeType.SLIENT.getValue());
		playerContextManager.push(entityId, ThingChangeMessage.Proto.ID, builder.build());
	}

	@Override
	protected void addFinished(long entityId, RewardReceipt rewardReceipt, NoticeType noticeType, String remark) {
		if(noticeType == NoticeType.NO) {
			return;
		}
		ThingChangeMessage.Builder builder = ThingChangeMessage.newBuilder();
		for(RewardDetail reward : rewardReceipt.getDetails()) {
			builder.addThings(PbHelper.build(reward));
		}
		builder.setShowType(noticeType.getValue());
		playerContextManager.push(entityId, ThingChangeMessage.Proto.ID, builder.build());
	}

	@Override
	protected List<CostThing> mergeCosts(List<CostThing> costThings) {
		return ThingUtil.mergeCost(costThings);
	}

	@Override
	protected List<RewardThing> mergeRewards(List<RewardThing> rewardThings) {
		return ThingUtil.mergeReward(rewardThings);
	}
}
