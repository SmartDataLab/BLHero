package com.xiugou.x1.game.server.module.evil.service;

import java.util.Map;

import org.gaming.tool.ListMapUtil;
import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.evil.model.Evil;

import pb.xiugou.x1.protobuf.refineevil.RefineEvil.PbEvil;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Service
public class EvilService extends OneToManyService<Evil> {

    public Evil getEntity(long pid, int identity) {
        return repository().getByKeys(pid, identity);
    }

    public Map<Integer, Evil> getEvilMap(long playerId) {
        return ListMapUtil.listToMap(this.getEntities(playerId), Evil::getIdentity);
    }


    public PbEvil build(Evil evil) {
    	PbEvil.Builder builder = PbEvil.newBuilder();
        builder.setIdentity(evil.getIdentity());
        builder.setLevel(evil.getLevel());
        builder.setFragment(evil.getFragment());
        return builder.build();
    }
}
