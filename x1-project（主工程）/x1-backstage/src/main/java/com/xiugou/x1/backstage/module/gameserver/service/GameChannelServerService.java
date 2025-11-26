/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.OneToManyRedisService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;

/**
 * @author YY
 *
 */
@Service
public class GameChannelServerService extends OneToManyRedisService<GameChannelServer> {

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
	
	public PageData<GameChannelServer> getList(int page, int pageSize) {
		QuerySet querySet = new QuerySet();
		querySet.limit(page, pageSize);
		querySet.orderBy("order by id desc");
		querySet.formWhere();
		List<GameChannelServer> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<GameChannelServer> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	/**
	 * 通过服务器的唯一ID获取关系数据
	 * @param serverUid
	 * @return
	 */
	public List<GameChannelServer> getByServerUid(int serverUid) {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("server_uid = ?", serverUid);
		querySet.formWhere();
		List<GameChannelServer> dbList = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		List<GameChannelServer> cacheList = new ArrayList<>();
		for(GameChannelServer dbObj : dbList) {
			GameChannelServer cacheObj = this.getEntity(dbObj.getChannelId(), dbObj.getServerId());
			cacheList.add(cacheObj);
		}
		return cacheList;
	}
}
