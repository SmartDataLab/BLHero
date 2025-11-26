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
import org.gaming.backstage.WebCode;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.model.User;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.form.UserChannelForm;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.UserChannel;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.UserChannelService;
import com.xiugou.x1.backstage.module.gameserver.struct.UserChannelQuery;
import com.xiugou.x1.backstage.module.gameserver.vo.ChannelGranting;
import com.xiugou.x1.backstage.module.gameserver.vo.UserVo;

/**
 * @author YY
 *
 */
@Controller
public class UserChannelController {

	@Autowired
	private UserChannelService userChannelService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求用户与渠道关系数据")
	@RequestMapping(value = "/userChannel/data.auth")
	@ResponseBody
	public PageData<UserVo> data(UserChannelQuery query) {
		RoleContext roleContext = userService.getCurrUser();
		User currUser = userService.getById(roleContext.getId());
		PageData<User> pageData = userService.queryUser(currUser.getLevel(), query.getPage(), query.getLimit());
		
		PageData<UserVo> result = new PageData<>();
		for(User user : pageData.getData()) {
			UserVo vo = new UserVo();
			vo.setId(user.getId());
			vo.setName(user.getName());
			vo.setPhone(user.getPhone());
			vo.setMailAddress(user.getMailAddress());
			result.getData().add(vo);
		}
		result.setCount(pageData.getCount());
		return result;
	}
	
	@ApiDocument("保存用户与渠道关系数据")
	@RequestMapping(value = "/userChannel/save.authw")
	@ResponseBody
	public PageData<ChannelGranting> save(UserChannelForm form) {
		RoleContext roleContext = userService.getCurrUser();
		User currUser = userService.getById(roleContext.getId());
		User targetUser = userService.getById(form.getUserId());
		Asserts.isTrue(currUser.getLevel() < targetUser.getLevel(), WebCode.USER_LEVEL_LACK);
		Asserts.isTrue(!targetUser.isSuperUser(), WebCode.USER_SUPER_DONT_GRANT);
		
		List<UserChannel> targetChannels = userChannelService.getEntityList(targetUser.getId());
		Map<Long, UserChannel> targetChannelMap = ListMapUtil.listToMap(targetChannels, UserChannel::getChannelId);
		
		List<UserChannel> insertList = new ArrayList<>();
		Set<Long> channelIds = new HashSet<>();
		
		if(!currUser.isSuperUser()) {
			List<UserChannel> userChannels = userChannelService.getEntityList(currUser.getId());
			Map<Long, UserChannel> userFunctionMap = ListMapUtil.listToMap(userChannels, UserChannel::getChannelId);
			
			for(long channelId : form.getChannel()) {
				UserChannel userChannel = userFunctionMap.get(channelId);
				if(userChannel == null) {
					//当前用户没有的权限，不能授权给下级用户
					continue;
				}
				if(channelIds.contains(channelId)) {
					continue;
				}
				GameChannel channel = gameChannelService.getEntity(channelId);
				if(channel == null) {
					continue;
				}
				channelIds.add(channelId);
				
				UserChannel targetChannel = targetChannelMap.get(channelId);
				if(targetChannel == null) {
					targetChannel = new UserChannel();
					targetChannel.setUserId(targetUser.getId());
					targetChannel.setUserName(targetUser.getName());
					targetChannel.setChannelId(channel.getId());
					targetChannel.setChannelName(channel.getName());
					targetChannel.setGrantUserId(currUser.getId());
					targetChannel.setGrantUserName(currUser.getName());
					insertList.add(targetChannel);
				}
			}
		} else {
			for(long channelId : form.getChannel()) {
				if(channelIds.contains(channelId)) {
					continue;
				}
				GameChannel channel = gameChannelService.getEntity(channelId);
				if(channel == null) {
					continue;
				}
				channelIds.add(channelId);
				
				UserChannel targetChannel = targetChannelMap.get(channelId);
				if(targetChannel == null) {
					targetChannel = new UserChannel();
					targetChannel.setUserId(targetUser.getId());
					targetChannel.setUserName(targetUser.getName());
					targetChannel.setChannelId(channel.getId());
					targetChannel.setChannelName(channel.getName());
					targetChannel.setGrantUserId(currUser.getId());
					targetChannel.setGrantUserName(currUser.getName());
					insertList.add(targetChannel);
				}
			}
		}
		//被移除的权限
		List<UserChannel> deleteList = new ArrayList<>();
		for(UserChannel targetChannel : targetChannels) {
			if(channelIds.contains(targetChannel.getChannelId())) {
				continue;
			}
			deleteList.add(targetChannel);
		}
		if(!deleteList.isEmpty()) {
			userChannelService.deleteAll(deleteList);
		}
		if(!insertList.isEmpty()) {
			userChannelService.insertAll(insertList);
		}
		return targetUserChannelOption(roleContext.getId(), form.getUserId());
	} 
	
	@ApiDocument("获取可授权的渠道数据")
	@RequestMapping(value = "/userChannel/options.auth")
	@ResponseBody
	public PageData<ChannelGranting> options(@RequestParam("userId") long userId) {
		RoleContext userContext = userService.getCurrUser();
		return targetUserChannelOption(userContext.getId(), userId);
	}
	
	private PageData<ChannelGranting> targetUserChannelOption(long userId, long targetUserId) {
		//当前用户
		User user = userService.getById(userId);
		User targetUser = userService.getById(targetUserId);
		//目标用户当前用的权限
		List<UserChannel> targetHavingChannels = userChannelService.getEntityList(targetUser.getId());
		Map<Long, UserChannel> targetHavingChannelMap = ListMapUtil.listToMap(targetHavingChannels, UserChannel::getChannelId);
		
		PageData<ChannelGranting> pageData = new PageData<>();
		if(user.isSuperUser()) {
			List<GameChannel> channels = gameChannelService.getEntities();
			
			for(GameChannel channel : channels) {
				ChannelGranting granting = new ChannelGranting();
				granting.setId(channel.getId());
				granting.setName(channel.getName());
				
				UserChannel havingChannel = targetHavingChannelMap.get(channel.getId());
				if(havingChannel != null) {
					granting.setHas(true);
				}
				pageData.getData().add(granting);
			}
		} else {
			//当前用户可以授权的菜单数据
			List<UserChannel> userChannels = userChannelService.getEntityList(user.getId());
			for(UserChannel userChannel : userChannels) {
				ChannelGranting granting = new ChannelGranting();
				granting.setId(userChannel.getId());
				granting.setName(userChannel.getChannelName());
				
				UserChannel havingChannel = targetHavingChannelMap.get(userChannel.getChannelId());
				if(havingChannel != null) {
					granting.setHas(true);
				}
				pageData.getData().add(granting);
			}
		}
		pageData.setCount(pageData.getData().size());
		SortUtil.sort(pageData.getData(), ChannelGranting::getId);
		return pageData;
	}
}
