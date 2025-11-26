/**
 * 
 */
package com.xiugou.x1.battle.mapdata2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class MapData2 {
	private List<MonsterPoint> monster_point = new ArrayList<>();
	private List<ResourcePoint> resource_point = new ArrayList<>();
	private List<MapMonsterPoint> map_monster_point = new ArrayList<>();
	
	public List<MonsterPoint> getMonster_point() {
		return monster_point;
	}
	public List<ResourcePoint> getResource_point() {
		return resource_point;
	}
	public List<MapMonsterPoint> getMap_monster_point() {
		return map_monster_point;
	}
}
