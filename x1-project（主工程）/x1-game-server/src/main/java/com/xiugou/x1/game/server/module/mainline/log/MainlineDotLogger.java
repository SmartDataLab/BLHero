/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.log;

import javax.annotation.PostConstruct;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.tool.ConsoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.MainlineTaskCache;
import com.xiugou.x1.design.module.MonsterCache.MonsterConfig;
import com.xiugou.x1.design.module.autogen.MainlineTaskAbstractCache.MainlineTaskCfg;
import com.xiugou.x1.design.module.autogen.SceneFogAbstractCache.SceneFogCfg;

import pojo.xiugou.x1.pojo.log.mainline.MainlineBossDot;
import pojo.xiugou.x1.pojo.log.mainline.MainlineBossTiming;
import pojo.xiugou.x1.pojo.log.mainline.MainlineFogDot;
import pojo.xiugou.x1.pojo.log.mainline.MainlineTaskDot;
import pojo.xiugou.x1.pojo.log.mainline.MainlineTaskTiming;

/**
 * @author YY
 * 主线任务打点日志
 */
@Service
public class MainlineDotLogger {

	@Autowired
	private MainlineTaskCache mainlineTaskCache;
	
	private BaseRepository<MainlineTaskDot> taskRepository;
	
	private BaseRepository<MainlineFogDot> fogRepository;
	
	private BaseRepository<MainlineBossDot> bossRepository;

    protected BaseRepository<MainlineTaskDot> taskRepository() {
        if (taskRepository == null) {
        	taskRepository = SlimDao.getRepository(MainlineTaskDot.class);
        }
        return taskRepository;
    }
    
    protected BaseRepository<MainlineFogDot> fogRepository() {
        if (fogRepository == null) {
        	fogRepository = SlimDao.getRepository(MainlineFogDot.class);
        }
        return fogRepository;
    }
    
    protected BaseRepository<MainlineBossDot> bossRepository() {
        if (bossRepository == null) {
        	bossRepository = SlimDao.getRepository(MainlineBossDot.class);
        }
        return bossRepository;
    }
    
    
    public void addTaskDot(long playerId, MainlineTaskCfg taskCfg, MainlineTaskTiming timing) {
    	MainlineTaskDot dot = new MainlineTaskDot();
    	dot.setPlayerId(playerId);
    	dot.setTaskId(taskCfg.getId());
    	dot.setTaskName(taskCfg.getDesc());
    	dot.setTiming(timing.getValue());
    	this.taskRepository().insert(dot);
    }
    
    public void addFogDot(long playerId, long fighting,  SceneFogCfg fogCfg) {
    	MainlineFogDot dot = new MainlineFogDot();
    	dot.setPlayerId(playerId);
    	dot.setFighting(fighting);
    	dot.setFogId(fogCfg.getFogId());
    	dot.setLevel(fogCfg.getOpenLevel());
    	this.fogRepository().insert(dot);
    }
    
    public void addBossDot(long playerId, long fighting, MonsterConfig bossCfg, MainlineBossTiming timing) {
    	MainlineBossDot dot = new MainlineBossDot();
    	dot.setPlayerId(playerId);
    	dot.setFighting(fighting);
    	dot.setBossId(bossCfg.getId());
    	dot.setBossName(bossCfg.getName());
    	dot.setLevel(bossCfg.getLevel());
    	dot.setTiming(timing.getValue());
    	this.bossRepository().insert(dot);
    }
    
    
    @PostConstruct
    private void testTaskDot() {
    	 ConsoleUtil.addFunction("testTaskDot", () -> { buildTestData(); });
    }
    
    private void buildTestData() {
    	for(MainlineTaskCfg mainlineTaskCfg : mainlineTaskCache.all()) {
    		mainlineTaskCfg.getId();
    		mainlineTaskCfg.getDesc();
    		
    	}
    }
}
