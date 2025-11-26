package com.xiugou.x1.game.server.module.dungeon.struct;

/**
 * @author yh
 * @date 2023/7/10
 * @apiNote 主线地下城完成情况数据
 */
public class DungeonMainData {
    private int dungeonId;  //地下城ID
    private int completeMapNum; //完成地图数量
    private boolean completeAll;//是否全部完成

    public int getDungeonId() {
        return dungeonId;
    }

    public void setDungeonId(int dungeonId) {
        this.dungeonId = dungeonId;
    }

    public int getCompleteMapNum() {
        return completeMapNum;
    }

    public void setCompleteMapNum(int completeMapNum) {
        this.completeMapNum = completeMapNum;
    }

    public boolean isCompleteAll() {
        return completeAll;
    }

    public void setCompleteAll(boolean completeAll) {
        this.completeAll = completeAll;
    }
}
