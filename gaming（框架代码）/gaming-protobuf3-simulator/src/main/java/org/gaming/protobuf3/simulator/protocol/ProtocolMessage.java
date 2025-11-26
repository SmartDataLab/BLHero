/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol;

/**
 * @author YY
 *
 */
public class ProtocolMessage {
	
	private int protocolId;
	private long serverTime;
	private byte[] content;
	private int messageId;
	
	public int getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}
	public long getServerTime() {
		return serverTime;
	}
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
}
