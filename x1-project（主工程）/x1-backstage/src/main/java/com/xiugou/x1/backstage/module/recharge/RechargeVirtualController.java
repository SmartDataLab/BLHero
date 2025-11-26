/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge;

import java.util.HashMap;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.module.recharge.form.RechargeVirtualForm;
import com.xiugou.x1.backstage.module.recharge.model.RechargeVirtual;
import com.xiugou.x1.backstage.module.recharge.service.RechargeVirtualService;
import com.xiugou.x1.backstage.module.recharge.struct.RechargeVirtualQuery;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;

/**
 * @author YY
 *
 */
@Controller
public class RechargeVirtualController {

	@Autowired
	private RechargeVirtualService rechargeVirtualService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameServerService gameServerService;
	
	
	@ApiDocument("请求虚拟充值（内部充值）数据")
	@RequestMapping(value = "/rechargeVirtual/data.auth")
	@ResponseBody
	public PageData<RechargeVirtual> data(RechargeVirtualQuery query) {
		QuerySet querySet = new QuerySet();
		if(query.getOpenId() != null && !"".equals(query.getOpenId())) {
			querySet.addCondition("open_id like ?", "%" + query.getOpenId() + "%");
		}
		if(query.getPlayerId() > 0) {
			querySet.addCondition("player_id = ?", query.getPlayerId());
		}
		if(query.getNick() != null && !"".equals(query.getNick())) {
			querySet.addCondition("nick like ?", "%" + query.getNick() + "%");
		}
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return rechargeVirtualService.query(querySet);
	}
	
	@ApiDocument("发放虚拟充值（内部充值）")
	@RequestMapping(value = "/rechargeVirtual/add.authw")
	@ResponseBody
	public RechargeVirtual add(RechargeVirtualForm form) {
		long currChannel = gameChannelService.currChannel();
		RoleContext roleContext = userService.getCurrUser();
		
		Player player = playerService.getById(form.getPlayerId());
		Asserts.isTrue(player != null, TipsCode.PLAYER_NOT_EXIST, form.getPlayerId());
		
		GameServer gameServer = gameServerService.getByPlatformAndServer(player.getPlatformId(), player.getServerId());
		
		RechargeVirtual rechargeVirtual = new RechargeVirtual();
		rechargeVirtual.setChannelId(currChannel);
		rechargeVirtual.setPlayerId(player.getId());
		rechargeVirtual.setNick(player.getNick());
		rechargeVirtual.setOpenId(player.getOpenId());
		rechargeVirtual.setProductId(form.getProductId());
//TODO		rechargeVirtual.setProductName(productName);
//		rechargeVirtual.setMoney(money);
		
		rechargeVirtual.setServerUid(gameServer.getId());
		rechargeVirtual.setServerId(gameServer.getServerId());
		rechargeVirtual.setServerName(gameServer.getName());
		rechargeVirtual.setUserId(roleContext.getId());
		rechargeVirtual.setUserName(roleContext.getName());
		rechargeVirtual.setRemark(form.getRemark());
		rechargeVirtualService.insert(rechargeVirtual);
		
		Map<String, Object> postForm = new HashMap<>();
		postForm.put("playerId", form.getPlayerId());
		postForm.put("productId", form.getProductId());
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.virtualRecharge, postForm);
		if(serverResponse == null) {
			rechargeVirtual.setStatus(2);
		} else if(serverResponse.getCode() == 0) {
			rechargeVirtual.setStatus(1);
		} else {
			rechargeVirtual.setStatus(2);
		}
		rechargeVirtualService.update(rechargeVirtual);
		
		return rechargeVirtual;
	}
}
