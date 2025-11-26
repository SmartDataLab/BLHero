/**
 * 
 */
package org.gaming.prefab.thing;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.MappedSuperclass;

/**
 * @author YY
 *
 */
@MappedSuperclass
public abstract class InstanceThingLog extends NumberThingLog {
	@Column(name = "instance_id", comment = "实物的ID")
	private long instanceId;
	@Column(name = "instance_data", comment = "实物的数据", extra = "text")
	private String instanceData;
	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}
	public void setInstanceData(String instanceData) {
		this.instanceData = instanceData;
	}
	public long getInstanceId() {
		return instanceId;
	}
	public String getInstanceData() {
		return instanceData;
	}
}
