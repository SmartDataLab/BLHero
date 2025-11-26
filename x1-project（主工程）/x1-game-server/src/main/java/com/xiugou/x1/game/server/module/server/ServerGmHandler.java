/**
 * 
 */
package com.xiugou.x1.game.server.module.server;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

import pb.xiugou.x1.protobuf.gm.Gm.GmServerTimeMessage;

/**
 * @author YY
 *
 */
@Controller
public class ServerGmHandler {

	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private PlayerContextManager playerContextManager;

	/**
	 * 修改开服时间
	 * 
	 * @param playerContext
	 * @param params
	 */
	@PlayerGmCmd(command = "OPEN_SERVER_TIME")
	public void openTime(PlayerContext playerContext, String[] params) {
		Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);
		String timeStr = params[0];

		ServerInfo serverInfo = serverInfoService.getMain();
		long time = DateTimeUtil.stringToMillis(DateTimeUtil.YYYYMMDDHHMMSS, timeStr);

		serverInfo.setOpenTime(LocalDateTimeUtil.ofEpochMilli(time));
		serverInfoService.update(serverInfo);
	}

	/**
	 * 获取开服天数
	 * 
	 * @param playerContext
	 * @param params
	 */
	@PlayerGmCmd(command = "SERVER_TIME")
	public void getServerTime(PlayerContext playerContext, String[] params) {
		GmServerTimeMessage.Builder builder = GmServerTimeMessage.newBuilder();
		builder.setOpenServerDays(serverInfoService.getOpenedDay());
		builder.setServerTimestamp(DateTimeUtil.currMillis());
		playerContextManager.push(playerContext.getId(), GmServerTimeMessage.Proto.ID, builder.build());
	}
	
	@PlayerGmCmd(command = "EDIT_SERVER_TIME")
	public void editServerTime(PlayerContext playerContext, String[] params) {
		Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);
		String timeStr = params[0];
		long time = DateTimeUtil.stringToMillis(DateTimeUtil.YYYYMMDDHHMMSS, timeStr);
		long offsetMillis = time - System.currentTimeMillis();
		DateTimeUtil.setOffset(offsetMillis);
		LocalDateTimeUtil.setOffset(offsetMillis / 1000L);
	}
}
