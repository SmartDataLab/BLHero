/**
 * 
 */
package com.xiugou.x1.game.server.module.recharge.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.module.RechargeProductCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.bag.RechargeProductCfgForm;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class RechargeProductCfgApi {
	
	@Autowired
	private RechargeProductCache rechargeProductCache;
	
	@RequestMapping(value = GameApi.rechargeProductCfgUpdate)
	@ResponseBody
	public String update() {
		List<RechargeProductCfgForm> list = new ArrayList<>();
		for(RechargeProductCfg cfg : rechargeProductCache.all()) {
			RechargeProductCfgForm form = new RechargeProductCfgForm();
			form.setId(cfg.getId());
			form.setName(cfg.getName());
			form.setDesc(cfg.getDescribe());
			form.setMoney(cfg.getMoney());
			list.add(form);
		}
		
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(list);
		return response.result();
	}
}
