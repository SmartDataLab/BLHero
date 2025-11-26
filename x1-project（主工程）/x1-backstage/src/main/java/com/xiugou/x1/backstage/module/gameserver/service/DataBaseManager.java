/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.backstage.advice.Asserts;
import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.mysql.database.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameDb;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;

/**
 * @author YY
 *
 */
@Service
public class DataBaseManager {
	
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private GameDbService gameDbService;
	
	//日志库的数据库连接
	private ConcurrentMap<Integer, DataBase> logDbMap = new ConcurrentHashMap<>();
	//TODO 多长时间断开连接
	
	public DataBase getLogDb(int gameServerUniqueId) {
		DataBase dataBase = logDbMap.get(gameServerUniqueId);
		if(dataBase == null) {
			synchronized (this) {
				dataBase = logDbMap.get(gameServerUniqueId);
				if(dataBase == null) {
					//TODO 初始化
					GameServer gameServer = gameServerService.getEntity(gameServerUniqueId);
					Asserts.isTrue(gameServer != null, TipsCode.GAME_SERVER_MISS, gameServerUniqueId);
					
					GameDb gameDb = gameDbService.getDb(gameServer.getDbLogName());
					if(gameDb == null) {
						return null;
					}
					
					DBConfig dbConfig = new DBConfig();
					dbConfig.setId(gameServerUniqueId);
					dbConfig.setUser(gameDb.getUser());
					dbConfig.setPassword(gameDb.getPassword());
					dbConfig.setIpPort(gameDb.getIpPort());
					dbConfig.setDbName(gameDb.getDbName());
					
					dataBase = new DataBase(dbConfig);
					logDbMap.put(gameServerUniqueId, dataBase);
				}
			}
		}
		return dataBase;
	}
	
	public void closeLogDb(int gameServerId) {
		DataBase dataBase = logDbMap.get(gameServerId);
		if(dataBase == null) {
			return;
		}
		logDbMap.remove(gameServerId);
		dataBase.close();
	}
}
