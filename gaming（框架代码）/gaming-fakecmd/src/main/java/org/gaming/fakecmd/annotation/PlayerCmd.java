/**
 * 
 */
package org.gaming.fakecmd.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YY
 * CMD注解只会被用于函数上，用以标明该函数将作为协议处理函数被调动
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PlayerCmd {
	/**
	 * 是否需要登录才能调用
	 * @return
	 */
	public boolean needLogin() default true;
	/**
	 * 需转发到哪里，默认在本地处理
	 * @return
	 */
	public String forwardTo() default "";
}
