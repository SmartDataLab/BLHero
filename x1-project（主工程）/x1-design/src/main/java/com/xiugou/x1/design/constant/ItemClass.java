/**
 *
 */
package com.xiugou.x1.design.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YY 物品类别
 */
public enum ItemClass {
    ITEM(1), // 道具类，存在于道具背包中
    NUMBER(2), // 数值类
    EXP(3), // 经验类
    OBJECT(4), // 实物类，如装备，具有实体数据的
    ;

    private static Map<Integer, ItemClass> map = new HashMap<>();

    static {
        for (ItemClass itemClass : ItemClass.values()) {
            map.put(itemClass.getValue(), itemClass);
        }
    }

    public static ItemClass valueOf(int type) {
        return map.get(type);
    }

    private final int value;

    private ItemClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
