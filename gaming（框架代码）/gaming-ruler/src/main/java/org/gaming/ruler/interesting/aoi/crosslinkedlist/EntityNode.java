/**
 * 
 */
package org.gaming.ruler.interesting.aoi.crosslinkedlist;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author YY
 * 实体节点
 */
public class EntityNode<T> extends Node {

	protected final CllEntity<T> host;
	//左边的哨兵
	protected final SentryNode<T> leftSentry;
	//右边的哨兵
	protected final SentryNode<T> rightSentry;
	//视野大小的一半
	protected final int halfViewSize;
	
	//观察与被观察
	//当A在观察B时，则A.watching中包含B，并且B.observer中包含A
	//正在观察哪些人
	protected final ConcurrentMap<CllEntity<T>, EntityNode<T>> watching = new ConcurrentHashMap<>();
	//正在被哪些人观察
	protected final ConcurrentMap<CllEntity<T>, EntityNode<T>> observer = new ConcurrentHashMap<>();
	
	protected EntityNode(CllEntity<T> host, int viewSize) {
		this.host = host;
		this.leftSentry = new SentryNode<>(this);
		this.leftSentry.next = this;
		this.prev = this.leftSentry;
		
		this.rightSentry = new SentryNode<>(this);
		this.rightSentry.prev = this;
		this.next = this.rightSentry;
		
		this.halfViewSize = viewSize / 2;
	}
	
	protected void setDistance(int distance) {
		this.distance = distance;
		this.leftSentry.distance = this.distance - halfViewSize;
		this.rightSentry.distance = this.distance + halfViewSize;
	}

	@Override
	public String toString() {
		return distance + "(" + host.object.toString() + ")";
	}
	
	protected void interestedIn(EntityNode<T> other) {
		this.watching.put(other.host, other);
		this.host.refreshWatching();
		other.observer.put(this.host, this);
		other.host.refreshObserver();
	}
	
	protected void uninterestedIn(EntityNode<T> other) {
		this.watching.remove(other.host);
		this.host.refreshWatching();
		other.observer.remove(this.host);
		other.host.refreshObserver();
	}
}
