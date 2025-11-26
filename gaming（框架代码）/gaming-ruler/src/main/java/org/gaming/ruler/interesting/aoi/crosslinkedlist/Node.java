/**
 * 
 */
package org.gaming.ruler.interesting.aoi.crosslinkedlist;

/**
 * @author YY
 *
 */
public abstract class Node {
	
	//前置节点
	protected Node prev;
	//后置节点
	protected Node next;
	//离0点的距离
	protected int distance;

	@Override
	public String toString() {
		if(prev == null) {
			return "head";
		} else if(next == null) {
			return "tail";
		} else {
			return distance + "";
		}
	}
}
