/**
 * 
 */
package org.gaming.db.repository.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.db.annotation.JvmCache;
import org.gaming.db.orm.EntityMeta;
import org.gaming.db.util.ReflectUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class EntityJvmCache<T> implements IEntityCache<T> {

	private static Logger logger = LoggerFactory.getLogger(EntityJvmCache.class);
	
	private final ConcurrentMap<String, CacheRoot<T>> cacheGroupMap = new ConcurrentHashMap<>();
	
	private Field[] cacheFields;
	//对象缓存的时间
	private long cacheTime;
	//首个缓存失效的时间
	private long firstExpireTime;
	
	private String[] fieldNames;
	
	private Class<T> clazz;
	//是否在启动的时候加载所有数据
	private boolean loadAllOnStart;
	
	public EntityJvmCache(EntityMeta<T> entityMeta) {
		
		clazz = entityMeta.getClazz();
		
		JvmCache jvmCache = clazz.getAnnotation(JvmCache.class);
		
		if(jvmCache.relation().length == 0) {
			cacheFields = new Field[1];
			cacheFields[0] = entityMeta.getPrimaryKeyMeta().getField();
		} else {
			Map<String, Field> fieldMap = ReflectUtil.getAllFieldsMap(clazz);
			cacheFields = new Field[jvmCache.relation().length];
			for(int i = 0; i < jvmCache.relation().length; i++) {
				String fieldName = jvmCache.relation()[i];
				Field field = fieldMap.get(fieldName);
				if(field == null) {
					throw new RuntimeException(clazz + " doesn't contains field named " + fieldName);
				}
				field.setAccessible(true);
				cacheFields[i] = field;
			}
		}
		cacheTime = jvmCache.cacheTime();
		loadAllOnStart = jvmCache.loadAllOnStart();
		
		fieldNames = new String[cacheFields.length];
		for(int i = 0; i < cacheFields.length; i++) {
			fieldNames[i] = cacheFields[i].getName();
		}
	}
	
	public void addCache(Collection<T> entitys) {
		entitys.stream().forEach(entity -> addCache(entity));
	}
	
	public void addCache(T entity) {
		try {
			Field field = cacheFields[0];
			String key = field.get(entity).toString();
			CacheRoot<T> root = cacheGroupMap.get(key);
			if(root == null) {
				root = initRoot(field.getName(), key);
			}
			synchronized (root) {
				addNode(entity, root, 1);
				//设置过期时间
				root.setExpireTime(System.currentTimeMillis());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void addNode(T entity, CacheNode<T> parentNode, int fieldIndex) throws IllegalArgumentException, IllegalAccessException {
		if(fieldIndex >= cacheFields.length) {
			if(parentNode.getEntity() != null) {
				//重复的实体异常
				StringBuilder builder = new StringBuilder();
				CacheNode<T> node = parentNode;
				while(node != null) {
					builder.insert(0, String.format(" (%s = %s)", node.getFieldName(), node.getKeyValue()));
					node = node.getParent();
				}
				builder.insert(0, "duplicate entity try to add to the cache, className = " + clazz.getSimpleName() + " with");
				throw new RuntimeException(builder.toString());
			}
			parentNode.setEntity(entity);
		} else {
			Field field = cacheFields[fieldIndex];
			String key = field.get(entity).toString();
			parentNode.initCache();
			CacheNode<T> childNode = parentNode.getChild(key);
			if(childNode == null) {
				childNode = new CacheNode<T>(parentNode, field.getName(), key);
				parentNode.addChild(key, childNode);
			}
			addNode(entity, childNode, fieldIndex + 1);
		}
	}
	
	
	private synchronized CacheRoot<T> initRoot(String fieldName, String key) {
		CacheRoot<T> root = cacheGroupMap.get(key);
		if(root != null) {
			return root;
		}
		root = new CacheRoot<T>(fieldName, key);
		
		cacheGroupMap.put(key, root);
		return root;
	}
	
	public String printCache() {
		StringBuilder builder = new StringBuilder();
		printNode(builder, cacheGroupMap, "");
		logger.info(builder.toString());
		return builder.toString();
	}
	
	private void printNode(StringBuilder builder, Map<String, ? extends CacheNode<T>> nodeMap, String tab) {
		for(Entry<String, ? extends CacheNode<T>> entry : nodeMap.entrySet()) {
			CacheNode<T> node = entry.getValue();
			builder.append("\n").append(tab).append(entry.getKey() + ":" + node.getFieldName() + "=" + node.getKeyValue() + ", entity=" + node.getEntity() + ", data=" + GsonUtil.toJson(node.getEntity()));
			if(node.getChildNodes() != null) {
				builder.append(", cacheList=" + node.getChildList().size() + ", cacheMap=");
				printNode(builder, node.getChildNodes(), tab + "\t");
			}
		}
	}
	
	public T get(Object... keys) {
		List<T> list = getList(keys);
		if(list == null) {
			return null;
		}
		if(list.size() > 1) {
			throw new RuntimeException("result set for keys " + Arrays.toString(keys) + " more than one");
		} else if(list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 返回null表示没有缓存数据
	 * 返回空列表，表示有缓存，但没有数据
	 * 
	 */
	public List<T> getList(Object... keys) {
		if(keys.length <= 0) {
			throw new RuntimeException("attemp to find empty keys");
		}
		if(keys.length > cacheFields.length) {
			//缓存的深度在类定义的时候就已经指定，如果想要获得得到超过缓存深度的数据，则会抛出此异常
			throw new RuntimeException("cache deep " + cacheFields.length + ", but want to get deep " + keys.length);
		}
		checkExpire();
		
		String key = keys[0].toString();
		CacheRoot<T> root = cacheGroupMap.get(key);
		if(root == null) {
			return null;
		}
		
		List<T> resultList = new ArrayList<>();
		
		CacheNode<T> node = root;
		for(int i = 1; i <= keys.length; i++) {
			if(i == keys.length) {
				fillList(resultList, node);
			} else {
				node = node.getChild(keys[i].toString());
				if(node == null) {
					break;
				}
			}
		}
		root.setExpireTime(System.currentTimeMillis());
		return resultList;
	}
	
	public List<T> getAll() {
		List<T> resultList = new ArrayList<>();
		getInNode(resultList, cacheGroupMap);
		return resultList;
	}
	
	private void getInNode(List<T> resultList, Map<String, ? extends CacheNode<T>> nodes) {
		for(Entry<String, ? extends CacheNode<T>> entry : nodes.entrySet()) {
			CacheNode<T> node = entry.getValue();
			if(node.getEntity() != null) {
				resultList.add(node.getEntity());
			}
			if(node.getChildNodes() != null) {
				getInNode(resultList, node.getChildNodes());
			}
		}
	}
	
	/**
	 * 判断当前是否有缓存，用于缓存击穿
	 * @param key
	 * @return
	 */
	public boolean hasCache(Object key) {
		return cacheGroupMap.containsKey(key.toString());
	}
	
	private void fillList(List<T> resultList, CacheNode<T> node) {
		if(node.getChildList() == null) {
			if(node.getEntity() != null) {
				//TODO 为了避免缓存击穿，在查询不到数据时会添加空的根节点，其中的entity等于null
				resultList.add(node.getEntity());
			}
		} else {
			for(CacheNode<T> childNode : node.getChildList()) {
				fillList(resultList, childNode);
			}
		}
	}
	
	
	public void deleteCache(T entity) {
		try {
			Field field = cacheFields[0];
			String key = field.get(entity).toString();
			CacheRoot<T> root = cacheGroupMap.get(key);
			if(root == null) {
				return;
			}
			
			CacheNode<T> node = root;
			for(int i = 1; i <= cacheFields.length; i++) {
				if(node.getEntity() != null) {
					if(node.getKeyValue().equals(key)) {
						node.setEntity(null);
						if(node.getParent() != null) {
							node.getParent().deleteCache(key);
						}
						node = node.getParent();
						break;
					}
				} else {
					field = cacheFields[i];
					key = field.get(entity).toString();
					
					node = node.getChild(key);
					if(node == null) {
						return;
					}
				}
			}
			
			//将本层已经空了的缓存节点删除
			CacheNode<T> curr = node;
			while(curr != null) {
				if(!curr.isEmpty()) {
					break;
				}
				CacheNode<T> parent = curr.getParent();
				if(parent != null) {
					parent.deleteCache(curr.getKeyValue());
				} else {
					//删除空的根节点
					deleteRoot(curr.getKeyValue());
				}
				curr = parent;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCache(Collection<T> entitys) {
		entitys.stream().forEach(entity -> deleteCache(entity));
	}
	
	/**
	 * 检查失效的缓存
	 * 在获取缓存数据的时候检测失效的数据，存在的问题是，
	 * 如果只对缓存进行增删改的操作，而没有去查询缓存，那么缓存数据会一直存在于缓存中
	 * 但是如果使用了缓存结构而没有去进行查询，那就不应该去用缓存结构
	 */
	private void checkExpire() {
		if(cacheTime == 0) {
			return;
		}
		if(cacheGroupMap.isEmpty()) {
			return;
		}
		long nowTime = System.currentTimeMillis();
		if(nowTime < firstExpireTime) {
			return;
		}
		synchronized (this) {
			long nextExpireTime = Long.MAX_VALUE;
			for(CacheRoot<T> root : cacheGroupMap.values()) {
				long expireTime = root.getExpireTime() + cacheTime;
				
				if(nowTime >= root.getExpireTime() + cacheTime) {
					deleteRoot(root.getKeyValue());
				}
				if(expireTime < nextExpireTime) {
					nextExpireTime = expireTime;
				}
			}
			firstExpireTime = nextExpireTime;
		}
	}
	
	private void deleteRoot(String keyValue) {
		this.cacheGroupMap.remove(keyValue);
	}
	
	/**
	 * 
	 */
	public void createEmptyRoot(Object keyValue) {
		initRoot(cacheFields[0].getName(), keyValue.toString());
	}
	
	public String mainCacheKey() {
		return cacheFields[0].getName();
	}
	
	public String[] getFieldNames() {
		return fieldNames;
	}

	public Field[] getCacheFields() {
		return cacheFields;
	}
	
	public boolean isOnlyMainKey() {
		return cacheFields.length == 1;
	}
	
	public boolean needLoadAllOnStart() {
		return loadAllOnStart;
	}

	@Override
	public void updateCache(T entity) {
		
	}

	@Override
	public void updateCache(List<T> entitys) {
		
	}

	@Override
	public void cleanCache() {
		cacheGroupMap.clear();
	}

	@Override
	public int getCacheSize() {
		return cacheGroupMap.size();
	}
}
