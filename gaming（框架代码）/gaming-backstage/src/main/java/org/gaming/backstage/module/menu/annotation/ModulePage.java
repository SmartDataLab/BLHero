/**
 * 
 */
package org.gaming.backstage.module.menu.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YY
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ModulePageContainer.class)
public @interface ModulePage {
	long id();
	String name();
	long systemId();
	/**
	 * 排序顺序，越小越前
	 * @return
	 */
	int sort() default 0;
	String icon() default "";
	long createTime() default 0;
	String routeName();
	String routeComponent();
}
