package com.xiugou.x1.game.server.module.evil.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Repository
@JvmCache(relation = {"pid", "identity"})
@Table(name = "evil", comment = "妖录表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class Evil extends AbstractEntity  {
    @Id(strategy = Id.Strategy.AUTO)
    @Column(comment = "唯一ID")
    private long id;
    @Column(comment = "玩家id")
    private long pid;
    @Column(comment = "妖ID")
    private int identity;
    @Column(comment = "阶")
    private int level;
    @Column(comment = "碎片数量")
    private long fragment;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getFragment() {
        return fragment;
    }

    public void setFragment(long fragment) {
        this.fragment = fragment;
    }
}
