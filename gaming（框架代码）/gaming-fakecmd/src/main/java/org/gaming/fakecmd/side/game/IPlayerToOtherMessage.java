/**
 * 
 */
package org.gaming.fakecmd.side.game;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

/**
 * @author YY
 *
 */
public interface IPlayerToOtherMessage {
	IPlayerContext getPlayerContext();
	IPlayer getPlayer();
	
	int getMessageId();
	int getCmd();
	ByteString getData();
	
	void onResponse(Object response);
	void onException(Exception e, Message requestObject);
	void fastResponse();
}
