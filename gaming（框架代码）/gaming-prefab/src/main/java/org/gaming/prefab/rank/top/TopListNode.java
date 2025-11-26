/**
 * 
 */
package org.gaming.prefab.rank.top;

/**
 * @author YY
 *
 */
public class TopListNode<T> {

	protected T ele;
	protected TopListNode<T> backward;
	protected SkipListLevel<T>[] level;
    
    public static class SkipListLevel<E> {
    	protected TopListNode<E> forward;
    	protected long span;
    }
    
    @SuppressWarnings("unchecked")
	protected TopListNode(int level, T ele) {
        this.ele = ele;
        this.level = new SkipListLevel[level];
        for(int i = 0; i < this.level.length; i++) {
        	this.level[i] = new SkipListLevel<>();
        }
    }
}
