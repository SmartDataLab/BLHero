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
public class Observer2 {

	@Subscribe
	public void listen(AEvent event) {
		EventBus.post(new CEvent());
	}
	
	@Subscribe
	public void listen(BEvent event) {
		EventBus.post(new FEvent());
	}
	
	@Subscribe
	public void listen(EEvent event) {
		System.out.println("EEvent in Observer2");
		EventBus.post(new GEvent());
	}
	
	@Subscribe
	public void listen(FEvent event) {
		System.out.println("FEvent in Observer2");
		EventBus.post(new GEvent());
	}
}
