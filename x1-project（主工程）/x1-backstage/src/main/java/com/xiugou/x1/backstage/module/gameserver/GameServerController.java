/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.clientversion.model.ClientVersion;
import com.xiugou.x1.backstage.module.clientversion.service.ClientVersionService;
import com.xiugou.x1.backstage.module.gameserver.form.GameServerForm;
import com.xiugou.x1.backstage.module.gameserver.model.Bulletin;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GamePlatform;
import com.xiugou.x1.backstage.module.gameserver.model.GameRegion;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServerRuntime;
import com.xiugou.x1.backstage.module.gameserver.service.BulletinService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GamePlatformService;
import com.xiugou.x1.backstage.module.gameserver.service.GameRegionService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerRuntimeService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.struct.GameServerQuery;
import com.xiugou.x1.backstage.module.gameserver.struct.ServerType;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.gameserver.vo.GameRegionVo;
import com.xiugou.x1.backstage.module.gameserver.vo.GameServerDetailVo;
import com.xiugou.x1.backstage.module.gameserver.vo.GameServerVo;
import com.xiugou.x1.backstage.module.gameserver.vo.PlayerVo;
import com.xiugou.x1.backstage.module.gameserver.vo.ServerList;
import com.xiugou.x1.backstage.module.gameserver.vo.VersionInfo;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.form.ServerRuntimeForm;

/**
 * @author YY
 *
 */
@Controller
public class GameServerController {

	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private GameRegionService gameRegionService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private BulletinService bulletinService;
	@Autowired
	private GamePlatformService gamePlatformService;
	@Autowired
	private ClientVersionService clientVersionService;
	@Autowired
	private GameServerRuntimeService gameServerRuntimeService;
	
	
	@ApiDocument("请求服务器数据")
	@RequestMapping(value = "/gameServer/data.auth")
	@ResponseBody
	public PageData<GameServerDetailVo> data(GameServerQuery query) {
		List<GameServer> list = gameServerService.getEntities();
		Map<Integer, GameServerRuntime> runtimeMap = ListMapUtil.listToMap(gameServerRuntimeService.getEntities(), GameServerRuntime::getServerUid);
		
		List<GameServerDetailVo> pageList = new ArrayList<>();
		for(GameServer server : list) {
			if(query.getPlatformId() != 0 && server.getPlatformId() != query.getPlatformId()) {
				continue;
			}
			if(query.getServerId() != 0 && server.getServerId() != query.getServerId()) {
				continue;
			}
			GameServerRuntime runtime = runtimeMap.get(server.getId());
			pageList.add(new GameServerDetailVo(server, runtime));
		}
		Collections.sort(pageList, comparator);
		
		PageData<GameServerDetailVo> pageData = new PageData<>();
		pageData.setCount(pageList.size());
		pageData.setData(PageUtil.pageN(pageList, query.getPage(), query.getLimit()));
		return pageData;
	}
	
	public static Comparator<GameServerDetailVo> comparator = new Comparator<GameServerDetailVo>() {
		@Override
		public int compare(GameServerDetailVo o1, GameServerDetailVo o2) {
			int result = Long.compare(o1.getPlatformId(), o2.getPlatformId());
			if(result != 0) {
				return result;
			}
			return Integer.compare(o2.getServerId(), o1.getServerId());
		}
	};
	
	@ApiDocument("保存服务器数据，ID小于等于0时新增，大于0时修改")
	@RequestMapping(value = "/gameServer/save.authw")
	@ResponseBody
	public GameServerDetailVo saveData(GameServerForm form) {
		GamePlatform gamePlatform = gamePlatformService.getEntity(form.getPlatformId());
		Asserts.isTrue(gamePlatform != null, TipsCode.PLATFORM_MISS, form.getPlatformId());
		
		List<GameServer> servers = gameServerService.getEntities();
		Map<Integer, GameServer> serverMap = new HashMap<>();
		for(GameServer server : servers) {
			if(server.getPlatformId() != gamePlatform.getId()) {
				continue;
			}
			serverMap.put(server.getServerId(), server);
		}
		
		if(form.getId() <= 0) {
			GameServer currServer = serverMap.get(form.getServerId());
			Asserts.isTrue(currServer == null, TipsCode.PLATFORM_SERVER_REPEAT, form.getServerId());
		}
		
		GameServer gameServer = serverMap.get(form.getServerId());
		boolean insert = false;
		if(gameServer == null) {
			gameServer = new GameServer();
			gameServer.setPlatformId(gamePlatform.getId());
			gameServer.setPlatformName(gamePlatform.getName());
			gameServer.setOpenTime(LocalDateTime.of(2099, 12, 31, 23, 59));
			gameServer.setRealOpenTime(LocalDateTime.of(2099, 12, 31, 23, 59));
			gameServer.setServerId(form.getServerId());
			insert = true;
		}
		
		gameServer.setName(form.getName());
		gameServer.setSocketType(form.getSocketType());
		gameServer.setExternalIp(form.getExternalIp());
		gameServer.setInternalIp(form.getInternalIp());
		gameServer.setTcpPort(form.getTcpPort());
		gameServer.setHttpPort(form.getHttpPort());
		gameServer.setDbGameName(form.getDbGameName());
		gameServer.setDbLogName(form.getDbLogName());
		gameServer.setStatus(form.getStatus());
		gameServer.setRecommend(form.isRecommend());
		gameServer.setServerType(form.getServerType());
		gameServer.setHide(form.isHide());
		
		if(insert) {
			gameServerService.insert(gameServer);
		} else {
			gameServerService.update(gameServer);
		}
		//更新渠道服务器关系数据
		List<GameChannelServer> gameChannelServers = gameChannelServerService.getByServerUid(gameServer.getId());
		for(GameChannelServer gameChannelServer : gameChannelServers) {
			gameChannelServer.setRecommend(gameServer.isRecommend());
		}
		gameChannelServerService.updateAll(gameChannelServers);
		
		GameServerRuntime runtime = gameServerRuntimeService.getEntity(gameServer.getId());
		return new GameServerDetailVo(gameServer, runtime);
	}
	
	@ApiDocument("服务器下拉菜单数据")
	@RequestMapping(value = "/gameServer/options.do")
	@ResponseBody
	public DropDownOptions options() {
		List<GameServer> list = gameServerService.getEntities();
		
		DropDownOptions options = new DropDownOptions();
		for(GameServer gameServer : list) {
			options.addOption(gameServer.getId(),
					gameServer.getName() + "-" + gameServer.getServerId() + "(" + gameServer.getId() + ")");
		}
		return options;
	}
	
	@ApiDocument("根据当前渠道获取服务器下拉菜单数据")
	@RequestMapping(value = "/gameServer/optionsInCurrChannel.do")
	@ResponseBody
	public DropDownOptions optionsInCurrChannel() {
		long currChannel = gameChannelService.currChannel();
		
		DropDownOptions options = new DropDownOptions();
		GameChannel gameChannel = gameChannelService.getEntity(currChannel);
		if(gameChannel == null) {
			return options;
		}
		List<GameChannelServer> relations = gameChannelServerService.getEntityList(gameChannel.getId());
		for(GameChannelServer relation : relations) {
			options.addOption(relation.getServerUid(),
					relation.getServerName() + "-" + relation.getServerId() + "(" + relation.getServerUid() + ")");
		}
		return options;
	}
	
	/**
	 * http://192.168.1.5:9001/api/versionInfo?channelId=2&versionId=10001
	 * http://127.0.0.1:9001/api/versionInfo?channelId=2&versionId=10001
	 * https://2f895u5730.oicp.vip/api/versionInfo?channelId=2&versionId=10001
	 * @param channelId
	 * @param versionId
	 * @return
	 */
	@ApiDocument("请求客户端版本信息，给游戏客户端请求用的")
	@RequestMapping(value = "/api/versionInfo")
	@ResponseBody
	public VersionInfo versionInfo(@RequestParam("channelId") long channelId, @RequestParam("versionId") int versionId) {
		ClientVersion clientVersion = clientVersionService.getEntity(channelId, versionId);
		Asserts.isTrue(clientVersion != null, TipsCode.CLIENT_VERSION_CHANNEL_MISS, channelId, versionId);
		
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setRemoteUrl(clientVersion.getRemoteUrl());
		versionInfo.setResourceVersion(clientVersion.getResourceVersion());
		versionInfo.setPcResourceVersion(clientVersion.getPcResourceVersion());
		return versionInfo;
	}
	
	/**
	 * 微信请求服务器列表，http://120.79.34.46:10000/api/serversList?channelId=1&openId=AAA&versionId
	 * http://192.168.1.5:9001/api/serversList?channelId=1&openId=AAA&versionId=0
	 * https://www.xiugouchedui.cn/api/serversList?channelId=2&openId=45718870&versionId=10004
	 * http://127.0.0.1:9001/api/serversList?channelId=2&openId=yy2&versionId=10001
	 * https://2f895u5730.oicp.vip/api/serversList?channelId=2&openId=yy2&versionId=10001
	 * @param channelId
	 * @return
	 */
	@ApiDocument("请求服务器列表，给游戏客户端请求用的")
	@RequestMapping(value = "/api/serversList")
	@ResponseBody
	public ServerList serversList(@RequestParam("channelId") long channelId, @RequestParam("openId") String openId,
			@RequestParam(name = "versionId", required = false) Integer versionId) {
		GameChannel gameChannel = gameChannelService.getEntity(channelId);
		Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, channelId);
		
		Bulletin bulletin = bulletinService.getEntity(gameChannel.getBulletinId());
		
		ServerList serverList = new ServerList();
		serverList.setTitle(bulletin == null ? "" : bulletin.getTitle());
		serverList.setContent(bulletin == null ? "" : bulletin.getContent());
		
		if(versionId == null) {
			versionId = 0;
		}
		ClientVersion clientVersion = clientVersionService.getEntity(channelId, versionId);
		
		ServerType serverType = null;
		if(clientVersion != null) {
			if(clientVersion.getServerType() == ServerType.TEST.getValue()) {
				serverType = ServerType.TEST;
			} else if(clientVersion.getServerType() == ServerType.REVIEW.getValue()) {
				serverType = ServerType.REVIEW;
			} else if(clientVersion.getServerType() == ServerType.NORMAL.getValue()) {
				serverType = ServerType.NORMAL;
			} else {
				serverType = ServerType.TEST;
			}
		} else {
			serverType = ServerType.TEST;
		}
		
		//服务器列表包含两部分的数据，一部分是玩家创过号所登录的服务器，一部分是推荐服的服务器
		
		//我的区服
		List<Player> players = playerService.getByOpenId(channelId, openId);
		Collections.sort(players, LOGIN_SORTER);
		Set<Integer> serverIds = new HashSet<>();
		
		for(Player player : players) {
			GameServer server = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
			if(server.getServerType() != serverType.getValue()) {
				continue;
			}
			
			PlayerVo playerVo = new PlayerVo();
			playerVo.setServerId(player.getServerId());
			playerVo.setNick(player.getNick());
			playerVo.setLevel(player.getLevel());
			playerVo.setHead(player.getHead());
			serverList.getPlayers().add(playerVo);
			
			serverList.getServers().add(new GameServerVo(server));
			serverIds.add(server.getServerId());
		}
		
		//推荐区服
		List<GameServer> servers = gameChannelService.getRecommendServersUnderChannel(gameChannel.getId());
		SortUtil.sortInt(servers, GameServer::getServerId);
		
		GameServer recommendServer = null;
		for(GameServer server : servers) {
			if(server.getServerType() != serverType.getValue()) {
				continue;
			}
			if(serverIds.contains(server.getServerId())) {
				continue;
			}
			if(server.isHide()) {
				continue;
			}
			if(recommendServer == null) {
				recommendServer = server;
			} else {
				if(server.getServerId() > recommendServer.getServerId()) {
					recommendServer = server;
				}
			}
		}
		if(recommendServer != null) {
			GameServerVo gameServerVo = new GameServerVo(recommendServer);
			serverList.getServers().add(gameServerVo);
		}
		
		//大区列表
		List<GameRegion> regions = gameRegionService.getEntityList(channelId);
		for(GameRegion region : regions) {
			if(region.getServerType() != serverType.getValue()) {
				continue;
			}
			GameRegionVo gameRegionVo = new GameRegionVo();
			gameRegionVo.setId(region.getId());
			gameRegionVo.setName(region.getName());
			serverList.getRegions().add(gameRegionVo);
		}
		SortUtil.sort(serverList.getRegions(), GameRegionVo::getId);
		return serverList;
	}
	
	public static Comparator<Player> LOGIN_SORTER = new Comparator<Player>() {
		@Override
		public int compare(Player o1, Player o2) {
			int result = Integer.compare(LocalDateTimeUtil.toEpochSecond(o2.getLastLoginTime()), LocalDateTimeUtil.toEpochSecond(o1.getLastLoginTime()));
			if(result != 0) {
				return result;
			}
			return Integer.compare(o2.getLevel(), o1.getLevel());
		}
	};
	
	/**
	 * http://120.79.34.46:10000/api/serverInRegion?regionId=1
	 * @param regionId
	 * @return
	 */
	@ApiDocument("请求游戏大区下的服务器列表，给游戏客户端请求用的")
	@RequestMapping(value = "/api/serverInRegion")
	@ResponseBody
	public List<GameServerVo> serverInRegion(@RequestParam("regionId") long regionId) {
		GameRegion region = gameRegionService.getById(regionId);
		
		List<GameChannelServer> regionServers = gameChannelServerService.getEntityList(region.getChannelId());
		List<GameServerVo> list = new ArrayList<>();
		
		for(GameChannelServer regionServer : regionServers) {
			if(regionServer.getRegionId() != region.getRegionId()) {
				continue;
			}
			GameServer server = gameServerService.getEntity(regionServer.getServerUid());
			if(server.isHide()) {
				continue;
			}
			GameServerVo gameServerVo = new GameServerVo(server);
			list.add(gameServerVo);
		}
		SortUtil.sort(list, GameServerVo::getId);
		return list;
	}
	
	@ApiDocument("游戏服务器上报心跳")
	@RequestMapping(value = GameApi.heartBeat)
	@ResponseBody
	public void heartBeat2(@RequestBody ServerRuntimeForm form) {
		GameServer gameServer = gameServerService.getByPlatformAndServer(form.getPlatformId(), form.getServerId());
		if (gameServer == null) {
			return;
		}
		GameServerRuntime runtime = gameServerRuntimeService.getEntity(gameServer.getId());
		if(runtime == null) {
			runtime = new GameServerRuntime();
			runtime.setServerUid(gameServer.getId());
			gameServerRuntimeService.insert(runtime);
		}
		
		runtime.setRunning(form.isRunning());
		runtime.setHeartTime(LocalDateTime.now());
		runtime.setCreateNum(form.getPlayerNum());
		runtime.setRegisterNum(form.getPlayerNum());
		runtime.setOnlineNum(form.getOnlineNum());
		runtime.setBattleNum(form.getBattleNum());
		runtime.setCurrBattleNum(form.getCurrBattleNum());
		if(!"".equals(form.getMaxMemory())) {
			runtime.setMaxMemory(form.getMaxMemory());
			runtime.setFreeMemory(form.getFreeMemory());
			runtime.setTotalMemory(form.getTotalMemory());
			runtime.setLeftMemory(form.getLeftMemory());
			runtime.setUsedMemory(form.getUsedMemory());
		}
		gameServerRuntimeService.update(runtime);
	}
}
