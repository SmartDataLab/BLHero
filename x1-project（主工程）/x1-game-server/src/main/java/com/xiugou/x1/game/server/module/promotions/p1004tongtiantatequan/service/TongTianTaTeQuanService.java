/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.model.TongTianTaTeQuan;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.struct.TTTTQDetail;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;

/**
 * @author YY
 *
 */
@Service
public class TongTianTaTeQuanService extends OneToManyService<TongTianTaTeQuan> {
	/**
     * @param entityId
     * @return
     */
    public TongTianTaTeQuan getEntity(long entityId) {
    	int typeId = 1004001;
        TongTianTaTeQuan entity = repository().getByKeys(entityId, typeId);
        if (entity == null) {
            entity = new TongTianTaTeQuan();
            entity.setPid(entityId);
            entity.setTypeId(typeId);
            for(TowerType towerType : TowerType.values()) {
            	TTTTQDetail detail = new TTTTQDetail();
            	detail.setTowerType(towerType.getValue());
            	detail.setRound(1);
            	detail.setOpenHigh(false);
            	entity.getTowerDetails().put(detail.getTowerType(), detail);
            }
            this.insert(entity);
        }
        return entity;
    }
}
