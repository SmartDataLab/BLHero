package com.xiugou.x1.game.server.module.evil.model;

import com.xiugou.x1.game.server.module.evil.struct.RefineEvilData;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "evil_furnace", comment = "炼妖炉", dbAlias = "game", asyncType = AsyncType.INSERT)
public class EvilFurnace extends AbstractEntity {
	@Id(strategy = Id.Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "speed_up_time", comment = "加速时间/分")
	private long speedUpTime;
	@Column(name = "refine_detail", comment = "炼妖详情", length = 1000)
	private List<RefineEvilData> refineDetail = new ArrayList<>();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getSpeedUpTime() {
		return speedUpTime;
	}

	public void setSpeedUpTime(long speedUpTime) {
		this.speedUpTime = speedUpTime;
	}

	public List<RefineEvilData> getRefineDetail() {
		return refineDetail;
	}

	public void setRefineDetail(List<RefineEvilData> refineDetail) {
		this.refineDetail = refineDetail;
	}
}
