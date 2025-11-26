/**
 * 
 */
package org.gaming.ruler.interesting.aoi.crosslinkedlist;

/**
 * @author YY
 * 哨兵节点
 */
public class SentryNode<T> extends Node {

	protected final EntityNode<T> entity;
	
	protected SentryNode(EntityNode<T> entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return distance + "(s(" + entity.host.object + "))";
	}
}
