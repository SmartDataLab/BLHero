/**
 *
 */
package com.xiugou.x1.game.server.module.hero.log;

import java.util.Map.Entry;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.hero.event.SomeHeroFightingChangeEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;

import pojo.xiugou.x1.pojo.log.hero.HeroFightingLog;

/**
 * @author YY
 *
 */
@Service
public class HeroLogger {

    private BaseRepository<HeroFightingLog> repository;

    protected BaseRepository<HeroFightingLog> repository() {
        if (repository == null) {
            repository = SlimDao.getRepository(HeroFightingLog.class);
        }
        return repository;
    }

    @Subscribe
    private void listen(SomeHeroFightingChangeEvent event) {
        for (Entry<Hero, Long> entry : event.getHeroOldFightingMap().entrySet()) {
            Hero hero = entry.getKey();
            HeroFightingLog log = new HeroFightingLog();
            log.setOwnerId(hero.getPid());
            log.setHeroId(hero.getId());
            log.setDelta(hero.getFighting() - entry.getValue());
            log.setCurr(hero.getFighting());
            log.setRemark(event.getRemark());
            log.setThingId(hero.getIdentity());
            log.setThingName(hero.getName());
            log.setGameCause(event.getGameCause());
            repository().insert(log);
        }
    }
}
