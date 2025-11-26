/**
 * 
 */
package com.xiugou.x1.design.struct;

import org.gaming.ruler.util.DropUtil.IDrop;

/**
 * @author YY
 *
 */
public class RandomItem implements IDrop {
	private int rate;
	private int itemId;
	private long num;
	public int getRate() {
		return rate;
	}
	public int getItemId() {
		return itemId;
	}
	@Override
	public int getDropId() {
		return itemId;
	}
	@Override
	public int getDropRate() {
		return rate;
	}
	public long getNum() {
		return num;
	}
}

