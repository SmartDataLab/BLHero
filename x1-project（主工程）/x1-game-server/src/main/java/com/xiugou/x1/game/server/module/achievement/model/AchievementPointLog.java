/**
 * 
 */
package com.xiugou.x1.game.server.module.achievement.model;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 *
 */
@Repository
@LogTable(name = "achievement_point_log", comment = "成就点数日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class AchievementPointLog extends NumberThingLog {

}
