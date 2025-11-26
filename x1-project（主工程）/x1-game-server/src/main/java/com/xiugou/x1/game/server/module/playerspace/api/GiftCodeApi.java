/**
 * 
 */
package com.xiugou.x1.game.server.module.playerspace.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.module.GiftCodeCache;
import com.xiugou.x1.design.module.autogen.GiftCodeAbstractCache.GiftCodeCfg;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.giftcode.GiftCodeCfgForm;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class GiftCodeApi {
	
	@Autowired
	private GiftCodeCache giftCodeCache;
	
	@RequestMapping(value = GameApi.giftCodeUpdate)
	@ResponseBody
	public String update() {
		List<GiftCodeCfgForm> list = new ArrayList<>();
		for(GiftCodeCfg giftCodeCfg : giftCodeCache.all()) {
			GiftCodeCfgForm form = new GiftCodeCfgForm();
			form.setId(giftCodeCfg.getId());
			form.setName(giftCodeCfg.getName());
			list.add(form);
		}
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(list);
		return response.result();
	}
}
