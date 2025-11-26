/**
 * 
 */
package com.xiugou.x1.game.server.module.goldenpig;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.goldenpig.model.GoldenPig;
import com.xiugou.x1.game.server.module.goldenpig.service.GoldenPigService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.goldenpig.GoldenPig.GoldenPigInfoResponse;

/**
 * @author YY
 *
 */
@Controller
public class GoldenPigHandler extends AbstractModuleHandler {

	@Autowired
	private GoldenPigService goldenPigService;

	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		GoldenPig goldenPig = goldenPigService.getEntity(playerId);
		
		GoldenPigInfoResponse.Builder response = GoldenPigInfoResponse.newBuilder();
		response.setChallengeNum(goldenPig.getChallengeNum());
		response.setMaxStage(goldenPig.getMaxStage());
		response.setDailyTime(LocalDateTimeUtil.toEpochMilli(goldenPig.getDailyTime()));
		playerContextManager.push(playerId, GoldenPigInfoResponse.Proto.ID, response.build());
	}
}
