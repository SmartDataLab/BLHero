/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class CostReceipt {

	private List<CostDetail> details = new ArrayList<>();
	
	public CostReceipt() {}
	
	public CostReceipt(List<? extends ICostThing> costThings) {
		for(ICostThing thing : costThings) {
			CostDetail detail = new CostDetail();
			detail.thingId = thing.getThingId();
			detail.num = thing.getNum();
			details.add(detail);
		}
	}
	
	public void merge(CostReceipt costReceipt) {
		this.details.addAll(costReceipt.details);
	}
	
	public List<CostDetail> getDetails() {
		return details;
	}
	
	public class CostDetail {
		private int thingId;
		private long num;
		public int getThingId() {
			return thingId;
		}
		public long getNum() {
			return num;
		}
	}
}
