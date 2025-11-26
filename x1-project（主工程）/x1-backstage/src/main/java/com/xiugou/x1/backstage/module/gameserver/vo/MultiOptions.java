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
public class MultiOptions {
	private List<MultiOption> options = new ArrayList<>();
	
	public void addOption(long value, String text, long filter) {
		MultiOption option = new MultiOption();
		option.value = value;
		option.text = text;
		option.filter = filter;
		options.add(option);
	}
	
	public class MultiOption {
		private long value;
		private String text;
		private long filter;
		public long getValue() {
			return value;
		}
		public void setValue(long value) {
			this.value = value;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public long getFilter() {
			return filter;
		}
		public void setFilter(long filter) {
			this.filter = filter;
		}
	}


	public List<MultiOption> getOptions() {
		return options;
	}
	public void setOptions(List<MultiOption> options) {
		this.options = options;
	}
}
