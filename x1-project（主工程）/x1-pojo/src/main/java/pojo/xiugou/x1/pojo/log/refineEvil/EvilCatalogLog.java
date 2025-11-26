package pojo.xiugou.x1.pojo.log.refineEvil;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Repository
@LogTable(name = "evil_catalog_log", comment = "妖录日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class EvilCatalogLog extends NumberThingLog {
}
