/**
 * 
 */
package org.gaming.db.repository.cache;

/**
 * @author YY
 *
 */
public class CacheRoot<T> extends CacheNode<T> {
	
	private long expireTime;
	
	public CacheRoot(String fieldName, String keyValue) {
		super(null, fieldName, keyValue);
		this.expireTime = System.currentTimeMillis();
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
