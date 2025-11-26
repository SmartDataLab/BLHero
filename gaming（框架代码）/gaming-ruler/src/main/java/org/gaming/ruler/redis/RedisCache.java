/**
 * 
 */
package org.gaming.ruler.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author YY
 *
 */
@Component
public class RedisCache {
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;
	
	//数据的缓存时间
	public static final long TIMEOUT = DateTimeUtil.ONE_HOUR_MILLIS;
	//以毫秒为单位
	private static TimeUnit unit = TimeUnit.MILLISECONDS;

	public <T extends RedisHashObj> T getHash(String redisKey, Object objKey, Class<T> clazz) {
		String str = (String) redisTemplate.opsForHash().get(redisKey, objKey.toString());
		if(str == null) {
			return null;
		}
		return GsonUtil.parseJson(str, clazz);
	}

//	public <T extends RedisHashObj> void updateHash(String redisKey, Object objKey, T t) {
//		String json = GsonUtil.toJson(t);
//		redisTemplate.opsForHash().put(redisKey, objKey.toString(), json);
//	}
	
	public <T extends RedisHashObj> void updateHash(String redisKey, T t) {
		String json = GsonUtil.toJson(t);
		redisTemplate.opsForHash().put(redisKey, t.redisHashKey().toString(), json);
	}
	
//	public <T extends RedisHashObj, K extends Object> void updateAllHash(String redisKey, Map<K, T> objMap) {
//		Map<Object, String> jsonMap = new HashMap<>();
//		for(Entry<K, T> entry : objMap.entrySet()) {
//			String json = GsonUtil.toJson(entry.getValue());
//			jsonMap.put(entry.getKey().toString(), json);
//		}
//		redisTemplate.opsForHash().putAll(redisKey, jsonMap);
//	}
	
	public <T extends RedisHashObj, K extends Object> void updateAllHash(String redisKey, List<T> objList) {
		Map<Object, String> jsonMap = new HashMap<>();
		for(T t : objList) {
			String json = GsonUtil.toJson(t);
			jsonMap.put(t.redisHashKey().toString(), json);
		}
		redisTemplate.opsForHash().putAll(redisKey, jsonMap);
	}
	
	
	public <T extends RedisHashObj> boolean insertHash(String redisKey, Object objKey, T t) {
		String json = GsonUtil.toJson(t);
		return redisTemplate.opsForHash().putIfAbsent(redisKey, objKey.toString(), json);
	}
	
	public <T extends RedisHashObj> void deleteHash(String redisKey, T t) {
		redisTemplate.opsForHash().delete(redisKey, t.redisHashKey().toString());
	}
	
	public void deleteHash(String redisKey, Object objKey) {
		redisTemplate.opsForHash().delete(redisKey, objKey.toString());
	}
	
	public <T extends RedisZsetObj> void updateZset(String redisKey, T t) {
		redisTemplate.opsForZSet().removeRangeByScore(redisKey, t.takeScore(), t.takeScore());
		String json = GsonUtil.toJson(t);
		redisTemplate.opsForZSet().add(redisKey, json, t.takeScore());
	}
	
	/**
	 * 更新同一个key下面的所有数据
	 * @param redisKey
	 * @param t
	 */
	public <T extends RedisZsetObj> void updateOneKeyAllZset(String redisKey, List<T> ts) {
		redisTemplate.delete(redisKey);
		for(T t : ts) {
			String json = GsonUtil.toJson(t);
			redisTemplate.opsForZSet().add(redisKey, json, t.takeScore());
		}
	}
	
	
	public <T extends RedisZsetObj> void deleteZset(String redisKey, T t) {
		redisTemplate.opsForZSet().removeRangeByScore(redisKey, t.takeScore(), t.takeScore());
	}
	
	public <T extends RedisZsetObj> List<T> getZsetList(String redisKey, long start, long end, Class<T> clazz) {
		if(!redisTemplate.hasKey(redisKey)) {
			return null;
		}
		Set<String> strs = redisTemplate.opsForZSet().range(redisKey, start, end);
		List<T> list = new ArrayList<>(strs.size());
		for (String strObj : strs) {
			T t = GsonUtil.parseJson(strObj, clazz);
			if (t != null) {
				list.add(t);
			}
		}
		return list;
	}
	
	

//	public <T extends RedisHashObj> List<T> getHashList(RedisHashKey redisKey, Collection<Object> objKeys,
//			Class<T> clazz) {
//		List<Object> strs = redisTemplate.opsForHash().multiGet(redisKey.value(), objKeys);
//		List<T> list = new ArrayList<>(strs.size());
//		for (Object strObj : strs) {
//			T t = JSONObject.parseObject((String) strObj, clazz);
//			if (t != null) {
//				list.add(t);
//			}
//		}
//		return list;
//	}
	
	/**
	 * 
	 * @param redisKey
	 * @param clazz
	 * @return 当返回null时需要去数据库中查找
	 */
	public <T extends RedisHashObj> List<T> getHashAllList(String redisKey,
			Class<T> clazz) {
		if(!redisTemplate.hasKey(redisKey)) {
			return null;
		}
		List<Object> strs = redisTemplate.opsForHash().values(redisKey);
		List<T> list = new ArrayList<>(strs.size());
		for (Object strObj : strs) {
			T t = GsonUtil.parseJson((String) strObj, clazz);
			if (t != null) {
				list.add(t);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取数据数量
	 * @param redisKey
	 * @return
	 */
	public long getHashSize(String redisKey) {
		return redisTemplate.opsForHash().size(redisKey);
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getHashAllEntries(String redisKey) {
		if(!redisTemplate.hasKey(redisKey)) {
			return Collections.EMPTY_MAP;
		}
		return redisTemplate.opsForHash().entries(redisKey);
	}
	
	public <T> T getValue(String key, Class<T> clazz) {
		String str = redisTemplate.opsForValue().get(key);
		if(str == null) {
			return null;
		}
		return GsonUtil.parseJson(str, clazz);
	}
	
	public <T> void updateValue(String key, T t) {
		String json = GsonUtil.toJson(t);
		redisTemplate.opsForValue().set(key, json);
	}
	
	public <T> void updateValueWillTimeout(String key, T t) {
		String json = GsonUtil.toJson(t);
		redisTemplate.opsForValue().set(key, json, TIMEOUT, unit);
	}
	
	public <T> void updateValueWillTimeout(String key, T t, long timeoutMillis) {
		String json = GsonUtil.toJson(t);
		redisTemplate.opsForValue().set(key, json, timeoutMillis, unit);
	}
	
	public <T> boolean insertValue(String key, T t) {
		String json = GsonUtil.toJson(t);
		return redisTemplate.opsForValue().setIfAbsent(key, json);
	}
	
	public <T> boolean insertValueWillTimeout(String key, T t) {
		String json = GsonUtil.toJson(t);
		return redisTemplate.opsForValue().setIfAbsent(key, json, TIMEOUT, unit);
	}
	
	public <T> boolean insertValueWillTimeout(String key, T t, long timeoutMillis) {
		String json = GsonUtil.toJson(t);
		return redisTemplate.opsForValue().setIfAbsent(key, json, timeoutMillis, unit);
	}
	
	public boolean deleteKey(String key) {
		return redisTemplate.delete(key);
	}
	
	public long deleteKeys(Collection<String> keys) {
		return redisTemplate.delete(keys);
	}
	
	public void setExpire(String key) {
		redisTemplate.expire(key, TIMEOUT, unit);
	}
	
	public void setExpire(String key, long milliSeconds) {
		redisTemplate.expire(key, milliSeconds, unit);
	}
	/**
	 * 插入一个redis的定时锁
	 * @param key
	 * @param t
	 * @param lockTimeMS
	 * @return
	 */
	public <T> boolean insertLock(String key, T t, long lockTimeMS) {
		String json = GsonUtil.toJson(t);
		return redisTemplate.opsForValue().setIfAbsent(key, json, lockTimeMS, unit);
	}
	public <T> void updateLock(String key, T t, long lockTimeMS) {
		String json = GsonUtil.toJson(t);
		redisTemplate.opsForValue().set(key, json, lockTimeMS, unit);
	}
	/**
	 * 发布消息到指定频道
	 */
	public void publish(String channelKey, String message) {
		redisTemplate.convertAndSend(channelKey, message);
	}
	
	/**
	 * 是否有缓存
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	
	/**
	 * 获取键的过期时间
	 * @param key
	 * @return
	 */
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, unit);
	}
	
	/**
	 * 模糊查询redis库中的key
	 * @param pattern
	 * @return
	 */
	public Set<String> getKeysLike(String pattern) {
		return redisTemplate.keys(pattern);
	}
	
	public <T extends RedisListObj> List<T> getList(String redisKey,
			Class<T> clazz) {
		if(!redisTemplate.hasKey(redisKey)) {
			return null;
		}
		List<String> strs = redisTemplate.opsForList().range(redisKey, 0, -1);
		List<T> list = new ArrayList<>(strs.size());
		for (Object strObj : strs) {
			T t = GsonUtil.parseJson((String) strObj, clazz);
			if (t != null) {
				list.add(t);
			}
		}
		return list;
	}
	
	public <T extends RedisListObj> void updateList(String redisKey, List<T> ts) {
		if(redisTemplate.hasKey(redisKey)) {
			redisTemplate.delete(redisKey);
		}
		for(T t : ts) {
			String json = GsonUtil.toJson(t);
			redisTemplate.opsForList().rightPush(redisKey, json);
		}
	}
}
