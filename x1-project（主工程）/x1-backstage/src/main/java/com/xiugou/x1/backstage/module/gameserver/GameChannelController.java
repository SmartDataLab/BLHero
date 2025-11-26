/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.service.UserService;
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
import com.xiugou.x1.backstage.module.gameserver.form.GameChannelForm;
import com.xiugou.x1.backstage.module.gameserver.model.Bulletin;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GamePlatform;
import com.xiugou.x1.backstage.module.gameserver.model.GameRegion;
import com.xiugou.x1.backstage.module.gameserver.model.UserChannel;
import com.xiugou.x1.backstage.module.gameserver.service.BulletinService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GamePlatformService;
import com.xiugou.x1.backstage.module.gameserver.service.GameRegionService;
import com.xiugou.x1.backstage.module.gameserver.service.UserChannelService;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions.DropDownOption;
import com.xiugou.x1.backstage.module.gameserver.vo.GameChannelVo;

/**
 * @author YY
 *
 */
@Controller
public class GameChannelController {

	private static Logger logger = LoggerFactory.getLogger(GameChannelController.class);
	
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserChannelService userChannelService;
	@Autowired
	private BulletinService bulletinService;
	@Autowired
	private GamePlatformService gamePlatformService;
	@Autowired
	private GameRegionService gameRegionService;
	
	@ApiDocument("请求渠道数据")
	@RequestMapping(value = "/gameChannel/data.auth")
	@ResponseBody
	public PageData<GameChannel> info(PageQuery query) {
		List<GameChannel> list = gameChannelService.getEntities();
		SortUtil.sort(list, GameChannel::getId);
		List<GameChannel> pageList = PageUtil.pageN(list, query.getPage(), query.getLimit());
		
		PageData<GameChannel> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(pageList);
		return pageData;
	}
	
	@ApiDocument("保存渠道数据，数据ID小于等于0时插入数据，大于0时更新对应数据")
	@RequestMapping(value = "/gameChannel/save.authw")
	@ResponseBody
	public GameChannel save(GameChannelForm form) {
		Bulletin bulletin = null;
		if(form.getBulletinId() > 0) {
			bulletin = bulletinService.getEntity(form.getBulletinId());
		}
		
		GameChannel gameChannel = null;
		if(form.getId() <= 0) {
			GamePlatform gamePlatform = gamePlatformService.getEntity(form.getPlatformId());
			Asserts.isTrue(gamePlatform != null, TipsCode.PLATFORM_MISS, form.getPlatformId());
			
			RoleContext roleContext = userService.getCurrUser();
			
			gameChannel = new GameChannel();
			gameChannel.setName(form.getName());
			gameChannel.setUserId(roleContext.getId());
			gameChannel.setUserName(roleContext.getName());
			gameChannel.setBulletinId(bulletin == null ? 0 : bulletin.getId());
			gameChannel.setPlatformId(gamePlatform.getId());
			gameChannel.setPlatformName(gamePlatform.getName());
			gameChannel.setProgramVersion(form.getProgramVersion());
			gameChannel.setResourceVersion(form.getResourceVersion());
			gameChannelService.insert(gameChannel);
		} else {
			gameChannel = gameChannelService.getEntity(form.getId());
			Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, form.getId());
			
			String oldName = gameChannel.getName();
			gameChannel.setName(form.getName());
			gameChannel.setBulletinId(bulletin == null ? 0 : bulletin.getId());
			gameChannel.setProgramVersion(form.getProgramVersion());
			gameChannel.setResourceVersion(form.getResourceVersion());
			gameChannelService.update(gameChannel);
			
			if(oldName != null && form.getName() != null && !oldName.equals(form.getName())) {
				List<GameChannelServer> list = gameChannelServerService.getEntityList(gameChannel.getId());
				for(GameChannelServer entity : list) {
					entity.setChannelName(gameChannel.getName());
				}
				gameChannelServerService.updateAll(list);
				
				List<GameRegion> regions = gameRegionService.getEntityList(gameChannel.getId());
				for(GameRegion gameRegion : regions) {
					gameRegion.setChannelName(gameChannel.getName());
				}
				gameRegionService.updateAll(regions);
			}
		}
		return gameChannel;
	}
	
	@ApiDocument("删除渠道数据，并删除与之关联的服务器关系数据")
	@RequestMapping(value = "/gameChannel/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("channelId") long channelId) {
		GameChannel gameChannel = gameChannelService.getEntity(channelId);
		Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, channelId);
		
		gameChannelService.delete(gameChannel);
		gameChannelServerService.deleteAllInOwner(gameChannel.getId());
	}
	
	@ApiDocument("请求渠道下拉菜单数据")
	@RequestMapping(value = "/gameChannel/options.do")
	@ResponseBody
	public DropDownOptions channelOptions() {
		RoleContext roleContext = userService.getCurrUser();
		User user = userService.getById(roleContext.getId());
		
		DropDownOptions options = new DropDownOptions();
		if(user.isSuperUser()) {
			List<GameChannel> list = gameChannelService.getEntities();
			for(GameChannel gameChannel : list) {
				options.addOption(gameChannel.getId(), gameChannel.getName());
			}
		} else {
			List<UserChannel> channels = userChannelService.getEntityList(user.getId());
			for(UserChannel channel : channels) {
				options.addOption(channel.getChannelId(), channel.getChannelName());
			}
		}
		SortUtil.sort(options.getOptions(), DropDownOption::getValue);
		return options;
	}
	
	@ApiDocument("切换渠道")
	@RequestMapping(value = "/gameChannel/change.do")
	@ResponseBody
	public void changeChannel(@RequestParam("channelId") long channelId) {
		if(channelId <= 0) {
			return;
		}
		
		RoleContext roleContext = userService.getCurrUser();
		User user = userService.getById(roleContext.getId());
		if(user.isSuperUser()) {
			GameChannel gameChannel = gameChannelService.getEntity(channelId);
			
			gameChannelService.changeChannel(roleContext.getId(), channelId);
			logger.info("用户{}-{}切换渠道为{}-{}", roleContext.getId(), roleContext.getName(), gameChannel.getId(), gameChannel.getName());
		} else {
			List<UserChannel> channels = userChannelService.getEntityList(user.getId());
			for(UserChannel channel : channels) {
				if(channel.getChannelId() == channelId) {
					gameChannelService.changeChannel(roleContext.getId(), channelId);
					logger.info("用户{}-{}切换渠道为{}-{}", roleContext.getId(), roleContext.getName(), channel.getChannelId(), channel.getChannelName());
					break;
				}
			}
		}
	}
	
	public GameChannelVo buildVo(GameChannel channel) {
		GameChannelVo vo = new GameChannelVo();
		vo.setId(channel.getId());
		vo.setName(channel.getName());
		vo.setBulletinId(channel.getBulletinId());
		vo.setPlatformInfo(channel.getPlatformName() + "[" + channel.getPlatformId() + "]");
		vo.setUserId(channel.getUserId());
		vo.setUserName(channel.getUserName());
		vo.setProgramVersion(channel.getProgramVersion());
		vo.setResourceVersion(channel.getResourceVersion());
		return vo;
	}
}
