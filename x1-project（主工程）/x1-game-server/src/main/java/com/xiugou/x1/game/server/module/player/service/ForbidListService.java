/**
 * 
 */
package com.xiugou.x1.game.server.module.player.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.module.player.model.ForbidList;

/**
 * @author YY
 *
 */
@Service
public class ForbidListService extends OneToOneService<ForbidList> implements Lifecycle {

	private ConcurrentMap<String, Boolean> forbidMap = new ConcurrentHashMap<>();

	@Override
	public void start() throws Exception {
		for(ForbidList forbidList : getAll()) {
			forbidMap.put(forbidList.getOpenId(), true);
		}
	}

	@Override
	protected ForbidList createWhenNull(long entityId) {
		return null;
	}
	
	public List<ForbidList> getAll() {
		return this.repository().getAllInCache();
	}
	
	public boolean isForbid(String openId) {
		return forbidMap.containsKey(openId);
	}
	
	public void deleteAll(List<ForbidList> list) {
		this.repository().deleteAll(list);
		for(ForbidList forbidList : list) {
			forbidMap.remove(forbidList.getOpenId());
		}
	}
	
	public void insertAll(List<ForbidList> list) {
		this.repository().insertAll(list);
		for(ForbidList forbidList : list) {
			forbidMap.put(forbidList.getOpenId(), true);
		}
	}
}
