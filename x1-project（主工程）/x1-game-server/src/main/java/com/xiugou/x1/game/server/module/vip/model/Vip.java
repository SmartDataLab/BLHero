package com.xiugou.x1.game.server.module.vip.model;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/28
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "privilege_boost", comment = "特权加成表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Vip extends AbstractEntity {
    @Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "privilege_list", comment = "特权ID", length = 500)
    private List<Integer> privilegeList = new ArrayList<>();
    @Column(name = "vip_gift", comment = "已购买的vip等级礼包", length = 500)
    private List<Integer> vipGift = new ArrayList<>();

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public List<Integer> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<Integer> privilegeList) {
        this.privilegeList = privilegeList;
    }

    public List<Integer> getVipGift() {
        return vipGift;
    }

    public void setVipGift(List<Integer> vipGift) {
        this.vipGift = vipGift;
    }
}
