/**
 * 
 */
package org.gaming.ruler.eventbus.example;

import org.gaming.ruler.eventbus.Subscribe;

/**
 * @author YY
 *
 */
public class EventObserver {

	@Subscribe
	public void listen(EventExample1 event) {
		System.out.println("EntityObserver");
	}
}
