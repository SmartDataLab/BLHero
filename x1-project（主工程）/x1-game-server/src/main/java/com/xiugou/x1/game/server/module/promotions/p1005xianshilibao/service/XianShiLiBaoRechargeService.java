package com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.service;

import java.util.List;

import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.XianShiLiBaoCache;
import com.xiugou.x1.design.module.autogen.RechargeProductAbstractCache.RechargeProductCfg;
import com.xiugou.x1.design.module.autogen.XianShiLiBaoAbstractCache.XianShiLiBaoCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.model.XianShiLiBao;
import com.xiugou.x1.game.server.module.promotions.p1005xianshilibao.strcut.XSLBGiftData;
import com.xiugou.x1.game.server.module.recharge.constant.ProductType;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;
import com.xiugou.x1.game.server.module.recharge.service.RechargeOrderingService;

/**
 * @author yh
 * @date 2023/9/25
 * @apiNote
 */
@Service
public class XianShiLiBaoRechargeService extends RechargeOrderingService {
    @Autowired
    private XianShiLiBaoCache xianShiLiBaoCache;
    @Autowired
    private XianShiLiBaoService xianShiLiBaoService;

    @Override
    public ProductType productType() {
        return ProductType.XIAN_SHI_LI_BAO;
    }


    //extraInfo 传递礼包ID
    @Override
    public void checkOrdering(long playerId, RechargeProductCfg rechargeProductCfg, String extraInfo) {
        XSLBGiftData xxlbGiftData = GsonUtil.parseJson(extraInfo, XSLBGiftData.class);
        XianShiLiBao xianShiLiBao = xianShiLiBaoService.getEntity(playerId, xxlbGiftData.getGiftId());
        Asserts.isTrue(xianShiLiBao != null, TipsCode.XSLB_NOT_EMPTY_CONDITION);
        Asserts.isTrue(LocalDateTimeUtil.now().isBefore(xianShiLiBao.getEndTime()) && !xianShiLiBao.isBuy(), TipsCode.XSLB_EXPIRED);
    }

    @Override
    public void buySuccess(long playerId, Recharge recharge, List<RewardThing> outRewards) {
        XSLBGiftData xxlbGiftData = GsonUtil.parseJson(recharge.getProductData(), XSLBGiftData.class);
        XianShiLiBaoCfg xianShiLiBaoCfg = xianShiLiBaoCache.getOrThrow(xxlbGiftData.getGiftId());
        XianShiLiBao xianShiLiBao = xianShiLiBaoService.getEntity(playerId, xxlbGiftData.getGiftId());
        xianShiLiBao.setBuy(true);
        xianShiLiBaoService.update(xianShiLiBao);
        outRewards.addAll(xianShiLiBaoCfg.getDiffReward());
    }
}
