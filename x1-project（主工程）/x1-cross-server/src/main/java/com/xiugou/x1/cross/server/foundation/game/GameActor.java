/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;

import org.gaming.fakecmd.side.common.InternalCmdRegister;
import org.gaming.fakecmd.side.common.InternalCmdRegister.InternalCmdMessage;
import org.gaming.fakecmd.side.cross.CrossCmdRegister;
import org.gaming.fakecmd.side.cross.PlayerCrossCmdRegister;

import akka.actor.AbstractActor;

/**
 * @author YY
 *
 */
public class GameActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(ProtocolMessage.class, msg -> {
			//协议服务器之间的协议
			CrossCmdRegister.INS.handle(msg);
			
		}).match(InternalCmdMessage.class, msg -> {
			//处理跨服内部消息
			InternalCmdRegister.INS.handle(msg);
			
		}).match(PlayerProtocolMessage.class, msg -> {
			//处理玩家协议
			PlayerCrossCmdRegister.INS.handle(msg);
			
		}).build();
	}
}
