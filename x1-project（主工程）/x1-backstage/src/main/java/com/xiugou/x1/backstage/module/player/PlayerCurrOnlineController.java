/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.player.form.PlayerOnlineTable;
import pojo.xiugou.x1.pojo.module.player.form.PlayerOnlineTable.PlayerOnlineData;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 * 当前在线玩家
 */
@Controller
public class PlayerCurrOnlineController {

	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private PlayerService playerService;
	
	@ApiDocument("请求当前在线玩家")
	@RequestMapping(value = "/playerCurrOnline/data.auth")
	@ResponseBody
	public PageData<PlayerOnlineData> data(@RequestParam("serverUid") long serverUid) {
		GameServer gameServer = gameServerService.getEntity(serverUid);
		
		PlayerOnlineTable table = null;
		try {
			table = X1HttpUtil.simplePost(gameServer, GameApi.playerOnlines, Collections.emptyMap(), PlayerOnlineTable.class);
		} catch (Exception e) {
		}
		
		PageData<PlayerOnlineData> pageData = new PageData<>();
		if(table != null) {
			pageData.setCount(table.getDatas().size());
			pageData.setData(table.getDatas());
		} else {
			pageData.setData(Collections.emptyList());
		}
		return pageData;
	}
	
	@ApiDocument("请求封禁玩家")
	@RequestMapping(value = "/playerCurrOnline/forbid.auth")
	@ResponseBody
	public void forbid(@RequestParam("playerId") long playerId, @RequestParam("forbidEndTime") long forbidEndTime) {
		Player player = playerService.getById(playerId);
		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
		
		Map<String, Object> map = new HashMap<>();
		map.put("playerId", playerId);
		map.put("forbidEndTime", forbidEndTime);
		ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.playerForbid, map);
		Asserts.isTrue(response != null && response.getCode() == 0, TipsCode.PLAYER_FORBID_FAIL);
	}


	@ApiDocument("踢玩家下线")
	@RequestMapping(value = "/playerCurrOnline/forcedOffLine.auth")
	@ResponseBody
	public void forcedOffLine(@RequestParam("playerId") long playerId) {
		Player player = playerService.getById(playerId);
		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
		
		Map<String, Object> map = new HashMap<>();
		map.put("playerId", playerId);
		ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.playerForcedOffline, map);
		Asserts.isTrue(response != null && response.getCode() == 0, TipsCode.PLAYER_FORCED_OFFLINE_FAIL);
	}
}
