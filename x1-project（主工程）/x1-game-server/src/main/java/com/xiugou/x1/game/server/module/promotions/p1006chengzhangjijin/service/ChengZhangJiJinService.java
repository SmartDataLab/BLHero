/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.model.ChengZhangJiJin;

/**
 * @author YY
 *
 */
@Service
public class ChengZhangJiJinService extends OneToManyService<ChengZhangJiJin> {

	public ChengZhangJiJin getEntity(long entityId) {
    	int typeId = 1006001;
    	ChengZhangJiJin entity = repository().getByKeys(entityId, typeId);
        if (entity == null) {
            entity = new ChengZhangJiJin();
            entity.setPid(entityId);
            entity.setTypeId(typeId);
            entity.setRound(1);
            entity.setOpen(false);
            this.insert(entity);
        }
        return entity;
    }
}
