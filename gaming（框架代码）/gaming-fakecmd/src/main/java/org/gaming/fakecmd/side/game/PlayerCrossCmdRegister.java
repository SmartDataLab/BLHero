/**
 * 
 */
package org.gaming.fakecmd.side.game;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.gaming.fakecmd.cmd.AbstractCmdRegister;
import org.gaming.fakecmd.side.common.ProtobufCmdInvoker;
import org.gaming.fakecmd.side.common.ProtobufHelper;
import org.gaming.tool.DateTimeUtil;
import org.slf4j.MDC;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;

/**
 * @author YY
 *
 */
public class PlayerCrossCmdRegister extends AbstractCmdRegister<Integer, ProtobufCmdInvoker> {

	public static PlayerCrossCmdRegister INS = new PlayerCrossCmdRegister();
	
	private PlayerCrossCmdRegister() {}
	
	@Override
	protected Class<? extends Annotation> annotationClass() {
		return PlayerCrossCmd.class;
	}

	@Override
	protected Class<?>[] methodParamsType() {
		return new Class<?>[] { long.class, GeneratedMessageV3.class };
	}
	
	@Override
	protected ProtobufCmdInvoker registerMethod(Object bean, Method method) {
		try {
			Class<?> protobufClazz = method.getParameterTypes()[1];
			
			int cmd = ProtobufHelper.getProtobufId(protobufClazz);
			ProtobufCmdInvoker cmdInvoker = new ProtobufCmdInvoker(cmd, bean, method, protobufClazz);
			return cmdInvoker;
		} catch (Exception e) {
			logger.error("类{}中的协议函数{}定义异常", bean.getClass().getSimpleName(), method.getName());
			throw new RuntimeException(e);
		}
	}

	public static interface IPlayerCrossCmdMessage {
		IPlayerContext getPlayerContext();
		int getMessageId();
		int getCmd();
		ByteString getData();
		void onException(Exception e, Message requestObject);
		void fastResponse();
	}
	
	public void handle(IPlayerCrossCmdMessage message) {
		IPlayerContext playerContext = message.getPlayerContext();
		int messageId = message.getMessageId();
		int cmd = message.getCmd();
		long playerId = playerContext.getId();
		
		MDC.put("player", "player=" + playerId);
		
		ProtobufCmdInvoker invoker = this.getInvoker(cmd);
		if (invoker == null) {
			logger.warn("没有找到协议号[{}]对应的处理函数，尝试进行快速响应", cmd);
			message.fastResponse();
			return;
		}
		long startTime = DateTimeUtil.currMillis();
		Message requestObject = null;
		try {
			requestObject = invoker.getMessage().getParserForType().parseFrom(message.getData());
			if (logger.isDebugEnabled()) {
				String messagePrint = TextFormat.printer().shortDebugString(requestObject);
				logger.debug("收到消息，ID【{}】，协议号【{}】，消息内容【{}】", messageId, cmd, messagePrint);
			}

			Object response = invoker.getMethod().invoke(invoker.getBean(), playerId, requestObject);
			if (response != null) {
				int redirectCmd = ProtobufHelper.getProtobufId(response.getClass());
				playerContext.write(redirectCmd, (GeneratedMessageV3) response, messageId);
			}
		} catch (Exception e) {
			message.onException(e, requestObject);
		} finally {
			long endTime = DateTimeUtil.currMillis();
			if (endTime - startTime > 30) {
				logger.warn("消息ID【{}】，协议号【{}】处理慢，耗时【{}】毫秒", messageId, cmd, endTime - startTime);
			}
			MDC.clear();
		}
	}
}
