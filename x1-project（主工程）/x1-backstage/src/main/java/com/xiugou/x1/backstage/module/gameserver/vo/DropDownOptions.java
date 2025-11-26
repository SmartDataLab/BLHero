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
public class DropDownOptions {
	private List<DropDownOption> options = new ArrayList<>();
	
	public void addOption(long value, String text) {
		DropDownOption option = new DropDownOption();
		option.value = value;
		option.text = text;
		options.add(option);
	}
	
	public static class DropDownOption {
		private long value;
		private String text;
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
	}


	public List<DropDownOption> getOptions() {
		return options;
	}
	public void setOptions(List<DropDownOption> options) {
		this.options = options;
	}
}
