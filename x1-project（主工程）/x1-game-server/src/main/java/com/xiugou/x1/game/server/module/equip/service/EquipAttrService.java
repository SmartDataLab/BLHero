package com.xiugou.x1.game.server.module.equip.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.design.module.EquipCache;
import com.xiugou.x1.design.module.EquipSuitAttrCache;
import com.xiugou.x1.design.module.autogen.EquipAbstractCache.EquipCfg;
import com.xiugou.x1.design.module.autogen.EquipSuitAttrAbstractCache.EquipSuitAttrCfg;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.model.EquipWear;
import com.xiugou.x1.game.server.module.equip.struct.EquipAttr;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.AbstractHeroFightingSystem;
import com.xiugou.x1.game.server.module.hero.service.FightingScope;

/**
 * @author yh
 * @date 2023/6/23
 * @apiNote
 */
@Service
public class EquipAttrService extends AbstractHeroFightingSystem {

    @Autowired
    private EquipWearService equipWearService;
    @Autowired
    private EquipService equipService;
    @Autowired
    private EquipCache equipCache;
    @Autowired
    private EquipSuitAttrCache equipSuitAttrCache;

    @Override
    public void calculateAttr(Hero hero, BattleAttr outAttr) {

    }

    @Override
    public void calculateTroopAttr(long playerId, BattleAttr outAttr) {
        List<Attr> equipAttrs = calculateEquipAttrs(playerId);
        List<Attr> suitAttrs = calculateSuitAttrs(playerId);
        equipAttrs.addAll(suitAttrs);
        for (Attr attr : equipAttrs) {
            outAttr.addById(attr.getAttrId(), attr.getValue());
        }
        //对基础属性进行结算
        settleBaseAttr(outAttr);
    }
    
    private List<Attr> calculateEquipAttrs(long playerId) {
        EquipWear equipWear = equipWearService.getEntity(playerId);
        ArrayList<Attr> attrsList = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : equipWear.getWearing().entrySet()) {
            Equip equip = equipService.getEntity(playerId, entry.getValue());
            //添加随机的装备词条属性
            for (EquipAttr attr : equip.getSubAttrs()) {
                attrsList.add(new Attr(attr.getAttrId(), attr.getValue()));
            }
            //添加装备的基础属性
            EquipCfg equipCfg = equipCache.getOrThrow(equip.getIdentity());
            List<Attr> attrs = equipCfg.getAttrs();
            for (Attr attr : attrs) {
                attrsList.add(new Attr(attr.getAttrId(), attr.getValue()));
            }
        }
        return attrsList;
    }

    private List<Attr> calculateSuitAttrs(long playerId) {
        Map<Integer, Integer> suitMap = equipWearService.calculateSuitNum(playerId); //套装ID - 数量
        List<Attr> attrsList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : suitMap.entrySet()) {
            List<EquipSuitAttrCfg> suitList = equipSuitAttrCache.getInSuitIdCollector(entry.getKey());
            for (EquipSuitAttrCfg suitCfg : suitList) {
                if (entry.getValue() < suitCfg.getNum()) {
                    continue;
                }
                attrsList.add(new Attr(suitCfg.getAttrs().getAttrId(), suitCfg.getAttrs().getValue()));
            }
        }
        return attrsList;
    }

	@Override
	public FightingScope fightingScope() {
		return FightingScope.EQUIP;
	}
}
