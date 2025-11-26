/**
 * 
 */
package com.xiugou.x1.game.server.module.equip.service;

import java.util.List;

import org.gaming.prefab.thing.RewardReceipt;

import com.xiugou.x1.game.server.module.equip.model.Equip;

/**
 * @author YY
 *
 */
public class EquipReceipt extends RewardReceipt {

	private List<Equip> equips;
	
	public EquipReceipt(List<Equip> equips) {
		this.equips = equips;
	}

	public List<Equip> getEquips() {
		return equips;
	}

	public void setEquips(List<Equip> equips) {
		this.equips = equips;
	}
}
