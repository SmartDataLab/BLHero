/**
 * 
 */
package com.xiugou.x1.cross.server.module.player.service;

import org.gaming.ruler.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pojo.xiugou.x1.pojo.module.player.model.PlayerSnapshot;

/**
 * @author YY
 *
 */
@Service
public class PlayerService {

	@Autowired
	private RedisCache redisCache;
	
	public PlayerSnapshot getPlayer(long playerId, int serverId) {
		String redisKey = "player:" + serverId + ":players";
		return redisCache.getHash(redisKey, playerId, PlayerSnapshot.class);
	}
}
