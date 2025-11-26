/**
 * 
 */
package org.gaming.ruler.interesting.astar;

import java.util.List;


/**
 * @author YY
 *
 */
public class SquareNode<T> extends BaseAStarNode<T, SquareNode<T>> {

	public SquareNode(int id, int x, int y) {
		super(id, x, y);
	}

	@SuppressWarnings("unchecked")
	public void setAroundNodes(List<? extends SquareNode<T>> aroundNodes) {
		this.aroundNodes = aroundNodes.toArray(new SquareNode[aroundNodes.size()]);
	}
}
