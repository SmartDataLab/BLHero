package pojo.xiugou.x1.pojo.log.recruit;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/6/21
 * @apiNote
 */
@Repository
@LogTable(name = "recruit_points_log", comment = "招募积分日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class RecruitPointsLog extends NumberThingLog {
}
