/**
 * 
 */
package org.gaming.design.loader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class DesignCacheManager {

	private static Logger logger = LoggerFactory.getLogger(DesignCacheManager.class);
	
	private static Map<String, DesignCache<?>> fileToCache = new HashMap<>();
	
	public static void register(DesignCache<?> cache) {
		if(fileToCache.containsKey(cache.fileName())) {
			throw new RuntimeException("重复的游戏配置缓存类：" + cache.getClass().getSimpleName() + "-" + cache.fileName());
		}
		fileToCache.put(cache.fileName(), cache);
	}
	
	public static void load(String path, IDesignReader reader, boolean strictCheck) {
		loadFull(path, reader, new JsonDesignParser(), strictCheck);
	}
	
	/**
	 * 完整加载
	 * @param path
	 * @param reader
	 * @param designParser
	 */
	public static void loadFull(String path, IDesignReader reader, DesignParser designParser, boolean strictCheck) {
		Map<String, DesignFile> fileMetaMap = loadFiles(path, reader);
		
		boolean hasError = false;
		for(DesignCache<?> designCache : fileToCache.values()) {
			if(designParser != null) {
				designCache.setDesignParser(designParser);
			}
			
			DesignFile fileMeta = fileMetaMap.get(designCache.fileName());
			if(fileMeta == null) {
				hasError = true;
				logger.info("加载失败[{}]，未能找到配置文件", designCache.fileName());
			} else {
				designCache.load(fileMeta.getFields(), fileMeta.getDatas());
				logger.info("加载成功[{}]，配置数量[{}]", fileMeta.getName(), designCache.all().size());
			}
		}
		for(DesignCache<?> designCache : fileToCache.values()) {
			designCache.loadAfterAllReady();
		}
		for(DesignCache<?> designCache : fileToCache.values()) {
			if(!designCache.check()) {
				hasError = true;
			}
		}
		if(strictCheck && hasError) {
			throw new RuntimeException("配置加载失败，请往上翻查日志");
		}
	}
	
	/**
	 * 部分加载
	 * @param path
	 * @param reader
	 * @param designParser
	 */
	public static void loadSome(String path, IDesignReader reader, DesignParser designParser) {
		Map<String, DesignFile> fileMetaMap = loadFiles(path, reader);
		
		for(Entry<String, DesignFile> entry : fileMetaMap.entrySet()) {
			DesignCache<?> designCache = fileToCache.get(entry.getKey());
			if(designCache == null) {
				continue;
			}
			DesignFile fileMeta = entry.getValue();
			designCache.load(fileMeta.getFields(), fileMeta.getDatas());
			logger.info("加载[{}]成功，配置数量[{}]", fileMeta.getName(), designCache.all().size());
		}
		
		boolean hasError = false;
		for(DesignCache<?> designCache : fileToCache.values()) {
			designCache.loadAfterAllReady();
		}
		for(DesignCache<?> designCache : fileToCache.values()) {
			if(!designCache.check()) {
				hasError = true;
			}
		}
		if(hasError) {
			throw new RuntimeException("配置加载失败，请往上翻查日志");
		}
	}
	
	private static Map<String, DesignFile> loadFiles(String path, IDesignReader reader) {
		File dirFile = new File(path);
		Map<String, DesignFile> fileMetaMap = new HashMap<>();
		for(File file : dirFile.listFiles()) {
			String fileName = file.getName();
			String[] parts = fileName.split("\\.");
			fileName = parts[0];
			
			List<DesignFile> fileMetas = reader.read(file);
			for(DesignFile fileMeta : fileMetas) {
				fileMetaMap.put(fileName, fileMeta);
			}
		}
		return fileMetaMap;
	}
}
