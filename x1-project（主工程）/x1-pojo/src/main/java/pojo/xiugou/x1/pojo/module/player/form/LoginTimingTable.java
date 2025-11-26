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
public class LoginTimingTable {
	private List<LoginTiming> datas = new ArrayList<>();
	
	public static class LoginTiming {
		private int channelId;
		private String openId;
		private int timing;
		private long time;
		public int getChannelId() {
			return channelId;
		}
		public void setChannelId(int channelId) {
			this.channelId = channelId;
		}
		public String getOpenId() {
			return openId;
		}
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public int getTiming() {
			return timing;
		}
		public void setTiming(int timing) {
			this.timing = timing;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
	}

	public List<LoginTiming> getDatas() {
		return datas;
	}

	public void setDatas(List<LoginTiming> datas) {
		this.datas = datas;
	}
}
