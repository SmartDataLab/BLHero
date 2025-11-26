/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import org.gaming.backstage.service.OneToManyRedisService;
import org.gaming.db.repository.QueryOptions;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.UserChannel;

/**
 * @author YY
 *
 */
@Service
public class UserChannelService extends OneToManyRedisService<UserChannel> {

	@Override
	protected String cacheContext() {
		return "user";
	}

	@Override
	protected QueryOptions queryForOwner(long ownerId) {
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.put("userId", ownerId);
		return queryOptions;
	}

	
}
