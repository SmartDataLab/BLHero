/**
 * 
 */
package pojo.xiugou.x1.pojo.log.player;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.ExpThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "player_exp_log", comment = "玩家经验日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class PlayerExpLog extends ExpThingLog {

}
