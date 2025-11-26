/**
 * 
 */
package com.xiugou.x1.battle.sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YY
 *
 */
public class Zone {
	//区域ID
	private int id;
	
	//TODO PVP的时候，把英雄创建到sprites中
	private final List<Sprite> sprites = new ArrayList<>();
	//可收割的东西，如树木、草堆、矿石
	private final Map<Integer, HarvestThing> harvests = new HashMap<>();
	//援军
	private Sprite aidSprite;
	//需要被移除的精灵ID
	private int removeSpriteId;
	
	//空闲开始时间，用于清理这个区域的数据以达到空出内存的效果
	private int idleTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, HarvestThing> getHarvests() {
		return harvests;
	}

	public int getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

	public List<Sprite> getSprites() {
		return sprites;
	}

	public Sprite getAidSprite() {
		return aidSprite;
	}

	public void setAidSprite(Sprite aidSprite) {
		this.aidSprite = aidSprite;
	}

	public int getRemoveSpriteId() {
		return removeSpriteId;
	}

	public void setRemoveSpriteId(int removeSpriteId) {
		this.removeSpriteId = removeSpriteId;
	}
}
