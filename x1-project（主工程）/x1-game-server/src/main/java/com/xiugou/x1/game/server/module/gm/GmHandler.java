/**
 * 
 */
package com.xiugou.x1.game.server.module.gm;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.common.StringKeyCmdInvoker;
import org.gaming.fakecmd.side.game.PlayerGmCmdRegister;
import org.gaming.prefab.exception.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

import pb.xiugou.x1.protobuf.gm.Gm.GmRequest;
import pb.xiugou.x1.protobuf.gm.Gm.GmResponse;

/**
 * @author YY
 *
 */
@Controller
public class GmHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GmHandler.class);
	
	@Autowired
	private ApplicationSettings applicationSettings;

	@PlayerCmd
	public GmResponse gm(PlayerContext playerContext, GmRequest msg) {
		Asserts.isTrue(applicationSettings.isGameDebugMode(), TipsCode.GM_NOT_AVAILABLE);
		
		logger.info("玩家{}调用GM命令{}开始", playerContext.getId(), msg.getValue());
		String[] values = msg.getValue().split(" ");
		
		StringKeyCmdInvoker gmCmdInvoker = PlayerGmCmdRegister.INS.getInvoker(values[0]);
		
		String[] params = new String[values.length - 1];
		for(int i = 1; i < values.length; i++) {
			params[i - 1] = values[i];
		}
		
		GmResponse.Builder builder = GmResponse.newBuilder();
		try {
			gmCmdInvoker.getMethod().invoke(gmCmdInvoker.getBean(), playerContext, params);
			builder.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			builder.setSuccess(false);
		}
		logger.info("玩家{}调用GM命令{}退出，结果{}", playerContext.getId(), msg.getValue(), builder.getSuccess());
		return builder.build();
	}
}
