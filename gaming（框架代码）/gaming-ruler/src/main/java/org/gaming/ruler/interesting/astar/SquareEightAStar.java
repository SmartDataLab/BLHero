/**
 * 
 */
package org.gaming.ruler.interesting.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 * 正方形八方向A星
 */
public class SquareEightAStar<T> extends SquareAStar<T> {

	public SquareEightAStar(T[][] entityMatrix) {
		super(entityMatrix);
	}

	@Override
	protected void initArroundNodes(SquareNode<T> node) {
		int startX = Math.max(0, node.getX() - 1);
		int endX = Math.min(nodeMatrix[0].length - 1, node.getX() + 1);
		int startY = Math.max(0, node.getY() - 1);
		int endY = Math.min(nodeMatrix.length - 1, node.getY() + 1);

		List<SquareNode<T>> aroundNodes = new ArrayList<>();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				SquareNode<T> around = nodeMatrix[y][x];
				if (around == node) {
					continue;
				}
				// 八方向
				aroundNodes.add(around);
			}
		}
		node.setAroundNodes(aroundNodes);
	}
}
