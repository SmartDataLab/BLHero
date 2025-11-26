/**
 * 
 */
package org.gaming.prefab.rank;

import java.util.List;
import java.util.Random;

/**
 * @author YY
 * 不要问，问就去看redis源码
 */
public class SkipList {
	
	int ZSKIPLIST_MAXLEVEL = 32;
	float ZSKIPLIST_P = 0.25f;
	
	SkipListNode header;
	SkipListNode tail;
    long length;
    int level;
    
    
    public SkipList() {
	    this.level = 1;
	    this.length = 0;
		this.header = new SkipListNode(ZSKIPLIST_MAXLEVEL, 0, null);
	    for (int j = 0; j < ZSKIPLIST_MAXLEVEL; j++) {
	    	this.header.level[j].forward = null;
	    	this.header.level[j].span = 0;
	    }
	    this.header.backward = null;
	    this.tail = null;
    }
    
    //TODO
    public int sdscmp(Object o1, Object o2) {
    	return (int)(((Integer)o1) - ((Integer)o2));
    }
    
    SkipListNode insert(long score, Object ele) {
        SkipListNode[] update = new SkipListNode[ZSKIPLIST_MAXLEVEL];
        int[] rank = new int[ZSKIPLIST_MAXLEVEL];
        int i;

        SkipListNode x = this.header;
        for (i = this.level-1; i >= 0; i--) {
        	
            rank[i] = (i == (this.level-1)) ? 0 : rank[i+1];
			while (x.level[i].forward != null && (x.level[i].forward.score < score
					|| (x.level[i].forward.score == score && sdscmp(x.level[i].forward.ele, ele) < 0))) {
				rank[i] += x.level[i].span;
				x = x.level[i].forward;
			}
            update[i] = x;
        }
        
        int level = randomLevel();
        if (level > this.level) {
            for (i = this.level; i < level; i++) {
                rank[i] = 0;
                update[i] = this.header;
                update[i].level[i].span = this.length;
            }
            this.level = level;
        }
        x = new SkipListNode(level,score,ele);
        for (i = 0; i < level; i++) {
            x.level[i].forward = update[i].level[i].forward;
            update[i].level[i].forward = x;

            x.level[i].span = update[i].level[i].span - (rank[0] - rank[i]);
            update[i].level[i].span = (rank[0] - rank[i]) + 1;
        }

        for (i = level; i < this.level; i++) {
            update[i].level[i].span++;
        }

        x.backward = (update[0] == this.header) ? null : update[0];
        if (x.level[0].forward != null) {
        	x.level[0].forward.backward = x;
        } else {
        	this.tail = x;
        }
            
        this.length++;
        return x;
    }
    
    Random random = new Random();
    
    int randomLevel() {
        int level = 1;
        while (random.nextFloat() < ZSKIPLIST_P) {
        	level += 1;
        }
		return (level < ZSKIPLIST_MAXLEVEL) ? level : ZSKIPLIST_MAXLEVEL;
    }
    
	private void deleteNode(SkipListNode x, SkipListNode[] update) {
		for (int i = 0; i < this.level; i++) {
			if (update[i].level[i].forward == x) {
				update[i].level[i].span += x.level[i].span - 1;
				update[i].level[i].forward = x.level[i].forward;
			} else {
				update[i].level[i].span -= 1;
			}
		}
		if (x.level[0].forward != null) {
			x.level[0].forward.backward = x.backward;
		} else {
			this.tail = x.backward;
		}
		while (this.level > 1 && this.header.level[this.level - 1].forward == null) {
			this.level--;
		}
		this.length--;
	}

	int delete(long score, Object ele) {
		SkipListNode[] update = new SkipListNode[ZSKIPLIST_MAXLEVEL];

		SkipListNode x = this.header;
		for (int i = this.level - 1; i >= 0; i--) {
			while (x.level[i].forward != null && (x.level[i].forward.score < score
					|| (x.level[i].forward.score == score && sdscmp(x.level[i].forward.ele, ele) < 0))) {
				x = x.level[i].forward;
			}
			update[i] = x;
		}
		x = x.level[0].forward;
		if (x != null && score == x.score && sdscmp(x.ele, ele) == 0) {
			deleteNode(x, update);
			return 1;
		}
		return 0;
	}
	
	
	SkipListNode updateScore(long curscore, Object ele, long newscore) {
		SkipListNode[] update = new SkipListNode[ZSKIPLIST_MAXLEVEL];

		SkipListNode x = this.header;
		for (int i = this.level - 1; i >= 0; i--) {
			while (x.level[i].forward != null && (x.level[i].forward.score < curscore
					|| (x.level[i].forward.score == curscore && sdscmp(x.level[i].forward.ele, ele) < 0))) {
				x = x.level[i].forward;
			}
			update[i] = x;
		}

		x = x.level[0].forward;
		if (!(x != null && curscore == x.score && sdscmp(x.ele, ele) == 0)) {
			// 跳表中的节点当前分数跟参数不一致
			return null;
		}

		if ((x.backward == null || x.backward.score < newscore)
				&& (x.level[0].forward == null || x.level[0].forward.score > newscore)) {
			x.score = newscore;
			return x;
		}

		deleteNode(x, update);
		SkipListNode newnode = insert(newscore, x.ele);
		x.ele = null;
		return newnode;
	}

	
	long getRank(long score, Object ele) {
		long rank = 0;

		SkipListNode x = this.header;
		for (int i = this.level - 1; i >= 0; i--) {
			while (x.level[i].forward != null && (x.level[i].forward.score < score
					|| (x.level[i].forward.score == score && sdscmp(x.level[i].forward.ele, ele) <= 0))) {
				rank += x.level[i].span;
				x = x.level[i].forward;
			}

			if (x.ele != null && sdscmp(x.ele, ele) == 0) {
				return rank;
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param rank 从1开始
	 * @return
	 */
	SkipListNode getElementByRank(long rank) {
		long traversed = 0;
		SkipListNode x = this.header;
		for (int i = this.level - 1; i >= 0; i--) {
			while (x.level[i].forward != null && (traversed + x.level[i].span) <= rank) {
				traversed += x.level[i].span;
				x = x.level[i].forward;
			}
			if (traversed == rank) {
				return x;
			}
		}
		return null;
	}
	
	List<SkipListNode> getElementsByRange(int rankMin, int rankMax) {
		return null;
	}
	
	
    
    public static void main(String[] args) {
    	SkipList skipList = new SkipList();

    	skipList.insert(555, 1001);
    	skipList.insert(111, 1002);
    	skipList.insert(777, 1003);
    	skipList.insert(222, 1004);
    	
    	SkipListNode currNode = skipList.header;
    	while(currNode != null) {
    		System.out.println(currNode.ele);
    		currNode = currNode.backward;
    	}
    	
    	System.out.println("getRank " + skipList.getRank(111, 1002));
    	
    	SkipListNode findNode = skipList.getElementByRank(1);
    	System.out.println(findNode.ele);
    	
    	System.out.println(skipList);
	}
}
