/**
 * 
 */
package org.gaming.db.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.db.mysql.dao.DefaultAsyncDao;
import org.gaming.db.mysql.dao.OriginDao;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.cache.EntityJvmCache;
import org.gaming.db.repository.cache.IEntityCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 * 数据缓存
 */
public abstract class CacheRepository<T extends AbstractEntity> extends BaseRepository<T> {

	private static Logger logger = LoggerFactory.getLogger(CacheRepository.class);
	
	private IEntityCache<T> cache;
	
	public CacheRepository(OriginDao<T> baseDao, IEntityCache<T> cache) {
		super(baseDao);
		this.cache = cache;
	}
	
	private boolean isKeysMatch(QueryOptions options) {
		String[] fieldNames = cache.getFieldNames();
		if(options.size() > fieldNames.length) {
			return false;
		} else if(options.size() == 0) {
			return false;
		} else {
			boolean match = true;
			//TODO 是否需要对QueryOptions重排序
			for(int i = 0; i < options.size(); i++) {
				if(!options.get(i).getFieldName().equals(fieldNames[i])) {
					match = false;
				}
			}
			return match;
		}
	}
	
	public T getByMainKey(Object key) {
		if(!cache.isOnlyMainKey()) {
			throw new UnsupportedOperationException("");
		}
		T entity = cache.get(key);
		if(entity == null) {
			if(cache.hasCache(key)) {
				//空节点
				return null;
			} else {
				loadEntitys(key);
				entity = cache.get(key);
			}
		}
		return entity;
	}
	
	public T get(QueryOptions options) {
		T entity = null;
		if(isKeysMatch(options)) {
			Object[] values = options.getValues();
			entity = cache.get(values);
			if(entity == null) {
				if(cache.hasCache(values[0])) {
					//空节点
					return null;
				} else {
					loadEntitys(values[0]);
					entity = cache.get(values);
				}
			}
		} else {
			//TODO 查询选项跟缓存格式不一致，则从数据库中进行查询
			//TODO 检查当前缓存中是否存在该对象，存在则返回该对象
			entity = super.get(options);
			if(entity == null) {
				return null;
			}
			try {
				Object[] values = new Object[cache.getCacheFields().length];
				for(int i = 0; i < cache.getCacheFields().length; i++) {
					Field field = cache.getCacheFields()[i];
					values[i] = field.get(entity);
				}
				T cacheEntity = cache.get(values);
				if(cacheEntity == null) {
					if(cache.getFieldNames().length == 1) {
						cache.addCache(entity);
					} else {
						//多级缓存的直接返回查询数据库得到的对象
						logger.warn("查询条件{}并未匹配缓存设定，该数据从数据库中提取，需谨慎使用", options.toString());
					}
				} else {
					//返回缓存中的对象
					entity = cacheEntity;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}
	
	@Override
	public List<T> getList(QueryOptions options) {
		List<T> entitys = null;
		if(isKeysMatch(options)) {
			Object[] values = options.getValues();
			entitys = cache.getList(values);
			if(entitys == null) {
				if(cache.hasCache(values[0])) {
					return Collections.emptyList();
				} else {
					loadEntitys(values[0]);
					entitys = cache.getList(values);
				}
			} else {
				if(logger.isDebugEnabled()) {
					logger.debug("已取得类{}根据条件{}查询的缓存数据，{}", baseDao.getEntityMeta().getClazz().getSimpleName(), options.toString(), entitys);
				}
			}
		} else {
			//查询选项跟缓存格式不一致，则从数据库中进行查询
			entitys = super.getList(options);
			logger.warn("查询条件{}并未匹配缓存设定，该数据从数据库中提取，需谨慎使用", options.toString());
		}
		return entitys;
	}
	
	public T getByKeys(Object... args) {
		if(args == null || args.length <= 0) {
			throw new RuntimeException("查询条件为空");
		}
		QueryOptions options = new QueryOptions();
		String[] fieldNames = cache.getFieldNames();
		for(int i = 0; i < args.length && i < fieldNames.length; i++) {
			options.put(fieldNames[i], args[i]);
		}
		return get(options);
	}
	
	public List<T> listByKeys(Object... args) {
		QueryOptions options = new QueryOptions();
		String[] fieldNames = cache.getFieldNames();
		for(int i = 0; i < args.length && i < fieldNames.length; i++) {
			options.put(fieldNames[i], args[i]);
		}
		return this.getList(options);
	}
	
	public List<T> getAllInCache() {
		return cache.getAll();
	}
	
	/**
	 * 从数据库中以数据持有者进行数据加载
	 * @param firstKeyValue
	 */
	private synchronized void loadEntitys(Object firstKeyValue) {
		if(cache.hasCache(firstKeyValue)) {
			return;
		}
		String columnName = this.baseDao.getEntityMeta().getColumnName(cache.mainCacheKey());
		String where = "WHERE " + columnName + "=?";
		List<T> list = this.baseDao.queryListWhere(where, firstKeyValue);
		if(list.isEmpty()) {
			cache.createEmptyRoot(firstKeyValue);
		} else {
			cache.addCache(list);
		}
		logger.debug("重新加载类{}根据条件{}={}查询的缓存数据，数据数量{}", baseDao.getEntityMeta().getClazz().getSimpleName(),
				cache.mainCacheKey(), firstKeyValue, list.size());
	}

	@Override
	public void insert(T t) {
		super.insert(t);
		cache.addCache(t);
	}

	@Override
	public void insertAll(List<T> ts) {
		super.insertAll(ts);
		for(T t : ts) {
			cache.addCache(t);
		}
	}

	@Override
	public void update(T t) {
		super.update(t);
		cache.updateCache(t);
	}

	@Override
	public void updateAll(List<T> ts) {
		super.updateAll(ts);
		cache.updateCache(ts);
	}

	@Override
	public void delete(T t) {
		super.delete(t);
		cache.deleteCache(t);
	}
	
	public void deleteAll(List<T> ts) {
		super.deleteAll(ts);
		cache.deleteCache(ts);
	}
	
	public void deleteAll() {
		super.deleteInDb("WHERE 1 = 1");
		cache.cleanCache();
	}

	@Override
	public void loadOnStart() {
		if(cache.needLoadAllOnStart()) {
			List<T> entities = super.getAllInDb();
			cache.addCache(entities);
			logger.info("加载所有的【{}】数据到内存中，数据量【{}】", super.baseDao.getEntityMeta().getClazz().getSimpleName(), entities.size());
		}
	}
	
	public void printCache() {
		try {
			if(cache instanceof EntityJvmCache) {
				EntityJvmCache<T> jvm = (EntityJvmCache<T>)cache;
				jvm.printCache();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public List<T> getAll() {
		List<T> dbList = getAllInDb();
		List<T> cacheList = getAllInCache();
		Map<Object, T> map = new HashMap<>();
		for(T t : cacheList) {
			Object key = this.baseDao.getEntityMeta().getPrimaryKeyValue(t);
			map.put(key, t);
		}
		List<T> result = new ArrayList<>();
		for(T t : dbList) {
			Object key = this.baseDao.getEntityMeta().getPrimaryKeyValue(t);
			T cacheT = map.get(key);
			if(cacheT != null) {
				result.add(cacheT);
			} else {
				result.add(t);
			}
		}
		return result;
	}
	
	/**
	 * 清空缓存
	 */
	public void cleanCache() {
		cache.cleanCache();
		if(cache.needLoadAllOnStart()) {
			if(this.baseDao instanceof DefaultAsyncDao) {
				DefaultAsyncDao<T> asyncDao = (DefaultAsyncDao<T>)this.baseDao;
				asyncDao.insertAllNow();
				asyncDao.updateAllNow();
			}
			List<T> entities = super.getAllInDb();
			cache.addCache(entities);
			logger.info("加载所有的【{}】数据到内存中，数据量【{}】", super.baseDao.getEntityMeta().getClazz().getSimpleName(), entities.size());
		}
	}
	
	public int getCacheSize() {
		return cache.getCacheSize();
	}
}
