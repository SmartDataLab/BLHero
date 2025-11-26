/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.model.PlayerPayRemain;
import com.xiugou.x1.backstage.module.player.service.PlayerPayRemainService;
import com.xiugou.x1.backstage.module.player.struct.PlayerRemainQuery;
import com.xiugou.x1.backstage.module.player.vo.PlayerRemainVo;

/**
 * @author YY
 *
 */
@Controller
public class PlayerPayRemainController {

	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private PlayerPayRemainService playerPayRemainService;
	
	
	@ApiDocument("请求付费留存数据")
	@RequestMapping(value = "/playerPayRemain/data.auth")
	@ResponseBody
	public PageData<PlayerRemainVo> data(PlayerRemainQuery query) {
		long channelId = gameChannelService.currChannel();
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		if (query.getServerUid() > 0) {
			querySet.addCondition("server_uid = ?", query.getServerUid());
		}
		long startTime = query.getStartTime() * 1000L;
		if(startTime == 0) {
			startTime = DateTimeUtil.monthZeroMillis();
		}
		String startBorn = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime);
		querySet.addCondition("born >= ?", startBorn);
		
		long endTime = query.getEndTime();
		if(endTime > 0) {
			String endBorn = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime * 1000L);
			querySet.addCondition("born <= ?", endBorn);
		}
		querySet.formWhere();
		
		List<PlayerPayRemain> playerRemains = playerPayRemainService.queryPayRemain(querySet);
		Map<Long, Map<String, List<PlayerPayRemain>>> map = ListMapUtil.fillTwoListMap(playerRemains, PlayerPayRemain::getServerUid, PlayerPayRemain::getBorn);
		
		List<PlayerRemainVo> result = new ArrayList<>();
		for(Entry<Long, Map<String, List<PlayerPayRemain>>> outEntry : map.entrySet()) {
			GameServer gameServer = gameServerService.getEntity(outEntry.getKey());
			
			for(Entry<String, List<PlayerPayRemain>> inEntry : outEntry.getValue().entrySet()) {
				PlayerRemainVo vo = new PlayerRemainVo();
				vo.setServerUid(gameServer.getId());
				vo.setServerId(gameServer.getServerId());
				vo.setServerName(gameServer.getName());
				vo.setBornDate(inEntry.getKey());
				for(PlayerPayRemain playerRemain : inEntry.getValue()) {
					if(playerRemain.getDayCount() == 0) {
						vo.setDay0(playerRemain.getPlayer());
						vo.setDayBase(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 1) {
						vo.setDay1(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 2) {
						vo.setDay2(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 3) {
						vo.setDay3(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 4) {
						vo.setDay4(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 5) {
						vo.setDay5(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 6) {
						vo.setDay6(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 7) {
						vo.setDay7(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 8) {
						vo.setDay8(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 9) {
						vo.setDay9(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 10) {
						vo.setDay10(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 11) {
						vo.setDay11(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 12) {
						vo.setDay12(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 13) {
						vo.setDay13(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 14) {
						vo.setDay14(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 30) {
						vo.setDay30(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 60) {
						vo.setDay60(playerRemain.getPlayer());
					} else if(playerRemain.getDayCount() == 90) {
						vo.setDay90(playerRemain.getPlayer());
					}
				}
				result.add(vo);
			}
		}
		
		Collections.sort(result, PlayerRemainController.COMPARATOR);
		
		PageData<PlayerRemainVo> pageData = new PageData<>();
		pageData.setCount(result.size());
		pageData.setData(result);
		return pageData;
	}
	
	@ApiDocument("手动触发昨天付费留存数据统计")
	@RequestMapping(value = "/playerPayRemain/manual.auth")
	@ResponseBody
	public void manual() {
		playerPayRemainService.countPayRemain(DateTimeUtil.currMillis());
	}
}
