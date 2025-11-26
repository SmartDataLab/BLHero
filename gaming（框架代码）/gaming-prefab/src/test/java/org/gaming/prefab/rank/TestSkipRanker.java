/**
 * 
 */
package org.gaming.prefab.rank;

import org.gaming.prefab.rank.top.TopComparator;
import org.gaming.prefab.rank.top.TopList;

/**
 * @author YY
 *
 */
public class TestSkipRanker {
	private long id;

	private long score;
	
	public TestSkipRanker(long id, long score) {
		this.id = id;
		this.score = score;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
	
	public static void main(String[] args) {
		TopList<TestSkipRanker> topList = new TopList<>(new TopComparator<TestSkipRanker>() {
			@Override
			public int compara(TestSkipRanker t1, TestSkipRanker t2) {
				return Long.compare(t2.score, t1.score);
			}

			@Override
			public int sdscmp(TestSkipRanker t1, TestSkipRanker t2) {
				return Long.compare(t1.id, t2.id);
			}
		});
		
		topList.insert(new TestSkipRanker(1, 3));
		topList.insert(new TestSkipRanker(1, 1));
		topList.insert(new TestSkipRanker(1, 2));
		
		
		for(TestSkipRanker rank : topList.getElementsByRange(10, 100)) {
			System.out.println(rank.getId() + " " + rank.getScore());
		}
		System.out.println(topList.getRank(new TestSkipRanker(1, 2)));
	}
}
