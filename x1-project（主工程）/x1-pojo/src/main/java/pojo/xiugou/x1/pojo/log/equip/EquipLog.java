package pojo.xiugou.x1.pojo.log.equip;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.InstanceThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Repository
@LogTable(name = "equip_log", comment = "装备日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class EquipLog extends InstanceThingLog {


}
