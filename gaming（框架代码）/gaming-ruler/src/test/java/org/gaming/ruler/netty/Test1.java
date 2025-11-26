/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author YY
 *
 */
public class Test1 {

	public static void main(String[] args) {
		Test1 test1 = new Test1();
		
//		[1,2,3,4,null,null,5]
//		[1,  | 2,3, | 4,5,   null,6,   | 7,null,   null,null,   null,8]
		
		Node node7 = new Node(7, null, null, null);
		Node node8 = new Node(8, null, null, null);
		
		
		
		Node node4 = new Node(4, node7, null, null);
		Node node5 = new Node(5, null, null, null);
		Node node6 = new Node(6, null, node8, null);
		
		
		Node node2 = new Node(2, node4, node5, null);
		Node node3 = new Node(3, null, node6, null);
		
		Node node1 = new Node(1, node2, node3, null);
		
		test1.connect(node1);
		StringBuilder result = new StringBuilder();
		test1.print(node1, result);
		System.out.println(result);
	}
	
	public int[][] reconstructQueue(int[][] peoples) {
		return null;
    }
	
	public String orderlyQueue(String s, int k) {
		char[] cs = s.toCharArray();
		Arrays.sort(cs);
		
		if(k >= s.length() - 1) {
			return new String(cs);
		}
		
		String oldResult = s;
		int startIndex = 0;
		while(true) {
			if(oldResult.charAt(startIndex) == cs[startIndex]) {
				startIndex ++;
			}
			
			char maxChar = cs[startIndex];
			int maxIndex = -1;
			for(int i = startIndex; i < k; i++) {
				char c = oldResult.charAt(i);
				if(c > maxChar) {
					maxChar = c;
					maxIndex = i;
				}
			}
			if(maxIndex != -1) {
				String newResult = oldResult.substring(0, maxIndex) + oldResult.substring(maxIndex + 1) + maxChar;
				if(oldResult.equals(newResult)) {
					break;
				}
				oldResult = newResult;
			} else {
				break;
			}
		}
		return oldResult;
    }
	
	public boolean validateStackSequences(int[] pushed, int[] popped) {
		Deque<Integer> stack = new LinkedList<>();
		int popIndex = 0;
		for(int i = 0; i < pushed.length; i++) {
			int push = pushed[i];
			stack.add(push);
			int pop = popped[popIndex];
			while(stack.peekLast() != null && stack.peekLast() == pop) {
				popIndex ++;
				stack.pollLast();
				if(popIndex >= popped.length) {
					break;
				}
				pop = popped[popIndex];
			}
		}
		return stack.isEmpty();
    }
	
	
	public Node connect(Node root) {
		if(root == null) {
			return null;
		}
		connectLeafInNode(root);
		if(root.left != null) {
			connect(root.left);
		}
		if(root.right != null) {
			connect(root.right);
		}
        return root;
    }
	
//	[1,2,3,4,null,null,5]
//	[1,2,3,4,5,null,6,7,null,null,null,null,8]
	
//	[1,#,2,3,#,4,5,6,#,7,#]
//	[1,#,2,3,#,4,5,6,#,7,8,#]
	
	public void connectLeafInNode(Node node) {
		if(node.left != null) {
			Node right = node.right;
			Node next = node.next;
			while(right == null) {
				if(next == null) {
					break;
				}
				if(next.left != null) {
					right = next.left;
					break;
				}
				if(next.right != null) {
					right = next.right;
					break;
				}
				next = next.next;
			}
			node.left.next = right;
		}
		if(node.right != null && node.next != null) {
			
			Node left = node.next.left;
			Node next = node.next;
			while(left == null) {
				if(next == null) {
					break;
				}
				if(next.left != null) {
					left = next.left;
					break;
				}
				if(next.right != null) {
					left = next.right;
					break;
				}
				next = next.next;
			}
			node.right.next = left;
		}
	}
	
	public void print(Node node, StringBuilder result) {
		result.append(node.val);
		if(node.next == null) {
			result.append("#");
		} else {
			Node curr = node.next;
			while(curr != null) {
				result.append(curr.val);
				curr = curr.next;
			}
			result.append("#");
		}
		Node left = node.left;
		if(left != null) {
			print(left, result);
		}
	}
}

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
