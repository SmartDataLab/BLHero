/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.giftcode.model.GiftCodeCfg;
import com.xiugou.x1.backstage.module.giftcode.service.GiftCodeCfgService;
import com.xiugou.x1.backstage.module.godfinger.struct.GodFingerQuery;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.giftcode.GiftCodeCfgForm;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class GiftCodeCfgController {

	@Autowired
	private GiftCodeCfgService giftCodeCfgService;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("请求游戏兑换码礼包配置数据")
	@RequestMapping(value = "/giftCodeCfg/data.auth")
	@ResponseBody
	public PageData<GiftCodeCfg> data(GodFingerQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return giftCodeCfgService.query(querySet);
	}
	
	@ApiDocument("更新游戏兑换码礼包配置数据")
	@RequestMapping(value = "/giftCodeCfg/refresh.auth")
	@ResponseBody
	public void refresh() {
		GameServer gameServer = gameServerService.randomRunning();
		
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.giftCodeUpdate, Collections.emptyMap());

		List<GiftCodeCfgForm> list = GsonUtil.getList(serverResponse.getData(), GiftCodeCfgForm.class);
		
		List<GiftCodeCfg> insertList = new ArrayList<>();
		for(GiftCodeCfgForm form : list) {
			GiftCodeCfg cfg = new GiftCodeCfg();
			cfg.setId(form.getId());
			cfg.setName(form.getName());
			insertList.add(cfg);
		}
		giftCodeCfgService.deleteAll();
		giftCodeCfgService.insertAll(insertList);
	}
	
	@ApiDocument("游戏兑换码礼包配置下拉菜单数据")
	@RequestMapping(value = "/giftCodeCfg/options.do")
	@ResponseBody
	public DropDownOptions options() {
		List<GiftCodeCfg> list = giftCodeCfgService.getAll();
		
		DropDownOptions options = new DropDownOptions();
		for(GiftCodeCfg cfg : list) {
			options.addOption(cfg.getId(), cfg.getName());
		}
		return options;
	}
}
