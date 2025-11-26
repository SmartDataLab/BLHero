/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.mainline.struct.SceneOpening;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(relation = {"pid", "identity"})
@Table(name = "mainline_scene", comment = "场景信息表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_identity", columns = { "pid", "identity" }, type = IndexType.UNIQUE) })
public class MainlineScene extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(comment = "场景标识ID", readonly = true)
	private int identity;
	@Column(name = "max_stage", comment = "当前开启的最大场景阶段")
	private int maxStage;
	@Column(name = "first_stages", comment = "已领取首通奖励的阶段")
	private Set<Integer> firstStages = new HashSet<>();
	
	@Column(name = "curr_stage", comment = "当前进行挑战的场景阶级")
	private int currStage;
	
	@Column(comment = "已经开启的传送点", length = 1000)
	private Set<Integer> teleports = new HashSet<>();
	@Column(name = "opening_teleports", comment = "正在开启的传送点", length = 2000)
	private Map<Integer, SceneOpening> openingTeleports = new HashMap<>();
	@Column(comment = "已经开启的区域", extra = "text")
	private Set<Integer> fogs = new HashSet<>();
	@Column(name = "opening_fogs", comment = "正在开启的迷雾", length = 2000)
	private Map<Integer, SceneOpening> openingFogs = new HashMap<>();
	@Column(comment = "已经完成缴纳的npc", extra = "text")
	private Set<Integer> npcs = new HashSet<>();
	@Column(name = "opening_npcs", comment = "正在缴纳素材的npc", length = 2000)
	private Map<Integer, SceneOpening> openingNpcs = new HashMap<>();
	@Column(comment = "已经通关的场景副本")
	private Set<Integer> dungeons = new HashSet<>();
	
	@Column(name = "portal_x", comment = "传送门X坐标")
	private int portalX;
	@Column(name = "portal_y", comment = "传送门Y坐标")
	private int portalY;
	
	@Column(name = "kill_monsters", comment = "不需要再刷新出来的怪物ID", length = 2000)
	private Set<Integer> killMonsters = new HashSet<>();
	
	@Column(name = "open_next", comment = "是否已经开启下一场景")
	private boolean openNext;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public int getMaxStage() {
		return maxStage;
	}
	public void setMaxStage(int maxStage) {
		this.maxStage = maxStage;
	}
	public int getCurrStage() {
		return currStage;
	}
	public void setCurrStage(int currStage) {
		this.currStage = currStage;
	}
	public Set<Integer> getTeleports() {
		return teleports;
	}
	public void setTeleports(Set<Integer> teleports) {
		this.teleports = teleports;
	}
	public Set<Integer> getFirstStages() {
		return firstStages;
	}
	public void setFirstStages(Set<Integer> firstStages) {
		this.firstStages = firstStages;
	}
	public Map<Integer, SceneOpening> getOpeningTeleports() {
		return openingTeleports;
	}
	public void setOpeningTeleports(Map<Integer, SceneOpening> openingTeleports) {
		this.openingTeleports = openingTeleports;
	}
	public Set<Integer> getNpcs() {
		return npcs;
	}
	public void setNpcs(Set<Integer> npcs) {
		this.npcs = npcs;
	}
	public Map<Integer, SceneOpening> getOpeningNpcs() {
		return openingNpcs;
	}
	public void setOpeningNpcs(Map<Integer, SceneOpening> openingNpcs) {
		this.openingNpcs = openingNpcs;
	}
	public int getPortalX() {
		return portalX;
	}
	public void setPortalX(int portalX) {
		this.portalX = portalX;
	}
	public int getPortalY() {
		return portalY;
	}
	public void setPortalY(int portalY) {
		this.portalY = portalY;
	}
	public Set<Integer> getFogs() {
		return fogs;
	}
	public void setFogs(Set<Integer> fogs) {
		this.fogs = fogs;
	}
	public Map<Integer, SceneOpening> getOpeningFogs() {
		return openingFogs;
	}
	public void setOpeningFogs(Map<Integer, SceneOpening> openingFogs) {
		this.openingFogs = openingFogs;
	}
	public Set<Integer> getDungeons() {
		return dungeons;
	}
	public void setDungeons(Set<Integer> dungeons) {
		this.dungeons = dungeons;
	}
	
	public SceneOpening getOpeningFog(int fogId) {
		SceneOpening sceneOpening = openingFogs.get(fogId);
		if(sceneOpening == null) {
			sceneOpening = new SceneOpening();
			sceneOpening.setId(fogId);
			openingFogs.put(fogId, sceneOpening);
		}
		return sceneOpening;
	}
	
	public SceneOpening getOpeningNpc(int npcId) {
		SceneOpening sceneOpening = openingNpcs.get(npcId);
		if(sceneOpening == null) {
			sceneOpening = new SceneOpening();
			sceneOpening.setId(npcId);
			openingNpcs.put(npcId, sceneOpening);
		}
		return sceneOpening;
	}
	
	public SceneOpening getOpeningTeleport(int teleportId) {
		SceneOpening sceneOpening = openingTeleports.get(teleportId);
		if(sceneOpening == null) {
			sceneOpening = new SceneOpening();
			sceneOpening.setId(teleportId);
			openingTeleports.put(teleportId, sceneOpening);
		}
		return sceneOpening;
	}
	public Set<Integer> getKillMonsters() {
		return killMonsters;
	}
	public void setKillMonsters(Set<Integer> killMonsters) {
		this.killMonsters = killMonsters;
	}
	public boolean isOpenNext() {
		return openNext;
	}
	public void setOpenNext(boolean openNext) {
		this.openNext = openNext;
	}
}
