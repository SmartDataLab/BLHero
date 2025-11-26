/**
 * 
 */
package com.xiugou.x1.battle.util;

import java.util.Random;


/**
 * @author YY
 * 可设置随机种子的随机类
 */
public class SeedRandomUtil {
	
	private static final ThreadLocal<SlimRandom> RANDOM_POOL = new ThreadLocal<>();
	
	private static SlimRandom get() {
		SlimRandom random = RANDOM_POOL.get();
		if(random == null) {
			random = new SlimRandom();
			RANDOM_POOL.set(random);
		}
		return random;
	}
	
	public static SlimRandom getNew() {
		return new SlimRandom();
	}
	
	public static int random(int min, int max) {
		return nextInt(max - min) + min;
	}
	
	public static int random(long seed, int min, int max) {
		return nextInt(seed, max - min) + min;
	}
	
	/**
	 * 不包含最大值
	 * @param bound
	 * @return
	 */
	public static int nextInt(int bound) {
		return get().nextInt(bound);
	}
	/**
	 * 包含最大之后
	 * @param bound
	 * @return
	 */
	public static int randomInt(int bound) {
		return get().nextInt(bound + 1);
	}
	
	public static int nextInt(long seed, int bound) {
		return get().nextInt(seed, bound);
	}
	
	public static int randomInt(long seed, int bound) {
		return get().nextInt(seed, bound + 1);
	}
	
	public static void setSeed(long seed) {
		get().setSeed(seed);
	}
	
	public static long getSeed() {
		return get().seedUniquifier() * System.nanoTime();
	}
	
	/**
	 * 从java.util.Random里面提取了部分随机的代码
	 */
	public static class SlimRandom {
		
		private static final long multiplier = 0x5DEECE66DL;
	    private static final long addend = 0xBL;
	    private static final long mask = (1L << 48) - 1;
	    
	    private long seed;
	    private long seedUniquifier = 8682522807148012L;
	    
	    public SlimRandom() {
	    	this.seed = initialScramble(seedUniquifier() ^ System.nanoTime());
	    }
	    
	    public SlimRandom(long seed) {
	        this.seed = initialScramble(seed);
	    }

	    private static long initialScramble(long seed) {
	        return (seed ^ multiplier) & mask;
	    }
	    
		private int next(int bits) {
	        long oldseed = this.seed;
	        long nextseed = (oldseed * multiplier + addend) & mask;
	        this.seed = nextseed;
	        return (int)(nextseed >>> (48 - bits));
	    }
		
		private long seedUniquifier() {
			return seedUniquifier = seedUniquifier * 181783497276652981L;
	    }
		
		public void setSeed(long seed) {
	        this.seed = initialScramble(seed);
	    }
		
		public int nextInt() {
	        return next(32);
	    }
		
		public int nextInt(int bound) {
	        if (bound <= 0) {
	        	return 0;
	        }

	        int r = next(31);
	        int m = bound - 1;
	        if ((bound & m) == 0) {  // i.e., bound is a power of 2
	        	r = (int)((bound * (long)r) >> 31);
	        } else {
	            for (int u = r;
	                 u - (r = u % bound) + m < 0;
	                 u = next(31))
	                ;
	        }
	        return r;
	    }
		
		public int nextInt(long seed, int bound) {
	        this.setSeed(seed);
	        return nextInt(bound);
	    }

		public long getSeed() {
			return seed;
		}
	}
	
	public static void main(String[] args) {
		long seed = getSeed();
		
		System.out.println(seed);
		
		SeedRandomUtil.setSeed(seed);
		
		for (int i = 0; i < 10; i++) {
			System.out.println(SeedRandomUtil.random(0, 1));
		}

		System.out.println("======================");
		
		Random random = new Random();
		random.setSeed(seed);
		
		for (int i = 0; i < 10; i++) {
//			System.out.println(random.nextInt(100));
		}
	}
}
