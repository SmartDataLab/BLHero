/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import org.gaming.backstage.service.SystemOneToManyService;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Service
public class GameServerService extends SystemOneToManyService<GameServer> {

	public GameServer randomRunning() {
		for(GameServer gameServer : this.getEntities()) {
			ServerResponse serverResponse = X1HttpUtil.get(gameServer, GameApi.serverTestRunning);
			if(serverResponse == null) {
				continue;
			}
			logger.info("当前可用的服务为{}-{}-{}", gameServer.getId(),gameServer.getServerId(),gameServer.getName());
			return gameServer;
		}
		logger.info("没有可用的服务器");
		return null;
	}
	
	public GameServer getByPlatformAndServer(long platformId, int serverId) {
		return this.repository().get("platformId", platformId, "serverId", serverId);
	}
}
