/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.common.InternalCmdRegister;
import org.gaming.fakecmd.side.common.InternalCmdRegister.InternalCmdMessage;
import org.gaming.fakecmd.side.game.PlayerCmdRegister;
import org.gaming.fakecmd.side.game.PlayerCrossCmdRegister;
import org.gaming.fakecmd.side.game.PlayerToOtherRequestCmdRegister;
import org.gaming.fakecmd.side.game.PlayerToOtherResponseCmdRegister;

import akka.actor.AbstractActor;

/**
 * @author YY
 *
 */
public class PlayerActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(ProtocolMessage.class, msg -> {
			//协议处理
			PlayerCmdRegister.INS.handle(msg);
			
		}).match(InternalCmdMessage.class, msg -> {
			//内部消息处理
			InternalCmdRegister.INS.handle(msg);
			
		}).match(PlayerCrossMessage.class, msg -> {
			//处理跨服消息
			PlayerCrossCmdRegister.INS.handle(msg);
			
		}).match(PlayerToOtherRequestMessage.class, msg -> {
			//处理跳转信息
			PlayerToOtherRequestCmdRegister.INS.handle(msg);
			
		}).match(PlayerToOtherResponseMessage.class, msg -> {
			//处理跳转信息
			PlayerToOtherResponseCmdRegister.INS.handle(msg);
			
		}).build();
	}
}
