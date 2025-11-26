package com.xiugou.x1.game.server.module.firstRecharge;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.firstRecharge.model.FirstRecharge;
import com.xiugou.x1.game.server.module.firstRecharge.service.FirstRechargeService;
import com.xiugou.x1.game.server.module.firstRecharge.struct.FirstRechargeData;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Controller
public class FirstRechargeGmHandler {
    @Autowired
    private FirstRechargeService firstRechargeService;
    
    /**
     * 修改首充状态
     * */
    @PlayerGmCmd(command = "FIRST_RECHARGE_NEXT")
    public void fistRecharge(PlayerContext playerContext, String[] params) {
        FirstRecharge firstRecharge = firstRechargeService.getEntity(playerContext.getId());
        for(FirstRechargeData rechargeData : firstRecharge.getRechargeDatas().values()) {
        	rechargeData.setCanTakeDay(rechargeData.getCanTakeDay() + 1);
            if(rechargeData.getCanTakeDay() > 3) {
            	rechargeData.setCanTakeDay(1);
            	rechargeData.setRewardDay(0);
            }
        }
        System.out.println(GsonUtil.toJson(firstRecharge));
        firstRechargeService.update(firstRecharge);
    }
}
