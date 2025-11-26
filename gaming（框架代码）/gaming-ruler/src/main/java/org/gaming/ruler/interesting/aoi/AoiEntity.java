/**
 * 
 */
package org.gaming.ruler.interesting.aoi;

import java.util.Set;

/**
 * @author YY
 *
 */
public abstract class AoiEntity<T> {

	public final T object;
	
	public AoiEntity(T object) {
		this.object = object;
	}
	
	public abstract Set<T> getObserver();
	public abstract Set<T> getWatching();
	public abstract boolean isObserverChange();
	public abstract boolean isWatchingChange();
}
