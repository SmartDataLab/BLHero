/**
 * 
 */
package com.xiugou.x1.backstage.module.godfinger;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.godfinger.form.GodFingerForm;
import com.xiugou.x1.backstage.module.godfinger.model.GodFinger;
import com.xiugou.x1.backstage.module.godfinger.service.GodFingerService;
import com.xiugou.x1.backstage.module.godfinger.struct.GodFingerQuery;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;

/**
 * @author YY
 *
 */
@Controller
public class GodFingerController {

	@Autowired
	private GodFingerService godFingerService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private UserService userService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("请求金手指名单数据")
	@RequestMapping(value = "/godFinger/data.auth")
	@ResponseBody
	public PageData<GodFinger> data(GodFingerQuery query) {
		long channelId = gameChannelService.currChannel();
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		if (query.getOpenId() != null && !"".equals(query.getOpenId())) {
			querySet.addCondition("open_id like ?", "%" + query.getOpenId() + "%");
		}
		if (query.getName() != null && !"".equals(query.getName())) {
			querySet.addCondition("nick like ?", "%" + query.getName() + "%");
		}
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return godFingerService.query(querySet);
	}
	
	@ApiDocument("保存金手指名单数据，数据ID小于等于0时插入数据，大于0时更新对应数据")
	@RequestMapping(value = "/godFinger/save.authw")
	@ResponseBody
	public GodFinger save(GodFingerForm form) {
		RoleContext roleContext = userService.getCurrUser();
		long currChannel = gameChannelService.currChannel();
		GameChannel gameChannel = gameChannelService.getEntity(currChannel);
		
		Player player = playerService.getById(form.getPlayerId());
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, form.getPlayerId());
		Asserts.isTrue(player.getChannelId() == currChannel, TipsCode.PLAYER_NOT_IN_CHANNEL, currChannel);
		
		Asserts.isTrue(form.getMoney() > 0, TipsCode.GOD_FINGER_BIGGER_ZERO);
		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
		
		GodFinger godFinger = null;
		if(form.getId() <= 0) {
			godFinger = new GodFinger();
			godFinger.setChannelId(gameChannel.getId());
			godFinger.setChannelName(gameChannel.getName());
			godFinger.setServerUid(gameServer.getId());
			godFinger.setServerId(gameServer.getServerId());
			godFinger.setServerName(gameServer.getName());
			godFinger.setPlayerId(player.getId());
			godFinger.setOpenId(player.getOpenId());
			godFinger.setNick(player.getNick());
			godFinger.setRemark(form.getRemark());
			godFinger.setMoney(form.getMoney());
			godFinger.setUserId(roleContext.getId());
			godFinger.setUserName(roleContext.getName());
			godFingerService.insert(godFinger);
		} else {
			godFinger = godFingerService.getById(form.getId());
			Asserts.isTrue(godFinger != null, TipsCode.GOD_FINGER_NOT_EXIST, form.getId());
			Asserts.isTrue(player.getId() == form.getPlayerId(), TipsCode.GOD_FINGER_PLAYER_WRONG);
			
			godFinger.setRemark(form.getRemark());
			godFinger.setMoney(form.getMoney());
			godFingerService.update(godFinger);
		}
		return godFinger;
	}
	
	@ApiDocument("删除金手指名单数据")
	@RequestMapping(value = "/godFinger/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("id") long id) {
		GodFinger godFinger = godFingerService.getById(id);
		Asserts.isTrue(godFinger != null, TipsCode.GOD_FINGER_NOT_EXIST, id);
		godFingerService.delete(godFinger);
	}
}
