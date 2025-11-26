/**
 * 
 */
package com.xiugou.x1.backstage.module.clientversion.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.OneToManyRedisService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.clientversion.model.ClientVersion;

/**
 * @author YY
 *
 */
@Service
public class ClientVersionService extends OneToManyRedisService<ClientVersion> {
	
	public PageData<ClientVersion> query(QuerySet querySet) {
		List<ClientVersion> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<ClientVersion> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}

	@Override
	protected String cacheContext() {
		return "game_channel";
	}

	@Override
	protected QueryOptions queryForOwner(long ownerId) {
		QueryOptions options = new QueryOptions();
		options.put("channelId", ownerId);
		return options;
	}
}
