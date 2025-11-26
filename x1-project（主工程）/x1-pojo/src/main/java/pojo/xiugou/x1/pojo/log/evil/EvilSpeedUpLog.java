package pojo.xiugou.x1.pojo.log.evil;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote
 */
@Repository
@LogTable(name = "evil_speedup_log", comment = "炼狱积分日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class EvilSpeedUpLog extends NumberThingLog {
}
