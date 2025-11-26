/**
 * 
 */
package com.xiugou.x1.game.server.module.hotfix.service;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

/**
 * @author YY
 *
 */
@Service
public class HotfixService implements Lifecycle {

	private static Logger logger = LoggerFactory.getLogger(HotfixService.class);
	
	@Autowired
	private ApplicationSettings applicationSettings;

	private String hotfixCodeFolder;
	private String hotfixDesignFolder;

	@Override
	public void start() throws Exception {
		String projectDir = System.getProperty("user.dir");
		
		hotfixCodeFolder = projectDir + applicationSettings.getGameFixClazzsFolder();
		logger.info("热更代码路径：{}", hotfixCodeFolder);
		
		hotfixDesignFolder = projectDir + applicationSettings.getGameFixDesignFolder();
		logger.info("热更配置路径：{}", hotfixDesignFolder);
	}

	public String getHotfixCodeFolder() {
		return hotfixCodeFolder;
	}

	public String getHotfixDesignFolder() {
		return hotfixDesignFolder;
	}
}
