package com.xiugou.x1.game.server.module.home.struct;

import com.xiugou.x1.game.server.module.mainline.struct.SceneOpening;

/**
 * @author yh
 * @date 2023/8/4
 * @apiNote
 */
public class BuildingData {
    private int id;
    private boolean active;
    private SceneOpening opening = new SceneOpening();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SceneOpening getOpening() {
        return opening;
    }

    public void setOpening(SceneOpening opening) {
        this.opening = opening;
    }
}
