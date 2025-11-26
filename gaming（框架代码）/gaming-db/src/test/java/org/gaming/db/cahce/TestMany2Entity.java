/**
 * 
 */
package org.gaming.db.cahce;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;

/**
 * @author YY
 *
 */
@JvmCache(relation = {"ownerId", "hostId"})
@Table(name = "test_many2", comment = "", dbAlias = "game")
public class TestMany2Entity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "")
	private long id;
	@Column(comment = "owner_id")
	private long ownerId;
	@Column(comment = "host_id")
	private long hostId;
	@Column(comment = "")
	private long type;
	@Column(comment = "")
	private int place;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	public long getHostId() {
		return hostId;
	}
	public void setHostId(long hostId) {
		this.hostId = hostId;
	}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}
}
