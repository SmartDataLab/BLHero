/**
 * 
 */
package org.gaming.fakecmd.side.game;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.gaming.fakecmd.annotation.CrossCmd;
import org.gaming.fakecmd.cmd.AbstractCmdRegister;
import org.gaming.fakecmd.side.common.ProtobufCmdInvoker;
import org.gaming.fakecmd.side.common.ProtobufHelper;
import org.gaming.tool.DateTimeUtil;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;

/**
 * @author YY
 *
 */
public class CrossCmdRegister extends AbstractCmdRegister<Integer, ProtobufCmdInvoker> {

	public static CrossCmdRegister INS = new CrossCmdRegister();
	
	private CrossCmdRegister() {}
	
	@Override
	protected Class<? extends Annotation> annotationClass() {
		return CrossCmd.class;
	}

	@Override
	protected Class<?>[] methodParamsType() {
		return new Class<?>[] { ICrossContext.class, GeneratedMessageV3.class };
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

	public static interface ICrossCmdMessage {
		ICrossContext getCrossContext();
		int getMessageId();
		int getCmd();
		ByteString getData();
	}
	
	public void handle(ICrossCmdMessage message) {
		int messageId = message.getMessageId();
		//协议号
		int cmd = message.getCmd();
		//协议内容
		ByteString content = message.getData();
		
		ProtobufCmdInvoker invoker = this.getInvoker(cmd);
        if (invoker == null) {
            logger.error("没有找到协议号[{}]对应的处理函数", cmd);
            return;
        }
        long startTime = DateTimeUtil.currMillis();
        Message responseObject = null;
        try {
        	responseObject = invoker.getMessage().getParserForType().parseFrom(content);
            if (logger.isDebugEnabled()) {
            	String messagePrint = TextFormat.printer().shortDebugString(responseObject);
            	logger.debug("收到跨服消息，ID【{}】，协议号【{}】，消息内容【{}】", messageId, cmd, messagePrint);
            }
            invoker.getMethod().invoke(invoker.getBean(), message.getCrossContext(), responseObject);
        } catch (Exception e) {
        	logger.error("处理跨服消息发生异常", e);
        } finally {
            long endTime = DateTimeUtil.currMillis();
            if (endTime - startTime > 30) {
                logger.warn("消息ID【{}】，协议号【{}】处理慢，耗时【{}】毫秒", messageId, cmd, endTime - startTime);
            }
        }
	}
}
