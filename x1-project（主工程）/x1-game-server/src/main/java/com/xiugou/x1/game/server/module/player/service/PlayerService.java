/**
 *
 */
package com.xiugou.x1.game.server.module.player.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.redis.RedisCache;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.backstage.BackstagePoster;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.mainline.event.MainlineFormationChangeEvent;
import com.xiugou.x1.game.server.module.player.constant.PlayerOnlineEnum;
import com.xiugou.x1.game.server.module.player.event.PlayerFightingEvent;
import com.xiugou.x1.game.server.module.player.log.PlayerLogger;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.recharge.event.RechargeEvent;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;
import com.xiugou.x1.game.server.module.recharge.service.RechargePlayerService;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.log.player.PlayerFightingLog;
import pojo.xiugou.x1.pojo.log.player.PlayerOnlineLog;
import pojo.xiugou.x1.pojo.log.player.PlayerScatterLog;
import pojo.xiugou.x1.pojo.log.player.PlayerTimeLog;
import pojo.xiugou.x1.pojo.module.player.form.LoginDotTiming;
import pojo.xiugou.x1.pojo.module.player.form.LoginTimingTable;
import pojo.xiugou.x1.pojo.module.player.form.LoginTimingTable.LoginTiming;
import pojo.xiugou.x1.pojo.module.player.form.PlayerTable;
import pojo.xiugou.x1.pojo.module.player.form.PlayerTable.PlayerData;
import pojo.xiugou.x1.pojo.module.player.model.PlayerSnapshot;

/**
 * @author YY
 *
 */
@Service
public class PlayerService extends OneToOneService<Player> implements Lifecycle {

	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private PlayerLogger playerLogger;
	@Autowired
	private BackstagePoster backstagePoster;
	@Autowired
	private FormationService formationService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private RechargePlayerService rechargePlayerService;
	@Autowired
	private TimeSetting timeSetting;
	@Autowired
	private RedisCache redisCache;

	// 上报数据的队列
	private Queue<Player> reportQueue = new ConcurrentLinkedQueue<>();
	// 每天5点重置，每天登录过的人
	public final ConcurrentMap<Long, Integer> onlineCount = new ConcurrentHashMap<>();
	// 已创号的玩家账号ID缓存<openId_服务器ID，玩家对象>
	private final ConcurrentMap<String, Player> createdPlayers = new ConcurrentHashMap<>();
	
	private final ConcurrentMap<String, Integer> nickCache = new ConcurrentHashMap<>();
	// 刚刚创号的玩家
	private Queue<Player> freshPlayers = new ConcurrentLinkedQueue<>();

	@Override
	protected Player createWhenNull(long entityId) {
		Asserts.isTrue(false, TipsCode.PLAYER_NOT_EXIST, entityId);
		return null;
	}

	public boolean isPlayer(long playerId) {
		return this.getEntity(playerId) != null;
	}

	/**
	 * 打点玩家在线注册数据
	 */
	protected void recordTimeLog() {
		ServerInfo serverInfo = serverInfoService.getMain();
		if (LocalDateTimeUtil.now().isBefore(serverInfo.getOnlineCountTime())) {
			return;
		}
		// 每小时统计一次数据
		long currHourMillis = DateTimeUtil.currHourMillis();
		long now = DateTimeUtil.currMillis();
		for(int i = 0; i < 10; i++) {
			currHourMillis += DateTimeUtil.ONE_HOUR_MILLIS / 2;
			if(currHourMillis > now) {
				break;
			}
		}
		serverInfo.setOnlineCountTime(LocalDateTimeUtil.ofEpochMilli(currHourMillis));
		serverInfoService.update(serverInfo);

		saveTimeLog();
	}

	private void saveTimeLog() {
		long nowTime = DateTimeUtil.currMillis();
		String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nowTime);
		String hourStr = DateTimeUtil.formatMillis("HH:mm", nowTime);

		// 注册分时打点数据
		PlayerTimeLog log = new PlayerTimeLog();
		log.setDateStr(dateStr);
		log.setTimePeriod(hourStr);
		log.setMaxOnline(playerContextManager.getMaxOnline());
		log.setMinOnline(playerContextManager.getMinOnline());
		log.setCreateNum(playerContextManager.getCreateNum());
		log.setLoginNum(playerContextManager.getLoginNum());
		playerLogger.insertTimeLog(log);

		playerContextManager.resetCounter();
		logger.info("记录打点玩家在线注册日志");
	}

	/**
	 * 每5分钟打点玩家在线人数数据
	 */
	protected void recordOnlineLog() {
		ServerInfo serverInfo = serverInfoService.getMain();
		if (LocalDateTimeUtil.now().isBefore(serverInfo.getOnline5minuteTime())) {
			return;
		}
		// 每5分钟统计一次数据
		serverInfo.setOnline5minuteTime(LocalDateTimeUtil.now().plusMinutes(30));
		serverInfoService.update(serverInfo);

		saveOnlineLog();
	}

	private void saveOnlineLog() {
		long nowTime = DateTimeUtil.currMillis();
		String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, nowTime);

		long zeroTime = DateTimeUtil.todayZeroMillis();
		int timePeriod = (int) ((nowTime - zeroTime) / (DateTimeUtil.ONE_MINUTE_MILLIS * 30));

		PlayerOnlineLog log = new PlayerOnlineLog();
		log.setDateStr(dateStr);
		log.setTimePeriod(timePeriod);
		log.setOnlineNum(playerContextManager.getMaxOnline5());
		log.setNewOnlineNum(playerContextManager.getMaxNewComer5());
		playerLogger.insertOnlineLog(log);

		playerContextManager.resetCounter5();
		logger.debug("记录打点5分钟玩家在线人数日志");
	}

	/**
	 * 打点在线分布日志
	 */
	protected void recordeScatterLog() {
		ServerInfo serverInfo = serverInfoService.getMain();
		if (LocalDateTimeUtil.now().isBefore(serverInfo.getOnlineScatterTime())) {
			return;
		}
		// 每天5点5分统计在线时长
		serverInfo.setOnlineScatterTime(timeSetting.nextDayOTime().plusMinutes(5));
		serverInfoService.update(serverInfo);

		saveScatterLog();
	}

	private void saveScatterLog() {
		Map<PlayerOnlineEnum, Integer> map = new HashMap<>();
		for (long playerId : onlineCount.keySet()) {
			Player player = getEntity(playerId);
			int dailyOnline = player.getDailyOnline();
			for (PlayerOnlineEnum playerOnlineEnum : PlayerOnlineEnum.values()) {
				if (dailyOnline < playerOnlineEnum.getTime()) {
					map.put(playerOnlineEnum, map.getOrDefault(playerOnlineEnum, 0) + 1);
					break;
				}
			}
		}
		List<PlayerScatterLog> logList = new ArrayList<>();

		String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD,
				DateTimeUtil.currMillis() - DateTimeUtil.ONE_DAY_MILLIS);
		int time = Integer.parseInt(dateStr);
		int onlineBase = onlineCount.size();
		for (Entry<PlayerOnlineEnum, Integer> entry : map.entrySet()) {
			PlayerScatterLog log = new PlayerScatterLog();
			log.setDate(time);
			log.setTimePeriod(entry.getKey().getTime());
			log.setTimePeriodText(entry.getKey().getDesc());
			log.setOnlineNum(entry.getValue());
			log.setOnlineBase(onlineBase);
			logList.add(log);
		}

		playerLogger.insertAllScatterLog(logList);
		onlineCount.clear();
		logger.info("记录玩家在线时长占比日志");
	}

	@Subscribe
	private void listen(RechargeEvent event) {
		Player player = this.getEntity(event.getPid());
		if (LocalDateTimeUtil.isSameDay(player.getInsertTime(), LocalDateTimeUtil.now())) {
			player.setCreatePay(true);
			this.update(player);
		}
	}

	public void addToReport(Player player) {
		if (this.reportQueue.contains(player)) {
			return;
		}
		this.reportQueue.add(player);
	}

	/**
	 * 向后台上报玩家数据
	 */
	protected void reportToBackstage() {
		if (reportQueue.isEmpty()) {
			return;
		}
		Player player = reportQueue.poll();
		PlayerTable playerTable = new PlayerTable();
		playerTable.setServerId(applicationSettings.getGameServerId());

		while (player != null) {
			PlayerData data = new PlayerData();
			data.setPlatformId(applicationSettings.getGameServerPlatformid());
			data.setChannelId(player.getCreateChannel());
			data.setPlayerId(player.getId());
			data.setOpenId(player.getOpenId());
			data.setServerId(player.getServerId());
			data.setNick(player.getNick());
			data.setHead(player.getHead());
			data.setSex(player.getSex());
			data.setLevel(player.getLevel());
			data.setGold(player.getGold());
			data.setDiamond(player.getDiamond());
			data.setOnline(player.isOnline());
			data.setDailyOnline(player.getDailyOnline());
			data.setBornTime(LocalDateTimeUtil.toEpochMilli(player.getInsertTime()));
			data.setLastLoginTime(LocalDateTimeUtil.toEpochMilli(player.getLastLoginTime()));
			Formation formation = formationService.getEntity(player.getId(), BattleType.MAINLINE);
			data.setFighting(formation.getFighting());
			RechargePlayer rechargePlayer = rechargePlayerService.getEntity(player.getId());
			data.setRecharge(rechargePlayer.getRealTotalPay());
			playerTable.getDatas().add(data);
			player = reportQueue.poll();
		}
		backstagePoster.jsonPost(GameApi.playerReport, playerTable);
		logger.info("向后台上报玩家数量{}", playerTable.getDatas().size());
	}

	public boolean isWhite(long channelId, String openId) {
		Map<String, Object> map = new HashMap<>();
		map.put("channelId", channelId);
		map.put("openId", openId);
		String response = backstagePoster.formPost(GameApi.isWhiteList, map);
		if (response == null) {
			return false;
		}
		return "true".equals(response);
	}

	@Override
	public void start() throws Exception {
		// 在window环境下运行，会出现在停服的时候报PlayerOnlineEnum未加载的情况，先这样处理用于观察，本地不报错，打包后部署会出现
		PlayerOnlineEnum.values();
		
		List<Player> allPlayers = this.repository().getAllInCache();
		
		List<PlayerSnapshot> playerSnapshots = new ArrayList<>();
		for(Player player : allPlayers) {
			addToCreatedPlayers(player);
			playerSnapshots.add(new PlayerSnapshot(player));
		}
		logger.info("加载玩家数据{}", createdPlayers.size());
		
		Map<Integer, List<PlayerSnapshot>> serverPlayerMap = ListMapUtil.fillListMap(playerSnapshots, PlayerSnapshot::getServerId);
		for(Entry<Integer, List<PlayerSnapshot>> entry : serverPlayerMap.entrySet()) {
			int serverId = entry.getKey();
			String redisKey = "player:" + serverId + ":players";
			if(redisCache.hasKey(redisKey)) {
				continue;
			}
			redisCache.updateAllHash(redisKey, entry.getValue());
			logger.info("加载{}服玩家数据至Redis数量{}", serverId, entry.getValue().size());
		}
	}
	
	/**
	 * 是否已经存在该昵称
	 * @param nick
	 * @return
	 */
	public boolean hasNick(String nick) {
		return nickCache.containsKey(nick);
	}
	
	public void addNick(String nick) {
		nickCache.put(nick, 1);
	}
	
	public void changeNick(String newNick, String oldNick) {
		nickCache.put(newNick, 1);
		nickCache.remove(oldNick);
	}
	
	public Player getByOpenIdServerId(String openId, int serverId) {
		return createdPlayers.get(openId + "_" + serverId);
	}
	
	public void addToCreatedPlayers(Player player) {
		createdPlayers.put(player.getOpenId() + "_" + player.getServerId(), player);
		addNick(player.getNick());
	}
	
	public void addToFreshPlayer(Player player) {
		freshPlayers.add(player);
	}

	@Override
	public void stop() throws Exception {
		saveTimeLog();
		saveScatterLog();
		saveOnlineLog();
		reportToBackstage();
		reportCreateTiming();
	}

	@Subscribe
	private void listen(MainlineFormationChangeEvent event) {
		Player player = this.getEntity(event.getPid());
		if (player.getFighting() == event.getFighting()) {
			return;
		}
		long delta = event.getFighting() - player.getFighting();
		player.setFighting(event.getFighting());
		player.setImage(event.getLeader());
		this.update(player);
		
		EventBus.post(PlayerFightingEvent.of(player));

		PlayerFightingLog log = new PlayerFightingLog();
		log.setOwnerId(player.getId());
		log.setOwnerName(player.getNick());
		log.setThingName("战斗力");
		log.setDelta(delta);
		log.setCurr(player.getFighting());
		log.setGameCause(event.getGameCause());
		playerLogger.insertFightingLog(log);
	}

	@Override
	public Player getEntity(long entityId) {
		Player player = super.getEntity(entityId);
		// 每日数据重置
		boolean needUpdate = false;
		LocalDateTime now = LocalDateTimeUtil.now();
		if (now.isAfter(player.getDailyTime())) {
			player.setDailyTime(timeSetting.nextDayOTime());

			player.setDailyOnline(0);
			needUpdate = true;
		}
		if (needUpdate) {
			this.update(player);
		}
		return player;
	}
	
	@Override
	protected void onUpdate(Player player) {
		PlayerSnapshot playerSnapshot = new PlayerSnapshot(player);
		String redisKey = "player:" + player.getServerId() + ":players";
		redisCache.updateHash(redisKey, playerSnapshot);
	}
	
	public int getPlayerNum() {
		return this.repository().getCacheSize();
	}
	
	protected void reportCreateTiming() {
		if(freshPlayers.isEmpty()) {
			return;
		}
		
		LoginTimingTable table = new LoginTimingTable();
		
		Player player = freshPlayers.poll();
		while(player != null) {
			LoginTiming loginTiming = new LoginTiming();
			loginTiming.setChannelId(player.getCreateChannel());
			loginTiming.setOpenId(player.getOpenId());
			loginTiming.setTime(LocalDateTimeUtil.toEpochMilli(player.getInsertTime()));
			loginTiming.setTiming(LoginDotTiming.CREATE_SUCC.getValue());
			table.getDatas().add(loginTiming);
			
			player = freshPlayers.poll();
		}
		backstagePoster.jsonPost(GameApi.loginDotReport, table);
		logger.info("向后台上报创号打点数量{}", table.getDatas().size());
	}
	
	
}
