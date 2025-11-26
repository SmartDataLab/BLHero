/**
 * 
 */
package com.xiugou.x1.cross.server.module.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.service.SystemOneToManyService;
import com.xiugou.x1.cross.server.module.server.model.GameServer;

/**
 * @author YY
 *
 */
@Service
public class GameServerService extends SystemOneToManyService<GameServer> {

	@Override
	public List<GameServer> getEntities() {
		return this.repository().getAllInCache();
	}

	public GameServer getEntity(int serverId) {
		GameServer gameServer = this.repository().getByMainKey(serverId);
		if(gameServer == null) {
			gameServer = new GameServer();
			gameServer.setId(serverId);
			this.insert(gameServer);
		}
		return gameServer;
	}
	
}
