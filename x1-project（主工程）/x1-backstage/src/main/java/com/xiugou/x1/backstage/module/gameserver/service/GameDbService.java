/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import java.util.List;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.repository.QueryOptions;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameDb;

/**
 * @author YY
 *
 */
@Service
public class GameDbService extends AbstractService<GameDb> {

	public List<GameDb> dbs(int platformId, int serverId, String type) {
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.put("platformId", platformId);
		queryOptions.put("serverId", serverId);
		queryOptions.put("type", type);
		return this.repository().getList(queryOptions);
	}
	
	public GameDb getDb(String dbName) {
		return this.repository().getByMainKey(dbName);
	}
}
