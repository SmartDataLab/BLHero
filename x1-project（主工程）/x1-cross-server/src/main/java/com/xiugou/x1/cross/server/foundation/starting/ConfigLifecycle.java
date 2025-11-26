/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.starting;

import org.gaming.design.export.CsvReader;
import org.gaming.design.loader.DesignCacheManager;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.TipsHelper;
import com.xiugou.x1.design.X1SeparatorDesignParser;

/**
 * @author YY
 *
 */
@Component
public class ConfigLifecycle implements Lifecycle {

	@Autowired
	private ApplicationSettings applicationSettings;
	
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.SYSTEM, Ordinal.MIN);
	}

	public void start() throws Exception {
		EventBus.useTracer(applicationSettings.isCrossDebugMode());
		TipsHelper.language = applicationSettings.getCrossLanguage();
		
		DesignCacheManager.loadFull(applicationSettings.getCsvFilePath(), new CsvReader(), new X1SeparatorDesignParser(), true);
	}
}
