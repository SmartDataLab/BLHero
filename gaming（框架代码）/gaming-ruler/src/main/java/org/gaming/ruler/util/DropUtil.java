/**
 *
 */
package org.gaming.ruler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.gaming.tool.RandomUtil;

/**
 * @author YY
 *
 */
public class DropUtil {

    /**
     * @author YY 掉落随机物接口
     */
    public static interface IDrop {
    	/**
    	 * 获取掉落物品的标识
    	 * @return
    	 */
        int getDropId();
        /**
         * 获取掉落物品的概率或者权重
         * @return
         */
        int getDropRate();
    }

    public static <T> T randomDrop(List<T> dropGroup, Function<T, Integer> idField, Function<T, Integer> rateField) {
        if (dropGroup.isEmpty()) {
            return null;
        }
        int totalRate = 0;
        for (T drop : dropGroup) {
            totalRate += rateField.apply(drop);
        }
        return randomOneDrop(dropGroup, totalRate, idField, rateField);
    }

    private static <T> T randomOneDrop(List<T> dropGroup, int totalRate, Function<T, Integer> idField, Function<T, Integer> rateField) {
    	// 随机概率
        int randomRate = RandomUtil.closeClose(1, totalRate);
    	// 当前几率和
        int rate = 0;
        for (T drop : dropGroup) {
            rate += rateField.apply(drop);
            if (randomRate <= rate) {
                if (idField.apply(drop) > 0) {
                    return drop;
                }
                break;
            }
        }
        return null;
    }

    /**
     * 随机一个物品
     *
     * @param dropGroup
     * @return
     */
    public static <T extends IDrop> T randomDrop(List<T> dropGroup) {
        if (dropGroup.isEmpty()) {
            return null;
        }
        int totalRate = 0;
        for (T drop : dropGroup) {
            totalRate += drop.getDropRate();
        }
        return randomOneDrop(dropGroup, totalRate);
    }

    /**
     * 随机出一个物品
     *
     * @param dropGroup 随机列表
     * @param totalRate 总概率
     * @return
     */
    private static <T extends IDrop> T randomOneDrop(List<T> dropGroup, int totalRate) {
        // 随机概率
        int randomRate = RandomUtil.closeClose(1, totalRate);
        // 当前几率和
        int rate = 0;
        for (T drop : dropGroup) {
            rate += drop.getDropRate();
            if (randomRate <= rate) {
                if (drop.getDropId() > 0) {
                    return drop;
                }
                break;
            }
        }
        return null;
    }

    /**
     * 从随机列表中随机出count个不同的物品
     *
     * @param dropGroup
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends IDrop> List<T> randomDifDrop(List<T> dropGroup, int count) {
        if (dropGroup == null || dropGroup.isEmpty()) {
            return Collections.emptyList();
        }
		if (count >= dropGroup.size()) {
			return dropGroup;
		}

        List<T> results = new ArrayList<>(count);
        // 统计概率
        int totalRate = 0;
        for (T drop : dropGroup) {
            totalRate += drop.getDropRate();
        }

        Object[] randomArray = dropGroup.toArray(new Object[dropGroup.size()]);
        for (int i = 0; i < count; i++) {
            int random = RandomUtil.closeClose(1, totalRate);
            // 当前几率和
            int value = 0;
            for (int j = 0; j < randomArray.length; j++) {
                T drop = (T) randomArray[j];
                if (drop == null) {
                    continue;
                }
                value += drop.getDropRate();
                if (random <= value) {
                    if (drop.getDropId() > 0) {
                        // 将随机得出的位置设置为null，并减去当次随机结果的概率
                        randomArray[j] = null;
                        totalRate = totalRate - drop.getDropRate();

                        results.add(drop);
                    }
                    break;
                }
            }
        }
        return results;
    }
    
    /**
     * 从随机列表中随机出count个可能重复结果
     * @param dropGroup
     * @param count
     * @return
     */
    public static <T extends IDrop> List<T> randomDrop(List<T> dropGroup, int count) {
    	if (dropGroup == null || dropGroup.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> results = new ArrayList<>(count);
        // 统计概率
        int totalRate = 0;
        for (T drop : dropGroup) {
            totalRate += drop.getDropRate();
        }
        for(int i = 0; i < count; i++) {
        	T t = randomOneDrop(dropGroup, totalRate);
        	if(t != null) {
        		results.add(t);
        	}
        }
        return results;
    }
    
    /**
     * 通过传入的随机数字来计算掉落的结果，适用于随机数由随机种子生成的场景
     * @param dropGroup
     * @param randomNumber
     * @return
     */
    public static <T extends IDrop> T randomUseRate(List<T> dropGroup, int randomNumber) {
    	if (dropGroup == null || dropGroup.isEmpty()) {
            return null;
        }
    	// 统计概率
        int totalRate = 0;
        for (T drop : dropGroup) {
            totalRate += drop.getDropRate();
        }
        int randomRate = randomNumber % totalRate + 1;
    	// 当前几率和
        int rate = 0;
        for (T drop : dropGroup) {
            rate += drop.getDropRate();
            if (randomRate <= rate) {
                if (drop.getDropId() > 0) {
                    return drop;
                }
                break;
            }
        }
        return null;
    }
    
    /**
     * 以万分比概率进行随机
     * @param dropGroup
     * @return
     */
    public static <T extends IDrop> T randomIn10000Ratio(List<T> dropGroup) {
    	if (dropGroup == null || dropGroup.isEmpty()) {
            return null;
        }
    	int random = RandomUtil.closeClose(1, 10000);
    	int currRate = 0;
    	for(T drop : dropGroup) {
    		currRate += drop.getDropRate();
    		if(random <= currRate) {
    			return drop;
    		}
    	}
    	return null;
    }
}
