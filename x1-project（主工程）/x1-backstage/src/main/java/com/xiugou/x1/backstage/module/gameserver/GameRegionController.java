/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.ArrayList;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.form.GameRegionForm;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameRegion;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameRegionService;
import com.xiugou.x1.backstage.module.gameserver.struct.GameRegionQuery;
import com.xiugou.x1.backstage.module.gameserver.struct.ServerType;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;

/**
 * @author YY
 *
 */
@Controller
public class GameRegionController {

	@Autowired
	private GameRegionService gameRegionService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求渠道大区数据")
	@RequestMapping(value = "/gameRegion/data.auth")
	@ResponseBody
	public PageData<GameRegion> info(GameRegionQuery query) {
		if(query.getChannelId() > 0) {
			List<GameRegion> list = gameRegionService.getEntityList(query.getChannelId());
			SortUtil.sortInt(list, GameRegion::getRegionId);
			
			PageData<GameRegion> pageData = new PageData<>();
			pageData.setCount(list.size());
			pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
			return pageData;
		} else {
			return gameRegionService.getList(query.getPage(), query.getLimit());
		}
	}
	
	@ApiDocument("保存渠道大区数据，ID小于等于0时新增，大于0时修改")
	@RequestMapping(value = "/gameRegion/save.authw")
	@ResponseBody
	public GameRegion save(GameRegionForm form) {
		GameChannel gameChannel = gameChannelService.getEntity(form.getChannelId());
		Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, form.getChannelId());
		GameRegion gameRegion = gameRegionService.getEntity(form.getChannelId(), form.getRegionId());
		
		if(form.getId() <= 0) {
			Asserts.isTrue(gameRegion == null, TipsCode.REGION_EXIST, form.getChannelId(), form.getRegionId());
		} else {
			Asserts.isTrue(gameRegion.getId() == form.getId(), TipsCode.ERROR_PARAM);
		}
		if(gameRegion == null) {
			gameRegion = new GameRegion();
			gameRegion.setRegionId(form.getRegionId());
			gameRegion.setName(form.getName());
			gameRegion.setChannelId(gameChannel.getId());
			gameRegion.setChannelName(gameChannel.getName());
			gameRegion.setServerType(form.getServerType());
			gameRegionService.insert(gameRegion);
		} else {
			gameRegion.setName(form.getName());
			gameRegion.setServerType(form.getServerType());
			gameRegionService.update(gameRegion);
		}
		return gameRegion;
	}
	
	@ApiDocument("删除渠道大区数据")
	@RequestMapping(value = "/gameRegion/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("channelId") long channelId, @RequestParam("regionId") long regionId) {
		GameRegion gameRegion = gameRegionService.getEntity(channelId, regionId);
		Asserts.isTrue(gameRegion != null, TipsCode.REGION_MISS, channelId, regionId);
		
		gameRegionService.delete(gameRegion);
		List<GameChannelServer> servers = gameChannelServerService.getEntityList(gameRegion.getChannelId());
		List<GameChannelServer> updateList = new ArrayList<>();
		for(GameChannelServer server : servers) {
			if(server.getRegionId() == gameRegion.getRegionId()) {
				server.setRegionId(0);
				server.setRegionName("");
				updateList.add(server);
			}
		}
		gameChannelServerService.updateAll(updateList);
	}
	
	@ApiDocument("渠道大区下拉菜单数据")
	@RequestMapping(value = "/gameRegion/options.do")
	@ResponseBody
	public DropDownOptions regionOptions(@RequestParam("channelId") long channelId) {
		List<GameRegion> list = gameRegionService.getEntityList(channelId);
		SortUtil.sortInt(list, GameRegion::getRegionId);
		
		DropDownOptions options = new DropDownOptions();
		for(GameRegion gameRegion : list) {
			String serverDesc = ServerType.TEST.getDesc();
			if(gameRegion.getServerType() == ServerType.REVIEW.getValue()) {
				serverDesc = ServerType.REVIEW.getDesc();
			} else if(gameRegion.getServerType() == ServerType.NORMAL.getValue()) {
				serverDesc = ServerType.NORMAL.getDesc();
			}
			options.addOption(gameRegion.getRegionId(), gameRegion.getName() + "（"+ serverDesc +"）");
		}
		return options;
	}
}
