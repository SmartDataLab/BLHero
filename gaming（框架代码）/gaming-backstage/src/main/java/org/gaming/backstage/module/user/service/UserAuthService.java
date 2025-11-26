/**
 * 
 */
package org.gaming.backstage.module.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YY
 *
 */
public class UserAuthService {
	
	//<请求URL，权限类>
	private static Map<String, String> authMap = new HashMap<>();
	
	public static void addAuth(String requestUrl, String authClazz) {
		authMap.put(requestUrl, authClazz);
	}
	
	public static String getAuthClazzPage(String requestUrl) {
		return authMap.get(requestUrl);
	}
	
	public static void print() {
		List<String> authUrls = new ArrayList<>(authMap.keySet());
		Collections.sort(authUrls);
		
		for(String authUrl : authUrls) {
			System.out.println(authUrl + " " + authMap.get(authUrl));
		}
	}
}
