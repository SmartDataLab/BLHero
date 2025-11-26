/**
 * 
 */
package org.gaming.tool;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author YY
 *
 */
public class RandomUtil {

	public static Random localRandom() {
		return ThreadLocalRandom.current();
	}
	/**
	 * (min,max)
	 * @param min
	 * @param max
	 * @return
	 */
	public static int openOpen(int min, int max) {
		Random random = localRandom();
		int num = random.nextInt(max - min - 1);
		return num + min + 1;
	}
	/**
	 * (min,max]
	 * @param min
	 * @param max
	 * @return
	 */
	public static int openClose(int min, int max) {
		Random random = localRandom();
		int num = random.nextInt(max - min);
		return num + min + 1;
	}
	/**
	 * [min,max]
	 * @param min
	 * @param max
	 * @return
	 */
	public static int closeClose(int min, int max) {
		Random random = localRandom();
		int num = random.nextInt(max - min + 1);
		return num + min;
	}
	/**
	 * [min,max)
	 * @param min
	 * @param max
	 * @return
	 */
	public static int closeOpen(int min, int max) {
		Random random = localRandom();
		int num = random.nextInt(max - min);
		return num + min;
	}
	/**
	 * [0,num)
	 * @param num
	 * @return
	 */
	public static int within(int num) {
		return closeOpen(0, num);
	}
	/**
	 * 获取一个根据随机种子创建的随机工具
	 * @param seed
	 * @return
	 */
	public static Random seed(long seed) {
		return new Random(seed);
	}
}
