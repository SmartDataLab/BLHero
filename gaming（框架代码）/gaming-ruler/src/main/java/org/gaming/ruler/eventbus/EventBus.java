/**
 * 
 */
package org.gaming.ruler.eventbus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class EventBus {

	private static Logger logger = LoggerFactory.getLogger(EventBus.class);
	
	private static final Map<Class<?>, EventDispatcher> RAW_DISPATCHER = new HashMap<>();
	//在运行期才最终确定的事件处理函数
	private static final ConcurrentMap<Class<?>, EventDispatcher> RUNTIME_DISPATCHER = new ConcurrentHashMap<>();
	//事件触发追踪器
	private static ThreadLocal<EventTraceNode> TRACER = new ThreadLocal<>();
	
	private static ConcurrentMap<Class<?>, EventTraceNode> traceMap = new ConcurrentHashMap<>();
	//是否启用追踪器，启用追踪器将停用异步特性
	private static boolean useTracer = true;
	//是否启用异步特性
	private static boolean useAsyncFeatures = false;
	
	/**
	 * 抛出事件
	 * @param event
	 */
	public static void post(Object event) {
		EventDispatcher dispatcher = getDispatcher(event.getClass());
		if(useTracer) {
			EventTraceNode node = TRACER.get();
			if(node == null) {
				node = new EventTraceNode(event.getClass(), null);
				TRACER.set(node);
			} else {
				EventTraceNode childNode = new EventTraceNode(event.getClass(), node);
				node.getChilds().add(childNode);
				TRACER.set(childNode);
				node = childNode;
			}
			dispatcher.syncExecute(event);
			TRACER.set(node.getParent());
			
			if(TRACER.get() == null) {
				if(!traceMap.containsKey(event.getClass())) {
					traceMap.put(event.getClass(), node);
				}
			}
		} else {
			if(useAsyncFeatures) {
				dispatcher.asyncExecute(event);
			} else {
				dispatcher.syncExecute(event);
			}
		}
	}
	
	private static EventDispatcher getDispatcher(Class<?> eventClazz) {
		EventDispatcher dispatcher = RUNTIME_DISPATCHER.get(eventClazz);
		if(dispatcher == null) {
			dispatcher = new EventDispatcher(eventClazz);
			Class<?> currClass = eventClazz;
			while(currClass != null) {
				//从本类到超类逐级获取事件分发器
				EventDispatcher rawDispatcher = RAW_DISPATCHER.get(currClass);
				if(rawDispatcher != null) {
					dispatcher.mergeHandler(rawDispatcher);
				}
				currClass = currClass.getSuperclass();
			}
			
			//获取本类的所有实现接口所对应的事件分发器
			List<Class<?>> interfaceClass = new ArrayList<>();
			getInterfaceClass(eventClazz, interfaceClass);
			for(Class<?> clazz : interfaceClass) {
				EventDispatcher rawDispatcher = RAW_DISPATCHER.get(clazz);
				if(rawDispatcher != null) {
					dispatcher.mergeHandler(rawDispatcher);
				}
			}
			RUNTIME_DISPATCHER.putIfAbsent(eventClazz, dispatcher);
		}
		return dispatcher;
	}
	
	private static void getInterfaceClass(Class<?> clazz, List<Class<?>> interfaces) {
		for(Class<?> interfaceClass : clazz.getInterfaces()) {
			interfaces.add(interfaceClass);
			getInterfaceClass(interfaceClass, interfaces);
		}
	}
	
	/**
	 * 注册事件观察者
	 * @param observer
	 */
	public static void register(Object observer) {
		Class<?> currClass = observer.getClass();
		while(currClass != null) {
			for(Method method : currClass.getDeclaredMethods()) {
				Subscribe subscribe = method.getAnnotation(Subscribe.class);
				if(subscribe == null) {
					continue;
				}
				if(method.getParameterCount() != 1) {
					throw new RuntimeException("使用@Subscribe注解的函数【" + currClass.getSimpleName() + "." + method.getName() + "】有且只能有一个参数");
				}
				if (!Modifier.isPrivate(method.getModifiers())) {
					logger.warn("建议将{}.{}的访问权限设定为private", currClass.getSimpleName(), method.getName());
				}
				method.setAccessible(true);
				
				Class<?> eventClazz = method.getParameterTypes()[0];
				
				EventDispatcher dispatcher = RAW_DISPATCHER.get(eventClazz);
				if(dispatcher == null) {
					dispatcher = new EventDispatcher(eventClazz);
					RAW_DISPATCHER.put(eventClazz, dispatcher);
				}
				dispatcher.addHandler(observer, method, subscribe);
			}
			
			currClass = currClass.getSuperclass();
		}
	}


	public static void useTracer(boolean useTracer) {
		EventBus.useTracer = useTracer;
	}

	public static void useAsyncFeatures(boolean useAsyncFeatures) {
		EventBus.useAsyncFeatures = useAsyncFeatures;
	}
	
	/**
	 * 打印跟踪信息
	 */
	public static void printTrace() {
		for(EventTraceNode trace : traceMap.values()) {
			System.out.println("========================");
			trace.print();
		}
		System.out.println("========================");
	}
}
