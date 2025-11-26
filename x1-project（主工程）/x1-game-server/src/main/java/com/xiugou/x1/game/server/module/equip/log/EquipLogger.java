package com.xiugou.x1.game.server.module.equip.log;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pojo.xiugou.x1.pojo.log.equip.EquipLog;

/**
 * @author yh
 * @date 2023/7/14
 * @apiNote
 */
@Service
public class EquipLogger {
	
	@Autowired
	private PlayerService playerService;
	
    private BaseRepository<EquipLog> repository;
    public void addEquipLogger(List<EquipLog> equipLogs) {
        repository().insertAll(equipLogs);
    }

    protected BaseRepository<EquipLog> repository() {
        if (repository == null) {
            repository = SlimDao.getRepository(EquipLog.class);
        }
        return repository;
    }
    
    public void deleteLog(long entityId, List<Equip> equips, IGameCause cause, String remark) {
    	String ownerName = playerService.getEntity(entityId).getNick();
    	List<EquipLog> logs = new ArrayList<>();
        for (Equip equip : equips) {
            EquipLog log = new EquipLog();
            log.setOwnerId(entityId);
            log.setOwnerName(ownerName);
            log.setCurr(0);
            log.setDelta(-1);
            log.setGameCause(cause);
            log.setThingId(equip.getIdentity());
            log.setThingName(equip.getName());
            log.setRemark(remark);
            log.setInstanceId(equip.getId());
            log.setInstanceData(GsonUtil.toJson(equip));
            logs.add(log);
        }
        repository().insertAll(logs);
    }
}
