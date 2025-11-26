/**
 *
 */
package com.xiugou.x1.game.server.module.mainline.struct;

import java.util.HashMap;

/**
 * @author YY
 *
 */
public class SceneOpening {
    private int id;
    //缴纳进度
    private HashMap<Integer, Long> payProgess = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, Long> getPayProgess() {
        return payProgess;
    }

    public void setPayProgess(HashMap<Integer, Long> payProgess) {
        this.payProgess = payProgess;
    }
}
