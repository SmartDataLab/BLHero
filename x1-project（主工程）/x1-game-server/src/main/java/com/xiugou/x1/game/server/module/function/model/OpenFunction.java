package com.xiugou.x1.game.server.module.function.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.function.struct.Guide;

/**
 * @author yh
 * @date 2023/6/7
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "open_function", comment = "功能开启表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class OpenFunction extends AbstractEntity {
    @Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "function_ids", comment = "已开启的功能ID", length = 5000)
    private Set<Integer> functionIds = new HashSet<>();
    @Column(name = "guides", comment = "已完成的新手引导步骤", length = 5000)
    private Map<Integer, Guide> guides = new HashMap<>();

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

	public Set<Integer> getFunctionIds() {
		return functionIds;
	}

	public void setFunctionIds(Set<Integer> functionIds) {
		this.functionIds = functionIds;
	}

	public Map<Integer, Guide> getGuides() {
		return guides;
	}

	public void setGuides(Map<Integer, Guide> guides) {
		this.guides = guides;
	}

}

