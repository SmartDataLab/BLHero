/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.service;

import java.util.Collections;
import java.util.List;

import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.ITaskSystem;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.QiRiMuBiaoTaskCache;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.model.QiRiMuBiao;
import com.xiugou.x1.game.server.module.task.PlayerTaskSystem;
import com.xiugou.x1.game.server.module.task.TaskSystem;

/**
 * @author YY
 *
 */
@Service
public class QiRiMuBiaoTaskService extends PlayerTaskSystem<QiRiMuBiao> {

	@Autowired
	private QiRiMuBiaoService qiRiMuBiaoService;
	@Autowired
	private QiRiMuBiaoTaskCache qiRiMuBiaoTaskCache;
	
	@Override
	protected ITaskSystem taskSystem() {
		return TaskSystem.QRMB_TASK;
	}

	@Override
	protected ITaskConfig taskConfig(int taskId) {
		return qiRiMuBiaoTaskCache.getOrThrow(taskId);
	}

	@Override
	protected List<QiRiMuBiao> taskContainers(long entityId) {
		return Collections.singletonList(qiRiMuBiaoService.getEntity(entityId));
	}
	
	@Override
	protected void saveTaskEntity(QiRiMuBiao taskContainer, List<Task> changedTasks) {
		qiRiMuBiaoService.update(taskContainer);
	}
}
