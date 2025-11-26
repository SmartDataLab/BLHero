/**
 * 
 */
package com.xiugou.x1.game.server.module.player;

import org.gaming.db.usecase.SlimDao;
import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.PlayerLevelCache;
import com.xiugou.x1.design.module.autogen.PlayerLevelAbstractCache.PlayerLevelCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.bag.Bag.ThingChangeMessage;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbPlayerExp;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThing;

/**
 * @author YY
 *
 */
@Controller
public class PlayerGmHandler {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerLevelCache playerLevelCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	@PlayerGmCmd(command = "LEVEL_UP")
    public void levelUp(PlayerContext playerContext, String[] params) {
        Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);

        int level = Integer.parseInt(params[0]);
        Player player = playerService.getEntity(playerContext.getId());
        
        if(level > player.getLevel()) {
        	long needExp = 0;
            for(int i = player.getLevel(); i < level; i++) {
            	PlayerLevelCfg playerLevelCfg = playerLevelCache.getOrNull(i);
            	if(playerLevelCfg != null) {
            		needExp += playerLevelCfg.getExp();
            	}
            }
            thingService.add(playerContext.getId(), RewardThing.of(ItemType.EXP.getThingId(), needExp), GameCause.GM);
        } else {
        	player.setLevel(level);
            playerService.update(player);
            
            PbPlayerExp.Builder pbPlayerExp = PbPlayerExp.newBuilder();
            pbPlayerExp.setNewLevel(level);
            pbPlayerExp.setOldLevel(level);
            pbPlayerExp.setCurrExp(player.getExp());
            
            PbThing.Builder pbThing = PbThing.newBuilder();
            pbThing.setIdentity(ItemType.EXP.getThingId());
            pbThing.setNum(0);
            pbThing.setData(pbPlayerExp.build().toByteString());
            
            
            ThingChangeMessage.Builder message = ThingChangeMessage.newBuilder();
            message.addThings(pbThing.build());
            playerContextManager.push(playerContext.getId(), ThingChangeMessage.Proto.ID, message.build());
        }
    }
	
	@PlayerGmCmd(command = "PRINT_CACHE")
	public void printCache(PlayerContext playerContext, String[] params) {
		if(params.length >= 1) {
			String className = params[0];
			SlimDao.printCacheRepository(className);
		}
		SlimDao.printCacheNum();
	}
}
