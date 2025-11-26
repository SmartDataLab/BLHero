/**
 * 
 */
package pojo.xiugou.x1.pojo.log.bag;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.LogTable.TimeWay;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;

/**
 * @author YY
 * 暂时先不实现交易日志，因为有点大
 */
//@Repository
@LogTable(name = "big_trade_log", comment = "交易日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time", way = TimeWay.WEEK)
public class BigTradeLog extends NumberThingLog {
	@Column(name = "other_data", comment = "其他信息", extra = "text")
	private String otherData;
	@Column(name = "serial", comment = "流水号")
	private long serial;

	public String getOtherData() {
		return otherData;
	}

	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}
}
