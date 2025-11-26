/**
 *
 */
package com.xiugou.x1.backstage.module.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.clientversion.model.ClientVersion;
import com.xiugou.x1.backstage.module.clientversion.service.ClientVersionService;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.struct.ServerType;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.module.player.struct.PlayerQuery;
import com.xiugou.x1.backstage.module.player.vo.PlayerInfoVo;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.bag.BagForm;
import pojo.xiugou.x1.pojo.module.hero.HeroForm;
import pojo.xiugou.x1.pojo.module.player.form.PlayerDetailForm;
import pojo.xiugou.x1.pojo.module.player.form.PlayerTable;
import pojo.xiugou.x1.pojo.module.player.form.PlayerTable.PlayerData;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class PlayerController {

	private static Logger logger = LoggerFactory.getLogger(PlayerController.class);
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private ClientVersionService clientVersionService;

	@ApiDocument("请求玩家数据")
	@RequestMapping(value = "/player/data.auth")
	@ResponseBody
	public PageData<PlayerInfoVo> data(PlayerQuery query) {
		long channelId = gameChannelService.currChannel();
		List<ClientVersion> clientVersions = clientVersionService.getEntityList(channelId);
		ClientVersion currVersion = null;
		for(ClientVersion clientVersion : clientVersions) {
			if(clientVersion.getServerType() != ServerType.NORMAL.getValue()) {
				continue;
			}
			if(currVersion == null) {
				currVersion = clientVersion;
				continue;
			}
			if(clientVersion.getVersionCode() > currVersion.getVersionCode()) {
				currVersion = clientVersion;
			}
		}
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		if (query.getPlayerId() > 0) {
			querySet.addCondition("id = ?", query.getPlayerId());
		}
		if (query.getName() != null && !"".equals(query.getName())) {
			querySet.addCondition("nick like ?", "%" + query.getName() + "%");
		}
		if (query.getOpenId() != null && !"".equals(query.getOpenId())) {
			querySet.addCondition("open_id like ?", "%" + query.getOpenId() + "%");
		}
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();

		PageData<Player> pageData = playerService.query(querySet);
		
		Map<Long, String> channelNames = new HashMap<>();
		Map<String, String> serverNames = new HashMap<>();
		
		PageData<PlayerInfoVo> result = new PageData<>();
		for(int i = 0; i < pageData.getData().size(); i++) {
			Player player = pageData.getData().get(i);
			PlayerInfoVo vo = new PlayerInfoVo();
			vo.setId(i + 1);
			String channelName = channelNames.get(player.getChannelId());
			if(channelName == null) {
				GameChannel gameChannel = gameChannelService.getEntity(player.getChannelId());
				if(gameChannel != null) {
					channelName = gameChannel.getName();
				} else {
					channelName = "";
				}
				channelNames.put(player.getChannelId(), channelName);
			}
			vo.setChannelInfo(channelName + "[" + player.getChannelId() + "]");
			
			String serverName = serverNames.get(player.getChannelId() + "_" + player.getServerId());
			if(serverName == null) {
				try {
					GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
					if(gameServer != null) {
						serverName = gameServer.getName();
					} else {
						serverName = "";
					}
				} catch (Exception e) {
					serverName = "";
				}
				serverNames.put(player.getChannelId() + "_" + player.getServerId(), serverName);
			}
			vo.setServerInfo(player.getServerId() + "-" + serverName);
			vo.setPlayerId(player.getId());
			vo.setOpenId(player.getOpenId());
			vo.setNick(player.getNick());
			vo.setHead(player.getHead());
			vo.setSex(player.getSex());
			vo.setLevel(player.getLevel());
			vo.setGold(player.getGold());
			vo.setDiamond(player.getDiamond());
			vo.setFighting(player.getFighting());
			vo.setOnline(player.isOnline());
			vo.setDailyOnline(player.getDailyOnline());
			vo.setBornTime(player.getBornTime());
			vo.setLastLoginTime(player.getLastLoginTime());
//			https://www.xiugoux1.cn/x1desktop/index.html?online=1&openId=2891954627&channelId=2&gameVersion=10002
			if(currVersion != null) {
				try {
					String quickUrl = String.format(currVersion.getQuickUrl(), player.getOpenId(), player.getChannelId(), currVersion.getVersionCode());
					vo.setQuickUrl(quickUrl);
				} catch (Exception e) {
					vo.setQuickUrl("");
				}
			} else {
				vo.setQuickUrl("");
			}
			result.getData().add(vo);
		}
		result.setCount(pageData.getCount());
		return result;
	}

	@ApiDocument("请求玩家数据详情")
	@RequestMapping(value = "/player/detail.auth")
	@ResponseBody
	public PageData<PlayerDetailForm> detail(@RequestParam("playerId") long playerId) {
		Player player = playerService.getById(playerId);
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, playerId);

		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());

		Map<String, Object> parameter = new HashMap<>();
		parameter.put("playerId", playerId);
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.playerDetails, parameter);
		Asserts.isTrue(serverResponse != null, TipsCode.PLAYER_NOT_EXIST, playerId);
		
		
		PlayerDetailForm playerForm = GsonUtil.parseJson(serverResponse.getData(), PlayerDetailForm.class);
		playerForm.setOldPlayer(playerService.isOld(player.getOpenId(), player.getServerId()));
		
		PageData<PlayerDetailForm> pageData = new PageData<>();
		pageData.setCount(1);
		pageData.setData(Collections.singletonList(playerForm));
		return pageData;
	}

	@ApiDocument("请求玩家英雄数据")
	@RequestMapping(value = "/player/hero.auth")
	@ResponseBody
	public PageData<HeroForm> detailHero(@RequestParam("playerId") long playerId) {
		Player player = playerService.getById(playerId);
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, playerId);

		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
		
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("playerId", playerId);
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.playerDetailsHeroes, parameter);

		List<HeroForm> list = GsonUtil.getList(serverResponse.getData(), HeroForm.class);
		PageData<HeroForm> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(list);
		return pageData;
	}

	@ApiDocument("请求玩家背包数据")
	@RequestMapping(value = "/player/bag.auth")
	@ResponseBody
	public PageData<BagForm> detailBag(@RequestParam("playerId") long playerId) {
		Player player = playerService.getById(playerId);
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, playerId);
		
		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
		
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("playerId", playerId);
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.playerDetailsBag, parameter);

		List<BagForm> list = GsonUtil.getList(serverResponse.getData(), BagForm.class);
		PageData<BagForm> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(list);
		return pageData;
	}
	
	@ApiDocument("上报玩家数据，给游戏服的接口")
	@RequestMapping(value = GameApi.playerReport)
	@ResponseBody
	public void playerReport(@RequestBody PlayerTable playerTable) {
		List<Player> updateList = new ArrayList<>();
		for(PlayerData playerData : playerTable.getDatas()) {
			Player player = new Player();
			player.setId(playerData.getPlayerId());
			player.setPlatformId(playerData.getPlatformId());
			player.setChannelId(playerData.getChannelId());
			player.setOpenId(playerData.getOpenId());
			player.setServerId(playerData.getServerId());
			player.setNick(playerData.getNick());
			player.setHead(playerData.getHead());
			player.setSex(playerData.getSex());
			player.setLevel(playerData.getLevel());
			player.setGold(playerData.getGold());
			player.setDiamond(playerData.getDiamond());
			player.setOnline(playerData.isOnline());
			player.setDailyOnline(playerData.getDailyOnline());
			player.setBornTime(LocalDateTimeUtil.ofEpochMilli(playerData.getBornTime()));
			player.setLastLoginTime(LocalDateTimeUtil.ofEpochMilli(playerData.getLastLoginTime()));
			player.setFighting(playerData.getFighting());
			player.setRecharge(playerData.getRecharge());
			updateList.add(player);
		}
		playerService.insertUpdate(updateList);
		logger.info("收到服务器{}上报的玩家数量{}", playerTable.getServerId(), playerTable.getDatas().size());
	}
}
