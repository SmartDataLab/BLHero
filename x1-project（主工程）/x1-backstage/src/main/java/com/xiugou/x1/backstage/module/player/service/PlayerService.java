/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.player.model.Player;

/**
 * @author YY
 *
 */
@Service
public class PlayerService extends AbstractService<Player> {

	public List<Player> getByOpenId(long channelId, String openId) {
		QueryOptions options = new QueryOptions();
		options.put("channelId", channelId);
		options.put("openId", openId);
		return repository().getList(options);
	}
	
	public List<Player> getByOpenId(String openId) {
		QueryOptions options = new QueryOptions();
		options.put("openId", openId);
		return repository().getList(options);
	}
	
	public PageData<Player> query(QuerySet querySet) {
		List<Player> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		PageData<Player> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}


	public void insertUpdate(List<Player> players) {
		this.repository().getBaseDao().insertUpdate(players);
	}
	
	/**
	 * 是否为老玩家
	 * @param openId
	 * @param serverId
	 * @return
	 */
	public boolean isOld(String openId, int serverId) {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("open_id = ?", openId);
		querySet.addCondition("server_id < ?", serverId);
		querySet.formWhere();
		List<Player> list = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		return !list.isEmpty();
	}
}
