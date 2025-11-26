/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1007qirimubiao;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskStatus;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.QiRiMuBiaoStageCache;
import com.xiugou.x1.design.module.QiRiMuBiaoTaskCache;
import com.xiugou.x1.design.module.QiRiMuBiaoTaskCache.QiRiMuBiaoTaskConfig;
import com.xiugou.x1.design.module.autogen.QiRiMuBiaoStageAbstractCache.QiRiMuBiaoStageCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.model.QiRiMuBiao;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.service.QiRiMuBiaoService;

import pb.xiugou.x1.protobuf.promotion.P1007QiRiMuBiao.QRMBTakeFinalRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1007QiRiMuBiao.QRMBTakeFinalRewardResponse;
import pb.xiugou.x1.protobuf.promotion.P1007QiRiMuBiao.QRMBTakeTaskRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1007QiRiMuBiao.QRMBTakeTaskRewardResponse;
import pb.xiugou.x1.protobuf.promotion.P1007QiRiMuBiao.QiRiMuBiaoInfoResponse;

/**
 * @author YY
 *
 */
@Controller
public class QiRiMuBiaoHandler extends AbstractModuleHandler {

	@Autowired
	private QiRiMuBiaoService qiRiMuBiaoService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private QiRiMuBiaoTaskCache qiRiMuBiaoTaskCache;
	@Autowired
	private QiRiMuBiaoStageCache qiRiMuBiaoStageCache;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		QiRiMuBiao entity = qiRiMuBiaoService.getEntity(playerId);
		
		QiRiMuBiaoInfoResponse.Builder response = QiRiMuBiaoInfoResponse.newBuilder();
		response.setDay(entity.getDay());
		response.setEndTime(LocalDateTimeUtil.toEpochMilli(entity.getEndTime()));
		for(Task task : entity.getTaskMap().values()) {
			response.addTasks(PbHelper.build(task));
		}
		response.addAllStageRewards(entity.getStageRewards());
		playerContextManager.push(playerId, QiRiMuBiaoInfoResponse.Proto.ID, response.build());
	}

	@PlayerCmd
	public QRMBTakeTaskRewardResponse taskReward(PlayerContext playerContext, QRMBTakeTaskRewardRequest request) {
		QiRiMuBiao entity = qiRiMuBiaoService.getEntity(playerContext.getId());
		
		Task task = entity.getTaskMap().get(request.getTaskId());
		Asserts.isTrue(task != null, TipsCode.QRMB_TASK_NULL, request.getTaskId());
		Asserts.isTrue(!task.isEmpty(), TipsCode.QRMB_TASK_REWARD_TOOK);
		Asserts.isTrue(task.isDone(), TipsCode.QRMB_TASK_NOT_FIN);
		
		QiRiMuBiaoTaskConfig qiRiMuBiaoTaskConfig = qiRiMuBiaoTaskCache.getOrThrow(request.getTaskId());
		
		task.setStatus(TaskStatus.EMPTY.getValue());
		qiRiMuBiaoService.update(entity);
		
		thingService.add(playerContext.getId(), qiRiMuBiaoTaskConfig.getRewards(), GameCause.QRMB_TASK_REWARD);
		
		QRMBTakeTaskRewardResponse.Builder response = QRMBTakeTaskRewardResponse.newBuilder();
		response.setTask(PbHelper.build(task));
		return response.build();
	}
	
	@PlayerCmd
	public QRMBTakeFinalRewardResponse finalReward(PlayerContext playerContext, QRMBTakeFinalRewardRequest request) {
		QiRiMuBiao entity = qiRiMuBiaoService.getEntity(playerContext.getId());
		
		Asserts.isTrue(!entity.getStageRewards().contains(request.getStageId()), TipsCode.QRMB_FINAL_REWARD_TOOK);
		int taskNum = 0;
		for(Task task : entity.getTaskMap().values()) {
			if(task.isEmpty()) {
				taskNum += 1;
			}
		}
		QiRiMuBiaoStageCfg qiRiMuBiaoStageCfg = qiRiMuBiaoStageCache.getOrThrow(request.getStageId());
		Asserts.isTrue(taskNum >= qiRiMuBiaoStageCfg.getCondition(), TipsCode.QRMB_FINAL_NOT_FIN);
		
		entity.getStageRewards().add(qiRiMuBiaoStageCfg.getId());
		qiRiMuBiaoService.update(entity);
		
		thingService.add(playerContext.getId(), qiRiMuBiaoStageCfg.getReward(), GameCause.QRMB_FINAL_REWARD);
		
		QRMBTakeFinalRewardResponse.Builder response = QRMBTakeFinalRewardResponse.newBuilder();
		response.setStageId(qiRiMuBiaoStageCfg.getId());
		return response.build();
	}
}
