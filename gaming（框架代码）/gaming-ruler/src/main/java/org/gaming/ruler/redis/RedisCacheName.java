/**
 * 
 */
package org.gaming.ruler.redis;

/**
 * @author YY
 *
 */
public class RedisCacheName {
	
	
	
	private final String cacheName;
	private RedisCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public static RedisCacheName build(String cacheName) {
		return new RedisCacheName(cacheName);
	}
	public String getCacheName() {
		return cacheName;
	}
}
