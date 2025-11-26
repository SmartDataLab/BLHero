/**
 * 
 */
package org.gaming.prefab.rank;

import java.util.Random;

/**
 * @author YY
 *
 */
public class SkipListNode {

	Object ele;
    long score;
    SkipListNode backward;
    SkipListLevel[] level;
    
    public class SkipListLevel {
    	SkipListNode forward;
        long span;
    }
    
    public SkipListNode(int level, long score, Object ele) {
        this.ele = ele;
        this.score = score;
        this.level = new SkipListLevel[level];
        for(int i = 0; i < this.level.length; i++) {
        	this.level[i] = new SkipListLevel();
        }
    }
    
    public static void main(String[] args) {
    	Random random = new Random();
    	for(int i = 0; i < 10; i++) {
    		System.out.println(random.nextFloat());
    	}
	}
}
