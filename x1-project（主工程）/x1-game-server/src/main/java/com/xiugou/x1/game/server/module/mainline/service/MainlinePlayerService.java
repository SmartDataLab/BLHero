/**
 *
 */
package com.xiugou.x1.game.server.module.mainline.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.prefab.task.Task;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.module.ItemCache;
import com.xiugou.x1.design.module.MainlineTaskCache;
import com.xiugou.x1.design.module.MainlineTaskCache.MainlineTaskConfig;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;
import com.xiugou.x1.design.module.autogen.MainlineTaskAbstractCache.MainlineTaskCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.mainline.event.CampTimeEvent;
import com.xiugou.x1.game.server.module.mainline.log.MainlineDotLogger;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.struct.SceneOpening;
import com.xiugou.x1.game.server.module.player.event.PlayerLogoutEvent;

import pojo.xiugou.x1.pojo.log.mainline.MainlineTaskTiming;

/**
 * @author YY
 *
 */
@Service
public class MainlinePlayerService extends PlayerOneToOneResetableService<MainlinePlayer> {

    @Autowired
    private ThingService thingService;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private MainlineTaskCache mainlineTaskCache;
    @Autowired
	private MainlineDotLogger mainlineTaskDotLogger;
    
    @Override
	public MainlinePlayer getEntity(long entityId) {
		MainlinePlayer entity = super.getEntity(entityId);
		checkFixTask(entity);
		return entity;
	}

    @Override
    protected MainlinePlayer createWhenNull(long entityId) {
        MainlinePlayer entity = new MainlinePlayer();
        entity.setPid(entityId);
        entity.setCurrScene(1);
        MainlineTaskCfg mainlineTaskCfg = mainlineTaskCache.getInPreTaskIndex(0);
		Task task = Task.create(mainlineTaskCfg.getId());
		entity.setTask(task);
		for(MainlineTaskCfg taskCfg : mainlineTaskCache.all()) {
			if(taskCfg.getPreTask() == 0) {
				continue;
			}
			//策划经常修改任务配置，会有打乱，中间插入的情况
			Task paraTask = Task.create(taskCfg.getId());
			entity.getParallelTasks().put(paraTask.getId(), paraTask);
		}
		mainlineTaskDotLogger.addTaskDot(entityId, mainlineTaskCfg, MainlineTaskTiming.START);
        return entity;
    }

    @Override
    protected void doDailyReset(MainlinePlayer entity) {
        entity.setCampTime(0);
        entity.setCampAdvNum(0);
        entity.setKillNum(0);
        entity.setTreasureNum(0);
    }

    @Subscribe
    private void listen(PlayerLogoutEvent event) {
        MainlinePlayer entity = this.getEntity(event.getPid());
        boolean needUpdate = false;
        //露营数据
        if (entity.isCamping()) {
        	long now = DateTimeUtil.currMillis();
            long newCampTime = now - entity.getStartCampTime();
            
            entity.setCamping(false);
            entity.setCampTime(entity.getCampTime() + newCampTime);
            entity.getCampProduces().clear();
            needUpdate = true;
            
            EventBus.post(CampTimeEvent.of(event.getPid(), newCampTime / DateTimeUtil.ONE_MINUTE_MILLIS));
        }
        
        //离线挂机数据
        if(entity.isHangOpened()) {
        	entity.setHangStartTime(DateTimeUtil.currMillis());
        	entity.setHasHangReward(true);
        	needUpdate = true;
        }
        if(needUpdate) {
        	this.update(entity);
        }
    }

    /**
     *
     * @param playerId
     * @param openingId
     * @param openingMap
     * @param openingCosts
     * @return
     */
    public boolean checkSceneOpening(long playerId, SceneOpening sceneOpening, List<CostThing> openingCosts, GameCause gameCasuse) {
        List<CostThing> needCosts = new ArrayList<>();
        for (CostThing cost : openingCosts) {
            //已缴纳的数量
            long hasPay = sceneOpening.getPayProgess().getOrDefault(cost.getThingId(), 0L);
            if (hasPay < cost.getNum()) {
                needCosts.add(CostThing.of(cost.getThingId(), cost.getNum() - hasPay));
            }
        }

        if (thingService.isEnough(playerId, needCosts)) {
            thingService.cost(playerId, needCosts, gameCasuse);

            for (CostThing cost : needCosts) {
                long hasPay = sceneOpening.getPayProgess().getOrDefault(cost.getThingId(), 0L);
                sceneOpening.getPayProgess().put(cost.getThingId(), hasPay + cost.getNum());
            }
        } else {
            //可以消耗的数量
            List<CostThing> canCosts = new ArrayList<>();
            for (CostThing cost : needCosts) {
                ItemCfg itemCfg = itemCache.getOrThrow(cost.getThingId());
                long hasCount = thingService.getStorer(ItemType.valueOf(itemCfg.getKind())).getCount(playerId, cost.getThingId());
                if (hasCount >= cost.getNum()) {
                    canCosts.add(CostThing.of(cost.getThingId(), cost.getNum()));
                } else {
                	if(hasCount > 0) {
                		canCosts.add(CostThing.of(cost.getThingId(), hasCount));
                	}
                }
            }
            if(!canCosts.isEmpty()) {
            	thingService.cost(playerId, canCosts, gameCasuse);
                for (CostThing cost : canCosts) {
                    long hasPay = sceneOpening.getPayProgess().getOrDefault(cost.getThingId(), 0L);
                    sceneOpening.getPayProgess().put(cost.getThingId(), hasPay + cost.getNum());
                }
            }
        }

        //验证所有支付项是不是已经满足
        boolean allPay = true;
        for (CostThing cost : openingCosts) {
            long hasPay = sceneOpening.getPayProgess().getOrDefault(cost.getThingId(), 0L);
            if (hasPay < cost.getNum()) {
                allPay = false;
                break;
            }
        }
        return allPay;
    }
    
    private void checkFixTask(MainlinePlayer entity) {
		int taskNum = 0;
		if(entity.getTask().getId() > 0) {
			taskNum += 1;
		}
		taskNum += entity.getParallelTasks().size();
		taskNum += entity.getFinishTasks().size();
		List<MainlineTaskConfig> taskList = mainlineTaskCache.all();
		if(taskNum != taskList.size()) {
			for(MainlineTaskConfig taskCfg : taskList) {
				//已经完成的任务
				if(entity.getFinishTasks().contains(taskCfg.getId())) {
					continue;
				}
				//当前任务
				if(entity.getTask().getId() == taskCfg.getId()) {
					continue;
				}
				//已经在并行任务中
				if(entity.getParallelTasks().containsKey(taskCfg.getId())) {
					continue;
				}
				Task paraTask = Task.create(taskCfg.getId());
				entity.getParallelTasks().put(paraTask.getId(), paraTask);
			}
			this.update(entity);
		}
	}
}
