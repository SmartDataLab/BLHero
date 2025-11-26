/**
 * 
 */
package com.xiugou.x1.battle;

import java.util.Collections;
import java.util.List;

import com.xiugou.x1.battle.util.SeedRandomUtil.SlimRandom;

/**
 * @author YY
 *
 */
public abstract class SeedContext {

	private final SlimRandom slimRandom;
	
	public SeedContext() {
		this.slimRandom = new SlimRandom();
	}
	
	/**
	 * 包含最大值
	 * @return
	 */
	public int randomRate() {
		return slimRandom.nextInt(10000 + 1);
	}
	/**
	 * 包含最大值
	 * @param bound
	 * @return
	 */
	public int randomClose(int bound) {
		return slimRandom.nextInt(bound + 1);
	}
	/**
	 * 不包含最大值
	 * @param bound
	 * @return
	 */
	public int randomOpen(int bound) {
		return slimRandom.nextInt(bound);
	}
	/**
	 * 随机一个对象
	 * @param list
	 * @return
	 */
	public <T> T randomOne(List<T> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		int index = randomOpen(list.size());
		return list.get(index);
	}
	
	/**
	 * 随机一个范围内的数，包含最大最小值
	 * @param min
	 * @param max
	 * @return
	 */
	public int randomRange(int min, int max) {
		return min + slimRandom.nextInt(max - min + 1);
	}
	
	public void shuffle(List<?> list) {
		int size = list.size();
		for (int i = size; i > 1; i--) {
			 Collections.swap(list, i-1, slimRandom.nextInt(i));
		}
	}
}
