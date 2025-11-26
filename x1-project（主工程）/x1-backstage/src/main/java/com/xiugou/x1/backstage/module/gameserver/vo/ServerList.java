/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class ServerList {
	private String title;
	private String content;
	private ArrayList<PlayerVo> players = new ArrayList<>();
	private ArrayList<GameRegionVo> regions = new ArrayList<>();
	private ArrayList<GameServerVo> servers = new ArrayList<>();
	
	public List<PlayerVo> getPlayers() {
		return players;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<GameRegionVo> getRegions() {
		return regions;
	}
	public void setRegions(ArrayList<GameRegionVo> regions) {
		this.regions = regions;
	}
	public ArrayList<GameServerVo> getServers() {
		return servers;
	}
	public void setServers(ArrayList<GameServerVo> servers) {
		this.servers = servers;
	}
	public void setPlayers(ArrayList<PlayerVo> players) {
		this.players = players;
	}
}
