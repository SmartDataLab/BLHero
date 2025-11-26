package com.xiugou.x1.game.server.module.vip.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.PrivilegeBoostCache;
import com.xiugou.x1.design.module.PrivilegeBoostTypeCache;
import com.xiugou.x1.design.module.VipCache;
import com.xiugou.x1.design.module.autogen.PrivilegeBoostAbstractCache.PrivilegeBoostCfg;
import com.xiugou.x1.design.module.autogen.PrivilegeBoostTypeAbstractCache.PrivilegeBoostTypeCfg;
import com.xiugou.x1.design.module.autogen.VipAbstractCache.VipCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.vip.constant.VipFuncType;
import com.xiugou.x1.game.server.module.vip.model.Vip;

/**
 * @author yh
 * @date 2023/8/29
 * @apiNote
 */
@Service
public class VipService extends PlayerOneToOneService<Vip> {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private VipService vipService;
    @Autowired
    private PrivilegeBoostCache privilegeBoostCache;
    @Autowired
    private PrivilegeBoostTypeCache privilegeBoostTypeCache;
    @Autowired
    private VipCache vipCache;

    @Override
    protected Vip createWhenNull(long entityId) {
        Vip vip = new Vip();
        vip.setPid(entityId);
        return vip;
    }

    /**
     * 加成比例
     * @param entityId
     * @param vipFuncType
     * @param causeCode
     * @return
     */
    public float boostRate(long entityId, VipFuncType vipFuncType, int causeCode) {
        PrivilegeBoostTypeCfg privilegeBoostTypeCfg = privilegeBoostTypeCache.getOrNull(vipFuncType.getType());
        if(privilegeBoostTypeCfg == null) {
        	return 0;
        }
        //如果有配产出途径 且 不包含当前产出途径则不加成
        if (!privilegeBoostTypeCfg.getPath().isEmpty() && !privilegeBoostTypeCfg.getPath().contains(causeCode)) {
            return 0;
        }
        Player player = playerService.getEntity(entityId);
        //Vip加成
        VipCfg vipCfg = vipCache.getOrThrow(player.getVipLevel());
        List<Keyv> totalKeyv = new ArrayList<>();
        totalKeyv.addAll(vipCfg.getPrivilege());
        //特权加成
        Vip vip = vipService.getEntity(entityId);
        for (int privilegeId : vip.getPrivilegeList()) {
        	PrivilegeBoostCfg privilegeCfg = privilegeBoostCache.getOrThrow(privilegeId);
            totalKeyv.addAll(privilegeCfg.getPrivilege());
        }
        int boost = 0;
        for (Keyv keyv : totalKeyv) {
            if (vipFuncType.getType() != keyv.getKey()) {
                continue;
            }
            boost += keyv.getValue();
        }
        return boost / 10000.f;
    }

    /**
     * 加成次数
     * @param playerId
     * @param vipFuncType
     * @return
     */
    public int boostTimes(long playerId, VipFuncType vipFuncType) {
        int times = 0;
        Player player = playerService.getEntity(playerId);
        //Vip加成次数
        VipCfg vipCfg = vipCache.getOrThrow(player.getVipLevel());
        for (Keyv keyv : vipCfg.getPrivilege()) {
            if (vipFuncType.getType() == keyv.getKey()) {
                times += keyv.getValue();
            }
        }
        //特权加成次数
        Vip vip = vipService.getEntity(playerId);
        for (int privilegeId : vip.getPrivilegeList()) {
        	PrivilegeBoostCfg privilegeCfg = privilegeBoostCache.getOrThrow(privilegeId);
            for (Keyv keyv : privilegeCfg.getPrivilege()) {
                if (vipFuncType.getType() == keyv.getKey()) {
                    times += keyv.getValue();
                }
            }
        }
        return times;

    }

}
