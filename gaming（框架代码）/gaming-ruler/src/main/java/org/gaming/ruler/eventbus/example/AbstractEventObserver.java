/**
 * 
 */
package org.gaming.ruler.eventbus.example;

import org.gaming.ruler.eventbus.Subscribe;

/**
 * @author YY
 *
 */
public abstract class AbstractEventObserver {

	@Subscribe
	public void listen(IInterface4 event) {
		System.out.println(this.getClass().getSimpleName() + " AbstractEventObserver");
	}
}
