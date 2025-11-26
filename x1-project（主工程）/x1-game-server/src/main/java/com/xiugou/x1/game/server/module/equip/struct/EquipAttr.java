package com.xiugou.x1.game.server.module.equip.struct;

/**
 * @author yh
 * @date 2023/7/4
 * @apiNote
 */
public class EquipAttr {
    private int id; //附属性表ID
    private int attrId;
    private long value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public int getAttrId() {
        return attrId;
    }

    public void setAttrId(int attrId) {
        this.attrId = attrId;
    }
}
