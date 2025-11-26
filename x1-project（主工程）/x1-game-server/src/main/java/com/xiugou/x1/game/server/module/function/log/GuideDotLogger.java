/**
 * 
 */
package com.xiugou.x1.game.server.module.function.log;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.GuideCache;
import com.xiugou.x1.design.module.autogen.GuideAbstractCache.GuideCfg;

import pojo.xiugou.x1.pojo.log.function.GuideDot;

/**
 * @author YY
 * 主线任务打点日志
 */
@Service
public class GuideDotLogger {

	@Autowired
	private GuideCache guideCache;
	
	private BaseRepository<GuideDot> repository;

    protected BaseRepository<GuideDot> repository() {
        if (repository == null) {
            repository = SlimDao.getRepository(GuideDot.class);
        }
        return repository;
    }
    
    public void addGuideDot(long playerId, int guideId, int step) {
    	GuideDot dot = new GuideDot();
    	dot.setPlayerId(playerId);
    	dot.setGuideId(guideId);
    	GuideCfg guideCfg = guideCache.findInGroupSubIdIndex(guideId, step);
    	dot.setGuideName(guideCfg.getBeizhu());
    	dot.setStep(step);
    	this.repository().insert(dot);
    }
}
