package com.xiugou.x1.game.server.module.recruit.log;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.tool.DateTimeUtil;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.recruit.constant.RefreshType;
import com.xiugou.x1.game.server.module.recruit.event.RecruitNumEvent;
import com.xiugou.x1.game.server.module.recruit.event.RecruitRefreshEvent;

import pojo.xiugou.x1.pojo.log.recruit.RecruitPrizeDrawLog;
import pojo.xiugou.x1.pojo.log.recruit.RecruitRefreshLog;

/**
 * @author yh
 * @date 2023/6/25
 * @apiNote
 */
@Service
public class RecruitTimesLogger {

    private BaseRepository<RecruitPrizeDrawLog> numRepository;
    private BaseRepository<RecruitRefreshLog> refreshRepository;

    private BaseRepository<RecruitPrizeDrawLog> numRepository() {
        if (numRepository == null) {
        	numRepository = SlimDao.getRepository(RecruitPrizeDrawLog.class);
        }
        return numRepository;
    }
    
    private BaseRepository<RecruitRefreshLog> refreshRepository() {
        if (refreshRepository == null) {
        	refreshRepository = SlimDao.getRepository(RecruitRefreshLog.class);
        }
        return refreshRepository;
    }

    @Subscribe
    private void listen(RecruitNumEvent event) {
    	RecruitPrizeDrawLog log = new RecruitPrizeDrawLog();
        log.setPid(event.getPid());
        log.setMultiple(event.getMultiple());
        log.setDateStr(DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, DateTimeUtil.currMillis()));
    	this.numRepository().insert(log);
    }
    
    @Subscribe
    private void listen(RecruitRefreshEvent event) {
    	if(event.getRefreshType() == RefreshType.GIVE_UP || event.getRefreshType().isCountHand()) {
    		RecruitRefreshLog log = new RecruitRefreshLog();
        	log.setPid(event.getPid());
        	log.setRefreshType(event.getRefreshType().getCode());
        	log.setDateStr(DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, DateTimeUtil.currMillis()));
        	this.refreshRepository().insert(log);
    	}
    }
}
