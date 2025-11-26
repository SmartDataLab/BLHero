/**
 * 
 */
package org.gaming.ruler.interesting.astar;


/**
 * @author YY
 *
 * 二叉堆分为最小二叉堆和最大二叉堆
 * 
 * 最小二叉堆能确保两件事
 * 第一件事，堆顶元素是所有元素中最小的
 * 第二件事，每一个节点都比它的左右子节点小
 * 
 * 最大二叉堆能确保两件事
 * 第一件事，堆顶元素是所有元素中最大的
 * 第二件事，每一个节点都比它的左右子节点大
 */
public class BinaryHeap<T> {
	
	private Object[] heapData;
	
	private int elementCount = 0;
	/**
	 * heapData[0]是空对象，用来做占位
	 */
	private final int firstEmpty = 1;
	/**
	 * 比较器
	 */
	private HeapComparator<T> comparator;
	
	public BinaryHeap(HeapComparator<T> comparator) {
		this(10, comparator);
	}
	
	public BinaryHeap(int capacity, HeapComparator<T> comparator) {
		heapData = new Object[capacity + firstEmpty];
		this.comparator = comparator;
	}
	
	public void add(T t) {
		
		ensureHeap();
		
		heapData[++elementCount] = t;
		
		int i = elementCount;
		int ii = elementCount >> 1;
		
		while (i > 1 && compare(heapData[i], heapData[ii])) {
			
			Object temp = heapData[i];
			heapData[i] = heapData[ii];
			heapData[ii] = temp;
			
			i = ii;
			ii = i >> 1;
		}
	}
	
	public T poll() {
		if(elementCount <= 0) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		T top = (T) heapData[1];
		
		// 将最后一个设置到第一位
		heapData[1] = heapData[elementCount];
		// 移除最后一个元素
		heapData[elementCount--] = null;
		
		int i = 1;
		int l = elementCount + firstEmpty;
		int left = i << 1;
		int right = left + 1;
		
		while (left < l) {
			int ii = 0;
			if (right < l) {
				ii = compare(heapData[right], heapData[left]) ? right : left;
			} else {
				ii = left;
			}
			
			if(compare(heapData[ii], heapData[i])) {
				Object temp = heapData[i];
				heapData[i] = heapData[ii];
				heapData[ii] = temp;
				
				i = ii;
				left = i << 1;
				right = left + 1;
			} else {
				break;
			}
		}
		return top;
	}
	
	private void ensureHeap() {
		if(elementCount >= heapData.length - firstEmpty) {
			Object[] tempHeap = heapData;
			heapData = new Object[(elementCount >> 1) + elementCount + firstEmpty];
			System.arraycopy(tempHeap, 0, heapData, 0, tempHeap.length);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean compare(Object t1, Object t2) {
		return this.comparator.compare((T) t1, (T) t2);
	}
	
	public void clear() {
		for(int i = 0; i < heapData.length; i++) {
			heapData[i] = null;
		}
		elementCount = 0;
	}
	
	public int size() {
		return elementCount;
	}
	
	public void print() {
		for(int i = 0; i < heapData.length; i++) {
			System.out.println(i + " " + heapData[i]);
		}
	}
	
	public static void main(String[] args) {
		BinaryHeap<Integer> heap = new BinaryHeap<>(new HeapComparator<Integer>() {
			@Override
			public boolean compare(Integer t1, Integer t2) {
				return t1 < t2;
			}
		});
		for(int i = 30; i > 0; i--) {
			heap.add(i);
		}
		heap.print();
		System.out.println("==============================");
		Integer min = heap.poll();
		System.out.println(min);
		System.out.println("==============================");
		heap.print();
		
		
		
		System.out.println("==============================");
		min = heap.poll();
		System.out.println(min);
		System.out.println("==============================");
		heap.print();
	}
	
	public static interface HeapComparator<T> {
		boolean compare(T t1, T t2);
	}
}
