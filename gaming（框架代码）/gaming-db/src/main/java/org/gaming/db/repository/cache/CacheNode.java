/**
 * 
 */
package org.gaming.db.repository.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YY
 *
 */
public class CacheNode<T> {
	/**
	 * 类的字段名，本层的缓存键
	 */
	private String fieldName;
	/**
	 * 本层的缓存键值
	 */
	private String keyValue;
	/**
	 * 被缓存的对象，只有叶子节点才会有entity的实体
	 */
	private T entity;
	/**
	 * 下一层的缓存索引，叶子节点
	 */
	private Map<String, CacheNode<T>> childNodes;
	
	private List<CacheNode<T>> childList;
	
	private boolean init = false;
	
	private CacheNode<T> parent;
	
	public CacheNode(CacheNode<T> parent, String fieldName, String keyValue) {
		this.parent = parent;
		this.fieldName = fieldName;
		this.keyValue = keyValue;
	}
	
	protected void initCache() {
		//这里不用加synchronized，因为initCache会在根节点加锁的情况下进行调用
		if(!init) {
			childNodes = new ConcurrentHashMap<>();
			childList = new ArrayList<>();
			init = true;
		}
	}
	
	public CacheNode<T> getChild(String key) {
		if(childNodes == null) return null;
		return childNodes.get(key);
	}
	
	public synchronized void addChild(String key, CacheNode<T> node) {
		childNodes.put(key, node);
		childList.add(node);
	}
	
	public synchronized void deleteCache(String key) {
		CacheNode<T> node = childNodes.remove(key);
		if(node != null) {
			childList.remove(node);
		}
	}
	
	public boolean isEmpty() {
		if(childNodes == null) {
			//叶子节点必定是某个数据实体的节点，因此该节点必定不为空
			return false;
		}
		return childNodes.isEmpty();
	}

	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
	public Map<String, CacheNode<T>> getChildNodes() {
		return childNodes;
	}
	public synchronized List<CacheNode<T>> getChildList() {
		return childList;
	}
	public String getFieldName() {
		return fieldName;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public CacheNode<T> getParent() {
		return parent;
	}
}
