/**
 * 
 */
package org.gaming.backstage.interceptor;

import java.util.Comparator;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author YY
 *
 */
public abstract class BusinessInterceptor extends HandlerInterceptorAdapter {

	public BusinessInterceptor() {
		InterceptorConfig.interceptors.add(this);
	}
	
	protected abstract String[] pathPatterns();
	
	protected int priority() {
		return InterceptorPriority._25;
	}
	
	public static Comparator<BusinessInterceptor> PRIORITY_SORTER = new Comparator<BusinessInterceptor>() {
		@Override
		public int compare(BusinessInterceptor o1, BusinessInterceptor o2) {
			return Integer.compare(o1.priority(), o2.priority());
		}
	};
}
