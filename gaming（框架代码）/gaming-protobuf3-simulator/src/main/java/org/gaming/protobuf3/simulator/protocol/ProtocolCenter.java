/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol;

import java.util.HashMap;
import java.util.Map;

import org.gaming.protobuf3.simulator.plugin.PluginLogic;

import com.google.protobuf.AbstractMessage;

/**
 * 
 * @author YY
 *
 */
public class ProtocolCenter {
	//按包分组的请求协议集合
	private static Map<String, Map<Integer, Class<? extends AbstractMessage>>> packagedRequestProtocols = new HashMap<>();
	//按包分组的响应协议集合
	private static Map<String, Map<Integer, Class<? extends AbstractMessage>>> packagedResponseProtocols = new HashMap<>();
	
	//根据协议号进行索引的请求协议集合
	private static Map<Integer, Class<? extends AbstractMessage>> idRequestProtocols = new HashMap<>();
	//根据协议号进行索引的响应协议集合
	private static Map<Integer, Class<? extends AbstractMessage>> idResponeProtocols = new HashMap<>();
	
	//根据类名进行索引的请求协议集合
	private static Map<String, Class<? extends AbstractMessage>> nameRequestProtocols = new HashMap<>();
	//根据类名进行索引的响应协议集合
	private static Map<String, Class<? extends AbstractMessage>> nameResponeProtocols = new HashMap<>();
	
	public static PluginLogic pluginLogic;
	/**
	 * 根据ID获取协议类
	 * @param protocolName
	 * @return
	 */
	public static Class<? extends AbstractMessage> getRequestProtocol(int protocolId) {
		return idRequestProtocols.get(protocolId);
	}
	/**
	 * 根据ID获取协议类
	 * @param protocolName
	 * @return
	 */
	public static Class<? extends AbstractMessage> getResponeProtocol(int protocolId) {
		return idResponeProtocols.get(protocolId);
	}
	
	/**
	 * 根据名字获取协议类
	 * @param protocolName
	 * @return
	 */
	public static Class<? extends AbstractMessage> getRequestProtocol(String protocolName) {
		return nameRequestProtocols.get(protocolName);
	}
	/**
	 * 根据名字获取协议类
	 * @param protocolName
	 * @return
	 */
	public static Class<? extends AbstractMessage> getResponeProtocol(String protocolName) {
		return nameResponeProtocols.get(protocolName);
	}
	
	/**
	 * 添加请求消息
	 * @param packageName
	 * @param protocolId
	 * @param protocolClass
	 */
	@SuppressWarnings("unchecked")
	public static void addRequest(String packageName, int protocolId, Class<?> protocolClass) {
		Map<Integer, Class<? extends AbstractMessage>> protocolMap = packagedRequestProtocols.get(packageName);
		if(protocolMap == null) {
			protocolMap = new HashMap<>();
			packagedRequestProtocols.put(packageName, protocolMap);
		}
		protocolMap.put(protocolId, (Class<? extends AbstractMessage>)protocolClass);
		idRequestProtocols.put(protocolId, (Class<? extends AbstractMessage>)protocolClass);
		nameRequestProtocols.put(protocolClass.getSimpleName(), (Class<? extends AbstractMessage>)protocolClass);
	}
	
	/**
	 * 添加响应消息
	 * @param packageName
	 * @param protocolId
	 * @param protocolClass
	 */
	@SuppressWarnings("unchecked")
	public static void addResponse(String packageName, int protocolId, Class<?> protocolClass) {
		Map<Integer, Class<? extends AbstractMessage>> protocolMap = packagedResponseProtocols.get(packageName);
		if(protocolMap == null) {
			protocolMap = new HashMap<>();
			packagedResponseProtocols.put(packageName, protocolMap);
		}
		protocolMap.put(protocolId, (Class<? extends AbstractMessage>)protocolClass);
		idResponeProtocols.put(protocolId, (Class<? extends AbstractMessage>)protocolClass);
		nameResponeProtocols.put(protocolClass.getSimpleName(), (Class<? extends AbstractMessage>)protocolClass);
	}
	
	public static Map<Integer, Class<? extends AbstractMessage>> getIdRequestProtocols() {
		return idRequestProtocols;
	}
	
	public static void setPluginLogic(PluginLogic pluginLogic) {
		ProtocolCenter.pluginLogic = pluginLogic;
	}
	
	public static ProtocolMessage decode(byte[] data) {
		return pluginLogic.decode(data);
	}
}
