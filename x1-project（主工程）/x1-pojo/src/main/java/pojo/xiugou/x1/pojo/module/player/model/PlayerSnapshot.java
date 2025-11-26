/**
 * 
 */
package pojo.xiugou.x1.pojo.module.player.model;

import org.gaming.ruler.redis.RedisHashObj;

/**
 * @author hyy
 *
 */
public class PlayerSnapshot implements RedisHashObj {
	private long id;
	private String nick;
	private int level;
	private long fighting;
	private String head;
	private boolean online;
	private int serverId;
	private int image;
	
	public PlayerSnapshot() {}
	public PlayerSnapshot(IPlayerEntity player) {
		this.id = player.getId();
		this.nick = player.getNick();
		this.level = player.getLevel();
		this.fighting = player.getFighting();
		this.head = player.getHead();
		this.online = player.isOnline();
		this.serverId = player.getServerId();
		this.image = player.getImage(); 
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	@Override
	public Long redisOwnerKey() {
		return (long)serverId;
	}
	@Override
	public Long redisHashKey() {
		return id;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
}
