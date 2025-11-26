/**
 * 
 */
package com.xiugou.x1.game.server.foundation.starting;

import java.util.List;

import org.gaming.design.export.CsvReader;
import org.gaming.design.loader.DesignCacheManager;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.mapdata2.MapCacheManager2;
import com.xiugou.x1.battle.mapdata2.MapData2;
import com.xiugou.x1.battle.mapdata2.MonsterPoint;
import com.xiugou.x1.battle.mapdata2.ResourcePoint;
import com.xiugou.x1.design.MapDataChecker;
import com.xiugou.x1.design.TipsHelper;
import com.xiugou.x1.design.X1SeparatorDesignParser;
import com.xiugou.x1.design.module.HarvestCache;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.SceneZoneCache;
import com.xiugou.x1.design.module.autogen.HarvestAbstractCache.HarvestCfg;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;

/**
 * @author YY
 *
 */
@Component
public class ConfigLifecycle implements Lifecycle {

	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private MapDataChecker mapDataChecker;
	@Autowired
	private SceneZoneCache sceneZoneCache;
	@Autowired
	private MonsterCache monsterCache;
	@Autowired
	private HarvestCache harvestCache;
	
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.SYSTEM, Ordinal.MIN);
	}

	public void start() throws Exception {
		EventBus.useTracer(applicationSettings.isGameDebugMode());
		TipsHelper.language = applicationSettings.getGameLanguage();
		
		String mapFilePath = null;
		if (applicationSettings.getMapFilePath().startsWith("file")) {
			ResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource(applicationSettings.getMapFilePath());
			mapFilePath = resource.getFile().getPath();
		} else {
			mapFilePath = applicationSettings.getMapFilePath();
		}
		
		String csvFilePath = null;
		if (applicationSettings.getCsvFilePath().startsWith("file")) {
			ResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource(applicationSettings.getCsvFilePath());
			csvFilePath = resource.getFile().getPath();
		} else {
			csvFilePath = applicationSettings.getCsvFilePath();
		}
		
		MapCacheManager2.loadMap(mapFilePath);
		DesignCacheManager.loadFull(csvFilePath, new CsvReader(), new X1SeparatorDesignParser(), false);
		mapDataChecker.checkMapData();
		
		List<SceneZoneCfg> sceneZoneCfgs = sceneZoneCache.getInSceneIdCollector(1001);
		for(SceneZoneCfg cfg : sceneZoneCfgs) {
			MapData2 mapData = MapCacheManager2.getMapcache().get(cfg.getMapData());
			// 初始化怪物分组
			if(mapData != null && mapData.getMonster_point() != null) {
				for (MonsterPoint monsterPoint : mapData.getMonster_point()) {
					for (int i = 0; i < monsterPoint.getMax_num(); i++) {
						IMonsterConfig monsterConfig = monsterCache.getConfig(monsterPoint.getId());
						if(monsterConfig == null) {
							System.out.println("场景" + cfg.getSceneId() + "的地图数据中设置了怪物，但配置表未找到怪物" + monsterPoint.getId());
						} else if(monsterConfig.getRefreshTime() <= 0) {
							System.out.println("怪物表怪物重生时间需要检查 " + monsterPoint.getId());
						}
					}
				}
			}
			

			// 初始化刷怪点
			if(mapData != null && mapData.getMap_monster_point() != null) {
//				for (MapMonsterPoint monsterPoint : mapData.getMap_monster_point()) {
//				}
			}
			

			// 初始化树木、矿石等收割分组
			if(mapData != null && mapData.getResource_point() != null) {
				for (ResourcePoint harvestPoint : mapData.getResource_point()) {
					for (int i = 0; i < harvestPoint.getMax_num(); i++) {
						HarvestCfg harvestCfg = harvestCache.getOrThrow(harvestPoint.getId());
						if(harvestCfg.getRefreshTime() <= 0) {
							System.out.println("收割物表重生时间需要检查 " + harvestCfg.getId());
						}
					}
				}
			}
			
		}
	}
	
	public void reloadConfig() {
		DesignCacheManager.loadFull(applicationSettings.getCsvFilePath(), new CsvReader(), new X1SeparatorDesignParser(), true);
	}
}
