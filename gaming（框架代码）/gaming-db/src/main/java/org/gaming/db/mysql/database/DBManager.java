/**
 * 
 */
package org.gaming.db.mysql.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class DBManager {
	private static Logger logger = LoggerFactory.getLogger(DBManager.class);
	
	/**
	 * 根据数据库连接配置文件信息进行数据库连接
	 * @param filePath
	 */
	public static List<DataBase> loadDBS(List<DBConfig> list) {
		Map<Integer, Set<String>> tempDBS = new HashMap<>();
		
		List<DataBase> dbsList = new ArrayList<>();
		for(DBConfig config : list) {
			Set<String> zoneDBS = tempDBS.get(config.getId());
			if(zoneDBS == null) {
				zoneDBS = new HashSet<>();
				tempDBS.put(config.getId(), zoneDBS);
			}
			if(zoneDBS.contains(config.getAlias())) {
				throw new RuntimeException("重复的数据库连接信息，id[" + config.getId() + "]，alias[" + config.getAlias() + "]");
			}
			zoneDBS.add(config.getAlias());
			logger.info("初始化" + config.toString());
			dbsList.add(new DataBase(config));
		}
		return dbsList;
	}
}
