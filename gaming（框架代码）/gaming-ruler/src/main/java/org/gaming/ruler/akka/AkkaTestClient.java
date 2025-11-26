/**
 * 
 */
package org.gaming.ruler.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * @author YY
 *
 */
public class AkkaTestClient {

	private static ActorRef actorRef;
	
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		builder.append("\nakka.actor.provider=cluster");
		builder.append("\nakka.remote.artery.enabled=on");
		builder.append("\nakka.remote.artery.canonical.hostname=127.0.0.1");
		builder.append("\nakka.remote.artery.canonical.port=8888");
		Config config = ConfigFactory.parseString(builder.toString());
		ActorSystem system = ActorSystem.create("AkkaTestClient", config);
		actorRef = system.actorOf(Props.create(ClientActor.class), "ClientActor");
		actorRef.tell("hello", ActorRef.noSender());
	}
	
	public static class ClientActor extends UntypedAbstractActor {

		@Override
		public void onReceive(Object message) throws Throwable {
			ActorSelection selection = this.context().actorSelection("akka://AkkaTestServer@127.0.0.1:9999/user/ServerActor");
			System.out.println(selection);
			System.out.println(message);
			selection.tell("client", ActorRef.noSender());
		}
	}
}
