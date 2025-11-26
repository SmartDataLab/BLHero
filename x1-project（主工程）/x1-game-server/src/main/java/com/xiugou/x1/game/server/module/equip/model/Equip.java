package com.xiugou.x1.game.server.module.equip.model;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.equip.struct.EquipAttr;

/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Repository
@JvmCache(relation = {"pid", "id"})
@Table(name = "equip", comment = "装备表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
        @Index(name = "pid", columns = {"pid"})})
public class Equip extends AbstractEntity {
    @Id(strategy = Id.Strategy.AUTO)
    @Column(comment = "唯一ID")
    private long id;
    @Column(comment = "玩家ID", readonly = true)
    private long pid;
    @Column(comment = "装备ID", readonly = true)
    private int identity;
    @Column(comment = "装备名称", readonly = true)
    private String name;
    @Column(comment = "是否穿戴")
    private boolean wear;
    @Column(comment = "是否锁定")
    private boolean lock;
    @Column(comment = "是否已鉴定")
    private boolean appraise;
    @Column(comment = "评分")
    private long score;
    @Column(name = "sub_attrs", comment = "附加属性", length = 300)
    private List<EquipAttr> subAttrs = new ArrayList<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWear() {
        return wear;
    }

    public void setWear(boolean wear) {
        this.wear = wear;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isAppraise() {
        return appraise;
    }

    public void setAppraise(boolean appraise) {
        this.appraise = appraise;
    }

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

    public List<EquipAttr> getSubAttrs() {
        return subAttrs;
    }

    public void setSubAttrs(List<EquipAttr> subAttrs) {
        this.subAttrs = subAttrs;
    }
}
