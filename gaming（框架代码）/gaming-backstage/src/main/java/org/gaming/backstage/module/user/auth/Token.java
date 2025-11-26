/**
 * 
 */
package org.gaming.backstage.module.user.auth;

import java.util.UUID;

import org.gaming.tool.MD5Util;

/**
 * @author YY
 *
 */
public class Token {

	public static String gen(long id) {
		StringBuilder builder = new StringBuilder();
		builder.append(id).append(System.currentTimeMillis()).append(UUID.randomUUID().toString());
		return MD5Util.getMD5(builder.toString());
	}
}
