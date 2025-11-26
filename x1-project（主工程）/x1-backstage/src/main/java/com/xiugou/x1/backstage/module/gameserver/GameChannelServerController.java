/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.form.ChannelServerRelationForm;
import com.xiugou.x1.backstage.module.gameserver.form.ServerToRegionForm;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameRegion;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameRegionService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.struct.GameChannelServerQuery;
import com.xiugou.x1.backstage.module.gameserver.vo.ServerSelected;
import com.xiugou.x1.backstage.module.gameserver.vo.ServerSelected.ServerSelectedData;

/**
 * @author YY
 *
 */
@Controller
public class GameChannelServerController {

	@Autowired
	private GameChannelServerService gameChannelServerService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private GameRegionService gameRegionService;
	@Autowired
	private UserService userService;
	
	@ApiDocument("请求渠道与服务器关系数据")
	@RequestMapping(value = "/gameChannelServer/data.auth")
	@ResponseBody
	public PageData<GameChannelServer> info(GameChannelServerQuery query) {
		if(query.getChannelId() > 0) {
			List<GameChannelServer> list = gameChannelServerService.getEntityList(query.getChannelId());
			SortUtil.sortIntDesc(list, GameChannelServer::getServerId);
			
			PageData<GameChannelServer> pageData = new PageData<>();
			pageData.setCount(list.size());
			pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
			return pageData;
			
		} else {
			return gameChannelServerService.getList(query.getPage(), query.getLimit());
		}
	}
	
	@ApiDocument("编辑服务器归属大区")
	@RequestMapping(value = "/gameChannelServer/saveToRegion.authw")
	@ResponseBody
	public GameChannelServer saveToRegion(ServerToRegionForm form) {
		GameChannel gameChannel = gameChannelService.getEntity(form.getChannelId());
		Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, form.getChannelId());
		GameRegion gameRegion = gameRegionService.getEntity(form.getChannelId(), form.getRegionId());
		Asserts.isTrue(gameRegion != null, TipsCode.REGION_MISS, form.getChannelId(), form.getRegionId());
		GameServer gameServer = gameServerService.getEntity(form.getServerUid());
		Asserts.isTrue(gameServer != null, TipsCode.GAME_SERVER_MISS, form.getServerUid());
		
		GameChannelServer gameChannelServer = gameChannelServerService.getEntity(form.getChannelId(), gameServer.getServerId());
		Asserts.isTrue(gameChannelServer != null, TipsCode.CHANNEL_SERVER_MISS, form.getChannelId(), gameServer.getServerId());
		
		Asserts.isTrue(gameServer.getServerType() == gameRegion.getServerType(), TipsCode.SERVER_REGION_TYPE_UNMATCH);
		
		gameChannelServer.setRegionId(gameRegion.getRegionId());
		gameChannelServer.setRegionName(gameRegion.getName());
		gameChannelServerService.update(gameChannelServer);
		return gameChannelServer;
	}
	
	@ApiDocument("保存渠道与服务器关系数据")
	@RequestMapping(value = "/gameChannelServer/save.authw")
	@ResponseBody
	public void save(ChannelServerRelationForm form) {
		RoleContext roleContext = userService.getCurrUser();
		
		GameChannel gameChannel = gameChannelService.getEntity(form.getChannelId());
		Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, form.getChannelId());
		
		List<GameChannelServer> relations = gameChannelServerService.getEntityList(form.getChannelId());
		Map<Integer, GameChannelServer> relationMap = ListMapUtil.listToMap(relations, GameChannelServer::getServerUid);
		
		List<GameChannelServer> newRelations = new ArrayList<>();
		
		Set<Integer> serverUidSet = new HashSet<>(form.getServerUids());
		for(Integer serverUid : serverUidSet) {
			GameServer gameServer = gameServerService.getEntity(serverUid);
			Asserts.isTrue(gameServer != null, TipsCode.GAME_SERVER_MISS, serverUid);
			
			GameChannelServer relation = relationMap.get(gameServer.getId());
			if(relation != null) {
				continue;
			}
			relation = new GameChannelServer();
			relation.setChannelId(gameChannel.getId());
			relation.setChannelName(gameChannel.getName());
			relation.setServerUid(gameServer.getId());
			relation.setServerId(gameServer.getServerId());
			relation.setServerName(gameServer.getName());
			relation.setUserId(roleContext.getId());
			relation.setUserName(roleContext.getName());
			relation.setRecommend(gameServer.isRecommend());
			newRelations.add(relation);
		}
		gameChannelServerService.insertAll(newRelations);
		
		List<GameChannelServer> deleteList = new ArrayList<>();
		for(GameChannelServer oriRelation : relations) {
			if(serverUidSet.contains(oriRelation.getServerUid())) {
				continue;
			}
			deleteList.add(oriRelation);
		}
		gameChannelServerService.deleteAll(deleteList);
	}
	
	
	@ApiDocument("请求渠道下当前的服务器")
	@RequestMapping(value = "/gameChannelServer/currRelation.auth")
	@ResponseBody
	public ServerSelected currRelation(@RequestParam("channelId") long channelId) {
		GameChannel gameChannel = gameChannelService.getEntity(channelId);
		
		List<GameChannelServer> list = gameChannelServerService.getEntityList(channelId);
		Map<Integer, GameChannelServer> relations = ListMapUtil.listToMap(list, GameChannelServer::getServerUid);
		
		List<GameServer> servers = gameServerService.getEntities();
		SortUtil.sortInt(servers, GameServer::getId);
		
		ServerSelected serverSelected = new ServerSelected();
		for(GameServer gameServer : servers) {
			if(gameServer.getPlatformId() != gameChannel.getPlatformId()) {
				continue;
			}
			ServerSelectedData data = new ServerSelectedData();
			data.setId(gameServer.getId());
			data.setText(gameServer.getServerId() + "-" + gameServer.getName() + "(" + gameServer.getId() + ")");
			GameChannelServer relation = relations.get(gameServer.getId());
			if(relation != null) {
				data.setSelect(true);
			} else {
				data.setSelect(false);
			}
			serverSelected.getDatas().add(data);
		}
		return serverSelected;
	}
}
