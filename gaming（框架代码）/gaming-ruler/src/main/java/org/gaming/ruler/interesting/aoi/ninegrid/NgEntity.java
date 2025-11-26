/**
 * 
 */
package org.gaming.ruler.interesting.aoi.ninegrid;

import java.util.HashSet;
import java.util.Set;

import org.gaming.ruler.interesting.aoi.AoiEntity;

/**
 * @author YY
 *
 */
public class NgEntity<T> extends AoiEntity<T> {

	protected int x;
	protected int y;
	
	private final NineGrid<T> nineGrid;
	
	protected boolean observerChange = true;
	protected boolean watchingChange = true;
	
	public NgEntity(T object, NineGrid<T> nineGrid) {
		super(object);
		this.nineGrid = nineGrid;
	}

	protected void refreshObserver() {
		observerChange = true;
	}
	
	protected void refreshWatching() {
		watchingChange = true;
	}
	
	@Override
	public Set<T> getObserver() {
		observerChange = false;
		return arrounds();
	}

	@Override
	public Set<T> getWatching() {
		watchingChange = false;
		return arrounds();
	}
	
	private Set<T> arrounds() {
		Set<T> result = new HashSet<>();
		for(int[] arround : ARROUNDS) {
			int gridX = this.x + arround[0];
			int gridY = this.y + arround[1];
			NineGrid.Grid<T> grid = nineGrid.gridAt(gridX, gridY);
			if(grid != null) {
				for(AoiEntity<T> aoi : grid.entities()) {
					result.add(aoi.object);
				}
			}
		}
		return result;
	}

	protected static final int[][] ARROUNDS = new int[][] {
		{-1, 1}, {0, 1}, {1, 1},
		{-1, 0}, {0, 0}, {1, 0},
		{-1,-1}, {0,-1}, {1,-1},};

	@Override
	public boolean isObserverChange() {
		return observerChange;
	}

	@Override
	public boolean isWatchingChange() {
		return watchingChange;
	}
}
