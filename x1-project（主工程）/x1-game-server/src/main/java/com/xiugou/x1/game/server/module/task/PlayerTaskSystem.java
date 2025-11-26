/**
 *
 */
package com.xiugou.x1.game.server.module.task;

import java.util.List;

import org.gaming.prefab.task.AbstractTaskSystem;
import org.gaming.prefab.task.ITaskContainer;
import org.gaming.prefab.task.ITaskEvent;
import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;

import pb.xiugou.x1.protobuf.task.Task.PlayerTaskChangeMessage;

/**
 * @author YY
 *
 */
public abstract class PlayerTaskSystem<T extends ITaskContainer> extends AbstractTaskSystem<T> {

    @Autowired
    protected PlayerContextManager playerContextManager;

    /**
     * 保存玩家的数据
     * @param playerId
     */
    protected abstract void saveTaskEntity(T taskContainer, List<Task> changedTasks);

    @Override
    protected final void onChangeTask(T taskContainer, ITaskEvent taskEvent, List<Task> changedTasks) {
        //保存玩家的数据
        saveTaskEntity(taskContainer, changedTasks);

        PlayerTaskChangeMessage.Builder builder = PlayerTaskChangeMessage.newBuilder();
        builder.setTaskSystemType(this.taskSystem().getValue());
        for (Task task : changedTasks) {
            builder.addTasks(PbHelper.build(task));
        }
        playerContextManager.push(taskEvent.getEntityId(), PlayerTaskChangeMessage.Proto.ID, builder.build());
        
        afterPushChangeTask(taskEvent.getEntityId(), changedTasks);
    }
    
    protected void afterPushChangeTask(long playerId, List<Task> changedTasks) {
    	//to be override
    }
}
