/**
 * 
 */
package org.gaming.ruler.interesting.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 * 正六边形A星算法
 */
public class HexagonAStar<T> extends AStar<T, HexagonNode<T>> {

	public HexagonAStar(T[][] nodeMatrix) {
		super(nodeMatrix);
	}

	@Override
	protected int costG(HexagonNode<T> aroundNode, HexagonNode<T> currentNode) {
		int costG = 0;
		if(aroundNode.isSameAs(currentNode)) {
			costG = 0;
		} else {
			costG = STRAIGHT;
		}
		if (gvalueAdaptor != null) {
			costG += gvalueAdaptor.adaptorG(aroundNode);
		}
		return costG;
	}

	@Override
	protected int euclid(HexagonNode<T> node1, HexagonNode<T> node2) {
		if(node1.isSameAs(node2)) {
			return 0;
		} else {
			return (int)(node1.distanceWith(node2) * HBASE);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HexagonNode<T>[][] createNodeMatrix(int width, int height) {
		return new HexagonNode[height][width];
	}

	@Override
	protected HexagonNode<T> createNode(int nodeId, int x, int y, T entity) {
		HexagonNode<T> node = null;
		if(y % 2 == 0) {
			if(x % 2 == 0) {
				node = new HexagonNode<>(nodeId, x, y, x, y, x + 1, y);
			} else {
				node = new HexagonNode<>(nodeId, x, y, x - 1, y, x, y);
			}
		} else {
			if(x % 2 == 0) {
				node = new HexagonNode<>(nodeId, x, y, x - 1, y, x, y);
			} else {
				node = new HexagonNode<>(nodeId, x, y, x, y, x + 1, y);
			}
		}
		node.setEntity(entity);
		return node;
	}

	@Override
	protected void initArroundNodes(HexagonNode<T> node) {
		int startX = Math.max(0, node.getX() - 1);
		int endX = Math.min(nodeMatrix[0].length - 1, node.getX() + 1);
		
		int startY = Math.max(0, node.getY() - 1);
		int endY = Math.min(nodeMatrix.length - 1, node.getY() + 1);
		
		List<HexagonNode<T>> aroundNodes = new ArrayList<>();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				HexagonNode<T> around = nodeMatrix[y][x];
				if(around == null) {
					continue;
				}
				if(around == node) {
					continue;
				}
				if(x == node.getX() || y == node.getY()) {
					aroundNodes.add(around);
				}
			}
		}
		node.setAroundNodes(aroundNodes);
	}
	
	
	public static void main(String[] args) {
		Integer[][] matrix = new Integer[][] {
											 {10, 11, 12, 13, 14, 15, 16, 17}, 
											 {20, 21, 22, 23, 24, 25, 26, 27},
											 {30, 31, 32, 33, 34, 35, 36, 37}};
		
		HexagonAStar<Integer> astar = new HexagonAStar<>(matrix);
		System.out.println(astar.allNodeList.size());
		
//		for(int y = 2; y >= 0; y--) {
//			List<HexagonNode<Integer>> nodes = astar.yNodexMap.get(y);
//			System.out.println(nodes);
//		}
		
		List<Integer> list = astar.searchPath(0, 0, 5, 2, 0);
		System.out.println(list);
		
		
	}
}


