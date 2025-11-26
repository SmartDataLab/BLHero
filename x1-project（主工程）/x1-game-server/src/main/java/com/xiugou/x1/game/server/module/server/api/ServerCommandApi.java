/**
 * 
 */
package com.xiugou.x1.game.server.module.server.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class ServerCommandApi {

	/**
	 * 执行一些命令
	 * @param command
	 * @return
	 */
	@RequestMapping(value = GameApi.serverRunCommand)
	@ResponseBody
	public String runCommand(@RequestParam("command") String command) {
		
		return ServerResponse.SUCCESES.result();
	}
}
