/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.List;

/**
 * @author YY
 *
 */
public class BuildTree {

	public static void main(String[] args) {
//		String sql = "";
//		for (int i = 0; i < ; i++) {
//			
//		}
		
		
		BuildTree solution = new BuildTree();
		
//		int[] preorder = new int[] {3,9,20,15,7};
//		int[] inorder = new int[] {9,3,15,20,7};
//		TreeNode node = solution.buildTree(preorder, inorder);
//		System.out.println(node);
//		List<Integer> list = new ArrayList<>();
//		solution.leftLoop(node, list);
//		System.out.println(list);
		
		
//		[5,1,4, null,null,3,6]
//		[5,4,6,null,null,3,7]
		
//		TreeNode node5 = new TreeNode(7);
//		TreeNode node4 = new TreeNode(3);
//		
//		TreeNode node3 = new TreeNode(6, node4, node5);
//		TreeNode node2 = new TreeNode(4);
//		
//		TreeNode node1 = new TreeNode(5, node2, node3);
//		
//		System.out.println(solution.isValidBST(node1));
		
		int result = solution.findTargetSumWays(new int[] {1,1,1,1,1}, 3);
		System.out.println(result);
	}
	
	public int findTargetSumWays(int[] nums, int target) {
		int loopNum = (int)Math.pow(2, nums.length);
		int result = 0;
		for(int i = 0; i < loopNum; i++) {
			int sum = 0;
			int temp = i;
			for(int j = 0; j < nums.length; j++) {
				if(temp % 2 == 1) {
					sum += nums[j] * -1;
				} else {
					sum += nums[j] * 1;
				}
				temp = temp / 2;
			}
			if(sum == target) {
				result++;
			}
		}
		return result;
    }
	
	
	
	public boolean isValidBST(TreeNode root) {
		return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
	
	public boolean isValidBST(TreeNode node, long lower, long upper) {
		if(node == null) {
			return true;
		}
		if(node.val <= lower || upper <= node.val) {
			return false;
		}
		return isValidBST(node.left, lower, node.val) && isValidBST(node.right, node.val, upper);
	}
	
	
	public boolean hasPathSum(TreeNode root, int targetSum) {
		if(root == null) {
			return false;
		}
		return sumPath(root, targetSum, 0);
    }
	
	public boolean sumPath(TreeNode node, int targetSum, int currSum) {
		currSum += node.val;
		if(node.left == null && node.right == null) {
			if(currSum == targetSum) {
				return true;
			} else {
				return false;
			}
		}
		if(node.left != null) {
			if(sumPath(node.left, targetSum, currSum)) {
				return true;
			}
		}
		if(node.right != null) {
			if(sumPath(node.right, targetSum, currSum)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public TreeNode buildTree(int[] preorder, int[] inorder) {
		if(preorder.length == 0 || inorder == null) {
			return null;
		}
		TreeNode root = new TreeNode(preorder[0]);
		
		int rootValIndex = -1;
		for(int i = 0; i < inorder.length; i++) {
			int nodeVal = inorder[i];
			if(nodeVal == root.val) {
				rootValIndex = i;
				break;
			}
		}
		
		int[] leftInorder = new int[rootValIndex];
		System.arraycopy(inorder, 0, leftInorder, 0, leftInorder.length);
		int[] rightInorder = new int[inorder.length - rootValIndex - 1];
		System.arraycopy(inorder, rootValIndex + 1, rightInorder, 0, rightInorder.length);
		
		int[] rightPreorder = new int[rightInorder.length];
		System.arraycopy(preorder, preorder.length - rightInorder.length, rightPreorder, 0, rightPreorder.length);
		int[] leftPreorder = new int[leftInorder.length];
		System.arraycopy(preorder, 1, leftPreorder, 0, leftPreorder.length);
		
		root.left = buildTree(leftPreorder, leftInorder);
		root.right = buildTree(rightPreorder, rightInorder);
		return root;
    }
	
	static class TreeNode {
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
}

