/**
 * 
 */
package org.gaming.ruler.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * @author YY
 *
 */
public class AkkaTestServer {

	private static ActorRef actorRef;
	
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		builder.append("\nakka.actor.provider=cluster");
		builder.append("\nakka.remote.artery.enabled=on");
		builder.append("\nakka.remote.artery.canonical.hostname=127.0.0.1");
		builder.append("\nakka.remote.artery.canonical.port=9999");
		Config config = ConfigFactory.parseString(builder.toString());
		
		
		ActorSystem system = ActorSystem.create("AkkaTestServer", config);
		actorRef = system.actorOf(Props.create(ServerActor.class), "ServerActor");
		System.out.println(actorRef);
	}
	
	public static class ServerActor extends UntypedAbstractActor {

		@Override
		public void onReceive(Object message) throws Throwable {
			System.out.println(message);
		}
	}
}
