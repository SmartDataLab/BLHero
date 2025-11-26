/**
 * 
 */
package com.xiugou.x1.game.server.module.server.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.db.mysql.dao.LogBaseDao;
import org.gaming.db.mysql.dao.OriginDao;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.JvmUtil;
import org.gaming.tool.JvmUtil.MemoryInfo;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.RechargeProductCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.backstage.BackstagePoster;
import com.xiugou.x1.game.server.module.battle.processor.BattleManager;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerDailyResetMessage;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.server.event.ServerOpenEvent;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.struct.QueryNumber;
import com.xiugou.x1.game.server.module.server.struct.QueryRecharge;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.log.player.PlayerLoginLog;
import pojo.xiugou.x1.pojo.log.server.ServerResumeLog;
import pojo.xiugou.x1.pojo.module.server.form.ServerRuntimeForm;

/**
 * @author YY
 *
 */
@Service
public class ServerInfoService extends OneToOneService<ServerInfo> implements Lifecycle {

	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private TimeSetting timeSetting;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private RechargeProductCache rechargeProductCache;
	@Autowired
	private BackstagePoster backstagePoster;
	@Autowired
	private PlayerService playerService;
	
	@Override
	protected ServerInfo createWhenNull(long entityId) {
		return null;
	}
	
	public ServerInfo getMain() {
		ServerInfo entity = this.getEntity(applicationSettings.getGameServerId());
		if(entity == null) {
			synchronized (this) {
				entity = this.getEntity(applicationSettings.getGameServerId());
				if(entity == null) {
					entity = new ServerInfo();
					entity.setId(applicationSettings.getGameServerId());
					if(applicationSettings.getGameOpenType() == 1) {
						//以服务器启动时间为开服时间
						entity.setOpenTime(LocalDateTime.now());
					} else {
						//以开服时间为开服时间
						entity.setOpenTime(LocalDateTime.of(2099, 12, 31, 23, 59));
					}
					this.insert(entity);
				}
			}
		}
		return entity;
	}
	
	public boolean isOpen() {
		ServerInfo serverInfo = this.getMain();
		return serverInfo.isOpened();
	}
	
	public boolean isMaintain() {
		ServerInfo serverInfo = this.getMain();
		return serverInfo.isMaintain();
	}
	
	/**
	 * 获取服务器已经开服的天数
	 * @return
	 */
	public int getOpenedDay() {
		ServerInfo serverInfo = this.getMain();
		int day = timeSetting.daysFromTimeToNow(serverInfo.getOpenTime());
		if(day < 0) {
			day = 0;
		}
		return day;
	}
	
	protected void runForOpen() {
		ServerInfo serverInfo = this.getMain();
		if(serverInfo.isOpened()) {
			//服务器已经执行过开服逻辑
			return;
		}
		if(LocalDateTimeUtil.now().isAfter(serverInfo.getOpenTime())) {
			serverInfo.setOpened(true);
			this.update(serverInfo);
			logger.info("服务器{}执行开服逻辑，开服时间{}", applicationSettings.getGameServerId(), serverInfo.getOpenTime());
			EventBus.post(new ServerOpenEvent());
		}
	}
	
	//游戏汇总统计
	protected void runForResume() {
		//玩家数据是5、8点重置，数据统计是每日0点
		ServerInfo serverInfo = this.getMain();
		if(!timeSetting.needReset(serverInfo.getResumeCountTime())) {
			return;
		}
		serverInfo.setResumeCountTime(LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.tomorrowZeroMillis()));
		this.update(serverInfo);
		
		//今天统计的是昨天的数据
		resumeCount(DateTimeUtil.currMillis());
	}
	
	/**
	 * 统计某个时间的昨天的游戏汇总数据
	 * @param millisTime
	 */
	protected void resumeCount(long millisTime) {
		String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, millisTime - DateTimeUtil.ONE_DAY_MILLIS);
		logger.info("统计{}的游戏汇总数据开始", dateStr);
		
		long todayZeroMillis = DateTimeUtil.somedayZeroMillis(millisTime);
		String todayZero = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, todayZeroMillis);
		
		long yesterdayZeroMillis = todayZeroMillis - DateTimeUtil.ONE_DAY_MILLIS;
		String yesterdayZero = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, yesterdayZeroMillis);
		
		//今天统计的是昨天的数据
		OriginDao<Player> playerDao = SlimDao.getDao(Player.class);
		long playerNum = playerDao.count("where insert_time <= ?", new Object[] {todayZero});
		long newPlayerNum = playerDao.count("where insert_time >= ? and insert_time <= ?", new Object[] {yesterdayZero, todayZero});
		
		LogBaseDao<PlayerLoginLog> playerLoginLogDao = (LogBaseDao<PlayerLoginLog>)SlimDao.getDao(PlayerLoginLog.class);
		
		String playerLoginLogTable = playerLoginLogDao.getTableName(yesterdayZeroMillis);
		QueryNumber loginPlayerNum = playerLoginLogDao.queryAliasObject(QueryNumber.class, "select count(DISTINCT pid) as number from " + playerLoginLogTable + " where login_time >= ? and login_time <= ?", yesterdayZero, todayZero);
		long loginNum = playerLoginLogDao.count(playerLoginLogTable, "where login_time >= ? and login_time <= ?", yesterdayZero, todayZero);
		
		long oldLoginNum = loginPlayerNum.getNumber() - newPlayerNum;
		
		OriginDao<Recharge> rechargeDao = SlimDao.getDao(Recharge.class);
		List<QueryRecharge> queryRecharges = rechargeDao.queryAliasObjects(QueryRecharge.class,
				"select player_id as playerId, pay_money as payMoney, product_id as productId from recharge "
						+ "where status = 1 and test != true and insert_time >= ? and insert_time <= ?",
				yesterdayZero, todayZero);
		
		Set<Long> playerSet = new HashSet<>();
		long rechargeNum = 0;
		long rechargeForDiamond = 0;
		for(QueryRecharge queryRecharge : queryRecharges) {
			playerSet.add(queryRecharge.getPlayerId());
			rechargeNum += queryRecharge.getPayMoney();
			RechargeProductCfg rechargeProductCfg = rechargeProductCache.getOrNull(queryRecharge.getProductId());
			if(rechargeProductCfg != null && rechargeProductCfg.getProductType() == ProductType.NORMAL.getValue()) {
				rechargeForDiamond += queryRecharge.getPayMoney();
			}
		}
		long rechargePlayerNum = playerSet.size();
		
		ServerResumeLog resumeLog = new ServerResumeLog();
		resumeLog.setDateStr(dateStr);
		resumeLog.setPlayerNum(playerNum);
		resumeLog.setNewPlayerNum(newPlayerNum);
		resumeLog.setLoginPlayerNum(loginPlayerNum.getNumber());
		resumeLog.setLoginNum(loginNum);
		resumeLog.setOldLoginNum(oldLoginNum);
		resumeLog.setRechargeNum(rechargeNum);
		resumeLog.setRechargePlayerNum(rechargePlayerNum);
		resumeLog.setRechargeForDiamond(rechargeForDiamond);
		SlimDao.getDao(ServerResumeLog.class).insertUpdate(Collections.singletonList(resumeLog));
		logger.info("统计{}的游戏汇总数据完成", dateStr);
	}
	
	//每日重置
	protected void runForDailyReset() {
		ServerInfo serverInfo = this.getMain();
		if(!timeSetting.needReset(serverInfo.getDailyTime())) {
			return;
		}
		serverInfo.setDailyTime(timeSetting.tomorrowOTime());
		this.update(serverInfo);
		
		for(PlayerContext playerContext : playerContextManager.onlines()) {
			playerContext.tell(new PlayerDailyResetMessage(playerContext));
		}
	}
	
	//服务器心跳
	protected void runForHeartBeat() {
		ServerInfo serverInfo = this.getMain();
		if(!timeSetting.needReset(serverInfo.getHeartBeatTime())) {
			return;
		}
		serverInfo.setHeartBeatTime(LocalDateTime.now().plusSeconds(60));
		this.update(serverInfo);
		
		MemoryInfo memoryInfo = JvmUtil.scanMemory();
		
		ServerRuntimeForm form = new ServerRuntimeForm();
		form.setRunning(true);
		form.setPlatformId(applicationSettings.getGameServerPlatformid());
		form.setServerId(applicationSettings.getGameServerId());
		form.setPlayerNum(playerService.getPlayerNum());
		form.setOnlineNum(playerContextManager.onlines().size());
		form.setBattleNum(BattleManager.getTotalBattleNum());
		form.setCurrBattleNum(BattleManager.getCurrBattleNum());
		form.setMaxMemory(memoryInfo.getMaxMemory());
		form.setFreeMemory(memoryInfo.getFreeMemory());
		form.setTotalMemory(memoryInfo.getTotalMemory());
		form.setLeftMemory(memoryInfo.getLeftMemory());
		form.setUsedMemory(memoryInfo.getUsedMemory());
		backstagePoster.jsonPost(GameApi.heartBeat, form);
	}

	@Override
	public void stop() throws Exception {
		ServerRuntimeForm form = new ServerRuntimeForm();
		form.setRunning(false);
		form.setPlatformId(applicationSettings.getGameServerPlatformid());
		form.setServerId(applicationSettings.getGameServerId());
		form.setPlayerNum(playerService.getPlayerNum());
		form.setOnlineNum(playerContextManager.onlines().size());
		form.setBattleNum(BattleManager.getTotalBattleNum());
		form.setCurrBattleNum(BattleManager.getCurrBattleNum());
		form.setMaxMemory("");
		form.setFreeMemory("");
		form.setTotalMemory("");
		form.setLeftMemory("");
		form.setUsedMemory("");
		backstagePoster.jsonPost(GameApi.heartBeat, form);
	}
}
