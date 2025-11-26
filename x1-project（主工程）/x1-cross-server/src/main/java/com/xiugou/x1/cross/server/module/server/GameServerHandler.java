/**
 * 
 */
package com.xiugou.x1.cross.server.module.server;

import org.gaming.fakecmd.annotation.CrossCmd;
import org.gaming.fakecmd.annotation.InternalCmd;
import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.gaming.prefab.exception.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.cross.server.foundation.game.GameActorPool;
import com.xiugou.x1.cross.server.foundation.game.GameContext;
import com.xiugou.x1.cross.server.foundation.game.GameContextManager;
import com.xiugou.x1.cross.server.foundation.game.GameDisconnectMessage;
import com.xiugou.x1.cross.server.foundation.game.PlayerProtocolMessage;
import com.xiugou.x1.cross.server.module.server.model.GameServer;
import com.xiugou.x1.cross.server.module.server.service.CrossGroupSystemService;
import com.xiugou.x1.cross.server.module.server.service.GameServerService;
import com.xiugou.x1.design.constant.TipsCode;

import akka.actor.ActorRef;
import pb.xiugou.x1.protobuf.cross.Cross.GameConnectConfirmRequest;
import pb.xiugou.x1.protobuf.cross.Cross.GameConnectConfirmResponse;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerCrossWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerTestSyncWriteRpcRequest;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerTestSyncWriteRpcResponse;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherRequestWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherResponseWrapper;

/**
 * @author YY
 *
 */
@Controller
public class GameServerHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GameServerHandler.class);
	
	@Autowired
	private GameContextManager gameContextManager;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private CrossGroupSystemService crossGroupSystemService;
	
	@CrossCmd
	public GameConnectConfirmResponse connect(GameContext gameContext, GameConnectConfirmRequest request) {
		logger.info("收到游戏服{}的连接确认请求", request.getMainServerId());
		gameContext.setId(request.getMainServerId());
		gameContext.getMergeServerIds().addAll(request.getMergeServerIdsList());
		gameContext.setActor(GameActorPool.getActor(gameContext.getId()));
		gameContextManager.addContext(gameContext.getId(), request.getMergeServerIdsList(), gameContext);
		
		gameServerService.getEntity(gameContext.getId());
		for(int mergeServerId : gameContext.getMergeServerIds()) {
			GameServer mergeServer = gameServerService.getEntity(mergeServerId);
			if(mergeServer.getMergeToServer() == gameContext.getId()) {
				continue;
			}
			//设置已合服
			mergeServer.setMergeToServer(gameContext.getId());
			gameServerService.update(mergeServer);
		}
		//添加到跨服中
		crossGroupSystemService.joinToCrossGroup(gameContext.getId());
		
		GameConnectConfirmResponse.Builder response = GameConnectConfirmResponse.newBuilder();
		response.setServerId(gameContext.getId());
		return response.build();
	}
	
	@CrossCmd
	public void player(GameContext gameContext, PlayerCrossWrapper wrapper) {
		//转发玩家的消息
		PlayerProtocolMessage message = new PlayerProtocolMessage(gameContext, wrapper);
		ActorRef actor = GameActorPool.getActor(wrapper.getPlayerId());
		actor.tell(message, actor);
	}
	
	@CrossCmd
	public void jump(GameContext gameContext, PlayerToOtherRequestWrapper wrapper) {
		System.out.println("GameServerHandler.jumpRequest " + wrapper.getProtoId());
		boolean isLinking = gameContextManager.isGameContextLinking(wrapper.getToServerId());
		Asserts.isTrue(isLinking, TipsCode.GAME_CONTEXT_NOT_LINK, wrapper.getToServerId());
		
		GameContextManager.writeTo(wrapper.getToServerId(), PlayerToOtherRequestWrapper.Proto.ID, wrapper);
	}
	
	@CrossCmd
	public void jump(GameContext gameContext, PlayerToOtherResponseWrapper wrapper) {
		System.out.println("GameServerHandler.jumpResponse " + wrapper.getProtoId());
		boolean isLinking = gameContextManager.isGameContextLinking(wrapper.getToServerId());
		Asserts.isTrue(isLinking, TipsCode.GAME_CONTEXT_NOT_LINK, wrapper.getToServerId());
		
		GameContextManager.writeTo(wrapper.getToServerId(), PlayerToOtherResponseWrapper.Proto.ID, wrapper);
	}
	
	@InternalCmd
	public void handle(GameDisconnectMessage message) {
		GameContext gameContext = message.getGameContext();
		logger.info("游戏服{}的连接已断开", gameContext.getId());
		gameContextManager.removeContext(gameContext);
	}
	
	@PlayerCrossCmd
	public PlayerTestSyncWriteRpcResponse testWriteSync(GameContext gameContext, long playerId, PlayerTestSyncWriteRpcRequest request) {
		PlayerTestSyncWriteRpcResponse.Builder response = PlayerTestSyncWriteRpcResponse.newBuilder();
		response.setSomeId(8848);
		response.setSomeStr("Hello Game");
		return response.build();
	}
}
