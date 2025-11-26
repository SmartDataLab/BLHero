/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.ConsoleUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.model.PlayerLTV;
import com.xiugou.x1.backstage.module.player.struct.CreateCount;
import com.xiugou.x1.backstage.module.player.struct.RechargeCount;
import com.xiugou.x1.backstage.module.system.model.SystemCounter;
import com.xiugou.x1.backstage.module.system.service.SystemCounterService;

/**
 * @author YY
 *
 */
@Service
public class PlayerLTVService extends AbstractService<PlayerLTV> {

	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private SystemCounterService systemCounterService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	
	public void insertUpdate(List<PlayerLTV> ltvs) {
		this.repository().getBaseDao().insertUpdate(ltvs);
	}
	
	public void runInSchedule() {
		SystemCounter systemCounter = systemCounterService.instance();
		if(DateTimeUtil.currMillis() < systemCounter.getLtvTime()) {
			return;
		}
		//12小时刷新一次
		systemCounter.setLtvTime(DateTimeUtil.currMillis() + DateTimeUtil.ONE_HOUR_MILLIS * 12);
		systemCounterService.update(systemCounter);
		
		countLTV(DateTimeUtil.todayZeroMillis());
	}
	
	
	public void countLTV(long dateTimeMillis) {
		//今天统计的是昨天的数据
		dateTimeMillis = dateTimeMillis - DateTimeUtil.ONE_DAY_MILLIS;
		
		String currYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, dateTimeMillis);
		long nextDateTime = dateTimeMillis + DateTimeUtil.ONE_DAY_MILLIS;
		String nextYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nextDateTime);
		
		logger.info("开始{}的LTV统计", currYMD);
		
		List<GameChannel> channels = gameChannelService.getEntities();
		for(GameChannel channel : channels) {
			List<PlayerLTV> ltvList = new ArrayList<>();
			
			List<GameChannelServer> relations = gameChannelServerService.getEntityList(channel.getId());
			
			for(GameChannelServer relation : relations) {
				List<PlayerLTV> results = countLTV(channel, relation.getServerUid(), currYMD, nextYMD);
				ltvList.addAll(results);
			}
			this.insertUpdate(ltvList);
		}
		logger.info("完成{}的LTV统计", currYMD);
	}
	
	public List<PlayerLTV> queryLTV(QuerySet query) {
		return this.repository().getBaseDao().queryListWhere(query.getWhere(), query.getParams());
	}
	
	@PostConstruct
	public void test1() {
		ConsoleUtil.addFunction("ltv", () -> {
			test();
		});
	}
	
	public void test() {
		LocalDateTime time = LocalDateTime.of(2024, 1, 11, 0, 0);
		
		List<PlayerLTV> list = new ArrayList<>();
		for(int i = 0; i < 7; i++) {
//			this.countLTV(LocalDateTimeUtil.toEpochMilli(time) + DateTimeUtil.ONE_DAY_MILLIS * i);
			
			GameChannel channel = gameChannelService.getEntity(1L);
			
			long dateTimeMillis = LocalDateTimeUtil.toEpochMilli(time) + DateTimeUtil.ONE_DAY_MILLIS * i;
			String currYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, dateTimeMillis);
			long nextDateTime = dateTimeMillis + DateTimeUtil.ONE_DAY_MILLIS;
			String nextYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nextDateTime);
			
			list.addAll(countLTV(channel, 10004, currYMD, nextYMD));
		}
		this.insertUpdate(list);
	}
	
	/**
	 * 
	 * @param channel
	 * @param serverUid
	 * @param currYMD 格式yyyy-MM-dd
	 * @param nextYMD 格式yyyy-MM-dd
	 * @return
	 */
	public List<PlayerLTV> countLTV(GameChannel channel, int serverUid, String currYMD, String nextYMD) {
		GameServer gameServer = gameServerService.getEntity(serverUid);
		if(gameServer == null) {
			logger.error("进行渠道{}-{}下服务器{}的LTV统计中，未找到对应服务器", channel.getId(), channel.getName(), serverUid);
			return Collections.emptyList();
		}
		logger.info("进行渠道{}-{}下服务器{}-{}的LTV统计", channel.getId(), channel.getName(), gameServer.getId(), gameServer.getName());
		
		try {
			DataBase dataBase = dataBaseManager.getLogDb(gameServer.getId());
			if(dataBase == null) {
				logger.error("进行渠道{}-{}下服务器{}的LTV统计中，未定义数据库连接信息", channel.getId(), channel.getName(), serverUid);
				return Collections.emptyList();
			}
			
			//1、统计创号人数
			String countCreateSql = "SELECT DATE_FORMAT(born_time, '%Y%m%d') AS bornDate, count(1) AS playerCount "
					+ "FROM player_create_log WHERE channel_id = ? AND born_time >= ? GROUP BY DATE_FORMAT(born_time, '%Y%m%d')";
		
			List<CreateCount> createResults = ReadOnlyDao.queryAliasObjects(dataBase, CreateCount.class, countCreateSql, channel.getId(), currYMD);
			
			//<日期，格式：20240101，统计数据>
			Map<String, CreateCount> createResultMap = ListMapUtil.listToMap(createResults, CreateCount::getBornDate);
			
			//2、统计充值金额
			String countRechargeSql = "SELECT DATE_FORMAT(born_time, '%Y%m%d') AS bornDate, DATEDIFF(recharge_time, born_time) AS dayCount, sum(money) AS money " + 
					"FROM player_recharge_log WHERE channel_id = ? AND recharge_time >= ? AND recharge_time < ? GROUP BY DATE_FORMAT(born_time, '%Y%m%d'), DATEDIFF(recharge_time, born_time)";
			
			List<RechargeCount> rechargeResults = ReadOnlyDao.queryAliasObjects(dataBase, RechargeCount.class, countRechargeSql, channel.getId(), currYMD, nextYMD);
			
			List<PlayerLTV> ltvList = new ArrayList<>();
			Set<String> ltvDataKeys = new HashSet<>();
			for(RechargeCount rechargeCount : rechargeResults) {
				CreateCount createCount = createResultMap.get(rechargeCount.getBornDate());
				
				PlayerLTV playerLTV = new PlayerLTV();
				playerLTV.setChannelId(channel.getId());
				playerLTV.setServerUid(gameServer.getId());
				playerLTV.setBorn(rechargeCount.getBornDate());
				playerLTV.setDayCount(rechargeCount.getDayCount());
				playerLTV.setMoney(rechargeCount.getMoney());
				playerLTV.setPlayerCount(createCount == null ? 0 : createCount.getPlayerCount());
				ltvList.add(playerLTV);
				ltvDataKeys.add(channel.getId() + "_" + rechargeCount.getBornDate() + "_" + rechargeCount.getDayCount());
			}
			long dateTime = DateTimeUtil.stringToMillis(DateTimeUtil.YYYY_MM_DD, currYMD);
			
			String bornDate = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, dateTime);
			String currDateKey = channel.getId() + "_" + bornDate + "_0";
			//补当前统计日期的数据，当某一天有创号，但是该创号的玩家没有进行过充值时，则需要补创号当天的数据
			if(!ltvDataKeys.contains(currDateKey)) {
				CreateCount createCount = createResultMap.get(bornDate);
				
				PlayerLTV playerLTV = new PlayerLTV();
				playerLTV.setChannelId(channel.getId());
				playerLTV.setServerUid(gameServer.getId());
				playerLTV.setBorn(bornDate);
				playerLTV.setDayCount(0);
				playerLTV.setMoney(0);
				playerLTV.setPlayerCount(createCount == null ? 0 : createCount.getPlayerCount());
				ltvList.add(playerLTV);
			}
			return ltvList;
		} catch (Exception e) {
			if(e.getCause() instanceof SQLSyntaxErrorException) {
				logger.error("统计付费留存发生异常，{}", e.getCause().getMessage());
			} else {
				logger.error("统计付费留存发生异常", e);
			}
			return Collections.emptyList();
		}
	}
}
