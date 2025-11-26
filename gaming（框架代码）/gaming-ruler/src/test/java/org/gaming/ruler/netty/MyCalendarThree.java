/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

/**
 * @author YY
 *
 */
public class MyCalendarThree {
	//TreeMap是有顺序的
	private TreeMap<Integer, Integer> cnt = new TreeMap<>();
	
	public MyCalendarThree() {

    }
    
    public int book(int start, int end) {
    	int ans = 0;
        int maxBook = 0;
        cnt.put(start, cnt.getOrDefault(start, 0) + 1);
        cnt.put(end, cnt.getOrDefault(end, 0) - 1);
        System.out.println(cnt);
        for (Map.Entry<Integer, Integer> entry : cnt.entrySet()) {
            int freq = entry.getValue();
            maxBook += freq;
            ans = Math.max(maxBook, ans);
        }
        return ans;
    }
    
    public static void main(String[] args) {
//    	["MyCalendarThree","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book","book"]
//    			
//    	[null,1,1,1,1,1,2,2,2,3,3,4,5,6,6,6,6,6,6,7,7,7,7,7,7,8,8,8,8,8,8]
		Gson gson = new Gson();
//		String json = "[[47,50],[1,10],[27,36],[40,47],[20,27],[15,23],[10,18],[27,36],[17,25],[8,17],[24,33],[23,28],[21,27],[47,50],[14,21],[26,32],[16,21],[2,7],[24,33],[6,13],[44,50],[33,39],[30,36],[6,15],[21,27],[49,50],[38,45],[4,12],[46,50],[13,21]]";
		
		String json = "[[2,10],[8,10],[11,20],[9,15]]";
		int[][] test = gson.fromJson(json, int[][].class);
		MyCalendarThree obj = new MyCalendarThree();
    	for(int i = 0; i < test.length; i++) {
    		System.out.println(obj.book(test[i][0],test[i][1]));
    	}
    					
//    	for(int i = 0; i < obj.layers.size(); i++) {
//    		List<int[]> layer = obj.layers.get(i);
//    		for(int[] time : layer) {
//    			System.out.print("("+time[0] + "," + time[1]+"),");
//    		}
//    		System.out.println();
//    	}
	}
}