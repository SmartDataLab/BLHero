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
 * 此注解无法跟异步insert的数据表同时使用
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JvmCache {
	/**
	 * 缓存的关系，实体类的属性字段名，不是数据库的字段名
	 * @return
	 */
	String[] relation() default {};
	/**
	 * 缓存的时间，单位毫秒，0为永久缓存
	 * @return
	 */
	long cacheTime() default 7200 * 1000l;
	/**
	 * 是否在启动的时候加载所有数据
	 * @return
	 */
	boolean loadAllOnStart() default false;
}
