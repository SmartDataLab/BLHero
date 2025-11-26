package pojo.xiugou.x1.pojo.log.village;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/17
 * @apiNote
 */
@Repository
@LogTable(name = "village", comment = "村庄积分日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class VillageLog extends NumberThingLog {
}
