/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.task.Task;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.MainlineTaskCache;
import com.xiugou.x1.design.module.MonsterCache;
import com.xiugou.x1.design.module.SceneFogCache;
import com.xiugou.x1.design.module.SceneTeleportCache;
import com.xiugou.x1.design.module.autogen.MainlineTaskAbstractCache.MainlineTaskCfg;
import com.xiugou.x1.design.module.autogen.SceneFogAbstractCache.SceneFogCfg;
import com.xiugou.x1.design.module.autogen.SceneTeleportAbstractCache.SceneTeleportCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.mainline.event.BuildingTransmitEvent;
import com.xiugou.x1.game.server.module.mainline.event.FogOpenEvent;
import com.xiugou.x1.game.server.module.mainline.model.MainlinePlayer;
import com.xiugou.x1.game.server.module.mainline.model.MainlineScene;
import com.xiugou.x1.game.server.module.mainline.service.MainlinePlayerService;
import com.xiugou.x1.game.server.module.mainline.service.MainlineSceneService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.task.TaskSystem;

import pb.xiugou.x1.protobuf.battle.Battle.BattleSpawnMonsterGmMessage;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineFogOpenResponse;
import pb.xiugou.x1.protobuf.mainline.Mainline.MainlineTeleportOpenResponse;
import pb.xiugou.x1.protobuf.task.Task.PlayerTaskChangeMessage;

/**
 * @author YY
 *
 */
@Controller
public class MainlineGmHandler {

	@Autowired
	private MainlineSceneService mainlineSceneService;
	@Autowired
	private MainlineTaskCache mainlineTaskCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private MainlinePlayerService mainlinePlayerService;
	@Autowired
	private SceneFogCache sceneFogCache;
	@Autowired
	private SceneTeleportCache sceneTeleportCache;
	@Autowired
	private MonsterCache monsterCache;
	
	//完成主线任务
	@PlayerGmCmd(command = "ML_TASK_FIN")
	public void taskFinish(PlayerContext playerContext, String[] params) {
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());

		Task task = mainlinePlayer.getTask();
		MainlineTaskCfg mainlineTaskCfg = mainlineTaskCache.getOrThrow(task.getId());
		
		task.setProcess(mainlineTaskCfg.getTaskTargetNum());
		task.checkFinish(mainlineTaskCfg.getTaskTargetNum());
		mainlinePlayerService.update(mainlinePlayer);
		
		PlayerTaskChangeMessage.Builder builder = PlayerTaskChangeMessage.newBuilder();
        builder.setTaskSystemType(TaskSystem.MAINLINE.getValue());
        builder.addTasks(PbHelper.build(task));
        playerContextManager.push(playerContext.getId(), PlayerTaskChangeMessage.Proto.ID, builder.build());
	}
	
	@PlayerGmCmd(command = "ML_TASK_JUMP")
	public void taskJump(PlayerContext playerContext, String[] params) {
		Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);
		int taskId = Integer.parseInt(params[0]);
		
		BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
		Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
		Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);
		
		MainlineTaskCfg mainlineTaskCfg = mainlineTaskCache.getOrThrow(taskId);
		Asserts.isTrue(mainlineTaskCfg.getMainlineId() == context.getSceneId(), TipsCode.MAINLINE_TASK_NOT_CURR);
		
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
		
		Asserts.isTrue(taskId > mainlinePlayer.getTask().getId(), TipsCode.MAINLINE_JUMP_TASK_FORWARD);
		Task parallelTask = mainlinePlayer.getParallelTasks().remove(taskId);
		if(parallelTask != null) {
			mainlinePlayer.setTask(parallelTask);
		} else {
			mainlinePlayer.setTask(Task.create(mainlineTaskCfg.getId()));
		}
		mainlinePlayerService.update(mainlinePlayer);
		
		PlayerTaskChangeMessage.Builder builder = PlayerTaskChangeMessage.newBuilder();
        builder.setTaskSystemType(TaskSystem.MAINLINE.getValue());
        builder.addTasks(PbHelper.build(mainlinePlayer.getTask()));
        playerContextManager.push(playerContext.getId(), PlayerTaskChangeMessage.Proto.ID, builder.build());
	}
	
	//重置露营时间
	@PlayerGmCmd(command = "ML_CAMP_RESET")
	public void campReset(PlayerContext playerContext, String[] params) {
		MainlinePlayer mainlinePlayer = mainlinePlayerService.getEntity(playerContext.getId());
		mainlinePlayer.setCampTime(0);
		mainlinePlayer.setStartCampTime(DateTimeUtil.currMillis());
		mainlinePlayerService.update(mainlinePlayer);
	}
	
	@PlayerGmCmd(command = "ML_OPEN_ALL")
	public void openAll(PlayerContext playerContext, String[] params) {
		int mainsceneId = Integer.parseInt(params[0]);
		MainlineScene mainlineScene = mainlineSceneService.getOrThrow(playerContext.getId(), mainsceneId);
		
		//解锁所有迷雾
		for(SceneFogCfg sceneFogCfg : sceneFogCache.all()) {
			if(sceneFogCfg.getMainlineId() != mainlineScene.getIdentity()) {
				continue;
			}
			mainlineScene.getOpeningFogs().remove(sceneFogCfg.getFogId());
            mainlineScene.getFogs().add(sceneFogCfg.getFogId());
            
            EventBus.post(FogOpenEvent.of(playerContext.getId(), mainlineScene.getIdentity(), sceneFogCfg.getFogId()));
            
            MainlineFogOpenResponse.Builder response = MainlineFogOpenResponse.newBuilder();
            response.setUnlockFog(sceneFogCfg.getFogId());
            playerContextManager.push(playerContext.getId(), MainlineFogOpenResponse.Proto.ID, response.build());
		}
		mainlineSceneService.update(mainlineScene);
		
		//解锁所有传送
		for(SceneTeleportCfg sceneTeleportCfg : sceneTeleportCache.all()) {
 			if(sceneTeleportCfg.getMainlineId() != mainlineScene.getIdentity()) {
 				continue;
 			}
 			mainlineScene.getOpeningTeleports().remove(sceneTeleportCfg.getTeleportId());
            mainlineScene.getTeleports().add(sceneTeleportCfg.getTeleportId());
            
            EventBus.post(BuildingTransmitEvent.of(playerContext.getId(), mainlineScene.getIdentity(), sceneTeleportCfg.getTeleportId()));
            
            MainlineTeleportOpenResponse.Builder response = MainlineTeleportOpenResponse.newBuilder();
            response.setUnlockTeleport(sceneTeleportCfg.getTeleportId());
            playerContextManager.push(playerContext.getId(), MainlineTeleportOpenResponse.Proto.ID, response.build());
		}
		mainlineSceneService.update(mainlineScene);
	}
	
	@PlayerGmCmd(command = "ML_SPAWN_MONSTER")
	public void spawnMonster(PlayerContext playerContext, String[] params) {
		int zoneId = Integer.parseInt(params[0]);
		int monsterId = Integer.parseInt(params[1]);
		int monsterX = Integer.parseInt(params[2]);
		int monsterY = Integer.parseInt(params[3]);
		
		BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.NOT_IN_MAINLINE);
        Asserts.isTrue(context.getBattleType() == BattleType.MAINLINE, TipsCode.NOT_IN_MAINLINE);
        
        IMonsterConfig monsterConfig = monsterCache.getConfig(monsterId);
        Sprite sprite = context.spawnMonster(monsterConfig, TeamSide.DEF, 1, monsterX, monsterY);
        context.addMonsterFromGm(zoneId, sprite);
        
        BattleSpawnMonsterGmMessage.Builder message = BattleSpawnMonsterGmMessage.newBuilder();
        message.setBattleType(context.getBattleType().getValue());
        message.setSprite(sprite.build());
        playerContextManager.push(playerContext.getId(), BattleSpawnMonsterGmMessage.Proto.ID, message.build());
	}
}
