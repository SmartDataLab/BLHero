package com.xiugou.x1.game.server.module.equip.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.EquipCache;
import com.xiugou.x1.design.module.autogen.EquipAbstractCache.EquipCfg;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.model.EquipWear;


/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Service
public class EquipWearService extends PlayerOneToOneService<EquipWear> {
	@Autowired
	private EquipCache equipCache;
	@Autowired
	private EquipService equipService;

	@Override
	protected EquipWear createWhenNull(long entityId) {
		EquipWear equipWear = new EquipWear();
		equipWear.setPid(entityId);
		return equipWear;
	}

	/**
	 * 计算套装数量
	 */
	public Map<Integer, Integer> calculateSuitNum(long playerId) {
		EquipWear equipWear = getEntity(playerId);
		Map<Integer, Integer> suitMap = new HashMap<>(); //套装ID - 数量
		for (long equipId : equipWear.getWearing().values()) {
			Equip equip = equipService.getEntity(playerId, equipId);
			EquipCfg equipCfg = equipCache.getOrThrow(equip.getIdentity());
			if (equipCfg.getSuitId() <= 0) {
				continue;
			}
			suitMap.put(equipCfg.getSuitId(), suitMap.getOrDefault(equipCfg.getSuitId(), 0) + 1);
		}
		return suitMap;
	}
	/**
	 *获取穿戴装备的详细信息
	 * */
	public List<Equip> getWearingEquipList(long playerId) {
		EquipWear equipWear = getEntity(playerId);
		Map<Integer, Long> wearing = equipWear.getWearing();
		List<Equip> equips = new ArrayList<>();
		for (long equipId : wearing.values()) {
			Equip equip = equipService.getEntity(playerId, equipId);
			equips.add(equip);
		}
		return equips;
	}
}
