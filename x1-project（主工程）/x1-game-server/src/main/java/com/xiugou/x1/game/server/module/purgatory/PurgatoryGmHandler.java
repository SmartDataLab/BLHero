package com.xiugou.x1.game.server.module.purgatory;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;
import com.xiugou.x1.game.server.module.purgatory.service.PurgatoryService;

/**
 * @author yh
 * @date 2023/8/11
 * @apiNote
 */
@Controller
public class PurgatoryGmHandler {
    @Autowired
    private PurgatoryService purgatoryService;
    
    @PlayerGmCmd(command = "PURGATORY_SKIP_LEVEL")
    public void skipLevel(PlayerContext playerContext, String[] params){
        Purgatory purgatory = purgatoryService.getEntity(playerContext.getId());
        purgatory.setLevel(Integer.parseInt(params[0]));
        purgatoryService.update(purgatory);
    }
}
