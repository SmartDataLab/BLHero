/**
 * 
 */
package com.xiugou.x1.battle.template;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class TemplateFighter {
	private long id;
	private int serverId;
	private String nick;
	private int level;
	//1玩家，2怪物
	private int type;
	private long fighting;
	private final List<TemplateSprite> sprites = new ArrayList<>();
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public List<TemplateSprite> getSprites() {
		return sprites;
	}
}
