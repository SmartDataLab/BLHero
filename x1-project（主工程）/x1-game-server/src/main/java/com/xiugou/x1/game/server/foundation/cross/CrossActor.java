/**
 * 
 */
package com.xiugou.x1.game.server.foundation.cross;

import org.gaming.fakecmd.side.game.CrossCmdRegister;

import akka.actor.AbstractActor;

/**
 * @author YY
 *
 */
public class CrossActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(CrossMessage.class, msg -> {
			//协议服务器之间的协议
			CrossCmdRegister.INS.handle(msg);
		}).build();
	}
}
