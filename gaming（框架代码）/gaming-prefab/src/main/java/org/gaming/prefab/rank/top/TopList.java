/**
 * 
 */
package org.gaming.prefab.rank.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author YY 不要问，问就去看redis源码
 */
public class TopList<T> {

	private int ZSKIPLIST_MAXLEVEL = 32;
	private float ZSKIPLIST_P = 0.25f;

	private TopListNode<T> header;
	private TopListNode<T> tail;
	private int length;
	private int level;

	// 比较器
	private TopComparator<T> comparator;
	// 排名的上限，0为无上限，如果一开始正序排行榜里面有51~100的积分排名，后来来了1~50的排名，则排行榜的长度会变成100
	// 整个排行榜的数据长度最大是limit的两倍
	// 处理方案可以交由业务进行定时清理
	private int limit;

	public TopList(TopComparator<T> comparator) {
		this(comparator, 0);
	}

	public TopList(TopComparator<T> comparator, int limit) {
		this.comparator = comparator;
		this.level = 1;
		this.length = 0;
		this.header = new TopListNode<>(ZSKIPLIST_MAXLEVEL, null);
		for (int j = 0; j < ZSKIPLIST_MAXLEVEL; j++) {
			this.header.level[j].forward = null;
			this.header.level[j].span = 0;
		}
		this.header.backward = null;
		this.tail = null;
		this.limit = limit;
	}
	
	public void clear() {
		this.level = 1;
		this.length = 0;
		this.header = new TopListNode<>(ZSKIPLIST_MAXLEVEL, null);
		for (int j = 0; j < ZSKIPLIST_MAXLEVEL; j++) {
			this.header.level[j].forward = null;
			this.header.level[j].span = 0;
		}
		this.header.backward = null;
		this.tail = null;
	}

	// TODO
	// 比较两个对象是否相等
	public int sdscmp(T o1, T o2) {
		return comparator.sdscmp(o1, o2);
	}

	public int getRank(T ele) {
		int rank = 0;
		TopListNode<T> x = this.header;
		for (int i = this.level - 1; i >= 0; i--) {

			while (x.level[i].forward != null) {
				int result = comparator.compara(x.level[i].forward.ele, ele);
				if (result < 0) {
					rank += x.level[i].span;
					x = x.level[i].forward;
				} else if (result == 0 && sdscmp(x.level[i].forward.ele, ele) <= 0) {
					rank += x.level[i].span;
					x = x.level[i].forward;
				} else {
					break;
				}
			}
			if (x.ele != null && sdscmp(x.ele, ele) == 0) {
				return rank;
			}
		}
		return 0;
	}

	/**
	 * 返回元素插入时的排名
	 * @param ele
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int insert(T ele) {
		TopListNode<T>[] update = new TopListNode[ZSKIPLIST_MAXLEVEL];
		int[] rank = new int[ZSKIPLIST_MAXLEVEL];
		int i;

		TopListNode<T> x = this.header;
		for (i = this.level - 1; i >= 0; i--) {

			rank[i] = (i == (this.level - 1)) ? 0 : rank[i + 1];
			while (x.level[i].forward != null) {
				int result = comparator.compara(x.level[i].forward.ele, ele);
				if (result < 0) {
					rank[i] += x.level[i].span;
					x = x.level[i].forward;
				} else if (result == 0 && sdscmp(x.level[i].forward.ele, ele) < 0) {
					rank[i] += x.level[i].span;
					x = x.level[i].forward;
				} else {
					break;
				}
			}
			update[i] = x;
		}
		if(limit > 0 && rank[0] >= limit) {
			return 0;
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
		x = new TopListNode<T>(level, ele);
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
		return rank[0] + 1;
	}

	private Random random = new Random();

	private int randomLevel() {
		int level = 1;
		while (random.nextFloat() < ZSKIPLIST_P) {
			level += 1;
		}
		return (level < ZSKIPLIST_MAXLEVEL) ? level : ZSKIPLIST_MAXLEVEL;
	}

	private void deleteNode(TopListNode<T> x, TopListNode<T>[] update) {
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

	@SuppressWarnings("unchecked")
	public int delete(T ele) {
		TopListNode<T>[] update = new TopListNode[ZSKIPLIST_MAXLEVEL];

		TopListNode<T> x = this.header;
		for (int i = this.level - 1; i >= 0; i--) {
			while (x.level[i].forward != null) {
				int result = comparator.compara(x.level[i].forward.ele, ele);
				if (result < 0) {
					x = x.level[i].forward;
				} else if (result == 0 && sdscmp(x.level[i].forward.ele, ele) < 0) {
					x = x.level[i].forward;
				} else {
					break;
				}
			}
			update[i] = x;
		}
		x = x.level[0].forward;
		if (x != null && comparator.compara(x.ele, ele) == 0 && sdscmp(x.ele, ele) == 0) {
			deleteNode(x, update);
			return 1;
		}
		return 0;
	}

	TopListNode<T> updateScore(long curscore, T ele, long newscore) {
		return null;
		// TopListNode<T>[] update = new TopListNode[ZSKIPLIST_MAXLEVEL];
		//
		// TopListNode<T> x = this.header;
		// for (int i = this.level - 1; i >= 0; i--) {
		// while (x.level[i].forward != null && (x.level[i].forward.score < curscore
		// || (x.level[i].forward.score == curscore && sdscmp(x.level[i].forward.ele,
		// ele) < 0))) {
		// x = x.level[i].forward;
		// }
		// update[i] = x;
		// }
		//
		// x = x.level[0].forward;
		// if (!(x != null && curscore == x.score && sdscmp(x.ele, ele) == 0)) {
		// // 跳表中的节点当前分数跟参数不一致
		// return null;
		// }
		//
		// if ((x.backward == null || x.backward.score < newscore)
		// && (x.level[0].forward == null || x.level[0].forward.score > newscore)) {
		// x.score = newscore;
		// return x;
		// }
		//
		// deleteNode(x, update);
		// TopListNode<T> newnode = insert(newscore, x.ele);
		// x.ele = null;
		// return newnode;
	}

	/**
	 * 
	 * @param rank
	 *            从1开始
	 * @return
	 */
	private TopListNode<T> getNodeByRank(long rank) {
		long traversed = 0;
		TopListNode<T> x = this.header;
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

	public T getElementByRank(long rank) {
		TopListNode<T> node = getNodeByRank(rank);
		if (node != null) {
			return node.ele;
		}
		return null;
	}

	/**
	 * 
	 * @param rankMin
	 *            从1开始
	 * @param rankMax
	 * @return
	 */
	public List<T> getElementsByRange(int rankMin, int rankMax) {
		TopListNode<T> node = this.getNodeByRank(rankMin);
		if (node == null) {
			return Collections.emptyList();
		}
		int size = rankMax - rankMin;
		if (size < 0) {
			return Collections.emptyList();
		}
		List<T> list = new ArrayList<>();
		list.add(node.ele);
		if (size == 0) {
			return list;
		}
		TopListNode<T> currNode = node.level[0].forward;
		int i = 0;
		while (currNode != null && i < size) {
			list.add(currNode.ele);
			currNode = currNode.level[0].forward;
			i += 1;
		}
		return list;
	}

	public int getLength() {
		return length;
	}

	public static void main(String[] args) {
		TopList<Integer> skipList = new TopList<>(new TopComparator<Integer>() {
			@Override
			public int compara(Integer t1, Integer t2) {
				return Integer.compare(t1, t2);
			}

			@Override
			public int sdscmp(Integer t1, Integer t2) {
				return Integer.compare(t1, t2);
			}
		}, 50);

		// skipList.insert(888);
		// skipList.insert(222);
		// skipList.insert(111);
		// skipList.insert(777);

		for (int i = 0; i < 55; i++) {
			// int score = RandomUtil.closeClose(1, 1000);
			int rank = skipList.insert(i + 1);
			System.out.println(rank);
		}

		// System.out.println("getRank " + skipList.getRank(111));
		//
		// int findNode = skipList.getElementByRank(1);
		// System.out.println(findNode);
		//
		// for(int i = 1; i <= skipList.length; i ++) {
		// int value = skipList.getElementByRank(i);
		// System.out.println(value);
		// }
		//
		// List<Integer> list1 = skipList.getElementsByRange(1, 1);
		// System.out.println(list1);
		//
		// List<Integer> list2 = skipList.getElementsByRange(1, 2);
		// System.out.println(list2);
		//
		// List<Integer> list3 = skipList.getElementsByRange(1, 5);
		// System.out.println(list3);
	}
}
