package com.xiugou.x1.game.server.module.recruit.struct;

import org.gaming.ruler.util.DropUtil;

/**
 * @author yh
 * @date 2023/6/20
 * @apiNote
 */
public class RandomData implements DropUtil.IDrop {
    private int identity;

    private int weight;


    public static RandomData of(int identity, int weight) {
        RandomData data = new RandomData();
        data.identity = identity;
        data.weight = weight;
        return data;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int getDropId() {
        return identity;
    }

    @Override
    public int getDropRate() {
        return weight;
    }
}
