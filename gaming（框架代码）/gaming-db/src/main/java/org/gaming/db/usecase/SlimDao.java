/**
 * 
 */
package org.gaming.db.usecase;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.db.mysql.dao.DaoManager;
import org.gaming.db.mysql.dao.OriginDao;
import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.BaseRepository;
import org.gaming.db.repository.CacheRepository;
import org.gaming.db.valueformat.ValueFormatters.ValueFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class SlimDao {
	
	private static Logger logger = LoggerFactory.getLogger(SlimDao.class);
	
	private static DaoManager daoManager;
	
	private static final Map<String, Class<?>> classSet = new HashMap<>();
	
//	public static void register(AbstractEntity abstractEntity) {
//		classSet.add(abstractEntity.getClass());
//	}
	
//	public static void register(Class<?> clazz) {
//		classSet.put(clazz.getSimpleName(), clazz);
//	}
	
//	public static void build(List<DBConfig> dbConfigs, ValueFormatter... valueFormater) {
//		daoManager = new DaoManager(dbConfigs, classSet.values(), valueFormater);
//	}
	
	public static void build(List<DBConfig> dbConfigs, Collection<Class<?>> entityClasses, ValueFormatter... valueFormater) {
		daoManager = new DaoManager(dbConfigs, entityClasses, valueFormater);
		daoManager.loadRepository();
	}
	
	public static <T extends AbstractEntity> OriginDao<T> getDao(int zoneId, Class<T> clazz) {
		if(daoManager == null) {
			throw new RuntimeException("使用SlimDaoManager前需要先调用build函数");
		}
		return daoManager.getDao(zoneId, clazz);
	}
	
	public static <T extends AbstractEntity> OriginDao<T> getDao(Class<T> clazz) {
		return getDao(1, clazz);
	}
	
	public static <T extends AbstractEntity> BaseRepository<T> getRepository(int zoneId, Class<T> clazz) {
		if(daoManager == null) {
			throw new RuntimeException("使用SlimDaoManager前需要先调用build函数");
		}
		return daoManager.getRepository(zoneId, clazz);
	}
	
	public static <T extends AbstractEntity> BaseRepository<T> getRepository(Class<T> clazz) {
		return getRepository(1, clazz);
	}
	
	public static <T extends AbstractEntity> CacheRepository<T> getCacheRepository(Class<T> clazz) {
		return (CacheRepository<T>)getRepository(1, clazz);
	}
	
	
	public static List<OriginDao<?>> getAllDaos() {
		if(daoManager == null) {
			throw new RuntimeException("使用SlimDaoManager前需要先调用build函数");
		}
		return daoManager.getAllDaos();
	}
	
	public static DaoManager getManager() {
		return daoManager;
	}
	
	public static <T extends AbstractEntity> void printCacheRepository(Class<T> clazz) {
		BaseRepository<T> repository = getRepository(clazz);
		if(repository == null) {
			return;
		}
		if(repository instanceof CacheRepository) {
			CacheRepository<T> cache = (CacheRepository<T>)repository;
			logger.info("打印{}缓存", clazz.getSimpleName());
			cache.printCache();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void printCacheRepository(String clazzName) {
		Class<?> clazz = classSet.get(clazzName);
		BaseRepository repository = getRepository((Class<AbstractEntity>)clazz);
		if(repository == null) {
			return;
		}
		if(repository instanceof CacheRepository) {
			CacheRepository cache = (CacheRepository)repository;
			logger.info("打印{}缓存", clazz.getSimpleName());
			cache.printCache();
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static void printCacheNum() {
		Map<Class<?>, BaseRepository<?>> repositorys = daoManager.getZoneRepository(1);
		for(Entry<Class<?>, BaseRepository<?>> entry : repositorys.entrySet()) {
			BaseRepository repository = entry.getValue();
			if(!(repository instanceof CacheRepository)) {
				continue;
			}
			CacheRepository cache = (CacheRepository)repository;
			logger.info("缓存{}包含数据{}", entry.getKey().getSimpleName(), cache.getCacheSize());
		}
	}
}
