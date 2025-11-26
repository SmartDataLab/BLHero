/**
 * 
 */
package org.gaming.ruler.eventbus.example;

import org.gaming.ruler.eventbus.Subscribe;

/**
 * @author YY
 *
 */
public class EventObserver3 extends AbstractEventObserver {

	@Subscribe
	public void listen(IInterface3 event) {
		System.out.println("EntityObserver3");
	}
}
