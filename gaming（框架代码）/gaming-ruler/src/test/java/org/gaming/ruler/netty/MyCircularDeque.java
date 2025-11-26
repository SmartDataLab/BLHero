/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.Arrays;

/**
 * @author YY
 *
 */
public class MyCircularDeque {

	public static void main(String[] args) {
//		["MyCircularDeque", "insertLast", "insertLast", "insertFront", "insertFront", "getRear", "isFull", "deleteLast", "insertFront", "getFront"]
//				[[3], [1], [2], [3], [4], [], [], [], [4], []]
//				输出
//				[null, true, true, true, false, 2, true, true, true, 4]

//		["MyCircularDeque","insertFront","insertLast","insertLast","getFront","deleteLast","getRear","insertFront","deleteFront","getRear","insertLast","isFull"]
//		[[3],[8],[8],[2],[],[],[],[9],[],[],[2],[]]
		
		
		
		MyCircularDeque deque = new MyCircularDeque(3);
		System.out.println(deque.insertFront(8));
		System.out.println(deque.insertFront(7));
		System.out.println(deque.insertFront(6));
//		System.out.println(deque.deleteFront());
//		System.out.println(deque.insertFront(5));
		System.out.println(deque.getRear());
//		System.out.println(deque.deleteLast());
		System.out.println(deque.insertLast(9));
//		System.out.println(deque.insertLast(5));
		
//		System.out.println(deque.toString());
//		System.out.println(deque.insertLast(8));
//		System.out.println(deque.toString());
//		System.out.println(deque.insertLast(2));
//		System.out.println(deque.toString());
//		System.out.println(deque.getFront());
//		System.out.println(deque.toString());
//		System.out.println(deque.deleteLast());
//		System.out.println(deque.toString());
//		System.out.println(deque.getRear());
//		System.out.println(deque.toString());
//		System.out.println(deque.insertFront(9));
//		System.out.println(deque.toString());
//		System.out.println(deque.deleteFront());
//		System.out.println(deque.toString());
//		System.out.println(deque.getRear());
//		System.out.println(deque.toString());
//		System.out.println(deque.insertLast(2));
		System.out.println(deque.toString());
		System.out.println(deque.isFull());
		System.out.println(deque.toString());
		
		
//		System.out.println(deque.insertLast(8));
//		System.out.println(deque.insertLast(2));
//		System.out.println(deque.insertFront(3));
//		System.out.println(deque.insertFront(4));
//		System.out.println(deque.getRear());
//		System.out.println(deque.isFull());
//		System.out.println(deque.deleteLast());
//		System.out.println(deque.insertFront(4));
//		System.out.println(deque.getFront());
	}
	
	
	
	int size;
	int[] array;
	int front;
	int tail;
	
	public MyCircularDeque(int k) {
		this.array = new int[k + 1];
		this.front = 0;
		this.tail = 0;
    }
    
    public boolean insertFront(int value) {
    	if(isFull()) {
    		return false;
    	}
    	front = (front - 1 + array.length) % array.length;
    	array[front] = value;
    	return true;
    }
    
    public boolean insertLast(int value) {
    	if(isFull()) {
    		return false;
    	}
    	array[tail] = value;
    	tail = (tail + 1) % array.length;
    	return true;
    }
    
    public boolean deleteFront() {
    	if(isEmpty()) {
    		return false;
    	}
    	front = (front + 1) % array.length;
    	return true;
    }
    
    public boolean deleteLast() {
    	if(isEmpty()) {
    		return false;
    	}
    	tail = (tail - 1 + array.length) % array.length;
    	return true;
    }
    
    public int getFront() {
    	if(isEmpty()) {
    		return -1;
    	}
    	return array[front];
    }
    
    public int getRear() {
    	if(isEmpty()) {
    		return -1;
    	}
    	return array[(tail - 1 + array.length) % array.length];
    }
    
    public boolean isEmpty() {
    	return front == tail;
    }
    
    public boolean isFull() {
    	return (tail + 1) % array.length == front;
    }

	@Override
	public String toString() {
		return Arrays.toString(array);
	}
}
