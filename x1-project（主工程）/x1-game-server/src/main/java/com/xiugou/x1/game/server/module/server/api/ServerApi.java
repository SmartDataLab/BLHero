/**
 * 
 */
package com.xiugou.x1.game.server.module.server.api;

import java.time.LocalDateTime;

import org.gaming.tool.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.game.server.foundation.player.LogoutInternalMessage;
import com.xiugou.x1.game.server.foundation.player.LogoutType;
import com.xiugou.x1.game.server.foundation.player.PlayerActorPool;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class ServerApi {

	private static Logger logger = LoggerFactory.getLogger(ServerApi.class);
	
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	//忽略预期开服时间，直接开服，以当前时间为实际开服时间
	@RequestMapping(value = GameApi.serverOpenNow)
	@ResponseBody
	public String openNow() {
		ServerInfo serverInfo = serverInfoService.getMain();
		if(serverInfo.getOpenTime().isBefore(LocalDateTime.now()) || serverInfo.isOpened()) {
			ServerResponse response = new ServerResponse(ServerResponseCode.SERVER_OPENED);
			return response.result();
		}
		serverInfo.setOpenTime(LocalDateTime.now());
		serverInfoService.update(serverInfo);
		
		return ServerResponse.SUCCESES.result();
	}
	
	//设置预期开服时间
	@RequestMapping(value = GameApi.serverSetOpenTime)
	@ResponseBody
	public String setOpenTime(@RequestParam("openTime") long openTime) {
		ServerInfo serverInfo = serverInfoService.getMain();
		if(serverInfo.getOpenTime().isBefore(LocalDateTime.now()) || serverInfo.isOpened()) {
			ServerResponse response = new ServerResponse(ServerResponseCode.SERVER_OPENED);
			return response.result();
		}
		serverInfo.setOpenTime(LocalDateTimeUtil.ofEpochMilli(openTime));
		serverInfoService.update(serverInfo);
		return ServerResponse.SUCCESES.result();
	}
	
	//服务器维护
	@RequestMapping(value = GameApi.serverMaintain)
	@ResponseBody
	public String maintain(@RequestParam("maintain") boolean maintain) {
		ServerInfo serverInfo = serverInfoService.getMain();
		serverInfo.setMaintain(maintain);
		serverInfoService.update(serverInfo);
		
		if(maintain) {
			for(PlayerContext playerContext : playerContextManager.onlines()) {
				LogoutInternalMessage message = new LogoutInternalMessage();
				message.playerContext = playerContext;
				message.logoutType = LogoutType.MAINTAIN;
				PlayerActorPool.tell(message);
			}
		}
		logger.info("收到服务器维护通知{}", maintain);
		return ServerResponse.SUCCESES.result();
	}
	
	/**
	 * 提供给后台请求当前服务器是否可用的接口
	 * @return
	 */
	@RequestMapping(value = GameApi.serverTestRunning)
	@ResponseBody
	public String testRunning() {
		return ServerResponse.SUCCESES.result();
	}
}
