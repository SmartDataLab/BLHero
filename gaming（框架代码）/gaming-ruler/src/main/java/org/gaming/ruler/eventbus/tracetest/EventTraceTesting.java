/**
 * 
 */
package org.gaming.ruler.eventbus.tracetest;

import org.gaming.ruler.eventbus.EventBus;

/**
 * @author YY
 *
 */
public class EventTraceTesting {

	public static void main(String[] args) {
		EventBus.register(new Observer1());
		EventBus.register(new Observer2());
		EventBus.register(new Observer3());
		
		EventBus.useTracer(true);
		
		EventBus.post(new AEvent());
		
		EventBus.post(new EEvent());
		
		EventBus.printTrace();
	}
}
