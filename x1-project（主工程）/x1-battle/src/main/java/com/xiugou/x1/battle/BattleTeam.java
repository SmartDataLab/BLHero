/**
 * 
 */
package com.xiugou.x1.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class BattleTeam {
	private final BattleContext context;
	private final TeamSide side;
	//队伍中的全部精灵<精灵ID，精灵>
	private final Map<Integer, Sprite> allSpriteMap = new HashMap<>();
	private final List<Sprite> aliveSpriteList = new ArrayList<>();
	//死掉的精灵，晚死的在列表后面
	private final List<Sprite> deadSpriteList = new ArrayList<>();
	
	public BattleTeam(BattleContext context, TeamSide side) {
		this.context = context;
		this.side = side;
	}
	
	public void addSprite(Sprite sprite) {
		allSpriteMap.put(sprite.getId(), sprite);
		aliveSpriteList.add(sprite);
	}
	
	public void cleanSprites() {
		allSpriteMap.clear();
		aliveSpriteList.clear();
		deadSpriteList.clear();
	}
	
	public void burySprite(Sprite sprite) {
		deadSpriteList.add(sprite);
		aliveSpriteList.remove(sprite);
	}
	
	public void reliveSprite(Sprite sprite) {
		deadSpriteList.remove(sprite);
		aliveSpriteList.add(sprite);
	}
	
	public void removeSprite(Sprite sprite) {
		aliveSpriteList.remove(sprite);
		deadSpriteList.remove(sprite);
		allSpriteMap.remove(sprite.getId());
	}

	public BattleContext getContext() {
		return context;
	}

	public TeamSide getSide() {
		return side;
	}

	public Sprite getSprite(int spriteId) {
		return allSpriteMap.get(spriteId);
	}

	public List<Sprite> getAliveSpriteList() {
		return aliveSpriteList;
	}

	public List<Sprite> getDeadSpriteList() {
		return deadSpriteList;
	}

	public Map<Integer, Sprite> getAllSpriteMap() {
		return allSpriteMap;
	}
}
