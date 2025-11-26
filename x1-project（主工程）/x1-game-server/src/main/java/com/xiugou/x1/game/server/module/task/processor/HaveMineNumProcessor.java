package com.xiugou.x1.game.server.module.task.processor;

import org.gaming.prefab.task.AbstractTaskEventProcessor;
import org.gaming.prefab.task.ITaskConfig;
import org.gaming.prefab.task.Task;
import org.gaming.prefab.task.TaskChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.module.home.service.HomeService;

/**
 * @author yh
 * @date 2023/8/31
 * @apiNote 简单的次数替换的任务处理器
 */
@Component
public class HaveMineNumProcessor extends AbstractTaskEventProcessor {
	
	@Autowired
	private HomeService homeService;
	
    @Override
    protected boolean handling(long rid, Task task, TaskChange change, ITaskConfig taskConfig) {
        task.setProcess(homeService.getEntity(rid).getMine());
        task.checkFinish(taskConfig.getTargetNum());
        return true;
    }
}
