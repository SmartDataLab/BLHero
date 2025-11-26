/**
 * 
 */
package org.gaming.db.repository.cache;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * @author YY
 *
 */
public interface IEntityCache<T> {
	/**
	 * 添加缓存对象
	 * @param entitys
	 */
	void addCache(Collection<T> entitys);
	/**
	 * 添加缓存对象
	 * @param entity
	 */
	void addCache(T entity);
	/**
	 * 获取缓存对象
	 * @param keys
	 * @return
	 */
	T get(Object... keys);
	/**
	 * 获取缓存对象
	 * @param keys
	 * @return
	 */
	List<T> getList(Object... keys);
	/**
	 * 获取所有缓存对象
	 * @return
	 */
	List<T> getAll();
	/**
	 * 删除缓存对象
	 * @param entitys
	 */
	void deleteCache(T entity);
	/**
	 * 删除缓存对象
	 * @param entitys
	 */
	void deleteCache(Collection<T> entitys);
	/**
	 * 清空缓存
	 */
	void cleanCache();
	/**
	 * 更新缓存
	 * @param entity
	 */
	void updateCache(T entity);
	/**
	 * 更新缓存
	 * @param entitys
	 */
	void updateCache(List<T> entitys);
	/**
	 * 是否有缓存
	 * @param key
	 * @return
	 */
	boolean hasCache(Object key);
	/**
	 * 获取缓存的主要Key，可以理解为数据的持有者，比如玩家持有装备的数据，玩家持有卡牌的数据等等
	 * @return
	 */
	String mainCacheKey();
	/**
	 * 创建一个空的根节点
	 * 当尝试获得缓存时得到null返回时，应先去数据库查询一遍，当从数据库中查询得到的结果也是空的话，
	 * 在不做处理的情况下，下一次获取相同的缓存数据也会去重新查找数据库，这样就导致缓存被击穿，因此
	 * 在查询数据库之后，如果从数据库中获得的数据也是空，则可以创建一个空的根节点，在根节点失效之前，
	 * 查询的判断会因为有空的根节点而直接返回null，而不是重新去查询数据库，当有数据新增至该根节点
	 * 下时，再次查询缓存将得到结果，通过这样的方式来处理缓存击穿的问题
	 * 
	 * @param keyValue
	 */
	void createEmptyRoot(Object keyValue);
	
	String[] getFieldNames();
	
	Field[] getCacheFields();
	/**
	 * 是否
	 * @return
	 */
	boolean isOnlyMainKey();
	/**
	 * 是否需要在启动的时候加载所有数据
	 * @return
	 */
	boolean needLoadAllOnStart();
	/**
	 * 获取当前缓存数据的根节点数量
	 * @return
	 */
	int getCacheSize();
}
