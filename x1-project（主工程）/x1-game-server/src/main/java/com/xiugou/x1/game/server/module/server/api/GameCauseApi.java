/**
 * 
 */
package com.xiugou.x1.game.server.module.server.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.constant.GameCause;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;
import pojo.xiugou.x1.pojo.module.server.form.GameCauseForm;

/**
 * @author YY
 *
 */
@Controller
public class GameCauseApi {
	
	@RequestMapping(value = GameApi.gameCauseUpdate)
	@ResponseBody
	public String update() {
		List<GameCauseForm> list = new ArrayList<>();
		for(GameCause cause : GameCause.values()) {
			GameCauseForm form = new GameCauseForm();
			form.setId(cause.getCode());
			form.setName(cause.getDesc());
			list.add(form);
		}
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(list);
		return response.result();
	}
}
