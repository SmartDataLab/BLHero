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
public class ServerSelected {
	private List<ServerSelectedData> datas = new ArrayList<>();
	
	public static class ServerSelectedData {
		private long id;
		private String text;
		private boolean select;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public boolean isSelect() {
			return select;
		}
		public void setSelect(boolean select) {
			this.select = select;
		}
	}

	public List<ServerSelectedData> getDatas() {
		return datas;
	}

	public void setDatas(List<ServerSelectedData> datas) {
		this.datas = datas;
	}
}
