/**
 * 
 */
package com.xiugou.x1.backstage.module.item;

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
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.item.model.ItemCfg;
import com.xiugou.x1.backstage.module.item.service.ItemCfgService;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.bag.ItemForm;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class ItemCfgController {

	@Autowired
	private ItemCfgService itemCfgService;
	@Autowired
	private GameServerService gameServerService;
	
	
	@ApiDocument("请求游戏道具数据")
	@RequestMapping(value = "/itemCfg/data.auth")
	@ResponseBody
	public PageData<ItemCfg> data(PageQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return itemCfgService.query(querySet);
	}
	
	@ApiDocument("更新游戏道具数据")
	@RequestMapping(value = "/itemCfg/refresh.auth")
	@ResponseBody
	public void refresh() {
		GameServer gameServer = gameServerService.randomRunning();
		
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.itemCfgUpdate, Collections.emptyMap());

		List<ItemForm> list = GsonUtil.getList(serverResponse.getData(), ItemForm.class);
		
		List<ItemCfg> insertList = new ArrayList<>();
		for(ItemForm form : list) {
			ItemCfg itemCfg = new ItemCfg();
			itemCfg.setId(form.getId());
			itemCfg.setName(form.getName());
			insertList.add(itemCfg);
		}
		itemCfgService.deleteAll();
		itemCfgService.insertAll(insertList);
	}
	
	@ApiDocument("道具下拉菜单数据")
	@RequestMapping(value = "/itemCfg/options.do")
	@ResponseBody
	public DropDownOptions options() {
		List<ItemCfg> list = itemCfgService.getAll();
		
		DropDownOptions options = new DropDownOptions();
		for(ItemCfg itemCfg : list) {
			options.addOption(itemCfg.getId(), itemCfg.getName());
		}
		return options;
	}
}
