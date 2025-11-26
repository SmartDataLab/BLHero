/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.OneToManyRedisService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameRegion;

/**
 * @author YY
 *
 */
@Service
public class GameRegionService extends OneToManyRedisService<GameRegion> {

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
	
	public PageData<GameRegion> getList(int page, int pageSize) {
		QuerySet querySet = new QuerySet();
		querySet.limit(page, pageSize);
		querySet.orderBy("order by channel_id, region_id");
		querySet.formWhere();
		List<GameRegion> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<GameRegion> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
}
