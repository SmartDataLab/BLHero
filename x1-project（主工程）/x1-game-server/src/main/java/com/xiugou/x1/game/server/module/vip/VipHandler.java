package com.xiugou.x1.game.server.module.vip;

import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.PrivilegeBoostCache;
import com.xiugou.x1.design.module.VipCache;
import com.xiugou.x1.design.module.autogen.PrivilegeBoostAbstractCache.PrivilegeBoostCfg;
import com.xiugou.x1.design.module.autogen.VipAbstractCache.VipCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.vip.model.Vip;
import com.xiugou.x1.game.server.module.vip.service.VipService;

import pb.xiugou.x1.protobuf.vip.Vip.VipBuyGiftRequest;
import pb.xiugou.x1.protobuf.vip.Vip.VipBuyGiftResponse;
import pb.xiugou.x1.protobuf.vip.Vip.VipInfoResponse;

/**
 * @author yh
 * @date 2023/8/29
 * @apiNoteb  VIP特权加成
 */
@Controller
public class VipHandler extends AbstractModuleHandler {
    @Autowired
    private VipService vipService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ThingService thingService;
    @Autowired
    private VipCache vipCache;
    @Autowired
    private PrivilegeBoostCache privilegeBoostCache;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
	public boolean needDailyPush() {
		return false;
	}

    @Override
    public void pushInfo(PlayerContext playerContext) {
        Vip vip = vipService.getEntity(playerContext.getId());
        
        VipInfoResponse.Builder response = VipInfoResponse.newBuilder();
        response.addAllPrivilegeId(vip.getPrivilegeList());
        response.addAllVipLevelGift(vip.getVipGift());
        
        Player player = playerService.getEntity(playerContext.getId());
        VipCfg vipCfg = vipCache.getOrThrow(player.getVipLevel());
        for(Keyv keyv : vipCfg.getPrivilege()) {
        	response.addBoosts(PbHelper.build(keyv));
        }
        for(int privilege : vip.getPrivilegeList()) {
        	PrivilegeBoostCfg privilegeCfg = privilegeBoostCache.getOrThrow(privilege);
        	for(Keyv keyv : privilegeCfg.getPrivilege()) {
        		response.addBoosts(PbHelper.build(keyv));
        	}
        }
        playerContextManager.push(playerContext.getId(), VipInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public VipBuyGiftResponse buyVipGift(PlayerContext playerContext, VipBuyGiftRequest request) {
        Player player = playerService.getEntity(playerContext.getId());
        Asserts.isTrue(player.getVipLevel() >= request.getVipLevel(), TipsCode.VIP_LEVEL_NOT_ENOUGH);
        
        Vip vip = vipService.getEntity(playerContext.getId());
        List<Integer> vipGift = vip.getVipGift();
        Asserts.isTrue(!vipGift.contains(request.getVipLevel()), TipsCode.VIP_GIFT_ALREADY_BOUGHT);
        
        VipCfg vipCfg = vipCache.getOrThrow(request.getVipLevel());
        CostThing costThing = CostThing.of(ItemType.DIAMOND, vipCfg.getCurrentPrice());
        thingService.cost(playerContext.getId(), costThing, GameCause.VIP_BUY_LEVEL_GIFT);
        
        vipGift.add(request.getVipLevel());
        vipService.update(vip);
        
        thingService.add(playerContext.getId(), vipCfg.getRewards(), GameCause.VIP_BUY_LEVEL_GIFT);
        
        VipBuyGiftResponse.Builder response = VipBuyGiftResponse.newBuilder();
        response.setVipLevel(request.getVipLevel());
        return response.build();

    }
}
