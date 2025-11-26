/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.tool.LocalDateTimeUtil;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GamePlatform;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GamePlatformService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.vo.GameServerDetailVo;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class ServerOpenController {

	private static Logger logger = LoggerFactory.getLogger(ServerOpenController.class);
	
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private UserService userService;
	@Autowired
	private GamePlatformService gamePlatformService;
	
	@ApiDocument("请求服务器开服数据")
	@RequestMapping(value = "/serverOpen/data.auth")
	@ResponseBody
	public PageData<GameServerDetailVo> data(PageQuery query) {
		long currChannel = gameChannelService.currChannel();
		GameChannel gameChannel = gameChannelService.getEntity(currChannel);
		
		GamePlatform gamePlatform = gamePlatformService.getEntity(gameChannel.getPlatformId());
		
		List<GameServer> serverList = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for(GameServer gameServer : gameServerService.getEntities()) {
			if(gameServer.getPlatformId() != gamePlatform.getId()) {
				continue;
			}
			if(gameServer.getRealOpenTime().isBefore(now)) {
				continue;
			}
			serverList.add(gameServer);
		}
		SortUtil.sortInt(serverList, GameServer::getId);
		List<GameServer> page = PageUtil.pageNDesc(serverList, query.getPage(), query.getLimit());
		
		List<GameServerDetailVo> list = new ArrayList<>();
		for(GameServer gameServer : page) {
			list.add(new GameServerDetailVo(gameServer));
		}
		
		PageData<GameServerDetailVo> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(list);
		return pageData;
	}
	
	@ApiDocument("设置服务器预期开服时间")
	@RequestMapping(value = "/serverOpen/editOpenTime.authw")
	@ResponseBody
	public GameServerDetailVo editOpenTime(@RequestParam("serverUid") int serverUid, @RequestParam("openTime") long openTime) {
		GameServer gameServer = gameServerService.getEntity(serverUid);
		Asserts.isTrue(gameServer != null, TipsCode.GAME_SERVER_MISS, serverUid);
		Asserts.isTrue(gameServer.getOpenTime().isAfter(LocalDateTime.now()), TipsCode.SERVER_OPENED, serverUid);
		Asserts.isTrue(gameServer.getRealOpenTime().isAfter(LocalDateTime.now()), TipsCode.SERVER_OPENED, serverUid);
		
		LocalDateTime openTime0 = LocalDateTimeUtil.ofEpochMilli(openTime);
		Asserts.isTrue(openTime0.isAfter(LocalDateTime.now()), TipsCode.SERVER_OPENTIME_NEED_FUTURE);
		
		RoleContext roleContext = userService.getCurrUser();
		logger.info("{}-{}操作修改服务器开服时间为{}，目标服务器是{}-{}-{}", roleContext.getId(), roleContext.getName(), openTime0,
				gameServer.getId(), gameServer.getServerId(), gameServer.getName());
		
		gameServer.setOpenTime(openTime0);
		gameServer.setRealOpenTime(openTime0);
		//先设置为未处理
		gameServer.setSendOpenStatus(0);
		gameServerService.update(gameServer);
		
		//发送给服务器
		Map<String, Object> map = new HashMap<>();
		map.put("openTime", openTime);
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.serverSetOpenTime, map);
		if(serverResponse != null && serverResponse.getCode() == 0) {
			gameServer.setSendOpenStatus(1);
		} else {
			gameServer.setSendOpenStatus(2);
		}
		gameServerService.update(gameServer);
		
		return new GameServerDetailVo(gameServer);
	}
	
	@ApiDocument("立刻开服")
	@RequestMapping(value = "/serverOpen/openNow.authw")
	@ResponseBody
	public GameServerDetailVo openNow(@RequestParam("serverUid") int serverUid) {
		GameServer gameServer = gameServerService.getEntity(serverUid);
		Asserts.isTrue(gameServer != null, TipsCode.GAME_SERVER_MISS, serverUid);
		Asserts.isTrue(gameServer.getOpenTime().isAfter(LocalDateTime.now()), TipsCode.SERVER_OPENED, serverUid);
		
		RoleContext roleContext = userService.getCurrUser();
		logger.info("{}-{}操作立刻开服，目标服务器是{}-{}-{}", roleContext.getId(), roleContext.getName(), gameServer.getId(),
				gameServer.getServerId(), gameServer.getName());
		
		gameServer.setSendOpenStatus(0);
		gameServer.setRealOpenTime(LocalDateTime.now());
		gameServer.setHide(false);
		gameServer.setRecommend(true);
		
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.serverOpenNow, Collections.emptyMap());
		if(serverResponse != null && serverResponse.getCode() == 0) {
			gameServer.setSendOpenStatus(1);
		} else {
			gameServer.setSendOpenStatus(2);
		}
		gameServerService.update(gameServer);
		
		return new GameServerDetailVo(gameServer);
	}
}
