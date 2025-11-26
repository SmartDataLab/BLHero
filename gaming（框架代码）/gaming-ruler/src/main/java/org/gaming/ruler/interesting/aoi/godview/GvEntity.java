/**
 * 
 */
package org.gaming.ruler.interesting.aoi.godview;

import java.util.Set;

import org.gaming.ruler.interesting.aoi.AoiEntity;

/**
 * @author YY
 *
 */
public class GvEntity<T> extends AoiEntity<T> {

	private final GodView<T> godView;
	
	public GvEntity(T object, GodView<T> godView) {
		super(object);
		this.godView = godView;
	}

	@Override
	public Set<T> getObserver() {
		return godView.allEntity();
	}

	@Override
	public Set<T> getWatching() {
		return godView.allEntity();
	}

	@Override
	public boolean isObserverChange() {
		return false;
	}

	@Override
	public boolean isWatchingChange() {
		return false;
	}
}
