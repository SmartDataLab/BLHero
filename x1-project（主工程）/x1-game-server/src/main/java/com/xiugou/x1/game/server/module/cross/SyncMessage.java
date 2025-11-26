/**
 * 
 */
package com.xiugou.x1.game.server.module.cross;

import com.google.protobuf.ByteString;

/**
 * @author YY
 *
 */
public class SyncMessage {
	private int protoId;
	private ByteString data;
	public int getProtoId() {
		return protoId;
	}
	public void setProtoId(int protoId) {
		this.protoId = protoId;
	}
	public ByteString getData() {
		return data;
	}
	public void setData(ByteString data) {
		this.data = data;
	}
}
