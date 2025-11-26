package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.strcut;

import org.gaming.tool.GsonUtil;

/**
 * @author yh
 * @date 2023/9/27
 * @apiNote
 */
public class XSLBGiftData {
    private int giftId;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }
    
    public static void main(String[] args) {
		System.out.println(GsonUtil.toJson(new XSLBGiftData()));
	}
}
