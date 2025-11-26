/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

import java.time.LocalDateTime;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "game_server_runtime", comment = "游戏服务器运行数据表", dbAlias = "backstage")
public class GameServerRuntime extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(name = "server_uid", comment = "服务器唯一ID")
	private int serverUid;
	@Column(name = "register_num", comment = "注册数")
	private int registerNum;
	@Column(name = "create_num", comment = "创角数")
	private int createNum;
	@Column(name = "online_num", comment = "在线数")
	private int onlineNum;
	@Column(name = "battle_num", comment = "战斗数")
	private int battleNum;
	@Column(name = "curr_battle_num", comment = "当前战斗数")
	private int currBattleNum;
	@Column(comment = "服务器是否正常运行")
	private boolean running;
	@Column(name = "heart_time", comment = "最后心跳时间")
	private LocalDateTime heartTime = LocalDateTime.now();
	@Column(name = "max_memory", comment = "最大可用内存")
	private String maxMemory;
	@Column(name = "free_memory", comment = "空闲内存")
	private String freeMemory;
	@Column(name = "total_memory", comment = "占用内存（占用内存=空闲内存+实际使用内存）")
	private String totalMemory;
	@Column(name = "used_memory", comment = "实际使用内存")
	private String usedMemory;
	@Column(name = "left_memory", comment = "剩余可用内存（剩余可用内存=最大可用内存-实际使用内存）")
	private String leftMemory;
	
	public int getServerUid() {
		return serverUid;
	}
	public void setServerUid(int serverUid) {
		this.serverUid = serverUid;
	}
	public int getRegisterNum() {
		return registerNum;
	}
	public void setRegisterNum(int registerNum) {
		this.registerNum = registerNum;
	}
	public int getCreateNum() {
		return createNum;
	}
	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}
	public int getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}
	public int getBattleNum() {
		return battleNum;
	}
	public void setBattleNum(int battleNum) {
		this.battleNum = battleNum;
	}
	public int getCurrBattleNum() {
		return currBattleNum;
	}
	public void setCurrBattleNum(int currBattleNum) {
		this.currBattleNum = currBattleNum;
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public LocalDateTime getHeartTime() {
		return heartTime;
	}
	public void setHeartTime(LocalDateTime heartTime) {
		this.heartTime = heartTime;
	}
	@Override
	public Long redisOwnerKey() {
		return 0L;
	}
	@Override
	public Long redisHashKey() {
		return (long)serverUid;
	}
	public String getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}
	public String getFreeMemory() {
		return freeMemory;
	}
	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}
	public String getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}
	public String getUsedMemory() {
		return usedMemory;
	}
	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}
	public String getLeftMemory() {
		return leftMemory;
	}
	public void setLeftMemory(String leftMemory) {
		this.leftMemory = leftMemory;
	}
}
