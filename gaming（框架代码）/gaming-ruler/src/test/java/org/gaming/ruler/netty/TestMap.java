/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YY
 *
 */
public class TestMap {

	public static void main(String[] args) {
		AtomicInteger id = new AtomicInteger();
		id.incrementAndGet();
		
	    List<String> list = new ArrayList<>();
	    list.add("jlkk");
	    list.add("lopi");
	    list.add("jmdw");
	    list.add("e4we");
	    list.add("io98");
	    list.add("nmhg");
	    list.add("vfg6");
	    list.add("gfrt");
	    list.add("alpo");
	    list.add("vfbh");
	    list.add("bnhj");
	    list.add("zuio");
	    list.add("iu8e");
	    list.add("yhjk");
	    list.add("plop");
	    list.add("dd0p");
	    for (String key : list) {
	        int hash = key.hashCode() ^ (key.hashCode() >>> 16);
	        
	        System.out.println(Integer.toBinaryString(key.hashCode()) + " " + key.hashCode());
	        System.out.println(Integer.toBinaryString(key.hashCode() >>> 16) + " " + (key.hashCode() >>> 16));
	        System.out.println(Integer.toBinaryString(hash) + " " + hash);
	        
	        System.out.println("字符串：" + key + " " + key.hashCode());
	        System.out.println("Idx(16)：" + ((16 - 1) & hash));
	        System.out.println("Bit值：" + Integer.toBinaryString(hash) + " " + Integer.toBinaryString((16 - 1) & hash) + " " + Integer.toBinaryString(16 - 1));
	        System.out.println("Idx(32)：" + ((32 - 1) & hash));
	        System.out.println("Bit值：" + Integer.toBinaryString(hash) + " " + Integer.toBinaryString((32 - 1) & hash) + " " + Integer.toBinaryString(32 - 1));
	        System.out.println("=============================");
	    }
	}
}
