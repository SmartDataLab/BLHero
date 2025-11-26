package com.xiugou.x1.game.server.module.recruit.constant;

import org.gaming.prefab.exception.Asserts;

import com.xiugou.x1.design.constant.TipsCode;

/**
 * @author yh
 * @date 2023/6/20
 * @apiNote
 */
public enum RefreshType {
    GIVE_UP(1, "放弃当前召唤", true),
    USE_TICKET(2, "使用刷新券", true),
    USE_DIAMOND(3, "使用钻石", true),
    ADVERTISEMENT(4, "广告免费刷新", true),
    COMPLETED(5, "抽完三个英雄", false),
    SYSTEM_REFRESH(6, "到达系统时间刷新", false),
    ;
    private int code ;
    private String desc;
    //是否累计为手动刷新
    private boolean countHand;
    

    private RefreshType(int code, String desc, boolean countHand) {
        this.code = code;
        this.desc = desc;
        this.countHand = countHand;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    
    public static RefreshType valueOf(int code) {
    	for(RefreshType refreshType : RefreshType.values()) {
    		if(refreshType.code == code) {
    			return refreshType;
    		}
    	}
    	Asserts.isTrue(false, TipsCode.ERROR_PARAM, code);
    	return null;
    }

	public boolean isCountHand() {
		return countHand;
	}
}
