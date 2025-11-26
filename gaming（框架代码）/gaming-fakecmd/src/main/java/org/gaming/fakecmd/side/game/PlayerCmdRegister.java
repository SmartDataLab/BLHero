/**
 * 
 */
package org.gaming.fakecmd.side.game;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.cmd.AbstractCmdRegister;
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
public class PlayerCmdRegister extends AbstractCmdRegister<Integer, PlayerCmdInvoker> {

	public static PlayerCmdRegister INS = new PlayerCmdRegister();
	
	private PlayerCmdRegister() {}

	@Override
	protected Class<? extends Annotation> annotationClass() {
		return PlayerCmd.class;
	}
	
	@Override
	protected Class<?>[] methodParamsType() {
		return new Class<?>[] { IPlayerContext.class, GeneratedMessageV3.class };
	}
	
	@Override
	protected PlayerCmdInvoker registerMethod(Object bean, Method method) {
		try {
			Class<?> protobufClazz = method.getParameterTypes()[1];
			PlayerCmd playerCmd = method.getAnnotation(PlayerCmd.class);
			
			int cmd = ProtobufHelper.getProtobufId(protobufClazz);
			PlayerCmdInvoker protocolInvoker = new PlayerCmdInvoker(cmd, playerCmd.needLogin(), playerCmd.forwardTo(), bean, method, protobufClazz);
			return protocolInvoker;
		} catch (Exception e) {
			logger.error("类{}中的协议函数{}定义异常", bean.getClass().getSimpleName(), method.getName());
			throw new RuntimeException(e);
		}
	}

	
	public static interface IPlayerCmdMessage {
		IPlayerContext getPlayerContext();
		int getMessageId();
		int getCmd();
		ByteString getData();
		void onException(Exception e, Message requestObject);
		void onResponse(Object response);
	}
	
	
	public void handle(IPlayerCmdMessage message) {
		IPlayerContext playerContext = message.getPlayerContext();
		int messageId = message.getMessageId();
		//协议号
		int cmd = message.getCmd();
		//协议内容
		ByteString content = message.getData();

        MDC.put("player", "player=" + playerContext.getId());
        
        playerContext.setCurrMsgId(message.getMessageId());
        PlayerCmdInvoker invoker = this.getInvoker(cmd);
        if (invoker == null) {
            logger.error("没有找到协议号[{}]对应的处理函数", cmd);
            return;
        }
        long startTime = DateTimeUtil.currMillis();
        Message requestObject = null;
        try {
            requestObject = invoker.getMessage().getParserForType().parseFrom(content);
            if (logger.isDebugEnabled()) {
            	String messagePrint = TextFormat.printer().shortDebugString(requestObject);
            	logger.debug("收到消息，ID【{}】，协议号【{}】，消息内容【{}】", messageId, cmd, messagePrint);
            }
            if (invoker.isNeedLogin() && !playerContext.isLogin()) {
            	logger.error("玩家在没有完成登录操作下调用协议【{}】函数{}.{}", cmd, invoker.getBean().getClass().getSimpleName(), invoker.getMethod().getName());
                return;
            }
            Object response = invoker.getMethod().invoke(invoker.getBean(), playerContext, requestObject);
            if(response != null) {
            	message.onResponse(response);
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
