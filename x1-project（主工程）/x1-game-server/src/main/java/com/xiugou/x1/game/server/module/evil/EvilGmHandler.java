package com.xiugou.x1.game.server.module.evil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.EvilSoulCache;
import com.xiugou.x1.design.module.EvilTypeCache;
import com.xiugou.x1.design.module.autogen.EvilSoulAbstractCache.EvilSoulCfg;
import com.xiugou.x1.design.module.autogen.EvilTypeAbstractCache.EvilTypeCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.evil.model.Evil;
import com.xiugou.x1.game.server.module.evil.model.EvilFurnace;
import com.xiugou.x1.game.server.module.evil.service.EvilFurnaceService;
import com.xiugou.x1.game.server.module.evil.service.EvilService;
import com.xiugou.x1.game.server.module.evil.struct.RefineEvilData;

/**
 * @author yh
 * @date 2023/8/3
 * @apiNote
 */
@Controller
public class EvilGmHandler {
    @Autowired
    private EvilFurnaceService evilFurnaceService;
    @Autowired
    private EvilSoulCache evilSoulCache;
    @Autowired
    private EvilService evilService;
    @Autowired
    private ThingService thingService;
    @Autowired
    private EvilTypeCache evilTypeCache;

    @PlayerGmCmd(command = "EVIL_SPEED_UP")
    public void speedEvil(PlayerContext playerContext, String[] params) {
        int param = Integer.parseInt(params[0]);
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(playerContext.getId());
        List<RefineEvilData> refineEvilDetail = evilFurnace.getRefineDetail();
        long nowTime = DateTimeUtil.currMillis();
        RefineEvilData evilData = refineEvilDetail.get(param);
        if (!(evilData.getStartTime() < nowTime && nowTime < evilData.getEndTime())) {
            return;
        }
        for (int i = param; i < refineEvilDetail.size(); i++) {
            RefineEvilData refineEvilData = refineEvilDetail.get(i);
            if (refineEvilData.getStartTime() < nowTime && nowTime < refineEvilData.getEndTime()) {
                long l = refineEvilData.getEndTime() - Integer.parseInt(params[1]) * DateTimeUtil.ONE_MINUTE_MILLIS;
                if (l < nowTime) {
                    refineEvilData.setEndTime(nowTime);
                } else {
                    refineEvilData.setEndTime(l);
                }
                continue;
            }
            RefineEvilData preData = refineEvilDetail.get(i - 1);
            refineEvilData.setStartTime(preData.getEndTime());
            EvilSoulCfg EvilSoulConfig = evilSoulCache.getOrThrow(refineEvilData.getIdentity());
            int refineTime = EvilSoulConfig.getRefineTime();
            refineEvilData.setEndTime(preData.getEndTime() + refineTime * DateTimeUtil.ONE_HOUR_MILLIS);
        }
        evilFurnaceService.update(evilFurnace);
    }

    @PlayerGmCmd(command = "EVIL_CLEAR")
    public void clearEvil(PlayerContext playerContext, String[] params) {
        EvilFurnace evilFurnace = evilFurnaceService.getEntity(playerContext.getId());
        List<RefineEvilData> refineEvilDetail = evilFurnace.getRefineDetail();
        refineEvilDetail.clear();
        evilFurnaceService.update(evilFurnace);
    }

    @PlayerGmCmd(command = "EVIL_SET_LEVEL")
    public void setEvilLevel(PlayerContext playerContext, String[] params) {
        Asserts.isTrue(params.length == 2, TipsCode.GM_PARAME_EMPTY);
        Map<Integer, Evil> evilMap = evilService.getEvilMap(playerContext.getId());
        Evil evil = evilMap.getOrDefault(Integer.parseInt(params[0]), null);
        Asserts.isTrue(evil != null, TipsCode.GM_PARAM_ERROR);
        evil.setLevel(Integer.parseInt(params[1]));
        evilService.update(evil);
    }

    @PlayerGmCmd(command = "EVIL_ACTIVE_ALL")
    public void activeAllEvil(PlayerContext playerContext, String[] params) {
        List<EvilTypeCfg> evilTypeCfg = evilTypeCache.all();
        Map<Integer, Evil> evilMap = evilService.getEvilMap(playerContext.getId());
        ArrayList<RewardThing> rewardThings = new ArrayList<>();
        for (EvilTypeCfg evilCatalog : evilTypeCfg) {
            if (evilMap.getOrDefault(evilCatalog.getIdentity(), null) != null) {
                continue;
            }
            rewardThings.add(RewardThing.of(evilCatalog.getIdentity(), 1));
        }
        thingService.add(playerContext.getId(), rewardThings, GameCause.GM);
    }

    @PlayerGmCmd(command = "EVIL_SET_ALL_LEVEL")
    public void oneClickSetAllEvilLevel(PlayerContext playerContext, String[] params) {
        Asserts.isTrue(params.length == 1, TipsCode.GM_PARAM_ERROR);
        List<Evil> entities = evilService.getEntities(playerContext.getId());
        int level = Integer.parseInt(params[0]);
        for (Evil evil :entities) {
            evil.setLevel(level);
        }
        evilService.update(entities);
    }
}
