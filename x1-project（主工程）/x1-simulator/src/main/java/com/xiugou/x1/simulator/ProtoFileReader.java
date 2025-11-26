/**
 * 
 */
package com.xiugou.x1.simulator;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.gaming.protobuf3.simulator.protocol.ProtocolCenter;

import com.google.protobuf.GeneratedMessageV3;

/**
 * @author YY
 *
 */
public class ProtoFileReader {

	public static Map<String, Integer> reqProtocolIdMap = new HashMap<>();
	public static Map<String, Integer> resProtocolIdMap = new HashMap<>();
	
	public static void main(String[] args) {
		readProtoFiles("F:/n1space/idle-server/idle-protobuf3/target/classes/com/ayjs/idle/protobuf");
	}
	
	
	public static void readProtoFiles(String protoClassPath) {
		File classDir = new File(protoClassPath);
		readClassDir(classDir);
		System.out.println("请求协议数: " + reqProtocolIdMap.size());
		System.out.println("响应协议数: " + resProtocolIdMap.size());
	}
	
	public static void readClassDir(File file) {
		File[] classFiles = file.listFiles();
		for(File subFile : classFiles) {
			if(subFile.isDirectory()) {
				readClassDir(subFile);
			} else {
				readClassFile(subFile);
			}
		}
	}
	
	
	private static void readClassFile(File file) {
		if(file.getName().contains("$")) {
			return;
		}
		
		String classPath = file.getPath();
		int packageIndex = classPath.indexOf("classes");
		classPath = classPath.substring(packageIndex + "classes".length() + 1);
		classPath = classPath.replaceAll("\\\\", ".");
		
		try {
			Class<?> protoClazz = Class.forName(classPath.replace(".class", ""));
			if(!GeneratedMessageV3.class.isAssignableFrom(protoClazz)) {
				//TODO 不是协议类
				for(Class<?> subProtoClazz : protoClazz.getDeclaredClasses()) {
					registerProtoClass(subProtoClazz);
				}
			} else {
				registerProtoClass(protoClazz);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void registerProtoClass(Class<?> protoClazz) {
		Class<?> protoIDClazz = null;
		for(Class<?> c : protoClazz.getDeclaredClasses()) {
			if(!"Proto".equals(c.getSimpleName())) {
				continue;
			}
			protoIDClazz = c;
			break;
		}
		if(protoIDClazz == null) {
			//TODO 协议类未定义Proto枚举
			return;
		}
		try {
			
			String[] packageNames = protoClazz.getPackage().getName().split("\\.");
			String packageName = packageNames[packageNames.length - 1];
			
			Object[] obj = (Object[])protoIDClazz.getDeclaredMethod("values").invoke(null);
			Method getNumber = protoIDClazz.getDeclaredMethod("getNumber");
			int id = (int)getNumber.invoke(obj[1]);
			
			if(protoClazz.getSimpleName().contains("Request")) {
				reqProtocolIdMap.put(protoClazz.getSimpleName(), id);
				ProtocolCenter.addRequest(packageName, id, protoClazz);
			} else {
				resProtocolIdMap.put(protoClazz.getSimpleName(), id);
				ProtocolCenter.addResponse(packageName, id, protoClazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
