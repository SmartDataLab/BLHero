/**
 * 
 */
package org.gaming.backstage.service;

import java.util.Collections;
import java.util.List;

import org.gaming.db.annotation.Table;
import org.gaming.db.repository.QueryOptions;
import org.gaming.ruler.redis.RedisCache;
import org.gaming.ruler.redis.RedisEmpty;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author YY
 *
 */
public abstract class OneToManyRedisService<T extends OneToManyRedisHashEntity> extends AbstractService<T> {

	@Autowired
	protected RedisCache redisCache;
	
	private String cacheName;
	
	public final List<T> getEntityList(long ownerId) {
		List<T> list = redisCache.getHashAllList(redisKey(ownerId), clazz);
		if(list == null) {
			String cacheEmptyKey = cacheEmptyKey(ownerId);
			if (redisCache.getValue(cacheEmptyKey, RedisEmpty.class) == null) {
				list = repository().getList(queryForOwner(ownerId));
				if (list.isEmpty()) {
					// 当从数据库中查询不出数据时在redis中设定空键，避免重复查数据库获取空数据
					redisCache.insertValue(cacheEmptyKey, RedisEmpty.EMPTY);
					redisCache.setExpire(cacheEmptyKey);
				} else {
					redisCache.updateAllHash(redisKey(ownerId), list);
					redisCache.setExpire(redisKey(ownerId));
					redisCache.deleteKey(cacheEmptyKey(ownerId));
				}
			} else {
				list = Collections.emptyList();
			}
		}
		return list;
	}
	
	public void insert(T t) {
		repository().insert(t);
		if(this.hasCacheEntity(t.redisOwnerKey())) {
			redisCache.updateHash(redisKey(t.redisOwnerKey()), t);
			redisCache.setExpire(redisKey(t.redisOwnerKey()));
		}
	}
	
	public void insertAll(List<T> ts) {
		repository().insertAll(ts);
		for(T t : ts) {
			if(this.hasCacheEntity(t.redisOwnerKey())) {
				redisCache.updateHash(redisKey(t.redisOwnerKey()), t);
				redisCache.setExpire(redisKey(t.redisOwnerKey()));
			}
		}
	}
	
	public void update(T t) {
		repository().update(t);
		if(this.hasCacheEntity(t.redisOwnerKey())) {
			redisCache.updateHash(redisKey(t.redisOwnerKey()), t);
			redisCache.setExpire(redisKey(t.redisOwnerKey()));
		}
	}
	
	public void updateAll(List<T> ts) {
		repository().updateAll(ts);
		for(T t : ts) {
			if(this.hasCacheEntity(t.redisOwnerKey())) {
				redisCache.updateHash(redisKey(t.redisOwnerKey()), t);
				redisCache.setExpire(redisKey(t.redisOwnerKey()));
			}
		}
	}
	
	protected void updateCache(T t) {
		if(this.hasCacheEntity(t.redisOwnerKey())) {
			redisCache.updateHash(redisKey(t.redisOwnerKey()), t);
			redisCache.setExpire(redisKey(t.redisOwnerKey()));
		}
	}
	
	public void delete(T t) {
		repository().delete(t);
		if(this.hasCacheEntity(t.redisOwnerKey())) {
			redisCache.deleteHash(redisKey(t.redisOwnerKey()), t);
		}
	}
	
	public void deleteAll(List<T> ts) {
		repository().deleteAll(ts);
		for(T t : ts) {
			if(this.hasCacheEntity(t.redisOwnerKey())) {
				redisCache.deleteHash(redisKey(t.redisOwnerKey()), t);
			}
		}
	}
	
	public void deleteAllInOwner(long ownerId) {
		List<T> list = this.getEntityList(ownerId);
		if(list.isEmpty()) {
			return;
		}
		repository().deleteAll(list);
		if(this.hasCacheEntity(ownerId)) {
			redisCache.deleteKey(redisKey(ownerId));
		}
	}
	
	public T getEntity(long ownerId, long hashId) {
		// 先尝试从缓存中获取单个数据
		T t = this.getCacheData(ownerId, hashId);
		if (t == null) {
			// 如果缓存中无法获取到单个数据的记录，有可能是数据已被删除，或者是缓存已经过期
			// 因此需要把数据拥有者持有的所有数据重新加载到缓存中
			List<T> list = getEntityList(ownerId);
			for (T temp : list) {
				if (temp.redisHashKey() == hashId) {
					t = temp;
					break;
				}
			}
		}
		return t;
	}
	
	/**
	 * 获取数据拥有者下的单个数据
	 * 
	 * @param ownerId
	 * @param uniqueId
	 * @return
	 */
	protected T getCacheData(long ownerId, long uniqueId) {
		return redisCache.getHash(redisKey(ownerId), uniqueId, clazz);
	}
	
	protected abstract String cacheContext();
	
	protected abstract QueryOptions queryForOwner(long ownerId);
	
	private final String redisKey(long ownerId) {
		return cacheContext() + ":" + ownerId + ":" + cacheName();
	}

	private final String cacheEmptyKey(long ownerId) {
		return redisKey(ownerId) + "empty";
	}
	
	/**
	 * 是否有缓存的数据
	 * @param ownerId
	 * @return
	 */
	protected boolean hasCacheEntity(long ownerId) {
		return redisCache.hasKey(redisKey(ownerId)) || redisCache.hasKey(cacheEmptyKey(ownerId));
	}
	
	protected final String cacheName() {
		if(cacheName == null) {
			Table table = this.clazz.getAnnotation(Table.class);
			if(table != null) {
				cacheName = table.name();
			}
		}
		return cacheName;
	}
}
