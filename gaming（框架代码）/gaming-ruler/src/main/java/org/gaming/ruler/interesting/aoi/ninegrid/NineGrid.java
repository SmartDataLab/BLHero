/**
 * 
 */
package org.gaming.ruler.interesting.aoi.ninegrid;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.interesting.aoi.AoiEntity;
import org.gaming.ruler.interesting.aoi.IAreaOfInterest;

/**
 * @author YY
 * 九宫格
 */
public class NineGrid<T> implements IAreaOfInterest<T> {
	//九宫格所表示的地图长宽
	public final int width;
	public final int height;
	//每个格子的长宽
	public final int gridWidth;
	public final int gridHeight;
	
	public final int xGridNum;
	public final int yGridNum;
	
	private Grid<T>[][] grids;
	
	private ConcurrentMap<T, NgEntity<T>> entityMap = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	public NineGrid(int width, int height, int gridWidth, int gridHeight) {
		if(width <= 0 || height <= 0 || gridWidth <= 0 || gridHeight <= 0) {
			throw new RuntimeException("");
		}
		this.width = width;
		this.height = height;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		
		int xGridNum = width / gridWidth;
		if(width % gridWidth != 0) {
			xGridNum += 1;
		}
		this.xGridNum = xGridNum;
		
		int yGridNum = height / gridHeight;
		if(height % gridHeight != 0) {
			yGridNum += 1;
		}
		this.yGridNum = yGridNum;
		
		this.grids = new Grid[yGridNum][];
		for(int y = 0; y < grids.length; y++) {
			grids[y] = new Grid[xGridNum];
			for(int x = 0; x < grids[y].length; x++) {
				grids[y][x] = new Grid<>();
			}
		}
	}
	
	protected static class Grid<E> {
		ConcurrentMap<NgEntity<E>, Boolean> entities = new ConcurrentHashMap<>();
		protected void add(NgEntity<E> e) {
			entities.put(e, true);
		}
		protected void remove(NgEntity<E> e) {
			entities.remove(e);
		}
		protected Set<NgEntity<E>> entities() {
			return entities.keySet();
		}
		protected void refresh() {
			for(NgEntity<E> e : entities.keySet()) {
				e.refreshObserver();
				e.refreshWatching();
			}
		}
	}
	
	
	protected Grid<T> gridAt(int gridX, int gridY) {
		if(gridX < 0 || gridY < 0 || gridX >= xGridNum || gridY >= yGridNum) {
			return null;
		}
		return grids[gridY][gridX];
	}

	@Override
	public AoiEntity<T> enter(T t, int x, int y, int widthSize, int heightSize) {
		int gridX = x / gridWidth;
		if(x % gridWidth != 0) {
			gridX += 1;
		}
		int gridY = y / gridHeight;
		if(y % gridHeight != 0) {
			gridY += 1;
		}
		NgEntity<T> entity = new NgEntity<>(t, this);
		entity.x = gridX;
		entity.y = gridY;
		entityMap.put(t, entity);
		grids[gridY][gridX].add(entity);
		
		for(int[] arround : NgEntity.ARROUNDS) {
			int arroundGridX = gridX + arround[0];
			int arroundGridY = gridY + arround[1];
			Grid<T> grid = gridAt(arroundGridX, arroundGridY);
			if(grid != null) {
				grid.refresh();
			}
		}
		return entity;
	}

	@Override
	public void moveTo(T t, int x, int y) {
		int gridX = x / gridWidth;
		if(x % gridWidth != 0) {
			gridX += 1;
		}
		int gridY = y / gridHeight;
		if(y % gridHeight != 0) {
			gridY += 1;
		}
		NgEntity<T> entity = entityMap.get(t);
		if(entity == null) {
			return;
		}
		if(entity.x != gridX || entity.y != gridY) {
			grids[entity.y][entity.x].remove(entity);
			grids[gridY][gridX].add(entity);
			
			//比如从（1，1）->移动到（2，1），那么
			//	（0，0）、（0，1）、（0，2）需要刷新
			//	（3，0）、（3，1）、（3，2）需要刷新
			//中间的6个格子可以不刷新
			
			//比如从（1，1）->移动到（2，2），那么
			//	（0，0）、（0，1）、（0，2）、（1，0）、（2，0）需要刷新
			//	（3，1）、（3，2）、（3，3）、（1，3）、（2，3）需要刷新
			//中间的4个格子可以不刷新
			for(int[] arround : NgEntity.ARROUNDS) {
				int arroundGridX = entity.x + arround[0];
				int arroundGridY = entity.y + arround[1];
				Grid<T> grid = gridAt(arroundGridX, arroundGridY);
				if(grid != null && (Math.abs(arroundGridX - gridX) > 1 || Math.abs(arroundGridY - gridY) > 1)) {
					grid.refresh();
				}
			}
			//
			for(int[] arround : NgEntity.ARROUNDS) {
				int arroundGridX = gridX + arround[0];
				int arroundGridY = gridY + arround[1];
				Grid<T> grid = gridAt(arroundGridX, arroundGridY);
				if(grid != null && (Math.abs(arroundGridX - entity.x) > 1 || Math.abs(arroundGridY - entity.y) > 1)) {
					grid.refresh();
				}
			}
			entity.x = gridX;
			entity.y = gridY;
		}
	}

	@Override
	public void exit(T t) {
		NgEntity<T> entity = entityMap.remove(t);
		if(entity == null) {
			return;
		}
		entityMap.put(t, entity);
		grids[entity.y][entity.x].remove(entity);
	}
}
