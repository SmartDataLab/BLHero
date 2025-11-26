package com.xiugou.x1.game.server.module.recruit.struct;

import org.gaming.ruler.util.DropUtil.IDrop;

/**
 * @author yh
 * @date 2023/6/20
 * @apiNote
 */
public class RecruitData implements IDrop {
    private int identity;		//英雄标识
    private int fragment;		//碎片数量
    private int weight;     	//权重
    private long score;			//积分
    private int multiple;		//积分倍数
    private boolean crit;		//积分暴击标识
    private boolean take;			//是否已经抽中

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public int getDropId() {
        return identity;
    }

    @Override
    public int getDropRate() {
        return weight;
    }

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public boolean isCrit() {
		return crit;
	}

	public void setCrit(boolean crit) {
		this.crit = crit;
	}

	public boolean isTake() {
		return take;
	}

	public void setTake(boolean take) {
		this.take = take;
	}
}
