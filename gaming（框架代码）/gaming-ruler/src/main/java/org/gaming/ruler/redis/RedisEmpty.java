/**
 * 
 */
package org.gaming.ruler.redis;

/**
 * @author YY
 * 用于处理redis缓存击穿的对象
 */
public class RedisEmpty {
	private String empty = "EMPTY";
	public String getEmpty() {
		return empty;
	}
	public void setEmpty(String empty) {
		this.empty = empty;
	}
	
	private RedisEmpty() {}
	
	public static final RedisEmpty EMPTY = new RedisEmpty();
}
