/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author YY
 *
 */
public class ShortestPathBinaryMatrix {

	public static void main(String[] args) {
//		[1,  2,2,  null,3, null,3]
//		[1,  2,2,   2,null,  2]
//		[5, | 4,1, | null,1,  null,4, | 2,null,  2,null]
//		[1,2,2,3,null,-1,3]
		
		TreeNode node6 = new TreeNode(2);
		TreeNode node7 = new TreeNode(2);
		
		TreeNode node4 = new TreeNode(1, node6, null);
		TreeNode node5 = new TreeNode(4, node7, null);
		
		TreeNode node2 = new TreeNode(4, null, node4);
		TreeNode node3 = new TreeNode(1, null, node5);
		
		TreeNode node1 = new TreeNode(1, node2, node3);
		
		ShortestPathBinaryMatrix solution = new ShortestPathBinaryMatrix();
		boolean same = solution.isSymmetric(node1);
		System.out.println(same);
	}
	
	public int openLock(String[] deadends, String target) {
		
		String curr = "0000";
		
		return 0;
    }
	
	
	
	
	public boolean isTreeSame(TreeNode left, TreeNode right) {
		if(left == null && right == null) {
			return true;
		}
		if(left == null || right == null) {
			return false;
		}
		return left.val == right.val && isTreeSame(left.left, right.right) && isTreeSame(left.right, right.left);
	}
	
	public boolean isSymmetric(TreeNode root) {
		return isTreeSame(root.left, root.right);
    }
	
	public void leftLoop(TreeNode node, List<Integer> result) {
		if(node == null) {
			return;
		}
		if(node.left == null && node.right == null) {
			result.add(node.val);
			return;
		}
		if(node.left != null) {
			leftLoop(node.left, result);
		} else {
			result.add(-999);
		}
		result.add(node.val);
		if(node.right != null) {
			leftLoop(node.right, result);
		} else {
			result.add(-999);
		}
	}
	
	public void rightLoop(TreeNode node, List<Integer> result) {
		if(node == null) {
			return;
		}
		if(node.left == null && node.right == null) {
			result.add(node.val);
			return;
		}
		if(node.right != null) {
			rightLoop(node.right, result);
		} else {
			result.add(-999);
		}
		result.add(node.val);
		if(node.left != null) {
			rightLoop(node.left, result);
		} else {
			result.add(-999);
		}
	}
	
	
	
	public List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> result = new ArrayList<>();
		addNode(result, root, 0);
		return result;
    }
	
	public void addNode(List<List<Integer>> result, TreeNode node, int layer) {
		if(node == null) {
			return;
		}
		List<Integer> layerList = null;
		if(result.size() <= layer) {
			layerList = new ArrayList<>();
			result.add(layerList);
		} else {
			layerList = result.get(layer);
		}
		layerList.add(node.val);
		addNode(result, node.left, layer + 1);
		addNode(result, node.right, layer + 1);
	}
	
	
	
	public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
		int[] result = new int[n];
		
		List<Integer>[][] colorArray = colorArray(n, redEdges, blueEdges);
		
		LinkedList<Integer> deque = new LinkedList<>();
		Set<String> searched = new HashSet<>();
		
		for(int target = 0; target < n; target++) {
			if(target == 0) {
				result[target] = 0;
				continue;
			}
			//TODO 这里怎么优化？
			int startWithRed = pointToPoint(colorArray, deque, searched, target, 1);
			int startWithBlue = pointToPoint(colorArray, deque, searched, target, 2);
			if(startWithRed == -1 && startWithBlue == -1) {
				result[target] = -1;
			} else if(startWithRed == -1 && startWithBlue != -1) {
				result[target] = startWithBlue;
			} else if(startWithRed != -1 && startWithBlue == -1) {
				result[target] = startWithRed;
			} else {
				result[target] = Math.min(startWithRed, startWithBlue);
			}
		}
		return result;
    }
	
	public static int pointToPoint(List<Integer>[][] colorArray,
			LinkedList<Integer> deque, Set<String> usePaths, int target, int startColor) {
		deque.clear();
		deque.add(0);
		//已经搜过的路径
		usePaths.clear();
		
		int useColor = startColor;	//当前使用的颜色0随便，1红色，2蓝色
		int path = 0;
		while(!deque.isEmpty()) {
			
			int layerNum = deque.size();
			while(layerNum > 0) {
				int curr = deque.pop();
				if(useColor == 1) {
					List<Integer> redPaths = colorArray[1][curr];
					for(int nextPoint : redPaths) {
						if(nextPoint == target) {
							return ++path;
						} else {
							String pathKey = curr + "_" + nextPoint + "_" + useColor;
							if(!usePaths.contains(pathKey)) {
								deque.add(nextPoint);
								usePaths.add(pathKey);
							}
						}
					}
				} else {
					List<Integer> bluePaths = colorArray[2][curr];
					for(int nextPoint : bluePaths) {
						if(nextPoint == target) {
							return ++path;
						} else {
							String pathKey = curr + "_" + nextPoint + "_" + useColor;
							if(!usePaths.contains(pathKey)) {
								deque.add(nextPoint);
								usePaths.add(pathKey);
							}
						}
					}
				}
				layerNum--;
			}
			useColor = (useColor == 1 ? 2 : 1);
			path++;
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Integer>[][] colorArray(int n, int[][] redEdges, int[][] blueEdges) {
		List<Integer>[][] colorArray = new List[3][];
		//2是颜色
		for(int i = 1; i <= 2; i++) {
			colorArray[i] = new List[n];
			//j是点
			for(int j = 0; j < n; j++) {
				colorArray[i][j] = new ArrayList<>();
			}
		}
		
		for(int[] vector : redEdges) {
			int startPoint = vector[0];
			//1红色
			colorArray[1][startPoint].add(vector[1]);
		}
		
		for(int[] vector : blueEdges) {
			int startPoint = vector[0];
			//2蓝色
			colorArray[2][startPoint].add(vector[1]);
		}
		return colorArray;
	}
	
	
	
	public int shortestPathBinaryMatrix(int[][] grid) {
		int height = grid.length;
		int width = grid[0].length;
		
		if(grid[0][0] == 1 || grid[height - 1][width - 1] == 1) {
			return - 1;
		}
		if(height == 1 && width == 1) {
			if(grid[0][0] == 0) {
				return 1;
			} else {
				return - 1;
			}
		}
		
		
		int[][] surround = new int[][] {
			{-1,-1}, {0,-1}, {1,-1}, {-1,0}, {1,0}, {-1,1}, {0,1}, {1,1}
		};
		LinkedList<int[]> deque = new LinkedList<>();
		deque.add(new int[] {0, 0});
		int sum = 1;
		while(!deque.isEmpty()) {
			int layerNum = deque.size();
			while(layerNum > 0) {
				int[] point = deque.pop();
				
				for(int[] round : surround) {
					int x = point[0] + round[0];
					int y = point[1] + round[1];
					
					if(x < 0 || y < 0 || x >= width || y >= height) {
						continue;
					}
					if(x == width - 1 && y == height - 1) {
						return ++sum;
					}
					
					if(grid[y][x] == 0) {
						deque.add(new int[] {x, y});
						grid[y][x] = 1;
					}
				}
				layerNum--;
			}
			sum ++;
		}
		return -1;
    }
	
	
}


class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode() {
	}

	TreeNode(int val) {
		this.val = val;
	}

	TreeNode(int val, TreeNode left, TreeNode right) {
		this.val = val;
		this.left = left;
		this.right = right;
	}
}
