/**
 * 
 */
package org.gaming.ruler.interesting.aoi.crosslinkedlist;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gaming.ruler.interesting.aoi.IAreaOfInterest;
import org.gaming.tool.RandomUtil;

/**
 * @author YY
 * 十字链表
 */
public class CrossLinkedList<T> implements IAreaOfInterest<T> {
	
	private final DistanceLinkedList<T> xAxis = new DistanceLinkedList<>();
	private final DistanceLinkedList<T> yAxis = new DistanceLinkedList<>();
	private final ConcurrentMap<T, CllEntity<T>> entityMap = new ConcurrentHashMap<>();
	
	/**
	 * 
	 * @param t
	 * @param x 进入时的X坐标
	 * @param y 进入时的Y坐标
	 * @param widthSize 视野的宽度
	 * @param heightSize 视野的高度
	 */
	public CllEntity<T> enter(T t, int x, int y, int widthSize, int heightSize) {
		CllEntity<T> entity = new CllEntity<>(t, widthSize, heightSize);
		this.xAxis.enter(entity.xNode, x);
//		this.xAxis.print();
		this.yAxis.enter(entity.yNode, y);
//		this.yAxis.print();
		entityMap.put(t, entity);
		return entity;
	}
	
	public void moveTo(T t, int x, int y) {
		CllEntity<T> entity = entityMap.get(t);
		if(entity == null) {
			return;
		}
		this.xAxis.move(entity.xNode, x);
		this.yAxis.move(entity.yNode, y);
	}
	
	public void exit(T t) {
		CllEntity<T> entity = entityMap.get(t);
		if(entity == null) {
			return;
		}
		this.xAxis.exit(entity.xNode);
		this.yAxis.exit(entity.yNode);
	}
	
	public static void main(String[] args) {
//		list.entry(new Entity<>(1), 10, 10);
//		list.entry(new Entity<>(2), 14, 10);
//		list.entry(new Entity<>(3), 8, 10);
		long time1 = System.currentTimeMillis();
		CrossLinkedList<Integer> cross = new CrossLinkedList<>();
		for(int i = 1; i <= 1000; i++) {
			int x = RandomUtil.closeClose(1, 300);
			int y = RandomUtil.closeClose(1, 200);
			cross.enter(i, x, y, 40, 60);
		}
		
		long time2 = System.currentTimeMillis();
		
		for(CllEntity<Integer> entity : cross.entityMap.values()) {
			int x = RandomUtil.closeClose(-10, 10);
			int y = RandomUtil.closeClose(-10, 10);
			int newX = entity.xNode.distance + x;
			int newY = entity.yNode.distance + y;
			cross.moveTo(entity.object, newX, newY);
		}
		
		long time3 = System.currentTimeMillis();
		System.out.println("use time1 " + (time2 - time1));
		System.out.println("use time2 " + (time3 - time2));
		
		System.out.println(cross.entityMap.get(500));
		
//		cross.enter(1, 10, 8);
//		cross.enter(2, 7, 10);
//		cross.enter(3, 15, 10);
		
		
//		Entity<Integer> entity1 = cross.entityMap.get(1);
//		System.out.println(entity1);
//		
//		Entity<Integer> entity2 = cross.entityMap.get(2);
//		System.out.println(entity2);
//		
//		Entity<Integer> entity3 = cross.entityMap.get(3);
//		System.out.println(entity3);
//		
//		Entity<Integer> entity4 = cross.entityMap.get(4);
//		System.out.println(entity4);
	}
}
