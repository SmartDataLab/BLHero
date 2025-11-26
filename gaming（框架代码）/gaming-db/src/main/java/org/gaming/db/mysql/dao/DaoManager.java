/**
 * 
 */
package org.gaming.db.mysql.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.db.annotation.JvmCache;
import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.mysql.database.DBManager;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.orm.DefaultAsyncEntityMeta;
import org.gaming.db.orm.DefaultEntityMeta;
import org.gaming.db.orm.EntityMeta;
import org.gaming.db.orm.LogAsyncEntityMeta;
import org.gaming.db.orm.LogEntityMeta;
import org.gaming.db.orm.MetaManager;
import org.gaming.db.repository.BaseRepository;
import org.gaming.db.repository.CacheRepository;
import org.gaming.db.repository.cache.EntityJvmCache;
import org.gaming.db.valueformat.ValueFormatters;
import org.gaming.db.valueformat.ValueFormatters.ValueFormatter;
import org.gaming.tool.ListMapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class DaoManager {
	
	private static Logger logger = LoggerFactory.getLogger(DaoManager.class);
	
	/**
	 * 程序运行期内实体类Dao集合
	 * <区号，<实体类类型，对应该实体类运行期Dao>>
	 */
	private Map<Integer, Map<Class<?>, OriginDao<?>>> RUNTIME_DAOS;
	/**
	 * 运行期内的异步实体类Dao列表
	 */
	private List<IAsyncDao<?>> RUNTIME_ASYNC_DAOS;
	/**
	 * 运行期内的数据缓存仓库
	 */
	private Map<Integer, Map<Class<?>, BaseRepository<?>>> RUNTIME_REPOSITORY;
	
	public DaoManager(List<DBConfig> dbConfigs, Collection<Class<?>> entityClasses, ValueFormatter... valueFormater) {
		this(dbConfigs, entityClasses, 4, valueFormater);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DaoManager(List<DBConfig> dbConfigs, Collection<Class<?>> entityClasses, int poolSize, ValueFormatter... valueFormater) {
		
		//加载数据库连接信息文件
		List<DataBase> dbs = DBManager.loadDBS(dbConfigs);
		Map<String, List<DataBase>> dbsByAlias = ListMapUtil.fillListMap(dbs, DataBase::getAlias);
		
		//构建所有实体类的运行期元数据信息
		MetaManager.buildMeta(entityClasses);
		
		ValueFormatters.addFormater(valueFormater);
		
		//用于保存所有运行期Dao的集合
		Map<Integer, Map<Class<?>, OriginDao<?>>> tempRuntimeDaos = new HashMap<>();
		
		
		Map<String, List<EntityMeta<?>>> metaByAlias = ListMapUtil.fillListMap(new ArrayList<>(MetaManager.getMetas().values()), EntityMeta::getDbAlias);
		
		for(Entry<String, List<EntityMeta<?>>> entry : metaByAlias.entrySet()) {
			String dbAlias = entry.getKey();
			List<DataBase> dbList = dbsByAlias.get(dbAlias);
			
			for(EntityMeta<?> entityMeta : entry.getValue()) {
				if(dbList == null) {
                    throw new RuntimeException("[" + entityMeta.getClazz().getSimpleName() + "]无法找到别名为[" + entityMeta.getDbAlias() + "]的数据源信息");
				}
				for(DataBase entityDb : dbList) {
					//通过实体类对应的数据库与元数据信息，动态地创建出该实体类对应的Dao对象
					OriginDao<?> runtimeDao = null;
					
					if(entityMeta instanceof DefaultAsyncEntityMeta) {
						runtimeDao = new DefaultAsyncDao(entityDb, (DefaultAsyncEntityMeta)entityMeta);
						
					} else if(entityMeta instanceof DefaultEntityMeta) {
						runtimeDao = new DefaultSyncDao(entityDb, (DefaultEntityMeta)entityMeta);
						
					} else if(entityMeta instanceof LogAsyncEntityMeta) {
						runtimeDao = new LogAsyncDao(entityDb, (LogAsyncEntityMeta)entityMeta);
						
					} else if(entityMeta instanceof LogEntityMeta) {
						runtimeDao = new LogSyncDao(entityDb, (LogEntityMeta)entityMeta);
						
					} else {
						throw new RuntimeException("unknow type entity meta " + entityMeta.getClazz().getSimpleName());
					}
					
					int zoneId = entityDb.getDbConfig().getId();
					Map<Class<?>, OriginDao<?>> zoneDaos = tempRuntimeDaos.get(zoneId);
					if(zoneDaos == null) {
						zoneDaos = new HashMap<>();
						tempRuntimeDaos.put(zoneId, zoneDaos);
					}
					zoneDaos.put(entityMeta.getClazz(), runtimeDao);
					logger.info("初始化[{}]数据保存器，类型{}，所在数据区{}-{}", runtimeDao.getEntityMeta().getTableName(),
							runtimeDao.getClass().getSimpleName(), zoneId, entityDb.getAlias());
				}
			}
		}
		RUNTIME_DAOS = tempRuntimeDaos;
		
		initRuntimeAsyncDaos();
		initRuntimeRepositorys();
		startAsync(poolSize);
	}
	
	private void initRuntimeAsyncDaos() {
		//异步Dao列表
		List<IAsyncDao<?>> tempRuntimeAsyncDaos = new ArrayList<>();
		for(Map<Class<?>, OriginDao<?>> daoMap : RUNTIME_DAOS.values()) {
			for(OriginDao<?> dao : daoMap.values()) {
				if(dao instanceof IAsyncDao) {
					tempRuntimeAsyncDaos.add((IAsyncDao<?>)dao);
				}
			}
		}
		RUNTIME_ASYNC_DAOS = tempRuntimeAsyncDaos;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initRuntimeRepositorys() {
		//缓存仓库
		Map<Integer, Map<Class<?>, BaseRepository<?>>> tempRuntimeRepositorys = new HashMap<>();
		for(Entry<Integer, Map<Class<?>, OriginDao<?>>> entry : RUNTIME_DAOS.entrySet()) {
			Map<Class<?>, BaseRepository<?>> daoMap = new HashMap<>();
			tempRuntimeRepositorys.put(entry.getKey(), daoMap);
			
			for(OriginDao<?> dao : entry.getValue().values()) {
				BaseRepository<?> runtimeRepository = null;
				if(dao.getEntityMeta().getClzAnnotation(JvmCache.class) != null) {
					runtimeRepository = new CacheRepository(dao, new EntityJvmCache<>(dao.getEntityMeta())) {};
				} else {
					runtimeRepository = new BaseRepository(dao) {};
				}
				daoMap.put(dao.getEntityMeta().getClazz(), runtimeRepository);
			}
		}
		RUNTIME_REPOSITORY = tempRuntimeRepositorys;
	}
	
	public void loadRepository() {
		for(Map<Class<?>, BaseRepository<?>> entry : RUNTIME_REPOSITORY.values()) {
			for(BaseRepository<?> repository : entry.values()) {
				repository.loadOnStart();
			}
		}
	}
	
	
	/**
	 * 通过区号、实体类类型获取对应的运行期Dao
	 * @param zoneId
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity> OriginDao<T> getDao(int zoneId, Class<T> clazz) {
		Map<Class<?>, OriginDao<?>> daos = RUNTIME_DAOS.get(zoneId);
		if(daos == null) {
			throw new RuntimeException("没有区号["+zoneId+"]对应的运行期Dao");
		}
		OriginDao<?> baseDao = daos.get(clazz);
		if(baseDao == null) {
			throw new RuntimeException("没有实体类["+clazz.getSimpleName()+"]对应的运行期Dao");
		}
		return (OriginDao<T>) baseDao;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity> BaseRepository<T> getRepository(int zoneId, Class<T> clazz) {
		Map<Class<?>, BaseRepository<?>> repositorys = RUNTIME_REPOSITORY.get(zoneId);
		if(repositorys == null) {
			throw new RuntimeException("没有区号["+zoneId+"]对应的运行期Repository");
		}
		BaseRepository<?> baseRepository = repositorys.get(clazz);
		if(baseRepository == null) {
			throw new RuntimeException("没有实体类["+clazz.getSimpleName()+"]对应的运行期Repository");
		}
		return (BaseRepository<T>) baseRepository;
	}
	
	public Map<Class<?>, BaseRepository<?>> getZoneRepository(int zoneId) {
		Map<Class<?>, BaseRepository<?>> repositorys = RUNTIME_REPOSITORY.get(zoneId);
		if(repositorys == null) {
			return Collections.emptyMap();
		}
		return repositorys;
	}
	
	/**
	 * 获取所有的运行期Dao
	 * @return
	 */
	public List<OriginDao<?>> getAllDaos() {
		List<OriginDao<?>> allDaos = new ArrayList<>();
		for(Map<Class<?>, OriginDao<?>> daoMap : RUNTIME_DAOS.values()) {
			for(OriginDao<?> baseDao : daoMap.values()) {
				allDaos.add(baseDao);
			}
		}
		return allDaos;
	}
	
	/**
	 * 开启异步Dao的定时任务
	 */
	private void startAsync(int poolSize) {
		if(RUNTIME_ASYNC_DAOS.isEmpty()) {
			return;
		}
		DaoExecutorService.start(poolSize, RUNTIME_ASYNC_DAOS);
	}
	
	public static void stopAsync() {
		DaoExecutorService.shutdownDaoExecutor();
	}
}
