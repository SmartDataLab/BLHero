/**
 * 
 */
package com.xiugou.x1.backstage.module.rechargeproduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.rechargeproduct.model.RechargeProductCfg;
import com.xiugou.x1.backstage.module.rechargeproduct.service.RechargeProductCfgService;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.bag.RechargeProductCfgForm;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class RechargeProductCfgController {

	@Autowired
	private RechargeProductCfgService rechargeProductCfgService;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("请求游戏充值商品配置数据")
	@RequestMapping(value = "/rechargeProductCfg/data.auth")
	@ResponseBody
	public PageData<RechargeProductCfg> data(PageQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return rechargeProductCfgService.query(querySet);
	}
	
	@ApiDocument("更新游戏充值商品配置数据")
	@RequestMapping(value = "/rechargeProductCfg/refresh.auth")
	@ResponseBody
	public void refresh() {
		GameServer gameServer = gameServerService.randomRunning();
		
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.rechargeProductCfgUpdate, Collections.emptyMap());

		List<RechargeProductCfgForm> list = GsonUtil.getList(serverResponse.getData(), RechargeProductCfgForm.class);
		
		List<RechargeProductCfg> insertList = new ArrayList<>();
		for(RechargeProductCfgForm form : list) {
			RechargeProductCfg cfg = new RechargeProductCfg();
			cfg.setId(form.getId());
			cfg.setName(form.getName());
			cfg.setDescribe(form.getDesc());
			cfg.setMoney(form.getMoney());
			insertList.add(cfg);
		}
		rechargeProductCfgService.deleteAll();
		rechargeProductCfgService.insertAll(insertList);
	}
}
