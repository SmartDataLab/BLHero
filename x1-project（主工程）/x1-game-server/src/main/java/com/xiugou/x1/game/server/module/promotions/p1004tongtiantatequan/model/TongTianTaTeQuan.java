/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.model;

import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.struct.TTTTQDetail;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(relation = {"pid", "typeId"})
@Table(name = "p1004_tong_tian_ta_te_quan", comment = "通天塔特权", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
        @Index(name = "pid", columns = {"pid"})})
public class TongTianTaTeQuan extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "唯一ID")
    private long id;
    @Column(comment = "玩家ID", readonly = true)
    private long pid;
    @Column(name = "type_id", comment = "活动类型ID", readonly = true)
    private int typeId;
    @Column(name = "tower_details", comment = "奖励数据", extra = "text")
    private Map<Integer, TTTTQDetail> towerDetails = new HashMap<>();
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
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public Map<Integer, TTTTQDetail> getTowerDetails() {
		return towerDetails;
	}
	public void setTowerDetails(Map<Integer, TTTTQDetail> towerDetails) {
		this.towerDetails = towerDetails;
	}
}
