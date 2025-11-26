/**
 * 
 */
package org.gaming.backstage.interceptor;

import org.gaming.backstage.module.user.auth.AuthorityInterceptor;

/**
 * @author YY
 *
 */
public abstract class RequestInterceptor {
	
	public RequestInterceptor() {
		AuthorityInterceptor.registerInterceptor(this);
	}
	
	public abstract IRoleType roleType();
	
	public abstract boolean beforeDo(RoleContext roleContext, String requestUrl);
	
	public abstract boolean beforeAuth(RoleContext roleContext, String requestUrl);
	
	public abstract boolean beforeAuthw(RoleContext roleContext, String requestUrl);
	
	public abstract void afterDo(RoleContext roleContext);
	
	public abstract void afterAuth(RoleContext roleContext);
	
	public abstract void afterAuthw(RoleContext roleContext);
}
