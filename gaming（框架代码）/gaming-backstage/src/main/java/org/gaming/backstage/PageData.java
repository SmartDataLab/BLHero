/**
 * 
 */
package org.gaming.backstage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class PageData<T> {
	private long count;
	private List<T> data = new ArrayList<>();
	
	public PageData() {}
	public PageData(long count, List<T> data) {
		this.count = count;
		this.data = data;
	}
	
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
