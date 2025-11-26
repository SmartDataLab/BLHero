/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * @author YY
 *
 */
public class MyCalendar {
	
	List<int[]> list = new ArrayList<>();
	
	public MyCalendar() {

	}

	public boolean book(int start, int end) {
		for(int[] time : list) {
			int s = time[0];
			int e = time[1];
			if(start < s && end <= s) {
			} else if(e <= start && e < end) {
			} else {
				return false;
			}
		}
		list.add(new int[] {start, end});
		return true;
	}
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		String json = "[[47,50],[33,41],[39,45],[33,42],[25,32],[26,35],[19,25],[3,8],[8,13],[18,27]]";
		int[][] test = gson.fromJson(json, int[][].class);
		
		MyCalendar obj = new MyCalendar();
    	for(int i = 0; i < test.length; i++) {
//    		System.out.println(i);
    		System.out.println(obj.book(test[i][0],test[i][1]) + " " + gson.toJson(obj.list));
    	}
	}
}
