/**
 * 
 */
package org.gaming.protobuf3.simulator.plugin;

import org.gaming.protobuf3.simulator.protocol.ProtocolMessage;
import org.gaming.protobuf3.simulator.protocol.parser.ProtocolType;

import com.google.protobuf.AbstractMessage;

/**
 * @author YY
 *
 */
public interface PluginLogic {
	
	AbstractMessage login(String openId, int areaId);
	AbstractMessage create(String openId, int areaId);
	void onConnect();
	void onRead(AbstractMessage object);
	void onDisconnect();
	
	int getProtocolId(String protocolName);
	ProtocolType getProtocolType(String protocolName);
	ProtocolMessage encode(String protocolName, AbstractMessage msg);
	ProtocolMessage decode(byte[] data);
}
