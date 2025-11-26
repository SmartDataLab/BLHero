/**
 * 
 */
package com.xiugou.x1.game.server.module.player;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;

/**
 * @author YY
 *
 */
public abstract class AbstractModuleHandler {
	
	protected static Logger logger = LoggerFactory.getLogger(AbstractModuleHandler.class);
	
	@Autowired
	protected PlayerContextManager playerContextManager;
	
	private final static Map<String, AbstractModuleHandler> handlerMap = new HashMap<>();
	
	public AbstractModuleHandler() {
		handlerMap.put(this.getClass().getSimpleName(), this);
	}
	
	public abstract InfoPriority infoPriority();
	/**
	 * 推送数据
	 * @param playerId
	 */
	public abstract void pushInfo(PlayerContext playerContext);
	/**
	 * 是否需要每日重置时推送
	 * @return
	 */
	public boolean needDailyPush() {
		return true;
	}
	
	protected void dailyResetForOnlinePlayer(long playerId) {
		//TODO to be override
	}
	
	public final static void pushInfo(PlayerContext playerContext, InfoPriority infoPriority) {
		for(AbstractModuleHandler handler : handlerMap.values()) {
			try {
				if(handler.infoPriority() == infoPriority) {
					handler.pushInfo(playerContext);
				}
			} catch (Exception e) {
				logger.error("玩家" + playerContext.getId() + "推送登录数据发生异常", e);
			}
		}
	}
	
	public final static AbstractModuleHandler getHandler(String name) {
		return handlerMap.get(name);
	}
	
	public final static void dailyPush(PlayerContext playerContext) {
		for(AbstractModuleHandler handler : handlerMap.values()) {
			if(!handler.needDailyPush()) {
				continue;
			}
			try {
				handler.pushInfo(playerContext);
			} catch (Exception e) {
				logger.error("玩家" + playerContext.getId() + "推送每日重置数据发生异常", e);
			}
		}
	}
	
	public final static void dailyReset(long playerId) {
		for(AbstractModuleHandler handler : handlerMap.values()) {
			try {
				handler.dailyResetForOnlinePlayer(playerId);
			} catch (Exception e) {
				logger.error("玩家" + playerId + "处理每日重置数据发生异常", e);
			}
		}
	}
	
	public enum InfoPriority {
		BASE,
		DETAIL,
	}
}
