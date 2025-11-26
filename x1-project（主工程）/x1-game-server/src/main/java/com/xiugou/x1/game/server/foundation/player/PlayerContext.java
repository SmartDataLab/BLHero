/**
 *
 */
package com.xiugou.x1.game.server.foundation.player;

import java.util.Objects;

import org.gaming.fakecmd.side.common.InternalCmdRegister.InternalCmdMessage;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.ruler.util.SymbolConstants;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

import akka.actor.ActorRef;
import io.netty.channel.Channel;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 *
 * @author YY
 *
 */
public class PlayerContext implements IPlayerContext {

	/**
	 * 是否已登录
	 */
	private long id;
	private int serverId;
	private String openId;

	private boolean login = false;

	private final Channel channel;

	private ActorRef actor;
	private boolean actorReady;

	private LogoutType logoutType;
	//是否为新人
	private boolean newComer;
	
	private ByteString channelData;
	
	//窃听器
	private PlayerContext wiretap;
	//设备类型
	private String deviceType;
	//设备ID
	private String deviceId;
	
	private String langType = "cn";
	
	private int currMsgId;

	public PlayerContext(Channel channel) {
		this.channel = channel;
		// 玩家刚连接的时候，随机给一个actor进行处理，在玩家确切登陆或者创号之后，会重定向到固定的actor中
		this.actor = PlayerActorPool.getOneActor();
	}

	public void write(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		write(proto.getNumber(), msg, 0);
	}

	public void write(int protoId, GeneratedMessageV3 msg, int messageId) {
		if (isChannelActive()) {
			MessageWrapper.Builder builder = MessageWrapper.newBuilder();
			builder.setId(messageId);
			builder.setProtoId(protoId);
			builder.setData(msg.toByteString());
			this.channel.writeAndFlush(builder.build().toByteArray());
			if(wiretap != null) {
				wiretap.write(protoId, msg, messageId);
			}
		} else {
			// TODO 记录日志
		}
	}

	public void write(byte[] data) {
		if (isChannelActive()) {
			this.channel.writeAndFlush(data);
			if(wiretap != null) {
				wiretap.write(data);
			}
		}
	}

	/**
	 * 玩家下线并清理消息
	 */
	public void offline() {
		this.login = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public Channel getChannel() {
		return channel;
	}

	/**
	 * 连接是否还有效
	 *
	 * @return
	 */
	public boolean isChannelActive() {
		if (channel != null && channel.isActive()) {
			return true;
		}
		return false;
	}

	public void closeChannel() {
		if (channel != null && channel.isActive()) {
			this.channel.close();
		}
	}

	public void tell(MessageWrapper wrapper) {
		ProtocolMessage message = new ProtocolMessage(this, wrapper);
		actor.tell(message, actor);
	}

	public void tell(InternalCmdMessage message) {
		actor.tell(message, actor);
	}

	public void setActor(ActorRef actor) {
		this.actor = actor;
	}

	public ActorRef getActor() {
		return actor;
	}

	public String getLocalAddress() {
		if (Objects.isNull(channel) || Objects.isNull(channel.localAddress())) {
			return SymbolConstants.EMPTY;
		}
		// channel.localAddress会得到ip+port，形如/192.168.0.1:12345
		String address = channel.localAddress().toString().replace(SymbolConstants.SLASH, SymbolConstants.EMPTY);
		return address.substring(0, address.indexOf(SymbolConstants.COLON));
	}

	public String getRemoteAddress() {
		if (Objects.isNull(channel) || Objects.isNull(channel.remoteAddress())) {
			return SymbolConstants.EMPTY;
		}
		// channel.remoteAddress会得到ip+port，形如/192.168.0.1:12345
		String address = "";
		try {
			address = channel.remoteAddress().toString().replace(SymbolConstants.SLASH, SymbolConstants.EMPTY);
		} catch (Exception e) {
			return SymbolConstants.EMPTY;
		}
		return address.substring(0, address.indexOf(SymbolConstants.COLON));
	}

	public LogoutType getLogoutType() {
		return logoutType;
	}

	public void setLogoutType(LogoutType logoutType) {
		this.logoutType = logoutType;
	}

	public boolean isActorReady() {
		return actorReady;
	}

	public void setActorReady(boolean actorReady) {
		this.actorReady = actorReady;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public boolean isNewComer() {
		return newComer;
	}

	public void setNewComer(boolean newComer) {
		this.newComer = newComer;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public ByteString getChannelData() {
		return channelData;
	}

	public void setChannelData(ByteString channelData) {
		this.channelData = channelData;
	}

	public void setWiretap(PlayerContext wiretap) {
		this.wiretap = wiretap;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	public int getCurrMsgId() {
		return currMsgId;
	}

	public void setCurrMsgId(int currMsgId) {
		this.currMsgId = currMsgId;
	}

	@Override
	public void write(int protoId, ByteString data, int messageId) {
		if (isChannelActive()) {
			MessageWrapper.Builder builder = MessageWrapper.newBuilder();
			builder.setId(messageId);
			builder.setProtoId(protoId);
			builder.setData(data);
			this.channel.writeAndFlush(builder.build().toByteArray());
			if(wiretap != null) {
				wiretap.write(protoId, data, messageId);
			}
		} else {
			// TODO 记录日志
		}
	}

	@Override
	public int getServerZone() {
		return serverId;
	}
}
