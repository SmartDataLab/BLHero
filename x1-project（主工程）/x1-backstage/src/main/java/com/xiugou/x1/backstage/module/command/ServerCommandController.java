/**
 * 
 */
package com.xiugou.x1.backstage.module.command;

import java.util.HashMap;
import java.util.Map;

import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;

/**
 * @author YY
 *
 */
@Controller
public class ServerCommandController {

	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("发送服务器指令")
	@RequestMapping(value = "/serverCommand/send.authw")
	@ResponseBody
	public void send(@RequestParam("serverUid") long serverUid, @RequestParam("command") String command) {
		GameServer gameServer = gameServerService.getEntity(serverUid);
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("command", command);
		X1HttpUtil.formPost(gameServer, GameApi.serverRunCommand, parameter);
	}
}
