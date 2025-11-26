/**
 * 
 */
package com.xiugou.x1.game.server.foundation.cross;

import org.gaming.fakecmd.side.common.ProtobufHelper;
import org.gaming.fakecmd.side.game.IPlayer;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.akka.SyncMessageManager;
import org.gaming.ruler.akka.SyncMessageManager.SyncFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.cross.SyncMessage;

import pb.xiugou.x1.protobuf.cross.Cross.PlayerCrossWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherRequestWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerToOtherResponseWrapper;
import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
@Component
public class CrossContextManager {

	private static Logger logger = LoggerFactory.getLogger(CrossContextManager.class);
	
	private static CrossContext context;
	
	public static final SyncMessageManager syncMessageManager = new SyncMessageManager();

	public static void setContext(CrossContext context) {
		if(CrossContextManager.context != null && CrossContextManager.context != context) {
			CrossContextManager.context.closeChannel();
		}
		CrossContextManager.context = context;
	}
	
	public static void write(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		if(context == null) {
			logger.error("还未连接跨服，消息{}-{}无法送达", proto.getNumber(), msg.getClass().getSimpleName());
			return;
		}
		context.write(proto, msg);
	}
	
	public static void write(PlayerContext playerContext, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		if(context == null) {
			logger.error("还未连接跨服，消息{}-{}-{}无法送达", playerContext.getCurrMsgId(), proto.getNumber(), msg.getClass().getSimpleName());
			return;
		}
		PlayerCrossWrapper.Builder builder = PlayerCrossWrapper.newBuilder();
		builder.setId(playerContext.getCurrMsgId());
		builder.setPlayerId(playerContext.getId());
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		context.write(PlayerCrossWrapper.Proto.ID, builder.build());
	}
	
	public static <T extends GeneratedMessageV3> T writeSync(IPlayerContext playerContext, ProtocolMessageEnum proto, GeneratedMessageV3 msg, Class<T> protobufClazz) {
		if(context == null) {
			logger.error("还未连接跨服，消息{}-{}-{}无法送达", playerContext.getCurrMsgId(), proto.getNumber(), msg.getClass().getSimpleName());
			return null;
		}
		SyncFuture future = syncMessageManager.register(protobufClazz, 2000);
		
		PlayerCrossWrapper.Builder builder = PlayerCrossWrapper.newBuilder();
		builder.setId(playerContext.getCurrMsgId());
		builder.setPlayerId(playerContext.getId());
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		builder.setSyncId(future.getSyncId());
		context.write(PlayerCrossWrapper.Proto.ID, builder.build());
		SyncMessage syncMessage = (SyncMessage)future.get();
		
		if(syncMessage.getProtoId() == TipsMessage.Proto.ID.getNumber()) {
			TipsMessage build = ProtobufHelper.parse(TipsMessage.class, syncMessage.getData());
			Asserts.isTrue(false, TipsCode.valueOf(build.getCode()), build.getParamsList().toArray());
			return null;
		} else {
			return ProtobufHelper.parse(protobufClazz, syncMessage.getData());
		}
	}
	
	public static void jumpRequest(IPlayerContext fromPlayer, IPlayer toPlayer, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		if(context == null) {
			logger.error("还未连接跨服，消息{}-{}-{}无法送达", fromPlayer.getCurrMsgId(), proto.getNumber(), msg.getClass().getSimpleName());
			return;
		}
		
		PlayerToOtherRequestWrapper.Builder builder = PlayerToOtherRequestWrapper.newBuilder();
		builder.setFromPlayerId(fromPlayer.getId());
		builder.setFromServerId(fromPlayer.getServerZone());
		builder.setToPlayerId(toPlayer.getId());
		builder.setToServerId(toPlayer.getServerZone());
		builder.setId(fromPlayer.getCurrMsgId());
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		PlayerToOtherRequestWrapper wrapper = builder.build();
		System.out.println("jumpRequest " + wrapper.getId());
		
		//TODO 后续优化时放开
//		if(fromPlayer.getDbZone() == toPlayer.getDbZone()) {
//			//如果双方都是同一个服务器，直接在本服内进行转发即可
//			PlayerCrossJumpMessage jumpMessage = new PlayerCrossJumpMessage(wrapper);
//			ActorRef actor = PlayerActorPool.getActor(toPlayer.getId());
//			actor.tell(jumpMessage, actor);
//			return;
//		}
		
		context.write(PlayerToOtherRequestWrapper.Proto.ID, wrapper);
	}
	
	public static <T extends GeneratedMessageV3> T jumpRequestSync(IPlayerContext fromPlayer, IPlayer toPlayer,
			ProtocolMessageEnum proto, GeneratedMessageV3 msg, Class<T> protobufClazz) {
		if(context == null) {
			logger.error("还未连接跨服，消息{}-{}-{}无法送达", fromPlayer.getCurrMsgId(), proto.getNumber(), msg.getClass().getSimpleName());
			return null;
		}
		SyncFuture future = syncMessageManager.register(protobufClazz, 2000);
		
		PlayerToOtherRequestWrapper.Builder builder = PlayerToOtherRequestWrapper.newBuilder();
		builder.setFromPlayerId(fromPlayer.getId());
		builder.setFromServerId(fromPlayer.getServerZone());
		builder.setToPlayerId(toPlayer.getId());
		builder.setToServerId(toPlayer.getServerZone());
		builder.setId(fromPlayer.getCurrMsgId());
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		builder.setSyncId(future.getSyncId());
		PlayerToOtherRequestWrapper wrapper = builder.build();
		
		//TODO 后续优化时放开
//		if(fromPlayer.getDbZone() == toPlayer.getDbZone()) {
//			//如果双方都是同一个服务器，直接在本服内进行转发即可
//			PlayerCrossJumpMessage jumpMessage = new PlayerCrossJumpMessage(wrapper);
//			ActorRef actor = PlayerActorPool.getActor(toPlayer.getId());
//			actor.tell(jumpMessage, actor);
//			return;
//		}
		
		context.write(PlayerToOtherRequestWrapper.Proto.ID, wrapper);
		System.out.println("jumpRequest " + wrapper.getId());
		SyncMessage syncMessage = (SyncMessage)future.get();
		
		if(syncMessage.getProtoId() == TipsMessage.Proto.ID.getNumber()) {
			TipsMessage build = ProtobufHelper.parse(TipsMessage.class, syncMessage.getData());
			Asserts.isTrue(false, TipsCode.valueOf(build.getCode()), build.getParamsList().toArray());
			return null;
		} else {
			return ProtobufHelper.parse(protobufClazz, syncMessage.getData());
		}
	}
	
	public static void jumpResponse(IPlayerContext fromPlayer, IPlayer toPlayer, int protoId, GeneratedMessageV3 msg) {
		if(context == null) {
			logger.error("还未连接跨服，消息{}-{}-{}无法送达", fromPlayer.getCurrMsgId(), protoId, msg.getClass().getSimpleName());
			return;
		}
		PlayerToOtherResponseWrapper.Builder builder = PlayerToOtherResponseWrapper.newBuilder();
		builder.setFromPlayerId(fromPlayer.getId());
		builder.setFromServerId(fromPlayer.getServerZone());
		builder.setToPlayerId(toPlayer.getId());
		builder.setToServerId(toPlayer.getServerZone());
		builder.setId(fromPlayer.getCurrMsgId());
		builder.setProtoId(protoId);
		builder.setData(msg.toByteString());
		PlayerToOtherResponseWrapper wrapper = builder.build();
		
		//TODO 后续优化时放开
//		if(fromPlayer.getDbZone() == toPlayer.getDbZone()) {
//			//如果双方都是同一个服务器，直接在本服内进行转发即可
//			PlayerCrossJumpMessage jumpMessage = new PlayerCrossJumpMessage(wrapper);
//			ActorRef actor = PlayerActorPool.getActor(toPlayer.getId());
//			actor.tell(jumpMessage, actor);
//			return;
//		}
		
		context.write(PlayerToOtherResponseWrapper.Proto.ID, wrapper);
	}
	
	public static void jumpResponse(IPlayerContext fromPlayer, IPlayer toPlayer, ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		jumpResponse(fromPlayer, toPlayer, proto.getNumber(), msg);
	}
	
}
