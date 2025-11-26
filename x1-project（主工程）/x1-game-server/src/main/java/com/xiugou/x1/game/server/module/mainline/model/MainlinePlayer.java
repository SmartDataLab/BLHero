/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.task.ITaskContainer;
import org.gaming.prefab.task.Task;
import org.gaming.tool.DateTimeUtil;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;
import com.xiugou.x1.game.server.module.mainline.struct.CampProduce;
import com.xiugou.x1.game.server.module.mainline.struct.TreasureBox;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "mainline_player", comment = "玩家场景数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class MainlinePlayer extends AbstractEntity implements DailyResetEntity, ITaskContainer {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "curr_battle_type", comment = "玩家当前的任务类型")
	private int currBattleType;
	@Column(name = "curr_scene", comment = "玩家当前所在的场景")
	private int currScene;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "start_camp_time", comment = "开始露营的时间，单位毫秒")
	private long startCampTime;
	@Column(name = "camping", comment = "是否正在露营")
	private boolean camping;
	@Column(name = "camp_time", comment = "已露营时间，单位毫秒")
	private long campTime;
	@Column(name = "camp_adv_end_time", comment = "高级露营结束时间，单位毫秒")
	private long campAdvEndTime;
	@Column(name = "camp_adv_num", comment = "已进行高级露营次数")
	private int campAdvNum;
	@Column(name = "camp_produces", comment = "露营累计产出，仅用于展示", length = 5000)
	private Map<Integer, CampProduce> campProduces = new HashMap<>();
	@Column(name = "revive_num", comment = "可复活次数")
	private int reviveNum;
	@Column(name = "kill_num", comment = "主线杀怪数量")
	private int killNum;
	@Column(name = "all_dead_num", comment = "主线野外全死次数")
	private int allDeadNum;
	
	@Column(name = "treasure_num", comment = "拾取的宝箱数量")
	private int treasureNum;
	@Column(name = "boss_treasure", comment = "BOSS宝箱", length = 1000)
	private TreasureBox bossTreasure = new TreasureBox();
	@Column(name = "map_treasure", comment = "是否有地图宝箱", length = 1000)
	private TreasureBox mapTreasure = new TreasureBox();
	@Column(name = "next_box_time", comment = "下一次出现时间宝箱的时间")
	private LocalDateTime nextBoxTime = LocalDateTime.now();
	
	@Column(name = "hang_opened", comment = "离线挂机功能是否已经开启")
	private boolean hangOpened;
	@Column(name = "hang_start_time", comment = "离线挂机开始时间")
	private long hangStartTime;
	@Column(name = "hang_boss_id", comment = "离线挂机BossID")
	private int hangBossId;
	@Column(name = "has_hang_reward", comment = "是否有离线挂机奖励")
	private boolean hasHangReward;
	
	@Column(comment = "当前场景下的任务数据")
	private Task task = new Task();
	@Column(name = "parallel_tasks", comment = "并行任务数据", extra = "text")
	private Map<Integer, Task> parallelTasks = new HashMap<>();
	@Column(name = "finish_tasks", comment = "已经完成的任务", extra = "text")
	private Set<Integer> finishTasks = new HashSet<>();
	
	public void addCampProduces(List<RewardThing> rewards) {
		for(RewardThing rewardThing : rewards) {
			CampProduce campProduce = campProduces.get(rewardThing.getThingId());
			if(campProduce == null) {
				campProduce = new CampProduce();
				campProduce.setItem(rewardThing.getThingId());
				campProduces.put(campProduce.getItem(), campProduce);
			}
			campProduce.setNum(campProduce.getNum() + rewardThing.getNum());
		}
	}
	
	public boolean isCampingAdv() {
		return camping && campAdvEndTime > DateTimeUtil.currMillis();
	}
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getCurrScene() {
		return currScene;
	}
	public void setCurrScene(int currScene) {
		this.currScene = currScene;
	}
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}
	public Map<Integer, CampProduce> getCampProduces() {
		return campProduces;
	}
	public void setCampProduces(Map<Integer, CampProduce> campProduces) {
		this.campProduces = campProduces;
	}
	public boolean isCamping() {
		return camping;
	}
	public void setCamping(boolean camping) {
		this.camping = camping;
	}
	public int getCurrBattleType() {
		return currBattleType;
	}
	public void setCurrBattleType(int currBattleType) {
		this.currBattleType = currBattleType;
	}
	public int getReviveNum() {
		return reviveNum;
	}
	public void setReviveNum(int reviveNum) {
		this.reviveNum = reviveNum;
	}
	public int getTreasureNum() {
		return treasureNum;
	}
	public void setTreasureNum(int treasureNum) {
		this.treasureNum = treasureNum;
	}
	public TreasureBox getBossTreasure() {
		return bossTreasure;
	}
	public void setBossTreasure(TreasureBox bossTreasure) {
		this.bossTreasure = bossTreasure;
	}
	public TreasureBox getMapTreasure() {
		return mapTreasure;
	}
	public void setMapTreasure(TreasureBox mapTreasure) {
		this.mapTreasure = mapTreasure;
	}
	public LocalDateTime getNextBoxTime() {
		return nextBoxTime;
	}
	public void setNextBoxTime(LocalDateTime nextBoxTime) {
		this.nextBoxTime = nextBoxTime;
	}

	public long getCampTime() {
		return campTime;
	}

	public void setCampTime(long campTime) {
		this.campTime = campTime;
	}

	public long getCampAdvEndTime() {
		return campAdvEndTime;
	}

	public void setCampAdvEndTime(long campAdvEndTime) {
		this.campAdvEndTime = campAdvEndTime;
	}

	public long getStartCampTime() {
		return startCampTime;
	}

	public void setStartCampTime(long startCampTime) {
		this.startCampTime = startCampTime;
	}

	public int getCampAdvNum() {
		return campAdvNum;
	}

	public void setCampAdvNum(int campAdvNum) {
		this.campAdvNum = campAdvNum;
	}

	public int getKillNum() {
		return killNum;
	}

	public void setKillNum(int killNum) {
		this.killNum = killNum;
	}

	public boolean isHangOpened() {
		return hangOpened;
	}

	public void setHangOpened(boolean hangOpened) {
		this.hangOpened = hangOpened;
	}

	public long getHangStartTime() {
		return hangStartTime;
	}

	public void setHangStartTime(long hangStartTime) {
		this.hangStartTime = hangStartTime;
	}

	public int getHangBossId() {
		return hangBossId;
	}

	public void setHangBossId(int hangBossId) {
		this.hangBossId = hangBossId;
	}

	public boolean isHasHangReward() {
		return hasHangReward;
	}

	public void setHasHangReward(boolean hasHangReward) {
		this.hasHangReward = hasHangReward;
	}

	public int getAllDeadNum() {
		return allDeadNum;
	}

	public void setAllDeadNum(int allDeadNum) {
		this.allDeadNum = allDeadNum;
	}
	
	@Override
	public List<Task> getTasks() {
		List<Task> outTasks = new ArrayList<>();
		if (task.getId() > 0) {
			outTasks.add(task);
		}
		outTasks.addAll(this.parallelTasks.values());
		return outTasks;
	}

	@Override
	public long getOwnerId() {
		return pid;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<Integer, Task> getParallelTasks() {
		return parallelTasks;
	}

	public void setParallelTasks(Map<Integer, Task> parallelTasks) {
		this.parallelTasks = parallelTasks;
	}

	public Set<Integer> getFinishTasks() {
		return finishTasks;
	}

	public void setFinishTasks(Set<Integer> finishTasks) {
		this.finishTasks = finishTasks;
	}
}
