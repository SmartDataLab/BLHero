/**
 * 
 */
package org.gaming.ruler.interesting.aoi.godview;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.interesting.aoi.AoiEntity;
import org.gaming.ruler.interesting.aoi.IAreaOfInterest;

/**
 * @author YY
 * 上帝视角的AOI
 */
public class GodView<T> implements IAreaOfInterest<T> {

	private final ConcurrentMap<T, GvEntity<T>> entityMap = new ConcurrentHashMap<>();
	
	@Override
	public AoiEntity<T> enter(T t, int x, int y, int widthSize, int heightSize) {
		GvEntity<T> entity = new GvEntity<>(t, this);
		entityMap.put(t, entity);
		return entity;
	}

	@Override
	public void moveTo(T t, int x, int y) {
		//nothing to do
	}

	@Override
	public void exit(T t) {
		entityMap.remove(t);
	}
	
	public Set<T> allEntity() {
		return entityMap.keySet();
	}
}
