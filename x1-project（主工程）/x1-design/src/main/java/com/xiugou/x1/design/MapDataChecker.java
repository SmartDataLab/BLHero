/**
 * 
 */
package com.xiugou.x1.design;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MonsterPoint;
import com.xiugou.x1.battle.mapdata2.ResourcePoint;
import com.xiugou.x1.design.module.HarvestCache;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.SceneZoneCache;
import com.xiugou.x1.design.module.autogen.HarvestAbstractCache.HarvestCfg;
import com.xiugou.x1.design.module.autogen.MonsterAbstractCache.MonsterCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;

/**
 * @author YY
 *
 */
@Component
public class MapDataChecker {

	private static Logger logger = LoggerFactory.getLogger(MapDataChecker.class);
	
	@Autowired
	private SceneZoneCache sceneZoneCache;
	@Autowired
	private MonsterCache monsterCache;
	@Autowired
	private HarvestCache harvestCache;
	
	/**
	 * 验证地图数据与配置数据是否有问题
	 */
	public void checkMapData() {
		List<String> errorList = new ArrayList<>();
		for(SceneZoneCfg sceneZoneCfg : sceneZoneCache.all()) {
			if("".equals(sceneZoneCfg.getMapData())) {
				logger.warn("{}区域的地图文件未配置", sceneZoneCfg.getComment());
				continue;
			}
			MapData2 mapData = MapCacheManager2.getMapcache().get(sceneZoneCfg.getMapData());
			if(mapData == null) {
				errorList.add(String.format("未找到%s区域的地图文件%s", sceneZoneCfg.getComment(), sceneZoneCfg.getMapData()));
				continue;
			}
			for(MonsterPoint monsterPoint : mapData.getMonster_point()) {
				//怪堆
				MonsterCfg monsterCfg = monsterCache.getOrNull(monsterPoint.getId());
				if(monsterCfg == null) {
					errorList.add(String.format("%s的地图文件中的怪物点%s未找到对应的怪物配置", sceneZoneCfg.getComment(), monsterPoint.getId()));
				}
			}
			for(ResourcePoint resourcePoint : mapData.getResource_point()) {
				//资源堆
				HarvestCfg harvestCfg = harvestCache.getOrNull(resourcePoint.getId());
				if(harvestCfg == null) {
					errorList.add(String.format("%s区域的地图文件中的资源点%s未找到对应的资源配置", sceneZoneCfg.getComment(), resourcePoint.getId()));
				}
			}
		}
		for(String errorMsg : errorList) {
			logger.error(errorMsg);
		}
		if(!errorList.isEmpty()) {
//			throw new RuntimeException("地图数据有误，请往上翻查日志");
		}
	}
}
