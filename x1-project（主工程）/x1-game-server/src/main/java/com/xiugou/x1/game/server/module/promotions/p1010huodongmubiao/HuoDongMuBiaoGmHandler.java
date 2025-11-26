/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.module.ActivityRewardsCache;
import com.xiugou.x1.design.module.autogen.ActivityRewardsAbstractCache.ActivityRewardsCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.model.HuoDongMuBiao;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service.HuoDongMuBiaoService;
import com.xiugou.x1.game.server.module.task.TaskSystem;

import pb.xiugou.x1.protobuf.task.Task.PlayerTaskChangeMessage;

/**
 * @author yy
 *
 */
@Controller
public class HuoDongMuBiaoGmHandler {

	@Autowired
	private HuoDongMuBiaoService huoDongMuBiaoService;
	@Autowired
	private ActivityRewardsCache activityRewardsCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	@PlayerGmCmd(command = "HDMB_FINISH")
	public void finish(PlayerContext playerContext, String[] params) {
		int typeId = Integer.parseInt(params[0]);
		int taskId = Integer.parseInt(params[1]);
		
		HuoDongMuBiao entity = huoDongMuBiaoService.getEntity(playerContext.getId(), typeId);
		Task task = null;
		for(Task temp : entity.getTasks()) {
			if(temp.getId() == taskId) {
				task = temp;
				break;
			}
		}
		if(task == null) {
			return;
		}
		ActivityRewardsCfg activityRewardsCfg = activityRewardsCache.getOrThrow(task.getId());
		task.setProcess(activityRewardsCfg.getTaskTargetNum());
		task.checkFinish(activityRewardsCfg.getTaskTargetNum());
		huoDongMuBiaoService.update(entity);
		
		PlayerTaskChangeMessage.Builder builder = PlayerTaskChangeMessage.newBuilder();
        builder.setTaskSystemType(TaskSystem.HDMB_TASK.getValue());
        builder.addTasks(PbHelper.build(task));
        playerContextManager.push(playerContext.getId(), PlayerTaskChangeMessage.Proto.ID, builder.build());
	}
	
	@PlayerGmCmd(command = "HDMB_FINISH_ALL")
	public void finishAll(PlayerContext playerContext, String[] params) {
		int typeId = Integer.parseInt(params[0]);
		
		List<Task> changeTasks = new ArrayList<>();
		HuoDongMuBiao entity = huoDongMuBiaoService.getEntity(playerContext.getId(), typeId);
		for(Task task : entity.getTasks()) {
			if(task.isDone() || task.isEmpty()) {
				continue;
			}
			ActivityRewardsCfg activityRewardsCfg = activityRewardsCache.getOrThrow(task.getId());
			task.setProcess(activityRewardsCfg.getTaskTargetNum());
			task.checkFinish(activityRewardsCfg.getTaskTargetNum());
			changeTasks.add(task);
		}
		huoDongMuBiaoService.update(entity);
		
		PlayerTaskChangeMessage.Builder builder = PlayerTaskChangeMessage.newBuilder();
        builder.setTaskSystemType(TaskSystem.HDMB_TASK.getValue());
        for(Task task : changeTasks) {
        	builder.addTasks(PbHelper.build(task));
        }
        playerContextManager.push(playerContext.getId(), PlayerTaskChangeMessage.Proto.ID, builder.build());
	}
}