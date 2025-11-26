/**
 * 
 */
package pojo.xiugou.x1.pojo.log.hero;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "hero_log", comment = "英雄日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class HeroLog extends NumberThingLog {

}
