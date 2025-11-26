/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author YY
 *
 */
public class FreqStack {
	
	private Map<Integer, Integer> freq = new HashMap<>();
	private Map<Integer, Deque<Integer>> group = new HashMap<>();
	private int maxFreq;
	
	public FreqStack() {

    }
    
    public void push(int val) {
    	freq.put(val, freq.getOrDefault(val, 0) + 1);
    	int freqNum = freq.get(val);
    	Deque<Integer> deque = group.get(freqNum);
    	if(deque == null) {
    		deque = new LinkedList<>();
    		group.put(freqNum, deque);
    	}
    	deque.add(val);
    	maxFreq = Math.max(maxFreq, freqNum);
    }
    
    public int pop() {
    	Deque<Integer> deque = group.get(maxFreq);
    	int val = deque.pollLast();
    	freq.put(val, freq.get(val) - 1);
    	if(deque.isEmpty()) {
    		maxFreq -= 1;
    	}
    	return val;
    }
    
    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        
//        String input = null;
//        while (in.hasNextLine()) {
//        	input = in.nextLine();
//        	break;
//        }
    	
    	String input = "OxAA";
        String ss = input.substring(2);
        int result = 0;
        for(int i = 0; i < ss.length(); i++) {
        	char c = ss.charAt(i);
        	int p = 0;
        	if(c == '0') {
        		p = 0;
        	} else if(c == '1') {
        		p = 1;
        	} else if(c == '2') {
        		p = 2;
        	} else if(c == '3') {
        		p = 3;
        	} else if(c == '4') {
        		p = 4;
        	} else if(c == '5') {
        		p = 5;
        	} else if(c == '6') {
        		p = 6;
        	} else if(c == '7') {
        		p = 7;
        	} else if(c == '8') {
        		p = 8;
        	} else if(c == '9') {
        		p = 9;
        	} else if(c == 'A') {
        		p = 10;
        	} else if(c == 'B') {
        		p = 11;
        	} else if(c == 'C') {
        		p = 12;
        	} else if(c == 'D') {
        		p = 13;
        	} else if(c == 'E') {
        		p = 14;
        	} else if(c == 'F') {
        		p = 15;
        	}
        	int rate = (int)Math.pow(16, ss.length() - i - 1);
        	result += p * rate;
        }
        System.out.println(result);
    }
    
    public ListNode rotateRight(ListNode head, int k) {
    	if(head == null) {
    		return null;
    	}
    	int count = 0;
    	ListNode curr = head;
    	while(curr != null) {
    		count ++;
    		curr = curr.next;
    	}
    	k = k % count;
    	
    	ListNode result = head;
    	for(int i = 0; i < k; i++) {
    		result = rotateRightOnce(result);
    	}
    	return result;
    }
    public ListNode rotateRightOnce(ListNode head) {
    	ListNode oldHead = head;
    	ListNode last = head;
    	ListNode prev = head;
    	while(last != null) {
    		if(last.next == null) {
    			break;
    		}
    		prev = last;
    		last = last.next;
    	}
    	last.next = oldHead;
    	if(prev != null) {
    		prev.next = null;
    	}
    	return last;
    }
    
    public void print(ListNode head) {
    	ListNode curr = head;
    	while(curr != null) {
    		System.out.print(curr.val + " ");
    		curr = curr.next;
    	}
    }
}

class ListNode {
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
