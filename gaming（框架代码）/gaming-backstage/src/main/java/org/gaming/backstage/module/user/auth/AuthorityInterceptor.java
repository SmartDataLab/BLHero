/**
 * 
 */
package org.gaming.backstage.module.user.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaming.backstage.interceptor.BusinessInterceptor;
import org.gaming.backstage.interceptor.InterceptorPriority;
import org.gaming.backstage.interceptor.RequestInterceptor;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.user.service.UserLogService;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author YY
 *
 */
@Component
public class AuthorityInterceptor extends BusinessInterceptor {

	private static Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserLogService userLogService;
	
	/**
	 * <角色类型，请求拦截器>
	 */
	private static Map<Integer, RequestInterceptor> interceptorMap = new HashMap<>();
	
	@Override
	public String[] pathPatterns() {
		return new String[] {"/**"};
	}
	
	@Override
	protected int priority() {
		return InterceptorPriority.MIN;
	}
	
	/**
	 * 不要用.json结尾，jquery、layui对.json的请求有特殊处理
	 * .page 页面
	 * .auth 需验证权限
	 * .authw 需验证写权限
	 * .gate 直接放行
	 * .do 需验证登录
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		logger.info(requestURI + " " + request.getContentLength() + " " + request.getContentType() + " " + request.getMethod());
		if("OPTIONS".equals(request.getMethod())) {
			return true;
		}
		if(requestURI.endsWith(".gate")) {
			return true;
		} else if(requestURI.endsWith(".do")) {
			RoleContext roleContext = getContext(request);
			if(roleContext == null) {
				logger.error("通过session或token均未找到用户上下文");
				return false;
			}
			RequestInterceptor interceptor = interceptorMap.get(roleContext.getRoleType().getValue());
			if(interceptor == null) {
				return true;
			}
			userLogService.addLog(roleContext, requestURI, GsonUtil.toJson(request.getParameterMap()));
			return interceptor.beforeDo(roleContext, requestURI);
		} else if(requestURI.endsWith(".auth")) {
			RoleContext roleContext = getContext(request);
			if(roleContext == null) {
				logger.error("通过session或token均未找到用户上下文");
				return false;
			}
			RequestInterceptor interceptor = interceptorMap.get(roleContext.getRoleType().getValue());
			if(interceptor == null) {
				return true;
			}
			userLogService.addLog(roleContext, requestURI, GsonUtil.toJson(request.getParameterMap()));
			return interceptor.beforeAuth(roleContext, requestURI);
		} else if(requestURI.endsWith(".authw")) {
			RoleContext roleContext = getContext(request);
			if(roleContext == null) {
				logger.error("通过session或token均未找到用户上下文");
				return false;
			}
			RequestInterceptor interceptor = interceptorMap.get(roleContext.getRoleType().getValue());
			if(interceptor == null) {
				return true;
			}
			userLogService.addLog(roleContext, requestURI, GsonUtil.toJson(request.getParameterMap()));
			return interceptor.beforeAuthw(roleContext, requestURI);
		} else {
			return true;
		}
	}
	
	private RoleContext getContext(HttpServletRequest request) {
		RoleContext roleContext = (RoleContext)request.getSession().getAttribute("ROLECONTEXT");
		if(roleContext == null) {
			String token = request.getHeader("token");
			if(token != null) {
				roleContext = userService.getContext(token);
			}
		}
		return roleContext;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String requestURI = request.getRequestURI();
		if(requestURI.endsWith(".do")) {
			RoleContext roleContext = getContext(request);
			if(roleContext == null) {
				return;
			}
			RequestInterceptor interceptor = interceptorMap.get(roleContext.getRoleType().getValue());
			if(interceptor == null) {
				return;
			}
			interceptor.afterDo(roleContext);
		} else if(requestURI.endsWith(".auth")) {
			RoleContext roleContext = getContext(request);
			if(roleContext == null) {
				return;
			}
			RequestInterceptor interceptor = interceptorMap.get(roleContext.getRoleType().getValue());
			if(interceptor == null) {
				return;
			}
			interceptor.afterAuth(roleContext);
		} else if(requestURI.endsWith(".authw")) {
			RoleContext roleContext = getContext(request);
			if(roleContext == null) {
				return;
			}
			RequestInterceptor interceptor = interceptorMap.get(roleContext.getRoleType().getValue());
			if(interceptor == null) {
				return;
			}
			interceptor.afterAuthw(roleContext);
		}
	}
	
	public static void registerInterceptor(RequestInterceptor interceptor) {
		interceptorMap.put(interceptor.roleType().getValue(), interceptor);
	}
}
