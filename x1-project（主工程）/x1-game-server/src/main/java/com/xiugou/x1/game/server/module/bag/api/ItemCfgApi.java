/**
 * 
 */
package com.xiugou.x1.game.server.module.bag.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.bag.ItemForm;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 *
 */
@Controller
public class ItemCfgApi {
	
	@Autowired
	private ItemCache itemCache;
	
	@RequestMapping(value = GameApi.itemCfgUpdate)
	@ResponseBody
	public String update() {
		List<ItemForm> list = new ArrayList<>();
		for(ItemCfg itemCfg : itemCache.all()) {
			ItemForm form = new ItemForm();
			form.setId(itemCfg.getId());
			form.setName(itemCfg.getName());
			list.add(form);
		}
		
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(list);
		return response.result();
	}
}
