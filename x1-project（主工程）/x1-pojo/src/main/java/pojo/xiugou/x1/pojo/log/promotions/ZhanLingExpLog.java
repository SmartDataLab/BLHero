/**
 * 
 */
package pojo.xiugou.x1.pojo.log.promotions;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.ExpThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 *
 */
@Repository
@LogTable(name = "zhan_ling_exp_log", comment = "战令经验日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class ZhanLingExpLog extends ExpThingLog {

}
