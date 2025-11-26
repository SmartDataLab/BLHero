package com.xiugou.x1.game.server.module.equip.constant;

/**
 * @author yh
 * @date 2023/6/13
 * @apiNote
 */
public enum EquipEnum {
    MAIN_WEAPON(1, "主武器"),
    SUB_WEAPON(2, "副武器"),
    HELMET(3, "头盔"),
    CLOTHES(4, "衣服"),
    TROUSERS(5, "裤子"),
    SHOES(6, "鞋子"),
    NECKLACE(7, "项链"),
    RING(8, "戒指"),
    PLACEHOLDER(9, "占位"),
    ;

    private int code;
    private String desc;

    private EquipEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
