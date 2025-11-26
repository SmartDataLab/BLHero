/**
 *
 */
package com.xiugou.x1.game.server.module.ministruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.prefab.task.Task;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.prefab.thing.RewardReceipt.RewardDetail;

import com.google.protobuf.ByteString;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.translate.BattleTranslate;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.mainline.struct.SceneOpening;

import pb.xiugou.x1.protobuf.hero.Hero.PbHero;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbAttr;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbItem;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbKeyV;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbSceneOpening;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbTask;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThing;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThingReceipt;

/**
 * @author YY
 */
public class PbHelper {

	public static PbTask build(Task task) {
		PbTask.Builder builder = PbTask.newBuilder();
		builder.setId(task.getId());
		builder.setProgress(task.getProcess());
		builder.setStatus(task.getStatus());
		return builder.build();
	}

	public static List<PbItem> buildReward(List<RewardThing> rewardThings) {
		List<PbItem> itemList = new ArrayList<>();
		for (RewardThing reward : rewardThings) {
			itemList.add(build(reward));
		}
		return itemList;
	}
	
	public static List<PbItem> buildCost(List<CostThing> costThings) {
		List<PbItem> itemList = new ArrayList<>();
		for (CostThing cost : costThings) {
			itemList.add(build(cost));
		}
		return itemList;
	}

	public static PbItem build(RewardThing rewardThing) {
		PbItem.Builder builder = PbItem.newBuilder();
		builder.setItem(rewardThing.getThingId());
		builder.setNum(rewardThing.getNum());
		return builder.build();
	}

	public static PbItem build(CostThing costThing) {
		PbItem.Builder builder = PbItem.newBuilder();
		builder.setItem(costThing.getThingId());
		builder.setNum(costThing.getNum());
		return builder.build();
	}

	public static PbSceneOpening build(SceneOpening sceneOpening) {
		PbSceneOpening.Builder builder = PbSceneOpening.newBuilder();
		builder.setId(sceneOpening.getId());
		for (Entry<Integer, Long> entry : sceneOpening.getPayProgess().entrySet()) {
			PbItem.Builder pay = PbItem.newBuilder();
			pay.setItem(entry.getKey());
			pay.setNum(entry.getValue());
			builder.addPay(pay.build());
		}
		return builder.build();
	}

	public static PbThingReceipt build(RewardReceipt rewardReceipt) {
		PbThingReceipt.Builder builder = PbThingReceipt.newBuilder();
		for (RewardDetail reward : rewardReceipt.getDetails()) {
			builder.addThings(build(reward));
		}
		return builder.build();
	}

	public static PbThing build(RewardDetail reward) {
		PbThing.Builder builder = PbThing.newBuilder();
		builder.setIdentity(reward.getThingId());
		builder.setNum(reward.getNum());
		if (reward.getExtra() != null) {
			builder.setData((ByteString) reward.getExtra());
		}
		return builder.build();
	}

	public static PbAttr build(Attr attr) {
		PbAttr.Builder builder = PbAttr.newBuilder();
		builder.setAttrId(attr.getAttrId());
		builder.setValue(attr.getValue());
		return builder.build();
	}

	public static PbHero build(Hero hero) {
		return build(hero, false);
	}

	/**
	 * 
	 * @param hero
	 * @param firstGet
	 *            是否首次获得
	 * @return
	 */
	public static PbHero build(Hero hero, boolean firstGet) {
		PbHero.Builder builder = PbHero.newBuilder();
		builder.setIdentity(hero.getIdentity());
		builder.setLevel(hero.getLevel());
		builder.setFragment(hero.getFragment());
		builder.setFighting(hero.getFighting());
		builder.setPanelAttr(BattleTranslate.build(hero.getPanelAttr()));
		builder.setFirstGet(firstGet);
		builder.setAwakenLevel(hero.getAwakenLevel());
		return builder.build();
	}

	public static PbKeyV build(int key, int value) {
		PbKeyV.Builder builder = PbKeyV.newBuilder();
		builder.setKey(key);
		builder.setValue(value);
		return builder.build();
	}
	
	public static PbKeyV build(Keyv keyv) {
		PbKeyV.Builder builder = PbKeyV.newBuilder();
		builder.setKey(keyv.getKey());
		builder.setValue(keyv.getValue());
		return builder.build();
	}

	public static PbKeyV build(Map.Entry<Integer, Integer> entry) {
		PbKeyV.Builder builder = PbKeyV.newBuilder();
		builder.setKey(entry.getKey());
		builder.setValue(entry.getValue());
		return builder.build();
	}
}
