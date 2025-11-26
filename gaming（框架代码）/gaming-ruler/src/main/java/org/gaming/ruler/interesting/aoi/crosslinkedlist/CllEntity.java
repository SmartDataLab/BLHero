/**
 * 
 */
package org.gaming.ruler.interesting.aoi.crosslinkedlist;

import java.util.HashSet;
import java.util.Set;

import org.gaming.ruler.interesting.aoi.AoiEntity;

/**
 * @author YY
 *
 */
public class CllEntity<T> extends AoiEntity<T> {

	protected final EntityNode<T> xNode;
	protected final EntityNode<T> yNode;
	
	private Set<T> watching;
	private Set<T> observer;
	
	protected CllEntity(T object, int widthSize, int heightSize) {
		super(object);
		xNode = new EntityNode<>(this, widthSize);
		yNode = new EntityNode<>(this, heightSize);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(object.toString());
		builder.append("\nX watching ");
		for(CllEntity<T> other : xNode.watching.keySet()) {
			builder.append(other.object).append(", ");
		}
		builder.append("\nX observer ");
		for(CllEntity<T> other : xNode.observer.keySet()) {
			builder.append(other.object).append(", ");
		}
		builder.append("\nY watching ");
		for(CllEntity<T> other : yNode.watching.keySet()) {
			builder.append(other.object).append(", ");
		}
		builder.append("\nY observer ");
		for(CllEntity<T> other : yNode.observer.keySet()) {
			builder.append(other.object).append(", ");
		}
		return builder.toString();
	}
	
	protected void refreshObserver() {
		observer = null;
	}
	
	protected void refreshWatching() {
		watching = null;
	}
	
	@Override
	public boolean isObserverChange() {
		return observer == null;
	}

	@Override
	public boolean isWatchingChange() {
		return watching == null;
	}
	
	public Set<T> getWatching() {
		if(watching == null) {
			Set<T> result = new HashSet<>();
			if(xNode.watching.size() > yNode.watching.size()) {
				for(CllEntity<T> t : yNode.watching.keySet()) {
					if(xNode.watching.containsKey(t)) {
						result.add(t.object);
					}
				}
			} else {
				for(CllEntity<T> t : xNode.watching.keySet()) {
					if(yNode.watching.containsKey(t)) {
						result.add(t.object);
					}
				}
			}
			return watching = result;
		} else {
			return watching;
		}
	}
	
	public Set<T> getObserver() {
		if(observer == null) {
			Set<T> result = new HashSet<>();
			if(xNode.observer.size() > yNode.observer.size()) {
				for(CllEntity<T> t : yNode.observer.keySet()) {
					if(xNode.observer.containsKey(t)) {
						result.add(t.object);
					}
				}
			} else {
				for(CllEntity<T> t : xNode.observer.keySet()) {
					if(yNode.observer.containsKey(t)) {
						result.add(t.object);
					}
				}
			}
			return observer = result;
		} else {
			return observer;
		}
	}
}
