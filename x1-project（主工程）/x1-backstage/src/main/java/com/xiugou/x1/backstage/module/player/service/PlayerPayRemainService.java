/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.ConsoleUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.model.PlayerPayRemain;
import com.xiugou.x1.backstage.module.player.struct.BornPayCount;
import com.xiugou.x1.backstage.module.player.struct.PayRemainCount;
import com.xiugou.x1.backstage.module.system.model.SystemCounter;
import com.xiugou.x1.backstage.module.system.service.SystemCounterService;

import pojo.xiugou.x1.pojo.log.player.PlayerLoginLog;

/**
 * @author YY
 *
 */
@Service
public class PlayerPayRemainService extends AbstractService<PlayerPayRemain> {

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
	
	public void insertUpdate(List<PlayerPayRemain> remains) {
		this.repository().getBaseDao().insertUpdate(remains);
	}
	
	public List<PlayerPayRemain> queryPayRemain(QuerySet query) {
		return this.repository().getBaseDao().queryListWhere(query.getWhere(), query.getParams());
	}
	
	public void runInSchedule() {
		SystemCounter systemCounter = systemCounterService.instance();
		if(DateTimeUtil.currMillis() < systemCounter.getPayRemainTime()) {
			return;
		}
		systemCounter.setPayRemainTime(DateTimeUtil.tomorrowZeroMillis());
		systemCounterService.update(systemCounter);
		
		countPayRemain(DateTimeUtil.todayZeroMillis());
	}
	
	public void countPayRemain(long dateTimeMillis) {
		//今天统计的是昨天的数据
		dateTimeMillis = dateTimeMillis - DateTimeUtil.ONE_DAY_MILLIS;
		
		LogTable logTable = PlayerLoginLog.class.getAnnotation(LogTable.class);
		
		String yearMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, dateTimeMillis);
		String tableName = logTable.name() + "_" + yearMonth;
		
		String currYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, dateTimeMillis);
		long nextDateTime = dateTimeMillis + DateTimeUtil.ONE_DAY_MILLIS;
		String nextYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nextDateTime);
		
		logger.info("开始{}的付费留存统计", currYMD);
		
		List<GameChannel> channels = gameChannelService.getEntities();
		for(GameChannel channel : channels) {
			List<GameChannelServer> relations = gameChannelServerService.getEntityList(channel.getId());
			
			List<PlayerPayRemain> payRemainList = new ArrayList<>();
			
			for(GameChannelServer relation : relations) {
				GameServer gameServer = gameServerService.getEntity(relation.getServerUid());
				if(gameServer == null) {
					logger.error("进行渠道{}-{}下服务器{}的付费留存统计中，未找到对应服务器", channel.getId(), channel.getName(), relation.getServerUid());
					continue;
				}
				logger.info("进行渠道{}-{}下服务器{}-{}的付费留存统计", channel.getId(), channel.getName(), gameServer.getId(), gameServer.getName());
				
				DataBase dataBase = dataBaseManager.getLogDb(gameServer.getId());
				if(dataBase == null) {
					logger.error("进行渠道{}-{}下服务器{}的付费留存统计中，未定义数据库连接信息", channel.getId(), channel.getName(), relation.getServerUid());
					continue;
				}
				
				//1、统计创号人数
				String countPayBaseSql = "SELECT DATE_FORMAT(born_time,'%Y%m%d') AS bornDate, count(DISTINCT pid) AS playerCount "
						+ "FROM `player_recharge_log` WHERE channel_id = ? AND born_time >= ? AND born_time < ? GROUP BY DATE_FORMAT(born_time,'%Y%m%d')";
				
				List<BornPayCount> baseResults = ReadOnlyDao.queryAliasObjects(dataBase, BornPayCount.class, countPayBaseSql, channel.getId(), currYMD, nextYMD);
				Map<String, BornPayCount> baseResultMap = ListMapUtil.listToMap(baseResults, BornPayCount::getBornDate);
				
				//2、统计当天的付费登录
				String countPayRemainSql = "select DATE_FORMAT(born_time, '%Y%m%d') AS bornDate, DATEDIFF(login_time, born_time) AS dayCount, count(DISTINCT pid) AS playerCount FROM "
						+ tableName
						+ " WHERE channel_id = ? AND create_pay = 1 AND login_time >= ? and login_time < ? GROUP BY DATE_FORMAT(born_time, '%Y%m%d'), DATEDIFF(login_time, born_time)";
				
				try {
					List<PayRemainCount> remainResults = ReadOnlyDao.queryAliasObjects(dataBase, PayRemainCount.class, countPayRemainSql, channel.getId(), currYMD, nextYMD);
					for(PayRemainCount payRemainCount : remainResults) {
						BornPayCount bornPayCount = baseResultMap.get(payRemainCount.getBornDate());
						
						PlayerPayRemain playerPayRemain = new PlayerPayRemain();
						playerPayRemain.setChannelId(channel.getId());
						playerPayRemain.setServerUid(gameServer.getId());
						playerPayRemain.setBorn(payRemainCount.getBornDate());
						playerPayRemain.setDayCount(payRemainCount.getDayCount());
						if(payRemainCount.getDayCount() == 0) {
							playerPayRemain.setPlayer(bornPayCount != null ? bornPayCount.getPlayerCount() : 0);
						} else {
							playerPayRemain.setPlayer(payRemainCount.getPlayerCount());
						}
						payRemainList.add(playerPayRemain);
					}
				} catch (Exception e) {
					if(e.getCause() instanceof SQLSyntaxErrorException) {
						logger.error("统计付费留存发生异常，{}", e.getCause().getMessage());
					} else {
						logger.error("统计付费留存发生异常", e);
					}
				}
			}
			this.insertUpdate(payRemainList);
		}
		logger.info("完成{}的付费留存统计", currYMD);
	}
	
	@PostConstruct
	public void test1() {
		ConsoleUtil.addFunction("pay", () -> {
			test();
		});
	}
	
	public void test() {
		for(int i = 1; i < 30; i++) {
			this.countPayRemain(DateTimeUtil.monthZeroMillis() + DateTimeUtil.ONE_DAY_MILLIS * i);
		}
	}
}
