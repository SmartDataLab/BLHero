/**
 * 
 */
package com.xiugou.x1.backstage.module.recharge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.player.model.Player;
import com.xiugou.x1.backstage.module.player.service.PlayerService;
import com.xiugou.x1.backstage.module.recharge.model.RechargeOneDate;
import com.xiugou.x1.backstage.module.recharge.model.RechargeToday;
import com.xiugou.x1.backstage.module.recharge.struct.RechargeTodayPlayerCount;
import com.xiugou.x1.backstage.module.recharge.vo.RechargeRankVo;

/**
 * @author YY
 *
 */
@Service
public class RechargeOneDateService extends AbstractService<RechargeOneDate> {

	@Autowired
	private PlayerService playerService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	
	private List<RechargeOneDate> getOneDate(QuerySet querySet) {
		return this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
	}
	
	private void insertUpdate(List<RechargeOneDate> oneDates) {
		this.repository().getBaseDao().insertUpdate(oneDates);
	}
	
	public List<RechargeRankVo> queryRank(QuerySet querySet) {
		String sql = "SELECT player_id AS playerId, sum(total_pay) AS money, max(last_recharge_time) AS lastRechargeTime "
				+ "FROM recharge_one_date " + querySet.getWhere();
		return this.repository().getBaseDao().queryAliasObjects(RechargeRankVo.class, sql, querySet.getParams());
	}
	
	/**
	 * 更新某日充值
	 * @param channelId
	 * @param todayList
	 */
	public void updateRank(long channelId, List<RechargeToday> todayList) {
		
		Map<String, List<RechargeToday>> dateSplitMap = new HashMap<>();
		for(RechargeToday rechargeToday : todayList) {
			String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, LocalDateTimeUtil.toEpochMilli(rechargeToday.getInsertTime()));
			List<RechargeToday> list = dateSplitMap.get(dateStr);
			if(list == null) {
				list = new ArrayList<>();
				dateSplitMap.put(dateStr, list);
			}
			list.add(rechargeToday);
		}
		for(Entry<String, List<RechargeToday>> entry : dateSplitMap.entrySet()) {
			updateInOneDate(channelId, entry.getKey(), entry.getValue());
		}
	}
	
	private void updateInOneDate(long channelId, String dateStr, List<RechargeToday> todayList) {
		Map<Long, RechargeTodayPlayerCount> playerCounts = new HashMap<>();
		
		for(RechargeToday rechargeToday : todayList) {
			//统计玩家充值排名
			RechargeTodayPlayerCount playerCount = playerCounts.get(rechargeToday.getPlayerId());
			if(playerCount == null) {
				playerCount = new RechargeTodayPlayerCount();
				playerCount.setPlayerId(rechargeToday.getPlayerId());
				playerCounts.put(playerCount.getPlayerId(), playerCount);
			}
			playerCount.setMoney(playerCount.getMoney() + rechargeToday.getMoney());
			if(playerCount.getRechargeTime() == null) {
				playerCount.setRechargeTime(rechargeToday.getInsertTime());
			} else {
				if(rechargeToday.getInsertTime().isAfter(playerCount.getRechargeTime())) {
					playerCount.setRechargeTime(rechargeToday.getInsertTime());
				}
			}
		}
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		querySet.addCondition("date_str = ?", dateStr);
		querySet.findInSet("player_id", playerCounts.keySet());
		
		querySet.formWhere();
		List<RechargeOneDate> oneDates = this.getOneDate(querySet);
		Map<Long, RechargeOneDate> oneDateMap = ListMapUtil.listToMap(oneDates, RechargeOneDate::getPlayerId);
		
		for(RechargeTodayPlayerCount playerCount : playerCounts.values()) {
			RechargeOneDate recharge = oneDateMap.get(playerCount.getPlayerId());
			if(recharge == null) {
				recharge = new RechargeOneDate();
				recharge.setPlayerId(playerCount.getPlayerId());
				recharge.setChannelId(channelId);
				recharge.setDateStr(dateStr);
				oneDateMap.put(recharge.getPlayerId(), recharge);
			}
			Player player = playerService.getById(playerCount.getPlayerId());
			if(player != null) {
				recharge.setNick(player.getNick());
				
				GameChannelServer relation = gameChannelServerService.getEntity(channelId, player.getServerId());
				if(relation != null) {
					recharge.setServerUid(relation.getServerUid());
					recharge.setServerId(relation.getServerId());
					recharge.setServerName(relation.getServerName());
				}
			}
			recharge.setTotalPay(recharge.getTotalPay() + playerCount.getMoney());
			recharge.setLastRechargeTime(playerCount.getRechargeTime());
		}
		this.insertUpdate(new ArrayList<>(oneDateMap.values()));
	}
}
