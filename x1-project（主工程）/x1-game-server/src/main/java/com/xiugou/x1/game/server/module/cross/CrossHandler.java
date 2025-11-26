/**
 * 
 */
package com.xiugou.x1.game.server.module.cross;

import org.gaming.fakecmd.annotation.CrossCmd;
import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.gaming.fakecmd.annotation.PlayerToOtherResponseCmd;
import org.gaming.fakecmd.side.game.IPlayer;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.protobuf.ByteString;
import com.xiugou.x1.game.server.foundation.cross.CrossContext;
import com.xiugou.x1.game.server.foundation.cross.CrossContextManager;
import com.xiugou.x1.game.server.foundation.player.PlayerActorPool;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.player.PlayerContextPhantom;
import com.xiugou.x1.game.server.foundation.player.PlayerCrossMessage;
import com.xiugou.x1.game.server.foundation.player.PlayerToOtherRequestMessage;
import com.xiugou.x1.game.server.foundation.player.PlayerToOtherResponseMessage;

import akka.actor.ActorRef;
import pb.xiugou.x1.protobuf.cross.Cross.GameConnectConfirmResponse;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerCrossWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherRequestWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherResponseWrapper;
import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
@Controller
public class CrossHandler {
	
private static Logger logger = LoggerFactory.getLogger(CrossHandler.class);
	
	@Autowired
	private PlayerContextManager playerContextManager;
	
	@CrossCmd
	public void connectConfirm(CrossContext crossContext, GameConnectConfirmResponse response) {
		logger.info("游戏服{}连接跨服已得到确认", response.getServerId());
		CrossContextManager.setContext(crossContext);
	}
	
	@CrossCmd
	public void playerCross(CrossContext crossContext, PlayerCrossWrapper wrapper) {
		long playerId = wrapper.getPlayerId();
		int messageId = wrapper.getId();
		int protoId = wrapper.getProtoId();
		ByteString data = wrapper.getData();
		long syncId = wrapper.getSyncId();
		if(syncId > 0) {
			//丢到同步队列
			SyncMessage syncMessage = new SyncMessage();
			syncMessage.setProtoId(protoId);
			syncMessage.setData(data);
			CrossContextManager.syncMessageManager.result(syncId, syncMessage);
		} else {
			IPlayerContext playerContext = playerContextManager.getContext(playerId);
			if(playerContext == null) {
				playerContext = new PlayerContextPhantom(playerId);
			}
			PlayerCrossMessage message = new PlayerCrossMessage(playerContext, messageId, protoId, data);
			ActorRef actor = PlayerActorPool.getActor(playerId);
			actor.tell(message, actor);
		}
	}
	
	@CrossCmd
	public void tips(CrossContext crossContext, TipsMessage message) {
		logger.error("收到跨服消息处理异常{}-{}", message.getCode(), message.getDevTips());
	}
	
	@CrossCmd
	public void jump(CrossContext crossContext, PlayerToOtherRequestWrapper request) {
		PlayerToOtherRequestMessage message = new PlayerToOtherRequestMessage(request);
		ActorRef actor = PlayerActorPool.getActor(request.getToPlayerId());
		actor.tell(message, actor);
	}
	
	@CrossCmd
	public void jump(CrossContext crossContext, PlayerToOtherResponseWrapper response) {
		IPlayerContext playerContext = playerContextManager.getContext(response.getToPlayerId());
		
		PlayerToOtherResponseMessage message = new PlayerToOtherResponseMessage(playerContext, response);
		ActorRef actor = PlayerActorPool.getActor(response.getToPlayerId());
		actor.tell(message, actor);
	}
	
	
	@PlayerToOtherResponseCmd
	public TipsMessage jumpTips(IPlayerContext playerContext, IPlayer otherPlayer, TipsMessage message) {
		return message;
	}
	
	@PlayerCrossCmd
	public TipsMessage crossTips(long playerId, TipsMessage message) {
		return message;
	}
}
