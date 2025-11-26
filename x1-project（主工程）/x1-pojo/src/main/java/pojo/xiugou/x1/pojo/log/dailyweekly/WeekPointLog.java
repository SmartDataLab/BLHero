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
@LogTable(name = "week_point_log", comment = "周常活跃度日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class WeekPointLog extends NumberThingLog {

}
