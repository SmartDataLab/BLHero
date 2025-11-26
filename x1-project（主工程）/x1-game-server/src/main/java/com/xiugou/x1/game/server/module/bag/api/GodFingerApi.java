/**
 * 
 */
package com.xiugou.x1.game.server.module.bag.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.game.server.foundation.player.PlayerActorPool;
import com.xiugou.x1.game.server.module.bag.message.GodFingerMessage;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.player.form.GodFingerTable;
import pojo.xiugou.x1.pojo.module.player.form.GodFingerTable.GodFingerData;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class GodFingerApi {

	private static Logger logger = LoggerFactory.getLogger(GodFingerApi.class);
	
	@RequestMapping(value = GameApi.godFingerSendGift)
	@ResponseBody
	public String sendGift(@RequestBody GodFingerTable table) {
		logger.info("收到金手指请求");
		for(GodFingerData data : table.getDatas()) {
			logger.info("发放玩家{}的金手指奖励", data.getPlayerId());
			PlayerActorPool.tell(GodFingerMessage.of(data.getPlayerId(), data.getMoney()));
		}
		return ServerResponse.SUCCESES.result();
	}
}
