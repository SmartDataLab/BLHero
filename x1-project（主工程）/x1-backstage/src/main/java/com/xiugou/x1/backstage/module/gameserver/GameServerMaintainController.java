/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.form.GameServerMaintainForm;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.vo.GameServerMaintainVo;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class GameServerMaintainController {

	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("请求服务器维护数据")
	@RequestMapping(value = "/gameServerMaintain/data.auth")
	@ResponseBody
	public PageData<GameServerMaintainVo> data(@RequestParam("platformId") long platformId) {
		List<GameServer> list = gameServerService.getEntities();
		
		List<GameServer> pageList = new ArrayList<>();
		for(GameServer server : list) {
			if(server.getPlatformId() != platformId) {
				continue;
			}
			if(server.isMerge()) {
				continue;
			}
			pageList.add(server);
		}
		return buildMaintainVo(pageList);
	}
	
	@ApiDocument("进行服务器维护")
	@RequestMapping(value = "/gameServerMaintain/maintain.authw")
	@ResponseBody
	public PageData<GameServerMaintainVo> maintain(GameServerMaintainForm form) {
		List<GameServer> list = gameServerService.getEntities();
		
		List<GameServer> updateList = new ArrayList<>();
		for(GameServer gameServer : list) {
			if(!form.getServerIds().contains(gameServer.getServerId())) {
				continue;
			}
			if(gameServer.getPlatformId() != form.getPlatformId()) {
				continue;
			}
			if(gameServer.isMerge()) {
				continue;
			}
			gameServer.setStatus(0);
			gameServer.setMaintain(true);
			gameServer.setMaintainResponse(false);
			updateList.add(gameServer);
		}
		for(GameServer gameServer : updateList) {
			Map<String, Object> map = new HashMap<>();
			map.put("maintain", true);
			ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.serverMaintain, map);
			if(response != null && response.getCode() == 0) {
				gameServer.setMaintainResponse(true);
			}
		}
		gameServerService.updateAll(updateList);
		return buildMaintainVo(updateList);
	}
	
	@ApiDocument("重新发送进行服务器维护")
	@RequestMapping(value = "/gameServerMaintain/resendMaintain.authw")
	@ResponseBody
	public PageData<GameServerMaintainVo> resendMaintain(GameServerMaintainForm form) {
		List<GameServer> list = gameServerService.getEntities();
		
		List<GameServer> updateList = new ArrayList<>();
		for(GameServer gameServer : list) {
			if(!form.getServerIds().contains(gameServer.getServerId())) {
				continue;
			}
			if(gameServer.getPlatformId() != form.getPlatformId()) {
				continue;
			}
			if(gameServer.isMerge()) {
				continue;
			}
			if(gameServer.isMaintainResponse()) {
				continue;
			}
			updateList.add(gameServer);
		}
		
		for(GameServer gameServer : updateList) {
			Map<String, Object> map = new HashMap<>();
			map.put("maintain", true);
			ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.serverMaintain, map);
			if(response != null && response.getCode() == 0) {
				gameServer.setMaintainResponse(true);
			}
		}
		
		gameServerService.updateAll(updateList);
		return buildMaintainVo(updateList);
	}
	
	@ApiDocument("结束服务器维护")
	@RequestMapping(value = "/gameServerMaintain/endMaintain.authw")
	@ResponseBody
	public PageData<GameServerMaintainVo> endMaintain(GameServerMaintainForm form) {
		List<GameServer> list = gameServerService.getEntities();
		
		List<GameServer> updateList = new ArrayList<>();
		for(GameServer gameServer : list) {
			if(!form.getServerIds().contains(gameServer.getServerId())) {
				continue;
			}
			if(gameServer.getPlatformId() != form.getPlatformId()) {
				continue;
			}
			if(gameServer.isMerge()) {
				continue;
			}
			gameServer.setStatus(1);
			gameServer.setMaintain(false);
			gameServer.setMaintainResponse(false);
			updateList.add(gameServer);
		}
		for(GameServer gameServer : updateList) {
			Map<String, Object> map = new HashMap<>();
			map.put("maintain", false);
			ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.serverMaintain, map);
			if(response != null && response.getCode() == 0) {
				gameServer.setMaintainResponse(true);
			}
		}
		gameServerService.updateAll(updateList);
		return buildMaintainVo(updateList);
	}
	
	@ApiDocument("重新发送结束服务器维护")
	@RequestMapping(value = "/gameServerMaintain/resendEndMaintain.authw")
	@ResponseBody
	public PageData<GameServerMaintainVo> resendEndMaintain(GameServerMaintainForm form) {
		List<GameServer> list = gameServerService.getEntities();
		
		List<GameServer> updateList = new ArrayList<>();
		for(GameServer gameServer : list) {
			if(!form.getServerIds().contains(gameServer.getServerId())) {
				continue;
			}
			if(gameServer.getPlatformId() != form.getPlatformId()) {
				continue;
			}
			if(gameServer.isMerge()) {
				continue;
			}
			if(gameServer.isMaintainResponse()) {
				continue;
			}
			updateList.add(gameServer);
		}
		
		for(GameServer gameServer : updateList) {
			Map<String, Object> map = new HashMap<>();
			map.put("maintain", false);
			ServerResponse response = X1HttpUtil.formPost(gameServer, GameApi.serverMaintain, map);
			if(response != null && response.getCode() == 0) {
				gameServer.setMaintainResponse(true);
			}
		}
		gameServerService.updateAll(updateList);
		return buildMaintainVo(updateList);
	}
	
	public PageData<GameServerMaintainVo> buildMaintainVo(List<GameServer> servers) {
		List<GameServerMaintainVo> pageList = new ArrayList<>();
		for(GameServer server : servers) {
			GameServerMaintainVo vo = new GameServerMaintainVo();
			vo.setServerId(server.getServerId());
			vo.setName(server.getName());
			vo.setMaintain(server.isMaintain());
			vo.setMaintainResponse(server.isMaintainResponse());
			pageList.add(vo);
		}
		
		SortUtil.sortInt(pageList, GameServerMaintainVo::getServerId);
		
		PageData<GameServerMaintainVo> pageData = new PageData<>();
		pageData.setCount(pageList.size());
		pageData.setData(pageList);
		return pageData;
	}
}
