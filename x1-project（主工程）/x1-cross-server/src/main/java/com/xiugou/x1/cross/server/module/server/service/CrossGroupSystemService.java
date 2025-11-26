/**
 * 
 */
package com.xiugou.x1.cross.server.module.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.service.SystemOneToOneService;
import com.xiugou.x1.cross.server.module.TimeSetting;
import com.xiugou.x1.cross.server.module.server.model.CrossGroup;
import com.xiugou.x1.cross.server.module.server.model.CrossGroupSystem;
import com.xiugou.x1.cross.server.module.server.model.GameServer;

/**
 * @author YY
 *
 */
@Service
public class CrossGroupSystemService extends SystemOneToOneService<CrossGroupSystem> implements Lifecycle {

	@Autowired
	private TimeSetting timeSetting;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private CrossGroupService crossGroupService;
	
	//<服务器ID，跨服分组ID>
	private ConcurrentMap<Integer, Integer> gameToGroupMap = new ConcurrentHashMap<>();
	
	private final static int GROUP_SIZE = 32;
	
	public void runInSchedule() {
		LocalDateTime now = LocalDateTimeUtil.now();
		CrossGroupSystem entity = this.getEntity();
		if(entity.getNextGroupTime().isAfter(now)) {
			//还未到重新分组的时间
			return;
		}
		entity.setNextGroupTime(getNextGroupTime());
		this.update(entity);
		
		synchronized (this) {
			List<CrossGroup> crossGroups = crossGroupService.getEntities();
			for(CrossGroup crossGroup : crossGroups) {
				crossGroup.getServers().clear();
				crossGroup.getMergeServers().clear();
			}
			Map<Integer, CrossGroup> groupMap = ListMapUtil.listToMap(crossGroups, CrossGroup::getId);
			//分组数量
			List<GameServer> gameServers = gameServerService.getEntities();
			SortUtil.sortInt(gameServers, GameServer::getId);
			
			//处理未合服过的服务器
			Map<Integer, CrossGroup> mainServerGroupMap = new HashMap<>();
			for(GameServer gameServer : gameServers) {
				//TODO 合服后又该怎么处理
				int groupId = (int)Math.ceil(gameServer.getId() * 1.0f / GROUP_SIZE);
				CrossGroup crossGroup = groupMap.get(groupId);
				if(crossGroup == null) {
					crossGroup = new CrossGroup();
					crossGroup.setId(groupId);
					crossGroupService.insert(crossGroup);
					groupMap.put(groupId, crossGroup);
				}
				if(gameServer.getMergeToServer() != 0) {
					continue;
				}
				crossGroup.getServers().add(gameServer.getId());
				mainServerGroupMap.put(gameServer.getId(), crossGroup);
				gameToGroupMap.put(gameServer.getId(), crossGroup.getId());
				if(crossGroup.getServers().size() >= GROUP_SIZE) {
					groupId += 1;
				}
			}
			//处理合服过的服务器
			for(GameServer gameServer : gameServers) {
				if(gameServer.getMergeToServer() == 0) {
					continue;
				}
				CrossGroup crossGroup = mainServerGroupMap.get(gameServer.getMergeToServer());
				if(crossGroup == null) {
					logger.error("未找到服务器{}归属的跨服分组，其主服务器为{}", gameServer.getId(), gameServer.getMergeToServer());
					continue;
				}
				crossGroup.getMergeServers().add(gameServer.getId());
				gameToGroupMap.put(gameServer.getId(), crossGroup.getId());
			}
			crossGroupService.updateAll(new ArrayList<>(groupMap.values()));
		}
	}

	@Override
	public CrossGroupSystem getEntity() {
		CrossGroupSystem entity = this.repository().getByMainKey(1L);
		if(entity == null) {
			synchronized (this) {
				entity = this.repository().getByMainKey(1L);
				if(entity == null) {
					entity = new CrossGroupSystem();
					entity.setId(1L);
					entity.setNextGroupTime(getNextGroupTime());
					this.insert(entity);
				}
			}
		}
		return entity;
	}
	
	private LocalDateTime getNextGroupTime() {
		long monthZero = DateTimeUtil.monthZeroMillis();
		long now = DateTimeUtil.currMillis();
		long nextTime = monthZero + DateTimeUtil.ONE_DAY_MILLIS * (15 - 1) + timeSetting.getOclock() * DateTimeUtil.ONE_HOUR_MILLIS;
		if(now > nextTime) {
			nextTime = DateTimeUtil.nextMonthZeroMillis() + timeSetting.getOclock() * DateTimeUtil.ONE_HOUR_MILLIS;
		}
		return timeSetting.theDayOTime(LocalDateTimeUtil.ofEpochMilli(nextTime));
	}

	@Override
	public void start() throws Exception {
		List<CrossGroup> crossGroups = crossGroupService.getEntities();
		for(CrossGroup crossGroup : crossGroups) {
			for(int serverId : crossGroup.getServers()) {
				gameToGroupMap.put(serverId, crossGroup.getId());
			}
			for(int serverId : crossGroup.getMergeServers()) {
				gameToGroupMap.put(serverId, crossGroup.getId());
			}
		}
	}
	
	public void joinToCrossGroup(int gameServerId) {
		if(gameToGroupMap.containsKey(gameServerId)) {
			return;
		}
		synchronized (this) {
			if(gameToGroupMap.containsKey(gameServerId)) {
				return;
			}
			//TODO 合服后又该怎么处理
			int groupId = (int)Math.ceil(gameServerId * 1.0f / GROUP_SIZE);
			CrossGroup crossGroup = crossGroupService.getEntity(groupId);
			if(crossGroup == null) {
				crossGroup = new CrossGroup();
				crossGroup.setId(groupId);
				crossGroupService.insert(crossGroup);
			}
			crossGroup.getServers().add(gameServerId);
			crossGroupService.update(crossGroup);
			gameToGroupMap.put(gameServerId, crossGroup.getId());
			logger.info("服务器{}加入到跨服分组{}中", gameServerId, crossGroup.getId());
		}
	}
	
	public int getCrossGroupId(int gameServerId) {
		return gameToGroupMap.get(gameServerId);
	}
	
	public CrossGroup getCrossGroup(int gameServerId) {
		int crossGroupId = gameToGroupMap.get(gameServerId);
		return crossGroupService.getEntity(crossGroupId);
	}
}
