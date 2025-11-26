/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.model;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 *
 */
@Repository
@Table(name = "arena_ranker", comment = "竞技场排行榜表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "cross_season", columns = { "cross_croup", "season" }),
		@Index(name = "cross_season_pid", columns = { "cross_croup", "season", "player_id" }, type = IndexType.UNIQUE) })
public class ArenaRanker extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "cross_croup", comment = "跨服分组ID", readonly = true)
	private int crossGroup;
	@Column(comment = "赛季", readonly = true)
	private int season;
	@Column(name = "player_id", comment = "玩家ID", readonly = true)
	private long playerId;
	@Column(comment = "昵称（仅用于看）", readonly = true)
	private String nick;
	@Column(name = "server_id", comment = "玩家所在服务器ID", readonly = true)
	private int serverId;
	@Column(comment = "积分")
	private long score;
	@Column(name = "daily_rank", comment = "每日结算时的排名")
	private int dailyRank;
	@Column(name = "season_rank", comment = "赛季结算时的排名")
	private int seasonRank;
	@Column(comment = "是不是机器人", readonly = true)
	private boolean robot;
	@Column(name = "robot_nick", comment = "机器人名字", readonly = true)
	private String robotNick = "";
	@Column(name = "robot_image", comment = "机器人形象", readonly = true)
	private int robotImage;
	@Column(name = "robot_head", comment = "机器人头像", readonly = true)
	private int robotHead;
	@Column(name = "robot_fighting", comment = "机器人战力", readonly = true)
	private long robotFighting;
	@Column(name = "monster_ids", comment = "怪物ID", length = 500, readonly = true)
	private List<Integer> monsterIds = new ArrayList<>();
	@Column(name = "has_fight", comment = "是否有战斗过，区分有战斗过的1000分跟没有战斗过的1000分")
	private boolean hasFight;
	@Column(name = "robot_cfg", comment = "机器人配置ID", readonly = true)
	private int robotCfg;

	public int getCrossGroup() {
		return crossGroup;
	}

	public void setCrossGroup(int crossGroup) {
		this.crossGroup = crossGroup;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getDailyRank() {
		return dailyRank;
	}

	public void setDailyRank(int dailyRank) {
		this.dailyRank = dailyRank;
	}

	public int getSeasonRank() {
		return seasonRank;
	}

	public void setSeasonRank(int seasonRank) {
		this.seasonRank = seasonRank;
	}

	public boolean isRobot() {
		return robot;
	}

	public void setRobot(boolean robot) {
		this.robot = robot;
	}

	public String getRobotNick() {
		return robotNick;
	}

	public void setRobotNick(String robotNick) {
		this.robotNick = robotNick;
	}

	public int getRobotImage() {
		return robotImage;
	}

	public void setRobotImage(int robotImage) {
		this.robotImage = robotImage;
	}

	public int getRobotHead() {
		return robotHead;
	}

	public void setRobotHead(int robotHead) {
		this.robotHead = robotHead;
	}

	public List<Integer> getMonsterIds() {
		return monsterIds;
	}

	public void setMonsterIds(List<Integer> monsterIds) {
		this.monsterIds = monsterIds;
	}

	public boolean isHasFight() {
		return hasFight;
	}

	public void setHasFight(boolean hasFight) {
		this.hasFight = hasFight;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRobotCfg() {
		return robotCfg;
	}

	public void setRobotCfg(int robotCfg) {
		this.robotCfg = robotCfg;
	}

	public long getRobotFighting() {
		return robotFighting;
	}

	public void setRobotFighting(long robotFighting) {
		this.robotFighting = robotFighting;
	}
}
