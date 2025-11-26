/**
 *
 */
package com.xiugou.x1.game.server.module.formation;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.formation.Formation.FormationInfoResponse;
import pb.xiugou.x1.protobuf.formation.Formation.FormationSaveRequest;
import pb.xiugou.x1.protobuf.formation.Formation.FormationSaveResponse;

/**
 * @author hy
 */
@Controller
public class FormationHandler extends AbstractModuleHandler {

    @Autowired
    private FormationService formationService;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.BASE;
	}
    
    @Override
	public boolean needDailyPush() {
		return false;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        FormationInfoResponse.Builder response = FormationInfoResponse.newBuilder();
        for (Formation formation : formationService.getEntities(playerId)) {
            response.addFormations(formationService.build(formation));
        }
        playerContextManager.push(playerId, FormationInfoResponse.Proto.ID, response.build());
    }
    
    @PlayerCmd
    public FormationSaveResponse save(PlayerContext playerContext, FormationSaveRequest request) {
    	BattleType battleType = BattleType.valueOf(request.getType());
    	FormationResult formationResult = formationService.buildFormationResult(playerContext.getId(), request.getMainHero(), request.getPosListList());
    	
    	Formation formation = formationService.saveFormation(playerContext.getId(), battleType, formationResult.getMainHero(), formationResult.getList());
    	
    	FormationSaveResponse.Builder response = FormationSaveResponse.newBuilder();
    	response.setFormation(formationService.build(formation));
    	return response.build();
    }
}
