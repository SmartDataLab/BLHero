/**
 * 
 */
package com.xiugou.x1.simulator.pressing;

import org.gaming.simulator.netty.websocket.WebSocketHandShakeHandler;
import org.gaming.simulator.ui.slim.panel.LinkType;

import com.xiugou.x1.simulator.pressing.message.ConnectMessage;
import com.xiugou.x1.simulator.pressing.message.ConnectToServerMessage;
import com.xiugou.x1.simulator.pressing.message.OperationMessage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.player.Player.LoginRequest;

/**
 * @author YY
 *
 */
public class PlayerActor extends AbstractActor {
	
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(ConnectToServerMessage.class, msg -> {
			//连接处理
			handle(msg);
		}).match(ConnectMessage.class, msg -> {
			//连接处理
			handle(msg);
		}).match(OperationMessage.class, msg -> {
			//协议处理
			handle(msg);
		}).build();
	}
	
	
	public void handle(ConnectToServerMessage message) {
		PlayerContext playerContext = message.getPlayerContext();
		playerContext.getNetty().connect(message.getUrl(), LinkType.Ws);
		playerContext.getNetty().getChannel().attr(X1Pressing.PLAYERCONTEXT).set(playerContext);
		playerContext.getActor().tell(new ConnectMessage(message.getPlayerContext()), ActorRef.noSender());
		System.out.println("ConnectToServerMessage");
	}
	
	public void handle(ConnectMessage message) {
		PlayerContext playerContext = message.getPlayerContext();
		WebSocketHandShakeHandler wsHandler = playerContext.getNetty().getChannel().pipeline().get(WebSocketHandShakeHandler.class);
//		int count = 0;
//		while(!wsHandler.isHandshaked() && count < 10) {
//			try {
//				System.out.println("Handshaked");
//				Thread.sleep(500);
//				count++;
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		LoginRequest.Builder builder = LoginRequest.newBuilder();
//		builder.setOpenId(playerContext.getOpenId());
//		builder.setServerId(playerContext.getServerId());
//		builder.setChannelId(1);
//		playerContext.write(LoginRequest.Proto.ID, builder.build());
		
		if(!wsHandler.isHandshaked()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			playerContext.getActor().tell(message, ActorRef.noSender());
		} else {
			System.out.println(playerContext.getId() + "Login " + playerContext.getNetty().getChannel().id().toString());
			LoginRequest.Builder builder = LoginRequest.newBuilder();
			builder.setOpenId(playerContext.getOpenId());
			builder.setServerId(playerContext.getServerId());
			builder.setChannelId(1);
			playerContext.write(LoginRequest.Proto.ID, builder.build());
		}
	}
	
	public void handle(OperationMessage message) {
		MessageWrapper wrapper = message.getWrapper();
		message.getPlayerContext().getMessageHandler().dispatch(message.getPlayerContext(), wrapper.getProtoId(), wrapper.getData());
	}
	
}
