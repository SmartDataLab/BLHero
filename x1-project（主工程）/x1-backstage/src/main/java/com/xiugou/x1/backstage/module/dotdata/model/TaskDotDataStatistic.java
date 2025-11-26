/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "dot_data_task_statistic", comment = "任务打点数据统计", dbAlias = "backstage", indexs = {
		@Index(name = "channel_id", columns = { "channel_id"}) })
public class TaskDotDataStatistic extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "channel_id", comment = "渠道ID", readonly = true)
	private long channelId;
	@Column(name = "server_uid", comment = "服务器ID")
	private long serverUid;
	@Column(name = "task_id", comment = "任务ID")
	private int taskId;
	@Column(name = "task_name", comment = "任务名称")
	private String taskName;
	@Column(name = "start_num", comment = "接取人数")
	private int startNum;
	@Column(name = "finish_num", comment = "完成人数")
	private int finishNum;
	
	//
//	1月1日                                           1月2日
//	taskId, num, timing   num, timing 
//	10001   10   开始                   12 
//	10001   8     完成                   10
//	10002   7   开始                       9
//	10002   6     完成                     7
	
	
}
