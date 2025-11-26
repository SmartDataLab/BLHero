/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge;

import java.util.List;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.module.recharge.service.RechargeOneDateService;
import com.xiugou.x1.backstage.module.recharge.struct.RechargeRankQuery;
import com.xiugou.x1.backstage.module.recharge.vo.RechargeRankVo;

/**
 * @author YY
 *
 */
@Controller
public class RechargeRankController {

	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private RechargeOneDateService rechargeOneDateService;
	@Autowired
	private PlayerService playerService;
	
	@ApiDocument("请求充值排名数据")
	@RequestMapping(value = "/rechargeRank/data.auth")
	@ResponseBody
	public PageData<RechargeRankVo> data(RechargeRankQuery query) {
		long channelId = gameChannelService.currChannel();
		
		long startTime = query.getStartTime() * 1000L;
		if(startTime <= 0) {
			startTime = DateTimeUtil.currMillis();
		}
		String startStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime);
		long endTime = query.getEndTime() * 1000L;
		if(endTime <= 0) {
			endTime = DateTimeUtil.currMillis();
		}
		String endStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime);
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		querySet.addCondition("date_str >= ?", startStr);
		querySet.addCondition("date_str <= ?", endStr);
		if(query.getServerUids() != null && !query.getServerUids().isEmpty()) {
			querySet.findInSet("server_uid", query.getServerUids());
		}
		querySet.groupBy("group by player_id");
		querySet.orderBy("order by money desc");
		querySet.limit(1, query.getLimit());
		querySet.formWhere();
		
		List<RechargeRankVo> rankList = rechargeOneDateService.queryRank(querySet);
		Map<Integer, GameServer> serverMap = ListMapUtil.listToMap(gameChannelService.getServers(), GameServer::getServerId);
		GameChannel gameChannel = gameChannelService.getEntity(channelId);
		
		for(RechargeRankVo vo : rankList) {
			Player player = playerService.getById(vo.getPlayerId());
			
			GameServer server = serverMap.get(player.getServerId());
			vo.setChannelId(gameChannel.getId());
			vo.setChannelName(gameChannel.getName());
			if(server != null) {
				vo.setServerUid(server.getId());
				vo.setServerId(server.getServerId());
				vo.setServerName(server.getName());
			}
			vo.setNick(player.getNick());
			vo.setLevel(player.getLevel());
			vo.setFighting(player.getFighting());
			vo.setDiamond(player.getDiamond());
			vo.setGold(player.getGold());
			vo.setLastLoginTime(player.getLastLoginTime());
			vo.setBornTime(player.getBornTime());
		}
		
		PageData<RechargeRankVo> pageData = new PageData<>();
		pageData.setData(rankList);
		pageData.setCount(rankList.size());
		return pageData;
	}
}
