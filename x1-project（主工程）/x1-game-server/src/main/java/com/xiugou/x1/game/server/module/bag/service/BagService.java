package com.xiugou.x1.game.server.module.bag.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.bag.model.Bag;

/**
 * @author yh
 * @date 2023/5/30
 * @apiNote
 */
@Service
public class BagService extends PlayerOneToOneService<Bag> {
	
    @Override
    protected Bag createWhenNull(long entityId) {
        Bag bag = new Bag();
        bag.setPId(entityId);
        return bag;
    }
}
