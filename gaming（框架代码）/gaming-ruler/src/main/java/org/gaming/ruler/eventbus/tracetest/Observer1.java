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
public class Observer1 {

	@Subscribe
	public void listen(AEvent event) {
		EventBus.post(new BEvent());
	}
	
	@Subscribe
	public void listen(BEvent event) {
		EventBus.post(new EEvent());
	}

	@Subscribe
	public void listen(CEvent event) {
	
	}

	@Subscribe
	public void listen(DEvent event) {
		
	}
	
	@Subscribe
	public void listen(EEvent event) {
		System.out.println("EEvent in Observer1");
		EventBus.post(new GEvent());
	}
	
	@Subscribe
	public void listen(FEvent event) {
		System.out.println("FEvent in Observer1");
	}
	
	@Subscribe
	public void listen(GEvent event) {
		System.out.println("GEvent in Observer1");
	}
	
	@Subscribe
	public void listen(HEvent event) {
		System.out.println("HEvent in Observer1");
	}
}
