/**
 * 
 */
package pojo.xiugou.x1.pojo.module.player.form;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class GodFingerTable {
	
	private List<GodFingerData> datas = new ArrayList<>();
	
	public static class GodFingerData {
		private long playerId;
		private long money;
		public long getPlayerId() {
			return playerId;
		}
		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}
		public long getMoney() {
			return money;
		}
		public void setMoney(long money) {
			this.money = money;
		}
	}

	public List<GodFingerData> getDatas() {
		return datas;
	}

	public void setDatas(List<GodFingerData> datas) {
		this.datas = datas;
	}
}
