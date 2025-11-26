/**
 *
 */
package com.xiugou.x1.battle.config;

/**
 * @author YY
 *
 */
public class Attr {
    private int attrId;
    private long value;

    public Attr() {}
    public Attr(int attrId, long value) {
        this.attrId = attrId;
        this.value = value;
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
