/**
 * 
 */
package pojo.xiugou.x1.pojo.log.home;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "wood_log", comment = "木日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class WoodLog extends NumberThingLog {

}
