/**
 * 
 */
package org.gaming.ruler.eventbus.example;

import org.gaming.ruler.eventbus.EventBus;

/**
 * @author YY
 *
 */
public class EventExample1 implements IInterface1 {

	
	public static void main(String[] args) {
		
		EventBus.register(new EventObserver());
		EventBus.register(new EventObserver1());
		EventBus.register(new EventObserver2());
		EventBus.register(new EventObserver3());
		
		
		EventBus.post(new EventExample1());
		System.out.println("========================");
		EventBus.post(new EventExample2());
	}
}
