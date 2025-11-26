/**
 * 
 */
package org.gaming.prefab.rank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.gaming.prefab.rank.TopN.TopPageLoader;
import org.gaming.tool.RandomUtil;

/**
 * @author YY
 *
 */
public class TopNTest {

	
	public static class TopNTesting implements TopEntity<Long> {
		private long id;
		private int rankIndex = -1;
		
		private int score;
		
		public TopNTesting(long id, int score) {
			this.id = id;
			this.score = score;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public int getRankIndex() {
			return rankIndex;
		}
		public void setRankIndex(int rankIndex) {
			this.rankIndex = rankIndex;
		}
	}
	
	public static Comparator<TopNTesting> COMPARATOR = new Comparator<TopNTesting>() {
		@Override
		public int compare(TopNTesting o1, TopNTesting o2) {
			return Integer.compare(o1.score, o2.score);
		}
	};
	
	public static void main(String[] args) {
		TopPageLoader<TopNTesting> pageLoader = new TopPageLoader<TopNTesting>() {
			@Override
			public List<TopNTesting> loadPage(int page, int pageSize) {
				return new ArrayList<>();
			}

			@Override
			public int dataCount() {
				return 0;
			}
		};
		TopN<Long, TopNTesting> topN = new TopN<>(3, pageLoader, COMPARATOR);
		
		for(int i = 0; i < 20; i++) {
			TopNTesting node = new TopNTesting(i + 1, RandomUtil.within(10000));
			topN.rank(node);
		}
		
		for(TopNTesting node : topN.topN(100)) {
			System.out.println(node.id + "\t " + node.getRankIndex() + "\t " + node.score);
		}
	}
}
