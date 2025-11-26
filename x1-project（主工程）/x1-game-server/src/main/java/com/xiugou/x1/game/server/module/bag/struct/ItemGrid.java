package com.xiugou.x1.game.server.module.bag.struct;

/**
 * @author yh
 * @date 2023/5/30
 * @apiNote
 */
public class ItemGrid {
    /**
     * 道具id
     */
    private int itemId;
    /**
     * 拥有数量
     */
    private long num;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
