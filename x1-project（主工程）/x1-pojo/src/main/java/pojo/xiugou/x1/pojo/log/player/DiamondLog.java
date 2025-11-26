/**
 * 
 */
package pojo.xiugou.x1.pojo.log.player;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "diamond_log", comment = "钻石日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class DiamondLog extends NumberThingLog {
	
}
