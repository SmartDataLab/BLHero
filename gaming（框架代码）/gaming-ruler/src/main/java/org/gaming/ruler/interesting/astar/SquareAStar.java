/**
 * 
 */
package org.gaming.ruler.interesting.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 * 正方形四方向A星算法
 */
public class SquareAStar<T> extends AStar<T, SquareNode<T>> {

	public SquareAStar(T[][] entityMatrix) {
		super(entityMatrix);
	}

	protected int costG(SquareNode<T> aroundNode, SquareNode<T> currentNode) {
		int costG = 0;
		if (aroundNode.getX() == currentNode.getX() || aroundNode.getY() == currentNode.getY()) {
			costG = STRAIGHT;
		} else {
			costG = OBLIQUE;
		}
		if (gvalueAdaptor != null) {
			costG += gvalueAdaptor.adaptorG(aroundNode);
		}
		return costG;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SquareNode<T>[][] createNodeMatrix(int width, int height) {
		return new SquareNode[height][width];
	}

	@Override
	protected SquareNode<T> createNode(int nodeId, int x, int y, T entity) {
		SquareNode<T> node = new SquareNode<>(nodeId, x, y);
		node.setEntity(entity);
		return node;
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
				// 四方向
				if (x == node.getX() || y == node.getY()) {
					aroundNodes.add(around);
				}
			}
		}
		node.setAroundNodes(aroundNodes);
	}
}
