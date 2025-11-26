/**
 * 
 */
package com.xiugou.x1.game.server.module.player.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "forbid_list", comment = "封禁账号表", dbAlias = "game", indexs = {
		@Index(name = "openid", columns = { "open_id" }, type = IndexType.UNIQUE) })
public class ForbidList extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "唯一ID")
    private long id;
    @Column(name = "open_id", comment = "账户ID")
    private String openId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
