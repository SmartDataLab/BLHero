/**
 * 
 */
package org.gaming.prefab.rank;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author YY
 *
 */
public class RangeService {

//	public static Comparator<SortKey> 
	
	public static void main(String[] args) {
		
		
		
		ConcurrentSkipListMap<SortKey, String> map = new ConcurrentSkipListMap<>();
		
		map.put(new SortKey("A", 588), "A");
		map.put(new SortKey("B", 11), "B");
		map.put(new SortKey("C", 113), "C");
		map.put(new SortKey("D", 7), "D");
//		map.put(new SortKey("E", 55), "E");
//		map.put(new SortKey("F", 789), "F");
//		map.put(new SortKey("G", 222), "G");
		
		for(Entry<SortKey, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.println("===================");

		System.out.println("map.containsKey " + map.containsKey(new SortKey("B", 10)));
		
//		map.
		
		System.out.println(map.get(new SortKey("B", 1)));
		map.remove(new SortKey("B", 1));
		map.put(new SortKey("B", 999), "B");
		for(Entry<SortKey, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}
	
	
	public static class SortKey implements Comparable<SortKey> {
		private String id;
		private long score;
		
		public SortKey(String id, long score) {
			this.id = id;
			this.score = score;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public long getScore() {
			return score;
		}
		public void setScore(long score) {
			this.score = score;
		}

		@Override
		public String toString() {
			return "SortKey [id=" + id + ", score=" + score + "]";
		}

		@Override
		public int compareTo(SortKey o) {
			System.out.println(this.id + " " + this.score + " " + o.id + " " + o.score);
			if(this.id.equals(o.id)) {
				return 0;
			} else {
				if(this.score > o.score) {
					return -1;
				} else if(this.score < o.score) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}
}
