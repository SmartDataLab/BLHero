/**
 *
 */
package com.xiugou.x1.game.server.module.player.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.ruler.util.SensitiveUtil;
import org.gaming.tool.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.LogoutInternalMessage;
import com.xiugou.x1.game.server.foundation.player.LogoutType;
import com.xiugou.x1.game.server.foundation.player.PlayerActorPool;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.bag.model.Bag;
import com.xiugou.x1.game.server.module.bag.service.BagService;
import com.xiugou.x1.game.server.module.bag.struct.ItemGrid;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerForbidListMessage;
import com.xiugou.x1.game.server.module.player.message.PlayerMessage.PlayerForbidMessage;
import com.xiugou.x1.game.server.module.player.model.ForbidList;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.ForbidListService;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;
import com.xiugou.x1.game.server.module.recharge.service.RechargePlayerService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.bag.BagForm;
import pojo.xiugou.x1.pojo.module.hero.HeroForm;
import pojo.xiugou.x1.pojo.module.player.form.ForbidTable;
import pojo.xiugou.x1.pojo.module.player.form.PlayerDetailForm;
import pojo.xiugou.x1.pojo.module.player.form.PlayerOnlineTable;
import pojo.xiugou.x1.pojo.module.player.form.PlayerOnlineTable.PlayerOnlineData;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.ServerResponseCode;

/**
 * @author YY
 */
@Controller
public class PlayerApi {

	private static Logger logger = LoggerFactory.getLogger(PlayerApi.class);
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private FormationService formationService;
	@Autowired
	private RechargePlayerService rechargePlayerService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private BagService bagService;
	@Autowired
	private ItemCache itemCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private ForbidListService forbidListService;
	@Autowired
	private MailService mailService;
	

	@RequestMapping(value = GameApi.playerDetails)
	@ResponseBody
	public String info(@RequestParam("playerId") long playerId) {
		Player player = playerService.getEntity(playerId);
		if (player == null) {
			return new ServerResponse(ServerResponseCode.PLAYER_NOT_FOUND).result();
		}	
		PlayerDetailForm form = buildPlayerForm(player);
	
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(form);
		return response.result();
	}
	
	public PlayerDetailForm buildPlayerForm(Player player) {
		Formation formation = formationService.getEntity(player.getId(), BattleType.MAINLINE);
		RechargePlayer rechargePlayer = rechargePlayerService.getEntity(player.getId());
        PlayerDetailForm form = new PlayerDetailForm();
        form.setId(player.getId());
        form.setOpenId(player.getOpenId());
        form.setServerId(player.getServerId());
        form.setNick(player.getNick());
        form.setHead(player.getHead());
        form.setSex(player.getSex());
        form.setLevel(player.getLevel());
        form.setExp(player.getExp());
        form.setGold(player.getGold());
        form.setDiamond(player.getDiamond());
        form.setNowFighting(formation.getFighting());
        form.setHisMaxFighting(formation.getMaxFighting());
        form.setChangeNameNum(player.getChangeNameNum());
        form.setOnline(player.isOnline());
        form.setDailyOnline(player.getDailyOnline());
        form.setLastLoginTime(player.getLastLoginTime());
        form.setLastLogoutTime(player.getLastLogoutTime());
        form.setForbidEndTime(player.getForbidEndTime());
        form.setRealTotalPay(rechargePlayer.getRealTotalPay());
        form.setLoginIp(player.getLoginIp());
        form.setLoginDeviceType(player.getLoginDeviceType());
        form.setLoginDeviceId(player.getLoginDeviceId());
        form.setLoginVersion(player.getLoginVersion());
        form.setCreateIp(player.getCreateIp());
        form.setCreateDeviceType(player.getCreateDeviceType());
        form.setCreateDeviceId(player.getCreateDeviceId());
        form.setCreateChannel(player.getCreateChannel());
        return form;
    }

	@RequestMapping(value = GameApi.playerForbid)
	@ResponseBody
	public String forbid(@RequestParam("playerId") long playerId, @RequestParam("forbidEndTime") long forbidEndTime) {
		Player player = playerService.getEntity(playerId);
		if (player == null) {
			return new ServerResponse(ServerResponseCode.PLAYER_NOT_FOUND).result();
		}
		PlayerActorPool.tell(new PlayerForbidMessage(playerId, forbidEndTime));
		return ServerResponse.SUCCESES.result();
	}
	
	@RequestMapping(value = GameApi.playerForbidList)
	@ResponseBody
	public String forbidList(@RequestBody ForbidTable table) {
		List<ForbidList> oldList = forbidListService.getAll();
		forbidListService.deleteAll(oldList);
		
		List<ForbidList> newList = new ArrayList<>();
		logger.info("收到封禁账号请求");
		for(String forbidOpenId : table.getOpenIds()) {
			logger.info("封禁账号{}", forbidOpenId);
			
			ForbidList forbidList = new ForbidList();
			forbidList.setOpenId(forbidOpenId);
			newList.add(forbidList);
		}
		forbidListService.insertAll(newList);
		
		for(PlayerContext playerContext : playerContextManager.onlines()) {
			if(forbidListService.isForbid(playerContext.getOpenId())) {
				PlayerActorPool.tell(new PlayerForbidListMessage(playerContext.getId()));
			}
		}
		return ServerResponse.SUCCESES.result();
	}
	

	@RequestMapping(value = GameApi.playerDetailsHeroes)
	@ResponseBody
	public String playerHeroData(@RequestParam("playerId") long playerId) {
		Player player = playerService.getEntity(playerId);
		if (player == null) {
			return new ServerResponse(ServerResponseCode.PLAYER_NOT_FOUND).result();
		}

		List<Hero> heroList = heroService.getEntities(playerId);
		List<HeroForm> heroFormList = new ArrayList<>();
		for (Hero hero : heroList) {
			HeroForm heroForm = heroService.buildHeroForm(hero);
			heroFormList.add(heroForm);
		}

		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(heroFormList);
		return response.result();
	}

	@RequestMapping(value = GameApi.playerDetailsBag)
	@ResponseBody
	public String playerBagData(@RequestParam("playerId") long playerId) {
		Bag bag = bagService.getEntity(playerId);
		Map<Integer, ItemGrid> idGridMap = bag.getIdGridMap();
		ArrayList<BagForm> bagFormList = new ArrayList<>();
		// 道具
		for (ItemGrid item : idGridMap.values()) {
			BagForm bagToGameServer = buildBagForm(item.getItemId(), item.getNum());
			bagFormList.add(bagToGameServer);
		}
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(bagFormList);
		return response.result();
	}

	public BagForm buildBagForm(int thingId, long num) {
		BagForm bagToGameServer = new BagForm();
		bagToGameServer.setItemId(thingId);
		bagToGameServer.setHaveNum(num);
		ItemAbstractCache.ItemCfg itemCfg = itemCache.getOrThrow(thingId);
		bagToGameServer.setItemName(itemCfg.getName());
		return bagToGameServer;
	}

	/**
	 * 当前在线玩家列表
	 */
	@RequestMapping(value = GameApi.playerOnlines)
	@ResponseBody
	public String onlines() {
		PlayerOnlineTable table = new PlayerOnlineTable();
		for (PlayerContext playerContext : playerContextManager.onlines()) {
			Player player = playerService.getEntity(playerContext.getId());
			PlayerOnlineData data = new PlayerOnlineData();
			data.setId(player.getId());
			data.setNick(player.getNick());
			data.setDiamond(player.getDiamond());
			data.setGold(player.getGold());
			data.setLevel(player.getLevel());
			data.setLastLoginTime(player.getLastLoginTime());
			data.setDailyOnline(player.getDailyOnline());
			table.getDatas().add(data);
		}
		ServerResponse response = new ServerResponse(ServerResponseCode.SUCCESS);
		response.setData(table);
		return response.result();
	}

	/**
	 * 强制玩家下线
	 */
	@RequestMapping(value = GameApi.playerForcedOffline)
	@ResponseBody
	public String forcedOffline(@RequestParam("playerId") long playerId) {
		PlayerContext playerContext = playerContextManager.getContext(playerId);
		if(playerContext != null) {
			LogoutInternalMessage message = new LogoutInternalMessage();
			message.playerContext = playerContext;
			message.logoutType = LogoutType.FORCED_OFFLINE;
			PlayerActorPool.tell(message);
		}
		return ServerResponse.SUCCESES.result();
	}

	@RequestMapping(value = GameApi.playerForceChangeName)
	@ResponseBody
	public String forceChangeName(@RequestParam("playerId") long playerId) {
		Player player = playerService.getEntity(playerId);
		if(player == null) {
			return ServerResponse.SUCCESES.result();
		}
		if(SensitiveUtil.has(player.getNick()) == null) {
			return ServerResponse.SUCCESES.result();
		}
		player.setNick("违规名字" + RandomUtil.closeClose(1000000, 9999999));
		playerService.update(player);
		
		RewardThing rewardThing = RewardThing.of(2000009, 1);
		Mail mail = mailService.newMail(player.getId(), MailTemplate.FORCE_CHANGE_NAME, rewardThing, GameCause.FORCE_CHANGE_NAME);
		mailService.insert(mail);
		
		return ServerResponse.SUCCESES.result();
	}
}
