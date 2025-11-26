/**
 * 
 */
package com.xiugou.x1.backstage.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.advice.Asserts;
import org.gaming.ruler.util.HttpUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;

import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
public class X1HttpUtil {
	
	private static Logger logger = LoggerFactory.getLogger(X1HttpUtil.class);
	
	public static ServerResponse get(GameServer gameServer, String url) {
		String httpUrl = "http://" + gameServer.getInternalIp() + ":" + gameServer.getHttpPort() + url;
		try {
			String response = HttpUtil.get(httpUrl);
			if(response == null) {
				return null;
			}
			return GsonUtil.parseJson(response, ServerResponse.class);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
			return null;
		}
	}
	
	public static ServerResponse formPost(GameServer gameServer, String url, Map<String, Object> parameter) {
		String httpUrl = "http://" + gameServer.getInternalIp() + ":" + gameServer.getHttpPort() + url;
		try {
			String response = HttpUtil.formPost(httpUrl, parameter);
			if(response == null) {
				return null;
			}
			return GsonUtil.parseJson(response, ServerResponse.class);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
			return null;
		}
	}
	
	public static <T> T simplePost(GameServer gameServer, String url, Map<String, Object> parameter, Class<T> clazz) {
		String httpUrl = "http://" + gameServer.getInternalIp() + ":" + gameServer.getHttpPort() + url;
		try {
			String response = HttpUtil.formPost(httpUrl, parameter);
			Asserts.isTrue(response != null, TipsCode.POST_NO_RESPONSE, url);
			ServerResponse serverResponse = GsonUtil.parseJson(response, ServerResponse.class);
			Asserts.isTrue(serverResponse.getCode() == 0, TipsCode.POST_FAIL, url);
			return GsonUtil.parseJson(serverResponse.getData(), clazz);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
			return null;
		}
	}
	
	
	public static ServerResponse jsonPost(GameServer gameServer, String url, Object parameter) {
		String httpUrl = "http://" + gameServer.getInternalIp() + ":" + gameServer.getHttpPort() + url;
		try {
			String response = HttpUtil.jsonPost(httpUrl, parameter);
			if(response == null) {
				return null;
			}
			return GsonUtil.parseJson(response, ServerResponse.class);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
			return null;
		}
	}
	
	public static ServerResponse postParamAndFile(GameServer gameServer, String url, Map<String, Object> params, List<File> files) {
		String httpUrl = "http://" + gameServer.getInternalIp() + ":" + gameServer.getHttpPort() + url;
		try {
			String response = HttpUtil.postParamAndFile(httpUrl, params, files);
			if(response == null) {
				return null;
			}
			return GsonUtil.parseJson(response, ServerResponse.class);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
			return null;
		}
	}
}
