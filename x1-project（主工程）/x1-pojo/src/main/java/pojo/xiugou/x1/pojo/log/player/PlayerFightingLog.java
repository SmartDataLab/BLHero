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
@LogTable(name = "player_fighting_log", comment = "玩家战斗力变化日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class PlayerFightingLog extends NumberThingLog {

}
