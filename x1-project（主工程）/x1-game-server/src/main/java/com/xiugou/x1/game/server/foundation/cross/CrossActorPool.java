/**
 * 
 */
package com.xiugou.x1.game.server.foundation.cross;

import org.gaming.ruler.akka.AkkaContext;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;

/**
 * @author YY
 *
 */
@Component
public class CrossActorPool implements Lifecycle {

	private static ActorRef ACTOR;

	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MIN);
	}

	@Override
	public void start() throws Exception {
		ACTOR = AkkaContext.createActor(CrossActor.class);
	}

	public static ActorRef getActor() {
		return ACTOR;
	}

	@Override
	public void stop() throws Exception {
		if(ACTOR != null) {
			AkkaContext.system().stop(ACTOR);
		}
	}
}
