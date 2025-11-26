/**
 *
 */
package com.xiugou.x1.design.constant;

import java.util.HashMap;
import java.util.Map;

import org.gaming.prefab.thing.IThingType;

/**
 * @author YY
 */
public enum ItemType implements IThingType {

    GOLD(1, "金币", 1, 2),
    DIAMOND(2, "钻石", 2, 2),
    EXP(3, "玩家经验", 3, 3),
    WOOD(4, "木", 4, 2),
    MEAT(5, "肉", 5, 2),
    MINE(6, "矿", 6, 2),
    VIP(7, "VIP经验",7, 2),
    RECRUIT_POINT(8, "招募积分", 8, 2),
    VILLAGE_POINT(9, "村庄积分", 9, 2),
    PURGATORY_POINTS(10, "炼狱积分", 10, 2),
    DAY_POINT(11, "日常活跃度", 101, 2),
    WEEK_POINT(12, "周常活跃度", 102, 2),
    ACHIEVEMENT_POINT(13, "成就点数", 103, 2),

    ZHAN_LING_EXP(80, "战令经验", 0, 3),	//废弃
    HERO(100, "英雄", 0, 4),
    ITEM(200, "道具", 0, 1),
    EQUIP(300, "装备", 0, 4),
    EVIL_CATALOG(400, "妖录", 0, 4),
    AUTO_USE_GIFT(500, "自动使用礼包", 0, 4),
    EVIL_SPEED_UP(600, "炼妖加速时间", 2006005, 3),
    
    ;

    private static Map<Integer, ItemType> map = new HashMap<>();

    static {
        for (ItemType itemType : ItemType.values()) {
            map.put(itemType.getType(), itemType);
        }
    }

    public static ItemType valueOf(int type) {
        return map.get(type);
    }

    private final int type;
    private final String desc;
    private final int thingId;
    //是不是数值类资源
    //1道具类，存在于道具背包中
    //2数值类
    //3经验类
    //4实物类，如装备，具有实体数据的
    private final int itemClass;

    private ItemType(int type, String desc, int thingId, int itemClass) {
        this.type = type;
        this.desc = desc;
        this.thingId = thingId;
        this.itemClass = itemClass;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getThingId() {
        return thingId;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public ItemClass getItemClass() {
        return ItemClass.valueOf(itemClass);
    }
}
