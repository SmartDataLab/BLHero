/**
 * 
 */
package org.gaming.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YY
 * redis缓存，还没实现
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RedisCache {
	/**
	 * 数据的持有者
	 * @return
	 */
	public String owner() default "";
	/**
	 * 缓存的时间，单位毫秒，0为永久缓存
	 * @return
	 */
	public long cacheTime() default 3600 * 1000l;
}
