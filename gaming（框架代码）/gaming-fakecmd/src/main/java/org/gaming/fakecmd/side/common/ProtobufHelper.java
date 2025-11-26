/**
 * 
 */
package org.gaming.fakecmd.side.common;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;

/**
 * @author YY
 *
 */
public class ProtobufHelper {

	private static ConcurrentMap<Class<?>, Integer> classToCmdMap = new ConcurrentHashMap<>();
	
	private static ConcurrentMap<Class<?>, Parser<? extends Message>> classToParseMap = new ConcurrentHashMap<>();
	
	public static int getProtobufId(Class<?> protobuffClazz) {
		int cmd = classToCmdMap.getOrDefault(protobuffClazz, 0);
		if(cmd != 0) {
			return cmd;
		}
		Class<?> protoClazz = null;
		for (Class<?> c : protobuffClazz.getDeclaredClasses()) {
			if (!"Proto".equals(c.getSimpleName())) {
				continue;
			}
			protoClazz = c;
			break;
		}
		if (protoClazz == null) {
			throw new RuntimeException("协议类" + protobuffClazz.getSimpleName() + "未包含ProtoID的枚举定义");
		}
		try {
			Object[] obj = (Object[]) protoClazz.getDeclaredMethod("values").invoke(null);
			Method getNumber = protoClazz.getDeclaredMethod("getNumber");
			int id = (int) getNumber.invoke(obj[1]);
			classToCmdMap.put(protobuffClazz, id);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends GeneratedMessageV3> T parse(Class<T> protobufClazz, ByteString data) {
		Parser<? extends Message> parser = classToParseMap.get(protobufClazz);
		if(parser == null) {
			try {
				Method insMethod = protobufClazz.getDeclaredMethod("getDefaultInstance");
				Message message = (Message)insMethod.invoke(null);
				parser = message.getParserForType();
				classToParseMap.put(protobufClazz, parser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			return (T) parser.parseFrom(data);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
