/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;

import org.gaming.fakecmd.side.common.InternalCmdRegister.InternalCmdMessage;

/**
 * @author YY
 *
 */
public interface GameInternalMessage extends InternalCmdMessage {
	long getServerId();
}
