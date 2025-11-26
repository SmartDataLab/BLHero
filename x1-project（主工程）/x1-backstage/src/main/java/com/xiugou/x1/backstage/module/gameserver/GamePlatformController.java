/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.ArrayList;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.form.GamePlatformForm;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GamePlatform;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GamePlatformService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions.DropDownOption;

/**
 * @author YY
 *
 */
@Controller
public class GamePlatformController {
	@Autowired
	private GamePlatformService gamePlatformService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("请求平台数据")
	@RequestMapping(value = "/gamePlatform/data.auth")
	@ResponseBody
	public PageData<GamePlatform> info(PageQuery query) {
		List<GamePlatform> list = gamePlatformService.getEntities();
		SortUtil.sort(list, GamePlatform::getId);
		
		PageData<GamePlatform> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
		return pageData;
	}
	
	@ApiDocument("保存平台数据，数据ID小于等于0时插入数据，大于0时更新对应数据")
	@RequestMapping(value = "/gamePlatform/save.authw")
	@ResponseBody
	public GamePlatform save(GamePlatformForm form) {
		GamePlatform gamePlatform = null;
		if(form.getId() <= 0) {
			RoleContext roleContext = userService.getCurrUser();
			
			gamePlatform = new GamePlatform();
			gamePlatform.setName(form.getName());
			gamePlatform.setUserId(roleContext.getId());
			gamePlatform.setUserName(roleContext.getName());
			gamePlatformService.insert(gamePlatform);
		} else {
			gamePlatform = gamePlatformService.getEntity(form.getId());
			Asserts.isTrue(gamePlatform != null, TipsCode.PLATFORM_MISS, form.getId());
			
			String oldName = gamePlatform.getName();
			gamePlatform.setName(form.getName());
			gamePlatformService.update(gamePlatform);
			
			if(oldName != null && form.getName() != null && !oldName.equals(form.getName())) {
				List<GameChannel> updateChannels = new ArrayList<>();
				for(GameChannel gameChannel : gameChannelService.getEntities()) {
					if(gameChannel.getPlatformId() != gamePlatform.getId()) {
						continue;
					}
					gameChannel.setPlatformName(gamePlatform.getName());
					updateChannels.add(gameChannel);
				}
				gameChannelService.updateAll(updateChannels);
				
				List<GameServer> updateServers = new ArrayList<>();
				for(GameServer gameServer : gameServerService.getEntities()) {
					if(gameServer.getPlatformId() != gamePlatform.getId()) {
						continue;
					}
					gameServer.setPlatformName(gamePlatform.getName());
					updateServers.add(gameServer);
				}
				gameServerService.updateAll(updateServers);
			}
		}
		return gamePlatform;
	}
	
	@ApiDocument("请求平台下拉菜单数据")
	@RequestMapping(value = "/gamePlatform/options.do")
	@ResponseBody
	public DropDownOptions channelOptions() {
		DropDownOptions options = new DropDownOptions();
		for(GamePlatform gamePlatform : gamePlatformService.getEntities()) {
			options.addOption(gamePlatform.getId(), gamePlatform.getName() + "[" + gamePlatform.getId() + "]");
		}
		SortUtil.sort(options.getOptions(), DropDownOption::getValue);
		return options;
	}
}
