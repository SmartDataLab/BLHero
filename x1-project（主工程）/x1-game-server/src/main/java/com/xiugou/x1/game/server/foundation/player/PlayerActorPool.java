package com.xiugou.x1.game.server.foundation.player;

import org.gaming.ruler.akka.AkkaContext;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.tool.RandomUtil;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;

@Component
public class PlayerActorPool implements Lifecycle {

	private static ActorRef[] ACTOR_POOL;
	private static int actorNum = 0;

	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MIN);
	}

	@Override
	public void start() throws Exception {
		actorNum = AkkaContext.PARALLELISM_MAX * 2;
		ACTOR_POOL = new ActorRef[actorNum];
		for (int i = 0; i < actorNum; i++) {
			ActorRef actorRef = AkkaContext.createActor(PlayerActor.class);
			ACTOR_POOL[i] = actorRef;
		}
	}

	public static ActorRef getActor(long id) {
		return ACTOR_POOL[(int) (id % ACTOR_POOL.length)];
	}

	@Override
	public void stop() throws Exception {
		if(ACTOR_POOL != null) {
			for (ActorRef actorRef : ACTOR_POOL) {
				AkkaContext.system().stop(actorRef);
			}
		}
	}

	public static void tell(PlayerInternalMessage message) {
		ActorRef actorRef = getActor(message.getPlayerId());
		actorRef.tell(message, actorRef);
	}

	public static ActorRef getOneActor() {
		int index = RandomUtil.within(actorNum);
		return ACTOR_POOL[index];
	}
}
