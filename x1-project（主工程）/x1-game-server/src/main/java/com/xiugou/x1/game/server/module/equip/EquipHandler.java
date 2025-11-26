package com.xiugou.x1.game.server.module.equip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.ruler.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.EquipCache;
import com.xiugou.x1.design.module.autogen.EquipAbstractCache.EquipCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.equip.constant.EquipEnum;
import com.xiugou.x1.game.server.module.equip.event.EquipWearChangEvent;
import com.xiugou.x1.game.server.module.equip.event.SalvageEquipEvent;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.model.EquipWear;
import com.xiugou.x1.game.server.module.equip.service.EquipService;
import com.xiugou.x1.game.server.module.equip.service.EquipWearService;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.privilegenormal.service.PrivilegeNormalService;

import pb.xiugou.x1.protobuf.equip.Equip.EquipBatchSalvageRequest;
import pb.xiugou.x1.protobuf.equip.Equip.EquipBatchSalvageResponse;
import pb.xiugou.x1.protobuf.equip.Equip.EquipChangeRequest;
import pb.xiugou.x1.protobuf.equip.Equip.EquipChangeResponse;
import pb.xiugou.x1.protobuf.equip.Equip.EquipLockRequest;
import pb.xiugou.x1.protobuf.equip.Equip.EquipLockResponse;
import pb.xiugou.x1.protobuf.equip.Equip.EquipOneClickRequest;
import pb.xiugou.x1.protobuf.equip.Equip.EquipOneClickResponse;
import pb.xiugou.x1.protobuf.equip.Equip.EquipSalvageRequest;
import pb.xiugou.x1.protobuf.equip.Equip.EquipSalvageResponse;
import pb.xiugou.x1.protobuf.equip.Equip.EquipVerifyRequest;
import pb.xiugou.x1.protobuf.equip.Equip.EquipVerifyResponse;
import pb.xiugou.x1.protobuf.equip.Equip.EquipWearInfoResponse;

/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Controller
public class EquipHandler extends AbstractModuleHandler {
	@Autowired
	private EquipService equipService;
	@Autowired
	private EquipCache equipCache;
	@Autowired
	private EquipWearService equipWearService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private FormationService formationService;
	@Autowired
	private PrivilegeNormalService privilegeNormalService;
	@Autowired
	private BattleConstCache battleConstCache;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public boolean needDailyPush() {
		return false;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		EquipWear entity = equipWearService.getEntity(playerId);

		EquipWearInfoResponse.Builder response = EquipWearInfoResponse.newBuilder();
		response.addAllEquipId(entity.getWearing().values());
		Formation formation = formationService.getEntity(playerId, BattleType.MAINLINE);
		response.setFighting(formation.getFighting());
		response.setEquipFighting(formation.getEquipFighting());
		playerContextManager.push(playerId, EquipWearInfoResponse.Proto.ID, response.build());
	}

	/**
	 * 换装或者穿戴
	 */
	@PlayerCmd
	public EquipChangeResponse change(PlayerContext playerContext, EquipChangeRequest request) {
		long equipId = request.getEquipId();
		Equip replacedEquip = equipService.getEntity(playerContext.getId(), equipId);
		Asserts.isTrue(replacedEquip != null, TipsCode.EQUIP_LACK);
		Asserts.isTrue(replacedEquip.isAppraise(), TipsCode.EQUIP_UNIDENTIFIED);

		EquipCfg equipCfg = equipCache.getOrThrow(replacedEquip.getIdentity());
		Player player = playerService.getEntity(playerContext.getId());
		Asserts.isTrue(player.getLevel() >= equipCfg.getLevel(), TipsCode.EQUIP_PLAYER_LEVEL_LACK);

		EquipWear equipWear = equipWearService.getEntity(playerContext.getId());
		Map<Integer, Long> equipWearMap = equipWear.getWearing();
		
		int type = equipCfg.getType();
		long wearingEquipId = equipWearMap.getOrDefault(type, 0L);
		if (wearingEquipId > 0) {
			Asserts.isTrue(wearingEquipId != equipId, TipsCode.EQUIP_WEARING);
			Equip wearingEquip = equipService.getEntity(playerContext.getId(), wearingEquipId);
			wearingEquip.setWear(false);
			equipService.update(wearingEquip);
		}
		equipWearMap.put(type, equipId);
		replacedEquip.setWear(true);
		equipService.update(replacedEquip);
		equipWearService.update(equipWear);
		heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.EQUIP_CHANGE);

		EventBus.post(EquipWearChangEvent.of(playerContext.getId()));

		EquipChangeResponse.Builder response = EquipChangeResponse.newBuilder();
		response.setEquipId(equipId);
		Formation formation = formationService.getEntity(playerContext.getId(), BattleType.MAINLINE);
		response.setFighting(formation.getFighting());
		response.setEquipFighting(formation.getEquipFighting());
		return response.build();
	}

	/**
	 * 鉴定副属性
	 *
	 */
	@PlayerCmd
	public EquipVerifyResponse verify(PlayerContext playerContext, EquipVerifyRequest request) {
		Equip equip = equipService.getEntity(playerContext.getId(), request.getEquipId());
		Asserts.isTrue(!equip.isAppraise(), TipsCode.EQUIP_IDENTIFIED);

		EquipCfg equipCfg = equipCache.getOrThrow(equip.getIdentity());
		int num = equipService.analyzeEquipSubAttr(equipCfg.getQuality());
		Asserts.isTrue(num > 0, TipsCode.EQUIP_NO_REQUIRED_IDENTIFIED);
		
		equipService.verify(equip, equipCfg.getAttrRep(), num);
		equip.setAppraise(true);
		equipService.update(equip);
		
		EquipVerifyResponse.Builder response = EquipVerifyResponse.newBuilder();
		response.setPbEquip(equipService.build(equip));
		return response.build();
	}

	/**
	 * 锁定或解锁装备
	 */
	@PlayerCmd
	public EquipLockResponse lock(PlayerContext playerContext, EquipLockRequest request) {
		Equip equip = equipService.getEntity(playerContext.getId(), request.getEquipId());
		equip.setLock(!equip.isLock());
		equipService.update(equip);

		EquipLockResponse.Builder response = EquipLockResponse.newBuilder();
		response.setEquipId(equip.getId());
		response.setLock(equip.isLock());
		return response.build();
	}

	/**
	 * 分解装备
	 */
	@PlayerCmd
	public EquipSalvageResponse salvage(PlayerContext playerContext, EquipSalvageRequest request) {
		List<Long> equipList = equipSalvage(playerContext.getId(), Collections.singletonList(request.getEquipId()));
		EquipSalvageResponse.Builder response = EquipSalvageResponse.newBuilder();
		if (!equipList.isEmpty()){
			response.setEquipId(request.getEquipId());
		}
		return response.build();
	}

	// 分解操作
	private List<Long> equipSalvage(long playerId, List<Long> equipList) {
		List<RewardThing> rewardThingList = new ArrayList<>();
		List<Equip> equips = new ArrayList<>();
		List<Long> returnList = new ArrayList<>();
		for (long id : equipList) {
			Equip equip = equipService.getEntity(playerId, id);
			if (equip.isWear()) {
				continue;
			}
			if (equip.isLock()) {
				continue;
			}
			if (!equip.isAppraise()) {
				continue;
			}
			EquipCfg equipCfg = equipCache.getOrThrow(equip.getIdentity());
			rewardThingList.addAll(equipCfg.getSmeltReward());
			equips.add(equip);
			returnList.add(id);
		}
		if (equips.isEmpty()) {
			return returnList;
		}

		if(privilegeNormalService.getEntity(playerId).isEquipSalvageDouble()) {
			rewardThingList = ThingUtil.multiplyReward(rewardThingList, battleConstCache.getPrivilege_normal_equip_salvage());
		}
		
		equipService.deleteAll(playerId, equips, GameCause.EQUIP_SALVAGE);
		thingService.add(playerId, rewardThingList, GameCause.EQUIP_SALVAGE, NoticeType.TIPS);
		
		EventBus.post(SalvageEquipEvent.of(playerId, returnList.size()));
		return returnList;
	}

	/**
	 * 批量分解装备
	 */
	@PlayerCmd
	public EquipBatchSalvageResponse batchSalvage(PlayerContext playerContext, EquipBatchSalvageRequest request) {
		List<Long> equipIdList = request.getEquipIdList();
		List<Long> salvageLsit = equipSalvage(playerContext.getId(), equipIdList);
		
		EquipBatchSalvageResponse.Builder response = EquipBatchSalvageResponse.newBuilder();
		response.addAllEquips(salvageLsit);
		return response.build();
	}

	/**
	 * 一键装备
	 */
	@PlayerCmd
	public EquipOneClickResponse oneClick(PlayerContext playerContext, EquipOneClickRequest request) {
		List<Equip> equipList = equipService.getEntities(playerContext.getId());
		EquipWear equipWear = equipWearService.getEntity(playerContext.getId());
		
		Map<Integer, Long> equipWearMap = equipWear.getWearing();
		
		boolean hasChange = false;
		for (EquipEnum value : EquipEnum.values()) {
			int code = value.getCode();
			// 筛选出 类型同类型 且评分最高的 装备
			Equip equip = equipList.stream().filter(e -> !e.isWear())
					.filter(e -> equipCache.getOrThrow(e.getIdentity()).getType() == code)
					.max(Comparator.comparing(Equip::getScore)).orElse(null);
			if (equip == null) {
				continue;
			}
			long wearingEquipId = equipWearMap.getOrDefault(code, 0L);
			if (wearingEquipId > 0) {
				Equip wearingequip = equipService.getEntity(playerContext.getId(), wearingEquipId);
				// 当前穿戴装备评分不低于背包中同类型的装备 则不需要有所反应
				if (wearingequip.getScore() >= equip.getScore()) { 
					continue;
				}
				wearingequip.setWear(false);
				equipService.update(wearingequip);
			}
			
			equip.setWear(true);
			equipWearMap.put(code, equip.getId());
			equipService.update(equip);
			hasChange = true;
		}
		
		if(!hasChange) {
			return EquipOneClickResponse.getDefaultInstance();
		}
		
		equipWearService.update(equipWear);
		heroService.calculateAllHeroAttr(playerContext.getId(), GameCause.EQUIP_CHANGE); // 计算属性
		
		// 装备穿戴变更事件
		EventBus.post(EquipWearChangEvent.of(playerContext.getId()));
		
		EquipOneClickResponse.Builder response = EquipOneClickResponse.newBuilder();
		response.addAllEquipId(equipWearMap.values());
		Formation formation = formationService.getEntity(playerContext.getId(), BattleType.MAINLINE);
		response.setFighting(formation.getFighting());
		response.setEquipFighting(formation.getEquipFighting());
		return response.build();
	}
}
