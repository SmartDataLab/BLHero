/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.player.model.ForbidList;
import com.xiugou.x1.backstage.module.player.service.ForbidListService;
import com.xiugou.x1.backstage.module.player.struct.WhiteListQuery;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.player.form.ForbidTable;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class ForbidListController {

	private static Logger logger = LoggerFactory.getLogger(ForbidListController.class);
	
	@Autowired
	private ForbidListService forbidListService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameChannelService gameChannelService;
	
	
	@ApiDocument("请求封禁账号数据")
	@RequestMapping(value = "/forbidList/data.auth")
	@ResponseBody
	public PageData<ForbidList> data(WhiteListQuery query) {
		long currChannel = gameChannelService.currChannel();
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", currChannel);
		if(query.getOpenId() != null && !"".equals(query.getOpenId())) {
			querySet.addCondition("open_id like ?", "%" + query.getOpenId() + "%");
		}
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return forbidListService.query(querySet);
	}
	
	@ApiDocument("添加封禁账号")
	@RequestMapping(value = "/forbidList/add.authw")
	@ResponseBody
	public ForbidList add(@RequestParam("openId") String openId) {
		RoleContext roleContext = userService.getCurrUser();
		long currChannel = gameChannelService.currChannel();
		
		ForbidList forbidList = new ForbidList();
		forbidList.setChannelId(currChannel);
		forbidList.setOpenId(openId);
		forbidList.setUserId(roleContext.getId());
		forbidList.setUserName(roleContext.getName());
		forbidListService.insert(forbidList);
		
		return forbidList;
	}
	
	@ApiDocument("删除封禁账号")
	@RequestMapping(value = "/forbidList/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("id") long id) {
		ForbidList forbidList = forbidListService.getById(id);
		forbidListService.delete(forbidList);
	}
	
	@ApiDocument("发送封禁账号到渠道下各游戏服")
	@RequestMapping(value = "/forbidList/send.auth")
	@ResponseBody
	public void send() {
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", gameChannelService.currChannel());
		PageData<ForbidList> pageData = forbidListService.query(querySet);
		
		ForbidTable forbidTable = new ForbidTable();
		for(ForbidList forbidList : pageData.getData()) {
			forbidTable.getOpenIds().add(forbidList.getOpenId());
		}
		
		List<GameServer> servers = gameChannelService.getServers();
		for(GameServer server : servers) {
			ServerResponse serverResponse = X1HttpUtil.jsonPost(server, GameApi.playerForbidList, forbidTable);
			if(serverResponse == null || serverResponse.getCode() != 0) {
				logger.error("同步封禁账号列表到游戏服{}-{}-{}失败", server.getId(), server.getServerId(), server.getName());
			} else {
				logger.info("同步封禁账号列表到游戏服{}-{}-{}成功", server.getId(), server.getServerId(), server.getName());
			}
		}
	}
}
