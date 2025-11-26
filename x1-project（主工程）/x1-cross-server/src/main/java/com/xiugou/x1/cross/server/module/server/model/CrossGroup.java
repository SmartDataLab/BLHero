/**
 * 
 */
package com.xiugou.x1.cross.server.module.server.model;

import java.util.HashSet;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yy
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "cross_group", comment = "跨服分组表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class CrossGroup extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "分区ID")
	private int id;
	@Column(comment = "分组内的主服务器ID", extra = "text")
	private Set<Integer> servers = new HashSet<>();
	@Column(name = "merge_servers", comment = "分组内的合服服务器ID", extra = "text")
	private Set<Integer> mergeServers = new HashSet<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Integer> getServers() {
		return servers;
	}
	public void setServers(Set<Integer> servers) {
		this.servers = servers;
	}
	public Set<Integer> getMergeServers() {
		return mergeServers;
	}
	public void setMergeServers(Set<Integer> mergeServers) {
		this.mergeServers = mergeServers;
	}
}
