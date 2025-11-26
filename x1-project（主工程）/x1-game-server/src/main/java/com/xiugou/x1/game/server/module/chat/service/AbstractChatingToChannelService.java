/**
 * 
 */
package com.xiugou.x1.game.server.module.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.player.model.PlayerDetail;
import com.xiugou.x1.game.server.module.player.service.PlayerDetailService;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.chat.Chat.PbChatChannel;
import pb.xiugou.x1.protobuf.chat.Chat.PbChatData;

/**
 * @author YY
 *
 */
public abstract class AbstractChatingToChannelService {

	@Autowired
	protected PlayerService playerService;
	
	private static Map<Integer, AbstractChatingToChannelService> chatingServices = new HashMap<>();
	public static AbstractChatingToChannelService getChatingService(int chatChannel) {
		return chatingServices.get(chatChannel);
	}
	
	public AbstractChatingToChannelService() {
		chatingServices.put(chatChannel().getNumber(), this);
	}
	
	
	public abstract PbChatChannel chatChannel();
	/**
	 * 获取聊天数据
	 * @param playerId
	 * @param targetId
	 * @return
	 */
	public abstract List<PbChatData> getChatDatas(PlayerContext playerContext, long targetId);
	/**
	 * 在某个频道发言
	 * @param playerContext
	 * @param request
	 * @return 发言冷却时间
	 */
	public abstract void chatTo(PlayerContext playerContext, long targetId, String content, String oriContent, boolean doubt);
	
	@Autowired
	protected PlayerDetailService playerDetailService;
	
	public long getChatCd(long playerId) {
		PlayerDetail detail = playerDetailService.getEntity(playerId);
		return detail.getChatCds().getOrDefault(chatChannel().getNumber(), 0L);
	}
	
	public long markChatCd(long playerId) {
		long now = DateTimeUtil.currMillis();
		PlayerDetail detail = playerDetailService.getEntity(playerId);
		//TODO 读常量
		detail.getChatCds().put(chatChannel().getNumber(), now + 30000);
		//这里更不更新都一样，无所谓
		return detail.getChatCds().get(chatChannel().getNumber());
	}
}
