/**
 * 
 */
package pojo.xiugou.x1.pojo.log.hero;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.prefab.thing.NumberThingLog;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@LogTable(name = "hero_fighting_log", comment = "英雄战力变化日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class HeroFightingLog extends NumberThingLog {
	@Column(name = "hero_id", comment = "英雄ID")
	private long heroId;
	
	public long getHeroId() {
		return heroId;
	}
	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}
}
