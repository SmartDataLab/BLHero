/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author YY
 *
 */
public class TestConfig {

	public static int startPort = 8001;
	public static int endPort = 8005;
	
	
	public static void main(String[] args) {
//		System.out.println(lengthOfLongestSubstring("pwwkew"));
		
//		System.out.println(findSubstring("wordgoodgoodgoodbestword", new String[] {"word","good","best","good"}));
		
//		nums = [], target = 1
		
//		System.out.println(threeSumClosest(new int[] {-1,2,1,-4}, 1));
		
		
		int[] array = new int[] {0,1,2,2,3,0,4,2};
		System.out.println(Arrays.toString(array));
		System.out.println(removeElement(array, 2));
		System.out.println(Arrays.toString(array));
	}
	
	public static int lengthOfLongestSubstring(String s) {
		Set<Character> set = new HashSet<>();
		int max = 0;
		int right = -1;
		for(int i = 0; i < s.length(); i++) {
			if(i != 0) {
				set.remove(s.charAt(i - 1));
			}
			while(right + 1 < s.length() && !set.contains(s.charAt(right + 1))) {
				set.add(s.charAt(right + 1));
				right += 1;
			}
			max = Math.max(max, right - i + 1);
		}
		return max;
	}
	
	 public static int lengthOfLongestSubstring2(String s) {
        // 哈希集合，记录每个字符是否出现过
        Set<Character> occ = new HashSet<Character>();
        int n = s.length();
        // 右指针，初始值为 -1，相当于我们在字符串的左边界的左侧，还没有开始移动
        int rk = -1, ans = 0;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                // 左指针向右移动一格，移除一个字符
                occ.remove(s.charAt(i - 1));
            }
            while (rk + 1 < n && !occ.contains(s.charAt(rk + 1))) {
                // 不断地移动右指针
                occ.add(s.charAt(rk + 1));
                ++rk;
            }
            // 第 i 到 rk 个字符是一个极长的无重复字符子串
            ans = Math.max(ans, rk - i + 1);
        }
        return ans;
    }
	 
	public List<List<String>> groupAnagrams(String[] strs) {
		Map<String, List<String>> map = new HashMap<>();
		for(String str : strs) {
			char[] chars = str.toCharArray();
			Arrays.sort(chars);
			String key = new String(chars);
			List<String> group = map.getOrDefault(key, new ArrayList<>());
			group.add(str);
			map.put(key, group);
		}
		return new ArrayList<>(map.values());
	}
	
	public static List<Integer> findSubstring(String s, String[] words) {
		if(words.length <= 0) {
			return Collections.emptyList();
		}
		List<Integer> result = new ArrayList<>();
		int length = words.length * words[0].length();
		List<String> tempList = new ArrayList<>();
		for(int i = 0; i < s.length() - length + 1; i++) {
			tempList.clear();
			tempList.addAll(Arrays.asList(words));
			String substr = s.substring(i, i + length);
			for(int j = 0; j < words.length; j++) {
				String curr = substr.substring(j * words[0].length(), (j + 1) * words[0].length());
				if(!tempList.remove(curr)) {
					break;
				}
			}
			if(tempList.isEmpty()) {
				result.add(i);
			}
		}
		return result;
    }
	
	public static class ListNode {
		int val;
		ListNode next;

		ListNode() {
		}

		ListNode(int val) {
			this.val = val;
		}

		ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}
	public static ListNode partition(ListNode head, int x) {
		ListNode leftHead = null;
		ListNode left = null;
		ListNode rightHead = null;
		ListNode right = null;
		ListNode curr = head;
		while(curr != null) {
			ListNode node = new ListNode(curr.val);
			if(curr.val < x) {
				if(left == null) {
					leftHead = node;
					left = node;
				} else {
					left.next = node;
					left = node;
				}
			} else {
				if(right == null) {
					rightHead = node;
					right = node;
				} else {
					right.next = node;
					right = node;
				}
			}
			curr = curr.next;
		}
		ListNode result = null;
		if(leftHead != null) {
			result = leftHead;
			left.next = rightHead;
		} else {
			result = rightHead;
		}
		return result;
    }
	
	public static int threeSumClosest(int[] nums, int target) {
		Arrays.sort(nums);
		
		int result = 0;
		int closeOffset = Integer.MAX_VALUE;
		
		for(int i = 0; i < nums.length; i++) {
			int a = nums[i];
			int bIndex = i + 1;
			int cIndex = nums.length - 1;
			int sum = 0;
			while(bIndex != cIndex && bIndex < nums.length && cIndex > 0) {
				sum = a + nums[bIndex] + nums[cIndex];
				if(Math.abs(sum - target) < closeOffset) {
					closeOffset = Math.abs(sum - target);
					result = sum;
				}
				if(sum > target) {
					cIndex--;
				} else if(sum < target) {
					bIndex++;
				} else {
					break;
				}
			}
		}
		return result;
    }
	
	public static int removeElement(int[] nums, int val) {
		int length = nums.length;
		for(int i = 0; i < length;) {
			if(nums[i] == val) {
				length -= 1;
				for(int j = i; j < nums.length - 1; j++) {
					nums[j] = nums[j + 1];
				}
			} else {
				i++;
			}
		}
		return length;
    }
	
	
}
