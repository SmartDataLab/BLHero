/**
 * 
 */
package org.gaming.ruler.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.gaming.ruler.eventbus.Subscribe.ListenType;
import org.gaming.ruler.thread.NamesThreadFactory;

/**
 * @author YY
 *
 */
public class EventDispatcher {

	private static Executor[] executors;
	static {
		executors = new Executor[2];
		for(int i = 0; i < executors.length; i++) {
			executors[i] = Executors.newSingleThreadExecutor(new NamesThreadFactory("ASYNC-EVENT-BUS-" + i));
		}
	}
	
	//事件类
	private final Class<?> eventClass;
	//同步的处理函数
	private final List<EventHandler> syncHandlers = new ArrayList<>();
	//异步的处理函数
	private final List<EventHandler> asyncHandlers = new ArrayList<>();
	
	protected EventDispatcher(Class<?> eventClass) {
		this.eventClass = eventClass;
	}
	
	protected void addHandler(Object observer, Method method, Subscribe subscribe) {
		EventHandler eventHandler = new EventHandler(observer, method);
		if(subscribe.type() == ListenType.SYNC) {
			//同步事件处理列表
			syncHandlers.add(eventHandler);
		} else if(subscribe.type() == ListenType.ASYNC) {
			//异步事件处理列表
			asyncHandlers.add(eventHandler);
		}
	}
	
	protected void mergeHandler(EventDispatcher other) {
		this.syncHandlers.addAll(other.syncHandlers);
		this.asyncHandlers.addAll(other.asyncHandlers);
	}
	
	protected void asyncExecute(Object event) {
		executeIn(syncHandlers, event);
		executors[event.hashCode() % executors.length].execute(() -> {
			executeIn(asyncHandlers, event);
		});
	}
	
	protected void syncExecute(Object event) {
		executeIn(syncHandlers, event);
		executeIn(asyncHandlers, event);
	}
	
	private void executeIn(List<EventHandler> handlers, Object event) {
		for(EventHandler eventHandler : handlers) {
			try {
				eventHandler.getMethod().invoke(eventHandler.getInvoker(), event);
			} catch (Exception e) {
				if(e.getCause() != null) {
					e.getCause().printStackTrace();
				} else {
					e.printStackTrace();
				}
			}
		}
	}

	protected Class<?> getEventClass() {
		return eventClass;
	}
	
}
