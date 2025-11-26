/**
 * 
 */
package com.xiugou.x1.backstage.module.player.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.ConsoleUtil;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannelServer;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelServerService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.model.PlayerRemain;
import com.xiugou.x1.backstage.module.player.struct.LoginCount;
import com.xiugou.x1.backstage.module.system.model.SystemCounter;
import com.xiugou.x1.backstage.module.system.service.SystemCounterService;

import pojo.xiugou.x1.pojo.log.player.PlayerLoginLog;

/**
 * @author YY
 *
 */
@Service
public class PlayerRemainService extends AbstractService<PlayerRemain> {

	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private GameChannelServerService gameChannelServerService;
	@Autowired
	private GameServerService gameServerService;
	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private SystemCounterService systemCounterService;
	
	public void insertUpdate(List<PlayerRemain> remains) {
		this.repository().getBaseDao().insertUpdate(remains);
	}
	
	public void runInSchedule() {
		SystemCounter systemCounter = systemCounterService.instance();
		if(DateTimeUtil.currMillis() < systemCounter.getRemainTime()) {
			return;
		}
		systemCounter.setRemainTime(DateTimeUtil.tomorrowZeroMillis());
		systemCounterService.update(systemCounter);
		
		countRemain(DateTimeUtil.todayZeroMillis());
	}
	
	/**
	 * 统计留存
	 * @param dateTime 某天的任一时间点
	 */
	public void countRemain(long dateTimeMillis) {
		//今天统计的是昨天的数据
		dateTimeMillis = dateTimeMillis - DateTimeUtil.ONE_DAY_MILLIS;
		
		LogTable logTable = PlayerLoginLog.class.getAnnotation(LogTable.class);
		
		String yearMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, dateTimeMillis);
		String tableName = logTable.name() + "_" + yearMonth;
		
		String currYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, dateTimeMillis);
		long nextDateTime = dateTimeMillis + DateTimeUtil.ONE_DAY_MILLIS;
		String nextYMD = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nextDateTime);
		
		logger.info("开始{}的留存统计", currYMD);
		
		List<GameChannel> channels = gameChannelService.getEntities();
		for(GameChannel channel : channels) {
			List<GameChannelServer> relations = gameChannelServerService.getEntityList(channel.getId());
			
			List<PlayerRemain> remainList = new ArrayList<>();
			
			for(GameChannelServer relation : relations) {
				GameServer gameServer = gameServerService.getEntity(relation.getServerUid());
				if(gameServer == null) {
					logger.error("进行渠道{}-{}下服务器{}的留存统计中，未找到对应服务器", channel.getId(), channel.getName(), relation.getServerUid());
					continue;
				}
				logger.info("进行渠道{}-{}下服务器{}-{}的留存统计", channel.getId(), channel.getName(), gameServer.getId(), gameServer.getName());
				
				DataBase dataBase = dataBaseManager.getLogDb(gameServer.getId());
				if(dataBase == null) {
					logger.error("进行渠道{}-{}下服务器{}的留存统计中，未定义数据库连接信息", channel.getId(), channel.getName(), relation.getServerUid());
					continue;
				}
				
				String sql = "SELECT DATE_FORMAT(born_time, '%Y%m%d') AS bornDate, DATEDIFF(login_time, born_time) AS dayCount, count(DISTINCT pid) AS playerCount FROM "
						+ tableName
						+ " WHERE channel_id = ? AND login_time >= ? AND login_time < ? GROUP BY DATE_FORMAT(born_time, '%Y%m%d'), DATEDIFF(login_time, born_time)";
				
				try {
					List<LoginCount> results = ReadOnlyDao.queryAliasObjects(dataBase, LoginCount.class, sql, channel.getId(), currYMD, nextYMD);
					
					for(LoginCount loginCount : results) {
						PlayerRemain playerRemain = new PlayerRemain();
						playerRemain.setBorn(loginCount.getBornDate());
						playerRemain.setDayCount(loginCount.getDayCount());
						playerRemain.setChannelId(channel.getId());
						playerRemain.setServerUid(gameServer.getId());
						playerRemain.setPlayer(loginCount.getPlayerCount());
						remainList.add(playerRemain);
					}
				} catch (Exception e) {
					logger.error("统计留存发生异常", e);
				}
			}
			//更新留存数据
			this.insertUpdate(remainList);
		}
		logger.info("完成{}的留存统计", currYMD);
	}
	
	public List<PlayerRemain> queryRemain(QuerySet query) {
		return this.repository().getBaseDao().queryListWhere(query.getWhere(), query.getParams());
	}
	
	@PostConstruct
	public void test1() {
		ConsoleUtil.addFunction("remain", () -> {
			test();
		});
	}
	
	public void test() {
//		this.countRemain(DateTimeUtil.currMillis() + DateTimeUtil.ONE_DAY_MILLIS * 2);
		for(int i = 1; i < 30; i++) {
			this.countRemain(DateTimeUtil.monthZeroMillis() + DateTimeUtil.ONE_DAY_MILLIS * i);
		}
	}
}
