/**
 * 
 */
package org.gaming.ruler.eventbus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YY
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
	
	public ListenType type() default ListenType.SYNC;
	
	public static enum ListenType {
		/**
		 * 同步
		 */
		SYNC,
		/**
		 * 异步
		 */
		ASYNC,
	}
}
