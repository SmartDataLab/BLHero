/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.fakecmd.side.game.IPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 * @author YY
 *
 */
@Component
public class GameContextManager {

	private static Logger logger = LoggerFactory.getLogger(GameContextManager.class);

	private static ConcurrentMap<Integer, GameContext> contextMap = new ConcurrentHashMap<>();

	public void addContext(int mainServerId, List<Integer> mergeServerIds, GameContext gameContext) {
		GameContext oldContext = contextMap.get(mainServerId);
		if (oldContext != null && oldContext != gameContext) {
			// 关闭旧连接
			oldContext.closeChannel();
			logger.info("关闭游戏服{}的旧连接，连接ID {}", mainServerId, oldContext.getChannel().id().toString());
		}
		contextMap.put(mainServerId, gameContext);
		logger.info("游戏服{}注册主连接成功，连接ID {}", mainServerId, gameContext.getChannel().id().toString());
		for (int mergeServerId : mergeServerIds) {
			GameContext oldSubContext = contextMap.get(mergeServerId);
			if (oldSubContext != null && oldSubContext != gameContext) {
				// 关闭旧连接
				oldSubContext.closeChannel();
			}
			contextMap.put(mergeServerId, gameContext);
			logger.info("游戏服{}注册合服连接成功，连接ID {}", mainServerId, gameContext.getChannel().id().toString());
		}
	}

	public void removeContext(GameContext gameContext) {
		boolean remove = contextMap.remove(gameContext.getId(), gameContext);
		if (remove) {
			gameContext.closeChannel();
			logger.info("游戏服{}主连接已关闭，连接ID {}", gameContext.getId(), gameContext.getChannel().id().toString());
		}
		for(int mergeServerId : gameContext.getMergeServerIds()) {
			boolean remove0 = contextMap.remove(mergeServerId, gameContext);
			if (remove0) {
				logger.info("游戏服{}合服连接已关闭，连接ID {}", gameContext.getId(), gameContext.getChannel().id().toString());
			}
		}
	}

	/**
	 * 
	 * @param serverId
	 * @return
	 */
	public boolean isGameContextLinking(int serverId) {
		GameContext context = contextMap.get(serverId);
		if (context == null) {
			return false;
		}
		return context.isChannelActive();
	}

	/**
	 * 广播消息
	 * 
	 * @param proto
	 * @param msg
	 */
	public static void broadcast(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
		builder.setId(0);
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		byte[] data = builder.build().toByteArray();
		for (GameContext gameContext : contextMap.values()) {
			gameContext.write(data);
		}
	}

	public static void writeTo(Collection<Integer> serverIds, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
		builder.setId(0);
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		byte[] data = builder.build().toByteArray();
		
		for(int serverId : serverIds) {
			GameContext gameContext = contextMap.get(serverId);
			if (gameContext == null) {
				logger.error("未找到游戏服连接，消息{}-{}无法送达", proto.getNumber(), msg.getClass().getSimpleName());
				return;
			}
			gameContext.write(data);
		}
	}
	
	/**
	 * 将消息发送至指定游戏服
	 * 
	 * @param serverId
	 * @param proto
	 * @param msg
	 */
	public static void writeTo(int serverId, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		GameContext gameContext = contextMap.get(serverId);
		if (gameContext == null) {
			logger.error("未找到游戏服连接，消息{}-{}无法送达", proto.getNumber(), msg.getClass().getSimpleName());
			return;
		}
		gameContext.write(proto, msg);
	}

	public static void writeToPlayer(IPlayer player, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		GameContext gameContext = contextMap.get(player.getServerZone());
		if (gameContext == null) {
			logger.error("未找到游戏服连接，消息{}-{}无法送达", proto.getNumber(), msg.getClass().getSimpleName());
			return;
		}
		gameContext.writeToPlayer(player.getId(), proto, msg, 0, 0);
	}

	public static void printGames() {
		for (GameContext gameContext : contextMap.values()) {
			System.out.println("服务器ID：" + gameContext.getId());
		}
	}
}
