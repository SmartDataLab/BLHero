/**
 * 
 */
package com.xiugou.x1.battle.mapdata2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class MapCacheManager2 {
	
	private static Logger logger = LoggerFactory.getLogger(MapCacheManager2.class);

	private static Map<String, MapData2> mapcache = new HashMap<>();

	public static void loadMap(String mapDir) {
		System.out.println(mapDir);
		File dir = new File(mapDir);
		for (File mapFile : dir.listFiles()) {
			if(!mapFile.getName().endsWith(".json")) {
				continue;
			}
			logger.info("加载地图文件{}开始", mapFile.getName());
			try {
				BufferedReader br = new BufferedReader(new FileReader(mapFile));
				StringBuilder builder = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					builder.append(line);
					line = br.readLine();
				}
				br.close();
				MapData2 mapData = GsonUtil.parseJson(builder.toString(), MapData2.class);
				mapcache.put(mapFile.getName(), mapData);
				logger.info("加载地图文件{}完成，怪物点数量{}，资源点数量{}，布怪点数量{}", mapFile.getName(), mapData.getMonster_point().size(),
						mapData.getResource_point().size(), mapData.getMap_monster_point().size());
			} catch (FileNotFoundException e) {
				logger.error("加载地图文件异常", e);
			} catch (IOException e) {
				logger.error("加载地图文件" + mapFile.getName() + "异常", e);
			}
		}
	}

	public static Map<String, MapData2> getMapcache() {
		return mapcache;
	}
}
