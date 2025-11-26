package com.xiugou.x1.game.server.module.shop.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ExchangeShopCache;
import com.xiugou.x1.design.module.ShopTypeCache;
import com.xiugou.x1.design.module.autogen.ExchangeShopAbstractCache.ExchangeShopCfg;
import com.xiugou.x1.design.module.autogen.ShopTypeAbstractCache.ShopTypeCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.service.OneToOneService;
import com.xiugou.x1.game.server.module.firstRecharge.model.FirstRecharge;
import com.xiugou.x1.game.server.module.firstRecharge.service.FirstRechargeService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;
import com.xiugou.x1.game.server.module.purgatory.service.PurgatoryService;
import com.xiugou.x1.game.server.module.recharge.model.RechargePlayer;
import com.xiugou.x1.game.server.module.recharge.service.RechargePlayerService;
import com.xiugou.x1.game.server.module.server.model.ServerInfo;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;
import com.xiugou.x1.game.server.module.shop.model.ShopSystem;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;
import com.xiugou.x1.game.server.module.villagedefense.service.VillageService;

/**
 * @author yh
 * @date 2023/8/4
 * @apiNote
 */
@Service
public class ShopSystemService extends OneToOneService<ShopSystem> {
    @Autowired
    private ShopTypeCache shopTypeCache;
    @Autowired
    private TimeSetting timeSetting;
    @Autowired
    private ExchangeShopCache exchangeShopCache;
    @Autowired
    private ServerInfoService serverInfoService;
    @Autowired
    private RechargePlayerService rechargePlayerService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PurgatoryService purgatoryService;
    @Autowired
    private VillageService villageService;
    @Autowired
    private FirstRechargeService firstRechargeService;



    @Override
    protected ShopSystem createWhenNull(long entityId) {
        ShopSystem shopSystem = new ShopSystem();
        shopSystem.setShopId((int) entityId);
        shopSystem.setConfigRound(1);
        shopSystem.setIncreaseRound(1);

        LocalDateTime localDateTime = calculateResetTime(shopSystem.getShopId());
        shopSystem.setNextReset(localDateTime);
        return shopSystem;
    }

    @Override
    public ShopSystem getEntity(long entityId) {
        ShopSystem entity = repository().getByMainKey(entityId);
        if (entity == null) {
            synchronized (ShopSystem.class) {
                entity = repository().getByMainKey(entityId);
                if (entity == null) {
                    entity = createWhenNull(entityId);
                    repository().insert(entity);
                }
            }
        }
        if (timeSetting.needReset(entity.getNextReset())) {
            synchronized (entity) {
                if (timeSetting.needReset(entity.getNextReset())) {
                    entity.setIncreaseRound(entity.getIncreaseRound() + 1);
                    entity.setConfigRound(entity.getConfigRound() + 1);
                    ExchangeShopCfg exchangeShopCfg = exchangeShopCache.findInShopIdRoundProductIdIndex(entity.getShopId(), entity.getConfigRound(), 1);
                    if (exchangeShopCfg == null) {
                        entity.setConfigRound(1);
                    }
                    LocalDateTime localDateTime = calculateResetTime(entity.getShopId());
                    entity.setNextReset(localDateTime);
                    this.update(entity);
                }
            }
        }
        return entity;
    }
    //：1每天重置、2每周重置,3每月重置，4每3天重置、8开服每七天重置，不重置
    public LocalDateTime calculateResetTime(int shopId) {
        ShopTypeCfg shopTypeCfg = shopTypeCache.getOrThrow(shopId);
        int resetType = shopTypeCfg.getResetType();
        if (resetType == 1) {
            return timeSetting.tomorrowOTime();
        } else if (resetType == 2) {
            return timeSetting.nextWeekMondayOTime();
        } else if (resetType == 3) {
            return timeSetting.nextMonthOTime();
        } else if (resetType == 4) {
            return timeSetting.tomorrowOTime().plusDays(2);
        } else if (resetType == 5) {
            return resetTimeByOpening(7);
        }
        return LocalDateTimeUtil.ofEpochMilli(DateTimeUtil.nextMonthZeroMillis() + DateTimeUtil.ONE_WEEK_MILLIS * 400);
    }
//1前置礼包
//2等级
//3vip等级
//4开服天数
//5炼狱阶数
//6仙境阶数
//7首充
    public boolean limitCondition(long pid, Keyv condition) {
    	return limitCondition(pid, Collections.singletonList(condition));
    }
    public boolean limitCondition(long pid, List<Keyv> conditions) {
        boolean result = false;
        for (Keyv keyv : conditions) {
        	if(keyv.getKey() == 0) {
        		result = true;
        	} else if (keyv.getKey() == 1) {
                RechargePlayer entity = rechargePlayerService.getEntity(pid);
                result = entity.getBuyProducts().contains(keyv.getValue());
            } else if (keyv.getKey() == 2) {
                Player player = playerService.getEntity(pid);
                result = player.getLevel() >= keyv.getValue();
            } else if (keyv.getKey() == 3) {
                Player player = playerService.getEntity(pid);
                result = player.getVipLevel() >= keyv.getValue();
            } else if (keyv.getKey() == 4) {
                result = serverInfoService.getOpenedDay() >= keyv.getValue();
            } else if (keyv.getKey() == 5) {
                Purgatory purgatory = purgatoryService.getEntity(pid);
                return purgatory.getLevel() >= keyv.getValue();
            } else if (keyv.getKey() == 6) {
                Village village = villageService.getEntity(pid);
                return village.getLevel() >= keyv.getValue();
            } else if (keyv.getKey() == 7) {
                FirstRecharge firstRecharge = firstRechargeService.getEntity(pid);
                result = !firstRecharge.getRechargeDatas().isEmpty();
            }
            if (!result) {
                break;
            }
        }
        return result;
    }
    //根据开服时间day天重置一次
    public LocalDateTime resetTimeByOpening(int day){
        ServerInfo main = serverInfoService.getMain();
        LocalDateTime openTime = main.getOpenTime();
        int openedDay = serverInfoService.getOpenedDay();
        int resetDateTime = openedDay + day - openedDay % day;
        return openTime.plusDays(resetDateTime);
    }
}
