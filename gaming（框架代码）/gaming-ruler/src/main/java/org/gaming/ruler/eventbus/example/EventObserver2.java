/**
 * 
 */
package org.gaming.ruler.eventbus.example;

import org.gaming.ruler.eventbus.Subscribe;

/**
 * @author YY
 *
 */
public class EventObserver2 {

	@Subscribe
	public void listen(IInterface2 event) {
		System.out.println("EntityObserver2");
	}
}
