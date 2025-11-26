/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.backstage.WebCode;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.user.event.UserLoginEvent;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.backstage.service.SystemOneToManyService;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.model.UserChannel;

/**
 * @author YY
 *
 */
@Service
public class GameChannelService extends SystemOneToManyService<GameChannel> {

	@Autowired
	private UserService userService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private UserChannelService userChannelService;
	
	private ConcurrentMap<Long, Long> userChannelMap = new ConcurrentHashMap<>();
	
	@Subscribe
	private void listen(UserLoginEvent event) {
		User user = userService.getById(event.getUserId());
		if(user.isSuperUser()) {
			List<GameChannel> list = this.getEntities();
			if(!list.isEmpty()) {
				SortUtil.sort(list, GameChannel::getId);
				userChannelMap.put(event.getUserId(), list.get(0).getId());
			}
		} else {
			List<UserChannel> channels = userChannelService.getEntityList(user.getId());
			if(!channels.isEmpty()) {
				SortUtil.sort(channels, UserChannel::getChannelId);
				userChannelMap.put(event.getUserId(), channels.get(0).getChannelId());
			}
		}
	}
	
	public void changeChannel(long userId, long channelId) {
		userChannelMap.put(userId, channelId);
	}
	
	public long currChannel() {
		RoleContext roleContext = userService.getCurrUser();
		Asserts.isTrue(roleContext != null, WebCode.USER_CONTEXT_MISS);
		Long channelId = userChannelMap.get(roleContext.getId());
		Asserts.isTrue(channelId != null, TipsCode.CHANNEL_NOT_CHOOSE);
		return channelId;
	}
	
	/**
	 * 获取当前渠道下的服务器
	 * @return
	 */
	public List<GameServer> getServers() {
		long currChannel = this.currChannel();
		List<GameChannelServer> relations = gameChannelServerService.getEntityList(currChannel);
		List<GameServer> servers = new ArrayList<>();
		for(GameChannelServer relation : relations) {
			GameServer gameServer = gameServerService.getEntity(relation.getServerUid());
			servers.add(gameServer);
		}
		return servers;
	}
	
	/**
	 * 获取渠道下的推荐服务器
	 * @param channelId
	 * @return
	 */
	public List<GameServer> getRecommendServersUnderChannel(long channelId) {
		List<GameChannelServer> relations = gameChannelServerService.getEntityList(channelId);
		List<GameServer> servers = new ArrayList<>();
		for(GameChannelServer relation : relations) {
			if(!relation.isRecommend()) {
				continue;
			}
			GameServer gameServer = gameServerService.getEntity(relation.getServerUid());
			servers.add(gameServer);
		}
		return servers;
	}
}
