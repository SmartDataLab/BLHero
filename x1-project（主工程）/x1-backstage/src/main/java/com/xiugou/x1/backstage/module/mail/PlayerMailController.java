/**
 * 
 */
package com.xiugou.x1.backstage.module.mail;

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
import com.xiugou.x1.backstage.module.mail.struct.PlayerMailQuery;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.mail.MailTable;
import pojo.xiugou.x1.pojo.module.mail.MailTable.MailData;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class PlayerMailController {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private GameServerService gameServerService;

	@ApiDocument("请求玩家邮件数据")
	@RequestMapping(value = "/playerMail/data.auth")
	@ResponseBody
	public PageData<MailData> data(PlayerMailQuery query) {
		Player player = playerService.getById(query.getPlayerId());
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, query.getPlayerId());

		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());

		Map<String, Object> parameter = new HashMap<>();
		parameter.put("playerId", player.getId());
		MailTable table = X1HttpUtil.simplePost(gameServer, GameApi.queryPlayerMails, parameter, MailTable.class);

		PageData<MailData> pageData = new PageData<>();
		pageData.setData(table.getDatas());
		pageData.setCount(table.getDatas().size());
		return pageData;
	}

	@ApiDocument("删除玩家邮件数据")
	@RequestMapping(value = "/playerMail/delete.authw")
	@ResponseBody
	public void deletePlayerMails(@RequestParam("channelId") long channelId, @RequestParam("playerId") long playerId,
			@RequestParam("mailId") long mailId) {
		Player player = playerService.getById(playerId);
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, playerId);

		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());

		Map<String, Object> parameter = new HashMap<>();
		parameter.put("playerId", player.getId());
		parameter.put("mailId", mailId);
		ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.deletePlayerMail, parameter);
		Asserts.isTrue(response != null && response.getCode() == 0, TipsCode.MAIL_DELETE_PLAYER_ERROR);
	}
}
