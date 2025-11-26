/**
 * 
 */
package pojo.xiugou.x1.pojo.module.server.form;

/**
 * @author YY
 *
 */
public class ServerRuntimeForm {
	private int platformId;
	private int serverId;
	private int playerNum;
	private int onlineNum;
	private int battleNum;
	private int currBattleNum;
	private String maxMemory;
	private String freeMemory;
	private String totalMemory;
	private String leftMemory;
	private String usedMemory;
	private boolean running;
	public int getPlatformId() {
		return platformId;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getPlayerNum() {
		return playerNum;
	}
	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}
	public int getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}
	public int getBattleNum() {
		return battleNum;
	}
	public void setBattleNum(int battleNum) {
		this.battleNum = battleNum;
	}
	public int getCurrBattleNum() {
		return currBattleNum;
	}
	public void setCurrBattleNum(int currBattleNum) {
		this.currBattleNum = currBattleNum;
	}
	public String getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}
	public String getFreeMemory() {
		return freeMemory;
	}
	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}
	public String getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}
	public String getLeftMemory() {
		return leftMemory;
	}
	public void setLeftMemory(String leftMemory) {
		this.leftMemory = leftMemory;
	}
	public String getUsedMemory() {
		return usedMemory;
	}
	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
}
