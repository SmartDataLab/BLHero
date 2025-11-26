/**
 *
 */
package com.xiugou.x1.game.server.module.player.log;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.player.event.PlayerCreateEvent;
import com.xiugou.x1.game.server.module.player.event.PlayerLoginEvent;
import com.xiugou.x1.game.server.module.player.model.Player;

import pojo.xiugou.x1.pojo.log.player.PlayerCreateLog;
import pojo.xiugou.x1.pojo.log.player.PlayerFightingLog;
import pojo.xiugou.x1.pojo.log.player.PlayerLoginLog;
import pojo.xiugou.x1.pojo.log.player.PlayerOnlineLog;
import pojo.xiugou.x1.pojo.log.player.PlayerScatterLog;
import pojo.xiugou.x1.pojo.log.player.PlayerTimeLog;

/**
 * @author YY
 *
 */
@Service
public class PlayerLogger {

    private BaseRepository<PlayerLoginLog> loginRepository;
    private BaseRepository<PlayerCreateLog> createRepository;
    private BaseRepository<PlayerTimeLog> timeRepository;
    private BaseRepository<PlayerScatterLog> scatterRepository;
    private BaseRepository<PlayerOnlineLog> onlineRepository;
    private BaseRepository<PlayerFightingLog> fightingRepository;

    private BaseRepository<PlayerLoginLog> loginRepository() {
        if (loginRepository == null) {
            loginRepository = SlimDao.getRepository(PlayerLoginLog.class);
        }
        return loginRepository;
    }

    private BaseRepository<PlayerCreateLog> createRepository() {
        if (createRepository == null) {
            createRepository = SlimDao.getRepository(PlayerCreateLog.class);
        }
        return createRepository;
    }
    
    private BaseRepository<PlayerTimeLog> timeRepository() {
        if (timeRepository == null) {
        	timeRepository = SlimDao.getRepository(PlayerTimeLog.class);
        }
        return timeRepository;
    }
    
    private BaseRepository<PlayerScatterLog> scatterRepository() {
		if (scatterRepository == null) {
			scatterRepository = SlimDao.getRepository(PlayerScatterLog.class);
		}
		return scatterRepository;
	}
    
    private BaseRepository<PlayerOnlineLog> onlineRepository() {
		if (onlineRepository == null) {
			onlineRepository = SlimDao.getRepository(PlayerOnlineLog.class);
		}
		return onlineRepository;
	}
    
    private BaseRepository<PlayerFightingLog> fightingRepository() {
        if (fightingRepository == null) {
        	fightingRepository = SlimDao.getRepository(PlayerFightingLog.class);
        }
        return fightingRepository;
    }
    

    @Subscribe
    private void listen(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerLoginLog log = new PlayerLoginLog();
        log.setPid(player.getId());
        log.setOpenId(player.getOpenId());
        log.setNick(player.getNick());
        log.setLevel(player.getLevel());
        log.setLoginTime(player.getLastLoginTime());
        log.setDeviceType(player.getLoginDeviceType());
        log.setBornTime(player.getInsertTime());
        log.setIp(player.getLoginIp());
        log.setChannelId(player.getCreateChannel());
        log.setCreatePay(player.isCreatePay() ? 1 : 0);
        loginRepository().insert(log);
    }

    @Subscribe
    private void listen(PlayerCreateEvent event) {
        Player player = event.getPlayer();
        PlayerCreateLog createLog = new PlayerCreateLog();
        createLog.setPid(player.getId());
        createLog.setOpenId(player.getOpenId());
        createLog.setNick(player.getNick());
        createLog.setDeviceType(player.getLoginDeviceType());
        createLog.setBornTime(player.getInsertTime());
        createLog.setIp(player.getLoginIp());
        createLog.setChannelId(player.getCreateChannel());
        createRepository().insert(createLog);
        
        PlayerLoginLog loginLog = new PlayerLoginLog();
        loginLog.setPid(player.getId());
        loginLog.setOpenId(player.getOpenId());
        loginLog.setNick(player.getNick());
        loginLog.setLevel(player.getLevel());
        loginLog.setLoginTime(player.getLastLoginTime());
        loginLog.setDeviceType(player.getLoginDeviceType());
        loginLog.setBornTime(player.getInsertTime());
        loginLog.setIp(player.getLoginIp());
        loginLog.setChannelId(player.getCreateChannel());
        loginLog.setCreatePay(player.isCreatePay() ? 1 : 0);
        loginRepository().insert(loginLog);
    }
    
    public void insertTimeLog(PlayerTimeLog log) {
    	timeRepository().insert(log);
    }
    
    public void insertAllScatterLog(List<PlayerScatterLog> logs) {
    	scatterRepository().insertAll(logs);
	}
    
    public void insertOnlineLog(PlayerOnlineLog log) {
    	onlineRepository().insert(log);
	}
    
    public void insertFightingLog(PlayerFightingLog log) {
    	fightingRepository().insert(log);
    }
}
