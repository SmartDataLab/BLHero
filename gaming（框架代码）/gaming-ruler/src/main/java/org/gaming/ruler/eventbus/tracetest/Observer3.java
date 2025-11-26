/**
 * 
 */
package org.gaming.ruler.eventbus.tracetest;

import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.eventbus.Subscribe;

/**
 * @author YY
 *
 */
public class Observer3 {

	@Subscribe
	public void listen(AEvent event) {
		EventBus.post(new DEvent());
	}
	
	@Subscribe
	public void listen(EEvent event) {
		System.out.println("EEvent in Observer3");
		EventBus.post(new HEvent());
	}
}
