/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.xiugou.x1.backstage.module.player.model.PlayerLTV;
import com.xiugou.x1.backstage.module.player.service.PlayerLTVService;
import com.xiugou.x1.backstage.module.player.struct.PlayeLTVQuery;
import com.xiugou.x1.backstage.module.player.vo.PlayerLtvVo;

/**
 * @author YY
 *
 */
@Controller
public class PlayerLTVController {

	private static Logger logger = LoggerFactory.getLogger(PlayerLTVController.class);
	
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private PlayerLTVService playerLTVService;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("请求付费LTV数据")
	@RequestMapping(value = "/playerLtv/data.auth")
	@ResponseBody
	public PageData<PlayerLtvVo> data(PlayeLTVQuery query) {
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
		
		long endTime = query.getEndTime() * 1000L;
		if(endTime > 0) {
			String endBorn = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime);
			querySet.addCondition("born <= ?", endBorn);
		}
		querySet.addCondition("day_count <= ?", 90);
		querySet.formWhere();
		
		List<PlayerLTV> playerLTVs = playerLTVService.queryLTV(querySet);
		Map<Long, List<PlayerLTV>> map = ListMapUtil.fillListMap(playerLTVs, PlayerLTV::getServerUid);
		
		List<PlayerLtvVo> resultList = new ArrayList<>();
		
		for(Entry<Long, List<PlayerLTV>> outEntry : map.entrySet()) {
			long serverUid = outEntry.getKey();
			GameServer gameServer = gameServerService.getEntity(serverUid);
			
			List<PlayerLTV> ltvList = outEntry.getValue();
			Collections.sort(ltvList, LTV_COMPARATOR);
			
			PlayerLtvVo firstDayLtvVo = new PlayerLtvVo();
			firstDayLtvVo.setServerUid(serverUid);
			firstDayLtvVo.setServerId(gameServer.getServerId());
			firstDayLtvVo.setServerName(gameServer.getName());
			for(int i = 0; i < ltvList.size(); i++) {
				PlayerLTV playerLTV = ltvList.get(i);
				//获取导量人数最多的一天
				if(playerLTV.getPlayerCount() > firstDayLtvVo.getPlayerCount()) {
					firstDayLtvVo.setBornDate(playerLTV.getBorn());
					firstDayLtvVo.setPlayerCount(playerLTV.getPlayerCount());
				}
				
				if(playerLTV.getDayCount() <= 0) {
					firstDayLtvVo.setDay0(firstDayLtvVo.getDay0() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 1) {
					firstDayLtvVo.setDay1(firstDayLtvVo.getDay1() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 2) {
					firstDayLtvVo.setDay2(firstDayLtvVo.getDay2() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 3) {
					firstDayLtvVo.setDay3(firstDayLtvVo.getDay3() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 4) {
					firstDayLtvVo.setDay4(firstDayLtvVo.getDay4() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 5) {
					firstDayLtvVo.setDay5(firstDayLtvVo.getDay5() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 6) {
					firstDayLtvVo.setDay6(firstDayLtvVo.getDay6() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 7) {
					firstDayLtvVo.setDay7(firstDayLtvVo.getDay7() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 8) {
					firstDayLtvVo.setDay8(firstDayLtvVo.getDay8() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 9) {
					firstDayLtvVo.setDay9(firstDayLtvVo.getDay9() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 10) {
					firstDayLtvVo.setDay10(firstDayLtvVo.getDay10() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 11) {
					firstDayLtvVo.setDay11(firstDayLtvVo.getDay11() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 12) {
					firstDayLtvVo.setDay12(firstDayLtvVo.getDay12() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 13) {
					firstDayLtvVo.setDay13(firstDayLtvVo.getDay13() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 14) {
					firstDayLtvVo.setDay14(firstDayLtvVo.getDay14() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 30) {
					firstDayLtvVo.setDay30(firstDayLtvVo.getDay30() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 60) {
					firstDayLtvVo.setDay60(firstDayLtvVo.getDay60() + playerLTV.getMoney());
				}
				if(playerLTV.getDayCount() <= 90) {
					firstDayLtvVo.setDay90(firstDayLtvVo.getDay90() + playerLTV.getMoney());
				}
			}
			resultList.add(firstDayLtvVo);
		}
		
		Collections.sort(resultList, COMPARATOR);
		
		PageData<PlayerLtvVo> pageData = new PageData<>();
		pageData.setCount(resultList.size());
		pageData.setData(resultList);
		return pageData;
	}
	
	public static Comparator<PlayerLtvVo> COMPARATOR = new Comparator<PlayerLtvVo>() {
		@Override
		public int compare(PlayerLtvVo o1, PlayerLtvVo o2) {
			int result = Long.compare(o2.getServerUid(), o1.getServerUid());
			if(result != 0) {
				return result;
			}
			return -o1.getBornDate().compareTo(o2.getBornDate());
		}
	};
	
	public static Comparator<PlayerLTV> LTV_COMPARATOR = new Comparator<PlayerLTV>() {
		@Override
		public int compare(PlayerLTV o1, PlayerLTV o2) {
			int result = o1.getBorn().compareTo(o2.getBorn());
			if(result != 0) {
				return result;
			}
			return Integer.compare(o2.getDayCount(), o1.getDayCount());
		}
	};
	
	@ApiDocument("手动触发昨天LTV数据统计")
	@RequestMapping(value = "/playerLtv/manual.auth")
	@ResponseBody
	public void manual(@RequestParam("command") String command) {
		try {
			String[] parts = command.split("_");
			logger.info("收到的命令{}", command);
			long channelId = Long.parseLong(parts[0]);
			int serverUid = Integer.parseInt(parts[1]);
			int dayBefore = Integer.parseInt(parts[2]);
			
			GameChannel gameChannel = gameChannelService.getEntity(channelId);
			
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime time = now.minusDays(dayBefore);
			
			List<PlayerLTV> resultList = new ArrayList<>();
			for(int i = 0; i < dayBefore; i++) {
				long dateTimeMillis = LocalDateTimeUtil.toEpochMilli(time) + DateTimeUtil.ONE_DAY_MILLIS * i;
				String currYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, dateTimeMillis);
				long nextDateTime = dateTimeMillis + DateTimeUtil.ONE_DAY_MILLIS;
				String nextYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nextDateTime);
				
				List<PlayerLTV> list = playerLTVService.countLTV(gameChannel, serverUid, currYMD, nextYMD);
				logger.info("重算服务器{}时间{}的LTV数据", serverUid, currYMD);
				
				resultList.addAll(list);
			}
			playerLTVService.insertUpdate(resultList);
		} catch (Exception e) {
			Asserts.isTrue(false, TipsCode.WHAT_DO_YOU_WANT_TO_DO);
		}
	}
}
