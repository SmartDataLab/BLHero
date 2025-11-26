/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.starting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.DaoManager;
import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.mysql.table.TableBuilder;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.spring.Spring;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class DatabaseLifecycle implements Lifecycle {

	private static Logger logger = LoggerFactory.getLogger(DatabaseLifecycle.class);
	
	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.INITIALIZATION, Ordinal.MIN);
	}

	@Override
	public void start() throws Exception {
		TableBuilder.SERVER_IDENTITY = applicationSettings.getCrossServerId();
		String dbFilePath = "";
		if (applicationSettings.getDatabaseConfigFile().startsWith("file")) {
			ResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource(applicationSettings.getDatabaseConfigFile());
			dbFilePath = resource.getFile().getPath();
		} else {
			dbFilePath = applicationSettings.getDatabaseConfigFile();
		}
		List<DBConfig> dbConfigs = DBConfig.loadConfigs(dbFilePath);
		for (DBConfig dbConfig : dbConfigs) {
			logger.info("加载数据库配置：{}", GsonUtil.toJson(dbConfig));
		}
		
		Collection<Object> entitySamples = Spring.getBeansWithAnnotation(Table.class);
		Collection<Object> logSamples = Spring.getBeansWithAnnotation(LogTable.class);
		
		List<Class<?>> classList = new ArrayList<>();
		for(Object bean : entitySamples) {
			if (bean instanceof AbstractEntity) {
				classList.add(Spring.getBeanRealClass(bean));
			}
		}
		for(Object bean : logSamples) {
			if (bean instanceof AbstractEntity) {
				classList.add(Spring.getBeanRealClass(bean));
			}
		}
		SlimDao.build(dbConfigs, classList);
	}

	@Override
	public void stop() throws Exception {
		DaoManager.stopAsync();
	}
}
