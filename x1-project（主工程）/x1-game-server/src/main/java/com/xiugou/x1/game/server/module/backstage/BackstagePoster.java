/**
 * 
 */
package com.xiugou.x1.game.server.module.backstage;

import java.util.Map;

import org.gaming.ruler.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

/**
 * @author YY
 *
 */
@Component
public class BackstagePoster {
	
	private static Logger logger = LoggerFactory.getLogger(BackstagePoster.class);

	@Autowired
	private ApplicationSettings applicationSettings;
	
	public String formPost(String url, Map<String, Object> parameter) {
		String httpUrl = applicationSettings.getBackstageUrl() + url;
		try {
			return HttpUtil.formPost(httpUrl, parameter);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
		}
		return null;
	}
	
	
	public String jsonPost(String url, Object parameter) {
		String httpUrl = applicationSettings.getBackstageUrl() + url;
		try {
			return HttpUtil.jsonPost(httpUrl, parameter);
		} catch (Exception e) {
			logger.error("请求" + httpUrl + "发生异常", e);
		}
		return null;
	}
}
