/**
 * 
 */
package org.gaming.fakecmd.side.game;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @author YY
 *
 */
public interface IPlayerContext {
	long getId();
	int getServerZone();
	int getCurrMsgId();
	void setCurrMsgId(int messageId);
	
	void write(int cmd, GeneratedMessageV3 msg, int messageId);
	void write(int cmd, ByteString msg, int messageId);
	boolean isLogin();
}
