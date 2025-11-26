/**
 * 
 */
package com.xiugou.x1.simulator.pressing.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.xiugou.x1.simulator.pressing.PlayerContext;

import pb.xiugou.x1.protobuf.bag.Bag.ThingChangeMessage;
import pb.xiugou.x1.protobuf.gm.Gm.GmRequest;
import pb.xiugou.x1.protobuf.player.Player.LoginResponse;

/**
 * @author YY
 *
 */
public class MessageHandler {

	private Map<Integer, MessageInvoker> protoMap = new HashMap<>();
	private AtomicInteger loginCounter = new AtomicInteger();
	
	@SuppressWarnings("unchecked")
	public MessageHandler() {
		for(Method method : MessageHandler.class.getDeclaredMethods()) {
			Class<?> param1Clazz = method.getParameterTypes()[1];
			if(!GeneratedMessageV3.class.isAssignableFrom(param1Clazz)) {
				continue;
			}
			Class<?> protoClazz = null;
			for(Class<?> c : param1Clazz.getDeclaredClasses()) {
				if(!"Proto".equals(c.getSimpleName())) {
					continue;
				}
				protoClazz = c;
				break;
			}
			if(protoClazz == null) {
				continue;
			}
			try {
				Object[] obj = (Object[])protoClazz.getDeclaredMethod("values").invoke(null);
				Method getNumber = protoClazz.getDeclaredMethod("getNumber");
				int protocolId = (int)getNumber.invoke(obj[1]);
				
				MessageInvoker messageInvoker = new MessageInvoker(protocolId, this, method, (Class<? extends GeneratedMessageV3>)param1Clazz);
				protoMap.put(protocolId, messageInvoker);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void dispatch(PlayerContext playerContext, int protocolId, ByteString byteString) {
		MessageInvoker messageInvoker = protoMap.get(protocolId);
		if(messageInvoker == null) {
			return;
		}
		try {
			Message message = messageInvoker.getMessage().getParserForType().parseFrom(byteString);
			messageInvoker.getMethod().invoke(messageInvoker.getInvoker(), playerContext, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void step1(PlayerContext playerContext, LoginResponse response) {
		GmRequest.Builder builder1 = GmRequest.newBuilder();
		builder1.setValue("ADD_PROP");
		playerContext.write(GmRequest.Proto.ID, builder1.build());
		
		GmRequest.Builder builder2 = GmRequest.newBuilder();
		builder2.setValue("ALL_ITEM");
		playerContext.write(GmRequest.Proto.ID, builder2.build());
		
		System.out.println("已有" + loginCounter.incrementAndGet() + "测试连接");
	}
	
	public void step2(PlayerContext playerContext, ThingChangeMessage response) {
//		System.out.println(playerContext.getId() + "收到资源变化推送");
	}
}
