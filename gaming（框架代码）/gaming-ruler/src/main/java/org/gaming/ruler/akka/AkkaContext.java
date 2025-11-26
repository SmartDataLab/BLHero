package org.gaming.ruler.akka;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSystemImpl;
import akka.actor.Props;

final public class AkkaContext {

	private static Logger logger = LoggerFactory.getLogger(AkkaContext.class);

	private static final ActorSystem SYSTEM;

	public static ActorSystem system() {
		return SYSTEM;
	}

	private static StringBuilder builder = new StringBuilder();

	//最少线程数
	public static final int PARALLELISM_MIN = 4;
	//最大线程数
	public static final int PARALLELISM_MAX = 16;
	
	static {
		builder.append("\n	akka {");
		builder.append("\n		actor {");
		builder.append("\n			default-dispatcher {");
		builder.append("\n				type = Dispatcher");
		builder.append("\n				executor = \"fork-join-executor\"");
		builder.append("\n				fork-join-executor {");
		// 最少线程数4
		builder.append("\n					parallelism-min = ").append(PARALLELISM_MIN);
		// 并发因子，影响线程池最大可用线程数，【因子最大线程数】=处理器个数*并发因子
		builder.append("\n					parallelism-factor = 3");
		// 最大线程数16，实际最大线程数=min（parallelism-max，因子最大线程数）
		builder.append("\n					parallelism-max = ").append(PARALLELISM_MAX);
		builder.append("\n				}");
		builder.append("\n			}");
		builder.append("\n		}");
		builder.append("\n	}");

		Config config = ConfigFactory.parseString(builder.toString());
		SYSTEM = ActorSystem.create("SYSTEM", config);
	}

	/**
	 * 打印jvm中所有的actor
	 *
	 * 用于检查actor泄露
	 */
	public static void printActors() {
		try {
			Method m = ActorSystemImpl.class.getDeclaredMethod("printTree");
			logger.info((String) m.invoke(SYSTEM));
		} catch (Exception e) {
			logger.error("!!!", e);
		}
	}

	/**
	 * 创建一个ActorRef
	 * @param clazz 对哪个类的actor
	 * @return
	 */
	public static ActorRef createActor(Class<?> clazz) {
		return SYSTEM.actorOf(Props.create(clazz));
	}
	
	public static ActorRef createActor(Class<?> clazz, Object... args) {
		return SYSTEM.actorOf(Props.create(clazz, args));
	}
	
	/**
	 * 创建一个ActorRef
	 * @param clazz 对哪个类的actor
	 * @param uniqueName actor的唯一名字
	 * @return
	 */
	public static ActorRef createActor(Class<?> clazz, String uniqueName) {
		return SYSTEM.actorOf(Props.create(clazz), uniqueName);
	}
}
