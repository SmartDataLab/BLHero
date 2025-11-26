/**
 * 
 */
package org.gaming.ruler.eventbus.example;

import org.gaming.ruler.eventbus.Subscribe;

/**
 * @author YY
 *
 */
public class EventObserver1 extends AbstractEventObserver {

	@Subscribe
	public void listen(IInterface1 event) {
		System.out.println("EntityObserver1");
	}
}
