/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import org.gaming.backstage.PageData;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.player.model.WhiteList;
import com.xiugou.x1.backstage.module.player.service.WhiteListService;
import com.xiugou.x1.backstage.module.player.struct.WhiteListQuery;

/**
 * @author YY
 *
 */
@Controller
public class WhiteListController {
	
	@Autowired
	private WhiteListService whiteListService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求白名单数据")
	@RequestMapping(value = "/whiteList/data.auth")
	@ResponseBody
	public PageData<WhiteList> data(WhiteListQuery query) {
		long currChannel = gameChannelService.currChannel();
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", currChannel);
		if(query.getOpenId() != null && !"".equals(query.getOpenId())) {
			querySet.addCondition("open_id like ?", "%" + query.getOpenId() + "%");
		}
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return whiteListService.query(querySet);
	}
	
	@ApiDocument("添加白名单")
	@RequestMapping(value = "/whiteList/add.authw")
	@ResponseBody
	public WhiteList save(@RequestParam("openId") String openId, @RequestParam("remark") String remark) {
		RoleContext roleContext = userService.getCurrUser();
		long currChannel = gameChannelService.currChannel();
		GameChannel gameChannel = gameChannelService.getEntity(currChannel);
		
		WhiteList whiteList = new WhiteList();
		whiteList.setChannelId(currChannel);
		whiteList.setChannelName(gameChannel.getName());
		whiteList.setOpenId(openId);
		whiteList.setUserId(roleContext.getId());
		whiteList.setUserName(roleContext.getName());
		whiteList.setRemark(remark);
		whiteListService.insert(whiteList);
		
		return whiteList;
	}
	
	@ApiDocument("删除白名单")
	@RequestMapping(value = "/whiteList/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("id") long id) {
		WhiteList whiteList = whiteListService.getById(id);
		whiteListService.delete(whiteList);
	}
	
	@ApiDocument("是否为白名单，给游戏服的接口")
	@RequestMapping(value = "/api/isWhiteList")
	@ResponseBody
	public String isWhiteList(@RequestParam("channelId") int channelId, @RequestParam("openId") String openId) {
		QueryOptions options = new QueryOptions();
		options.put("channelId", channelId);
		options.put("openId", openId);
		WhiteList whiteList = whiteListService.query(options);
		
		if(whiteList != null) {
			return "true";
		} else {
			return "false";
		}
	}
}
