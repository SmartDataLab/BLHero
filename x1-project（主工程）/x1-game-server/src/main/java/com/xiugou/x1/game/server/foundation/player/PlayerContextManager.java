/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.player.Player.LogoutMessage;

/**
 * @author YY
 *
 */
@Service
public class PlayerContextManager implements Lifecycle {

	private static Logger logger = LoggerFactory.getLogger(PlayerContextManager.class);
	
	private ConcurrentMap<Long, PlayerContext> contexts = new ConcurrentHashMap<>();
	
	//每小时统计的数据
	//最高在线人数
	private int maxOnline;
	//最低在线人数
	private int minOnline;
	//创建账号人数
	private int createNum;
	//登录账号人数
	private ConcurrentMap<Long, Integer> loginNum = new ConcurrentHashMap<>();
	
	
	//每5分钟统计的数据
	//在线人数
	private int maxOnline5;
	//在线新人数
	private int maxNewComer5;
	
	private ConcurrentMap<Long, PlayerContext> newComers = new ConcurrentHashMap<>();
	
	public PlayerContext getContext(long playerId) {
		return contexts.get(playerId);
	}
	
	/**
	 * 添加到在线集合
	 * @param playerContext
	 */
	public void addToOnline(PlayerContext playerContext) {
		contexts.put(playerContext.getId(), playerContext);
		if(contexts.size() > maxOnline) {
			maxOnline = contexts.size();
		}
		if(contexts.size() > maxOnline5) {
			maxOnline5 = contexts.size();
		}
		if(playerContext.isNewComer()) {
			newComers.put(playerContext.getId(), playerContext);
			if(newComers.size() > maxNewComer5) {
				maxNewComer5 = newComers.size();
			}
			createNum += 1;
		}
		loginNum.put(playerContext.getId(), 1);
	}
	
	public void removeFromOnline(PlayerContext playerContext) {
		boolean removed = contexts.remove(playerContext.getId(), playerContext);
		logger.info("移除连接:" + playerContext.getId() + " " + removed);
		if(contexts.size() < minOnline) {
			minOnline = contexts.size();
		}
		if(playerContext.isNewComer()) {
			newComers.remove(playerContext.getId());
		}
	}
	
	/**
	 * 重置在线人数记录器
	 */
	public void resetCounter() {
		this.maxOnline = contexts.size();
		this.minOnline = contexts.size();
		this.createNum = 0;
		this.loginNum.clear();
	}
	
	/**
	 * 重置5分钟的在线人数记录器
	 */
	public void resetCounter5() {
		maxOnline5 = contexts.size();
		maxNewComer5 = newComers.size();
	}
	
	/**
	 * 推送
	 * @param playerId
	 * @param proto
	 * @param msg
	 */
	public void push(long playerId, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		PlayerContext playerContext = getContext(playerId);
		if(playerContext != null) {
			playerContext.write(proto, msg);
		}
	}
	
	public void pushAll(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
    	builder.setProtoId(proto.getNumber());
    	builder.setData(msg.toByteString());
    	byte[] data = builder.build().toByteArray();
    	for(PlayerContext playerContext : contexts.values()) {
    		playerContext.write(data);
    	}
	}
	
	public void pushSome(Collection<Long> playerIds, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
    	builder.setProtoId(proto.getNumber());
    	builder.setData(msg.toByteString());
    	byte[] data = builder.build().toByteArray();
    	for(Long playerId : playerIds) {
    		PlayerContext playerContext = getContext(playerId);
    		if(playerContext != null) {
    			playerContext.write(data);
    		}
    	}
	}
	
	public Collection<PlayerContext> onlines() {
		return contexts.values();
	}
	
	public boolean isOnline(long playerId) {
		return contexts.containsKey(playerId);
	}

	public int getMaxOnline() {
		return maxOnline;
	}

	public int getMinOnline() {
		return minOnline;
	}

	public int getCreateNum() {
		return createNum;
	}

	public int getLoginNum() {
		return loginNum.size();
	}

	public int getMaxOnline5() {
		return maxOnline5;
	}

	public int getMaxNewComer5() {
		return maxNewComer5;
	}

	@Override
	public void stop() throws Exception {
		LogoutMessage.Builder message = LogoutMessage.newBuilder();
		message.setType(LogoutType.MAINTAIN.getCode());
		this.pushAll(LogoutMessage.Proto.ID, message.build());
	}

	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MAX);
	}
}
