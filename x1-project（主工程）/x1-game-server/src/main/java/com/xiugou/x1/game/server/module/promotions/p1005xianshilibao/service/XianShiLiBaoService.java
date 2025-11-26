package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.XianShiLiBaoCache;
import com.xiugou.x1.design.module.autogen.XianShiLiBaoAbstractCache.XianShiLiBaoCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.dungeon.event.DungeonLoseEvent;
import com.xiugou.x1.game.server.module.goldenpig.event.GoldenPigLoseEvent;
import com.xiugou.x1.game.server.module.hero.event.GetNewHeroEvent;
import com.xiugou.x1.game.server.module.hero.event.HeroUpLevelEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.mainline.event.MainlineAllDeadEvent;
import com.xiugou.x1.game.server.module.mainline.event.MainlineTaskRewardEvent;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.service.MainlinePlayerService;
import com.xiugou.x1.game.server.module.player.event.CostGoldEvent;
import com.xiugou.x1.game.server.module.player.event.PlayerUpLevelEvent;
import com.xiugou.x1.game.server.module.player.event.PlayerVipUpLevelEvent;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model.XianShiLiBao;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model.XianShiLiBaoPlayer;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.strcut.XianShiLiBaoType;
import com.xiugou.x1.game.server.module.recruit.event.RecruitNumEvent;
import com.xiugou.x1.game.server.module.tower.event.TowerLoseEvent;
import com.xiugou.x1.game.server.module.tower.event.TowerWinEvent;
import com.xiugou.x1.game.server.module.tower.model.Tower;
import com.xiugou.x1.game.server.module.tower.service.BaseTowerBattleProcessor;
import com.xiugou.x1.game.server.module.tower.service.TowerService;

import pb.xiugou.x1.protobuf.xianshilibao.P1005XianShiLiBao.PbXianShiLiBao;
import pb.xiugou.x1.protobuf.xianshilibao.P1005XianShiLiBao.XianShiLiPushDetailMessage;

/**
 * @author yh
 * @date 2023/9/25
 * @apiNote
 */
@Service
public class XianShiLiBaoService extends OneToManyService<XianShiLiBao> {
	@Autowired
	private XianShiLiBaoCache xianShiLiBaoCache;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private XianShiLiBaoPlayerService xianShiLiBaoPlayerService;
	@Autowired
	private TowerService towerService;
	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	@Autowired
	private ApplicationSettings applicationSettings;

	public XianShiLiBao getEntity(long entityId, int giftId) {
		List<XianShiLiBao> gifts = this.getEntities(entityId);
		List<XianShiLiBao> deleteList = null;
		for(XianShiLiBao gift : gifts) {
			if(gift.getEndTime().isAfter(LocalDateTimeUtil.now())) {
				continue;
			}
			if(deleteList == null) {
				deleteList = new ArrayList<>();
			}
			deleteList.add(gift);
		}
		if(deleteList != null) {
			this.repository().deleteAll(deleteList);
		}
		return repository().getByKeys(entityId, giftId);
	}

	public PbXianShiLiBao build(XianShiLiBao xianShiLiBao) {
		PbXianShiLiBao.Builder builder = PbXianShiLiBao.newBuilder();
		builder.setGiftId(xianShiLiBao.getGiftId());
		builder.setBuy(xianShiLiBao.isBuy());
		builder.setEndTime(LocalDateTimeUtil.toEpochMilli(xianShiLiBao.getEndTime()));
		return builder.build();
	}

	public Map<Integer, XianShiLiBao> getXianShiLiBaoMap(long playerId) {
		return ListMapUtil.listToMap(this.getEntities(playerId), XianShiLiBao::getGiftId);
	}

	@Subscribe
	private void listen(PlayerUpLevelEvent event) {
		checkGiftConditions(event.getPlayer().getId(), XianShiLiBaoType.LEVEL);
	}

	@Subscribe
	private void listen(HeroUpLevelEvent event) {
		checkGiftConditions(event.getEntityId(), XianShiLiBaoType.HERO_LEVEL);
	}

	@Subscribe
	private void listen(MainlineTaskRewardEvent event) {
		checkGiftConditions(event.getPid(), XianShiLiBaoType.MAINLINE_TASK);
	}
	
	@Subscribe
	private void listen(GetNewHeroEvent event) {
		checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.HERO_LEVEL);
	}

	@Subscribe
	private void listen(PlayerVipUpLevelEvent event) {
		checkGiftConditions(event.getPlayer().getId(), XianShiLiBaoType.ALL);
	}
	
	@Subscribe
	private void listen(CostGoldEvent event) {
		checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.GOLD);
	}
	
	@Subscribe
	private void listen(TowerWinEvent event) {
		checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.TOWER);
	}
	
	@Subscribe
	private void listen(MainlineAllDeadEvent event) {
		checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.MAINLINE_DEAD);
	}
	
	@Subscribe
	private void listen(RecruitNumEvent event) {
		if(event.isCostDiamond()) {
			checkGiftConditions(event.getPid(), XianShiLiBaoType.DIAMOND_RECRUIT);
		}
	}
	
	@Subscribe
	private void listen(DungeonLoseEvent event) {
		if(event.getDungeonType() == 2) {
			//主线副本
			checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.ML_DUNGEON_DEAD);
		} else {
			//秘境副本
			checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.DUNGEON_DEAD);
		}
	}
	
	@Subscribe
	private void listen(TowerLoseEvent event) {
		checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.TOWER_DEAD);
	}
	
	@Subscribe
	private void listen(GoldenPigLoseEvent event) {
		checkGiftConditions(event.getPlayerId(), XianShiLiBaoType.GOLDENPIG_DEAD);
	}

	public void checkGiftConditions(long pid, XianShiLiBaoType type) {
		List<XianShiLiBaoCfg> xianShiLiBaoCfgList = null;
		if (type == XianShiLiBaoType.ALL) {
			xianShiLiBaoCfgList = xianShiLiBaoCache.all();
		} else {
			xianShiLiBaoCfgList = xianShiLiBaoCache.findInTypeCollector(type.getValue());
		}
		if(xianShiLiBaoCfgList == null) {
			return;
		}
		Player player = playerService.getEntity(pid);
		XianShiLiBaoPlayer xslbPlayer = xianShiLiBaoPlayerService.getEntity(pid);
		boolean updateXslbPlayer = false;
		
		List<XianShiLiBao> xianShiLiBaoList = new ArrayList<>();
		for (XianShiLiBaoCfg xianShiLiBaoCfg : xianShiLiBaoCfgList) {
			XianShiLiBao xianShiLiBao = getEntity(pid, xianShiLiBaoCfg.getId());
			if (xianShiLiBao != null) {
				continue;
			}
			if (xianShiLiBaoCfg.getVipLevel() > player.getVipLevel()) {
				continue;
			}
			List<Integer> condition = xianShiLiBaoCfg.getCondition();
			boolean flag = false;
			if (xianShiLiBaoCfg.getType() == 1) {
				//等级礼包
				int limitNum = xslbPlayer.getLifeLimits().getOrDefault(xianShiLiBaoCfg.getId(), 0);
				if(limitNum <= 0) {
					flag = player.getLevel() >= condition.get(0);
					if(flag) {
						xslbPlayer.getLifeLimits().put(xianShiLiBaoCfg.getId(), limitNum + 1);
						updateXslbPlayer = true;
					}
				}
			} else if (xianShiLiBaoCfg.getType() == 2) {
				//英雄等级礼包
				int limitNum = xslbPlayer.getLifeLimits().getOrDefault(xianShiLiBaoCfg.getId(), 0);
				if(limitNum <= 0) {
					Hero hero = heroService.getEntity(pid, condition.get(0));
					if (hero != null) {
						flag = hero.getLevel() >= condition.get(1);
						if(flag) {
							xslbPlayer.getLifeLimits().put(xianShiLiBaoCfg.getId(), limitNum + 1);
							updateXslbPlayer = true;
						}
					}
				}
			} else if (xianShiLiBaoCfg.getType() == 3) {
				//主线任务礼包
				int limitNum = xslbPlayer.getLifeLimits().getOrDefault(xianShiLiBaoCfg.getId(), 0);
				if(limitNum <= 0) {
					MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(pid);
					flag = mainlinePlayer.getFinishTasks().contains(condition.get(1));
					if(flag) {
						xslbPlayer.getLifeLimits().put(xianShiLiBaoCfg.getId(), limitNum + 1);
						updateXslbPlayer = true;
					}
				}
			} else if(xianShiLiBaoCfg.getType() == 5) {
				//金币礼包
				int limitNum = xslbPlayer.getDailyLimits().getOrDefault(xianShiLiBaoCfg.getType(), 0);
				//TODO 触发数量限制
				if(limitNum < 3 && player.getGold() < condition.get(0)) {
					xslbPlayer.getDailyLimits().put(xianShiLiBaoCfg.getType(), limitNum + 1);
					updateXslbPlayer = true;
					flag = true;
				}
			} else if(xianShiLiBaoCfg.getType() == XianShiLiBaoType.TOWER.getValue()) {
				Tower tower = towerService.getEntity(pid);
				BaseTowerBattleProcessor towerProcessor = TowerService.getProcessor(condition.get(0));
				int layer = towerProcessor.getTowerLayer(tower);
				if(layer == condition.get(1)) {
					flag = true;
				}
			} else if(xianShiLiBaoCfg.getType() == 7) {
				MainlinePlayer mlPlayer = mainlinePlayerService.getEntity(pid);
				int limitNum = xslbPlayer.getDailyLimits().getOrDefault(xianShiLiBaoCfg.getType(), 0);
				//TODO 触发数量限制
				if(limitNum < 1 && mlPlayer.getAllDeadNum() >= condition.get(0)) {
					xslbPlayer.getDailyLimits().put(xianShiLiBaoCfg.getType(), limitNum + 1);
					updateXslbPlayer = true;
					flag = true;
				}
			} else if(xianShiLiBaoCfg.getType() == 8) {
				int limitNum = xslbPlayer.getDailyLimits().getOrDefault(xianShiLiBaoCfg.getType(), 0);
				//TODO 触发数量限制
				if(limitNum < 3 && player.getDiamond() < condition.get(0)) {
					xslbPlayer.getDailyLimits().put(xianShiLiBaoCfg.getType(), limitNum + 1);
					updateXslbPlayer = true;
					flag = true;
				}
			} else if (xianShiLiBaoCfg.getType() == 9 || xianShiLiBaoCfg.getType() == 10
					|| xianShiLiBaoCfg.getType() == 11 || xianShiLiBaoCfg.getType() == 12) {
				if(type == XianShiLiBaoType.ALL && xianShiLiBaoCfg.getVipLevel() <= 0) {
					flag = false;
				} else {
					flag = true;
				}
			}
			if (flag) {
				xianShiLiBao = new XianShiLiBao();
				xianShiLiBao.setPid(pid);
				xianShiLiBao.setGiftId(xianShiLiBaoCfg.getId());
				xianShiLiBao.setEndTime(LocalDateTimeUtil.now().plusMinutes(xianShiLiBaoCfg.getCountdown()));
				xianShiLiBaoList.add(xianShiLiBao);
			}
		}
		if(updateXslbPlayer) {
			xianShiLiBaoPlayerService.update(xslbPlayer);
		}
		addXianShiLiBao(pid, xianShiLiBaoList);
	}

	private void addXianShiLiBao(long playerId, List<XianShiLiBao> xianShiLiBaoList) {
		if(applicationSettings.isGameArraignType()) {
			Player player = playerService.getEntity(playerId);
			if("IOS".equals(player.getLoginDeviceType())) {
				return;
			}
		}
		if (!xianShiLiBaoList.isEmpty()) {
			this.insert(xianShiLiBaoList);
			XianShiLiPushDetailMessage.Builder response = XianShiLiPushDetailMessage.newBuilder();
			for (XianShiLiBao xianShiLiBao : xianShiLiBaoList) {
				response.addGiftDetail(build(xianShiLiBao));
			}
			playerContextManager.push(playerId, XianShiLiPushDetailMessage.Proto.ID, response.build());
		}
	}
	
	static boolean run = true;
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new T1Run());
		t1.start();
		
		Thread t2 = new Thread(()->{
			int i = 0;
			while(run) {
				System.out.println("t2");
				i++;
			}
			System.out.println("t2 " + run + i);
		});
		t2.start();
		
		try {
			Thread.sleep(1000);
			run = false;
			System.out.println("停止");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static class T1Run implements Runnable {
		@Override
		public void run() {
			int i = 0;
			while(run) {
//				System.out.println("t1");
				i++;
			}
			System.out.println("t1 " + run + i);
			
		}
	}
}
