/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.Scanner;

/**
 * @author YY
 *
 */
public class Bottle {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
		java.util.HashSet<Integer> set = new java.util.HashSet<>();
		int totalNum = -1;
		int inputNum = 0;
        while (in.hasNextInt()) { // 注意 while 处理多个 case
        	if(totalNum < 0) {
        		totalNum = in.nextInt();
        	} else {
        		int a = in.nextInt();
        		set.add(a);
        		inputNum++;
        		if(inputNum >= totalNum) {
        			break;
        		}
        	}
        }
        java.util.ArrayList<Integer> list = new java.util.ArrayList<>(set);
        java.util.Collections.sort(list);
        for(int i : list) {
        	System.out.println(i);
        }
        in.close();
        
//        solution(1);
//        solution(2);
//        solution(3);
//        solution(11);
//        solution(81);
	}
	
	public static void solution(int bottleNum) {
		int each = 3;
		int currNum = bottleNum;
		int result = 0;
		while(currNum >= each) {
			int num = currNum / each;
			result += num;
			currNum = currNum % each + num;
		}
		if(currNum == each - 1) {
			result += 1;
		}
		System.out.println(result);
	}
	
	public static void solution() {
		
		
		
	}
}
