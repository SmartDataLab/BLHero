/**
 * 
 */
package pojo.xiugou.x1.pojo.log.dailyweekly;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 *
 */
@Repository
@LogTable(name = "day_point_log", comment = "日常活跃度日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class DayPointLog extends NumberThingLog {

}
