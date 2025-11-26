/**
 * 
 */
package com.xiugou.x1.backstage.foundation.starting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.mysql.table.TableBuilder;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.spring.Spring;
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

	@Autowired
	private ApplicationSettings applicationSettings;
	
	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.INITIALIZATION, Ordinal.MIN);
	}

	@Override
	public void start() throws Exception {
		String dbFilePath = "";
		if(applicationSettings.getDatabaseConfigFile().startsWith("file")) {
			ResourceLoader loader = new DefaultResourceLoader();
	        Resource resource = loader.getResource(applicationSettings.getDatabaseConfigFile());
	        dbFilePath = resource.getFile().getPath();
		} else {
			dbFilePath = applicationSettings.getDatabaseConfigFile();
		}
		TableBuilder.SERVER_IDENTITY = 1;
		List<DBConfig> dbConfigs = DBConfig.loadConfigs(dbFilePath);
		
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
}
