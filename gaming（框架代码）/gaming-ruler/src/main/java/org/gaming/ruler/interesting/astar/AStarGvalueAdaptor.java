/**
 * 
 */
package org.gaming.ruler.interesting.astar;

/**
 * @author YY
 *
 */
public interface AStarGvalueAdaptor<E> {
	
	public <Node extends BaseAStarNode<E, Node>> int adaptorG(Node nextNode);
}
