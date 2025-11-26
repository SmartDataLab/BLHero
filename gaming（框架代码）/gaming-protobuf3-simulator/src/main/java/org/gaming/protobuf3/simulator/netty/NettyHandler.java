/**
 * 
 */
package org.gaming.protobuf3.simulator.netty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.gaming.protobuf3.simulator.protocol.ProtocolCenter;
import org.gaming.protobuf3.simulator.protocol.ProtocolMessage;
import org.gaming.protobuf3.simulator.protocol.ProtocolPrinter;
import org.gaming.simulator.netty.AbsNettyHandler;
import org.gaming.simulator.ui.base.ViewManager;
import org.gaming.simulator.ui.slim.ISimulatorCtrl.SendResult;

import com.google.protobuf.AbstractMessage;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author YY
 *
 */
@Sharable
public class NettyHandler extends AbsNettyHandler {

	private static Set<Class<?>> ignoreClass = new HashSet<>();
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ProtocolMessage message = ProtocolCenter.decode((byte[])msg);
		Class<?> clz = ProtocolCenter.getResponeProtocol(message.getProtocolId());
		Method method;
		try {
			method = clz.getMethod("parseFrom", byte[].class);
			AbstractMessage object = (AbstractMessage)method.invoke(clz, message.getContent());
			
			if(ignoreClass.contains(object.getClass())) {
				return;
			}
			
			String str = ProtocolPrinter.print(object);
			
			SendResult sendResult = new SendResult();
			sendResult.setTitle("================消息ID：" + message.getMessageId() + " 协议号：" + message.getProtocolId() + " 协议类：" + clz.getSimpleName() + " 接收时间：" + System.currentTimeMillis() + "================");
			sendResult.setContent(str);
			//输出到客户端
			ViewManager.addResult(sendResult);
			
			ProtocolCenter.pluginLogic.onRead(object);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		ProtocolCenter.pluginLogic.onConnect();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ProtocolCenter.pluginLogic.onDisconnect();
	}
	
	public static void addIgnoreClass(Class<?> clazz) {
		ignoreClass.add(clazz);
	}
}
