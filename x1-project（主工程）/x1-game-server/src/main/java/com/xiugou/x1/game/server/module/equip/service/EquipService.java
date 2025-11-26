package com.xiugou.x1.game.server.module.equip.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.IThing;
import org.gaming.ruler.util.DropUtil;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.EquipCache;
import com.xiugou.x1.design.module.EquipRateCache;
import com.xiugou.x1.design.module.EquipattrStashCache;
import com.xiugou.x1.design.module.EquipattrStashCache.EquipattrStashConfig;
import com.xiugou.x1.design.module.autogen.EquipAbstractCache.EquipCfg;
import com.xiugou.x1.design.module.autogen.EquipRateAbstractCache.EquipRateCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.equip.log.EquipLogger;
import com.xiugou.x1.game.server.module.equip.model.Equip;
import com.xiugou.x1.game.server.module.equip.struct.EquipAttr;

import pb.xiugou.x1.protobuf.equip.Equip.PbEquip;
import pb.xiugou.x1.protobuf.equip.Equip.PbEquipAttr;

/**
 * @author yh
 * @date 2023/6/12
 * @apiNote
 */
@Service
public class EquipService extends OneToManyService<Equip> {
    @Autowired
    private EquipCache equipCache;
    @Autowired
    private EquipattrStashCache equipattrStashCache;
    @Autowired
    private BattleConstCache battleConstCache;
    @Autowired
    private EquipLogger equipLogger;


    public Equip getEntity(long playerId, long id) {
    	Equip equip = repository().getByKeys(playerId, id);
    	Asserts.isTrue(equip != null, TipsCode.EQUIP_DATA_ERROR, id);
    	return equip;
    }

    public void deleteAll(long playerId, List<Equip> equips, IGameCause cause) {
        this.repository().deleteAll(equips);
        equipLogger.deleteLog(playerId, equips, cause, "");
    }

    public void delete(Equip equip, IGameCause cause) {
        deleteAll(equip.getPid(), Collections.singletonList(equip), cause);
    }

    public Equip create(long pid, int equipId) {
        EquipCfg equipCfg = equipCache.getOrThrow(equipId);
        Equip equip = new Equip();
        equip.setPid(pid);
        equip.setIdentity(equipCfg.getEquipId());
        equip.setName(equipCfg.getName());
        equip.setWear(false);
        equip.setLock(false);
        long baseScore = equipCfg.getScore();
        equip.setScore(baseScore);
        if (equipCfg.getAppraise() == 0) {
        	//装备是否需要在获取时就完成鉴定
            int num = analyzeEquipSubAttr(equipCfg.getQuality());
            if (num > 0 && equipCfg.getAttrRep() > 0) {
            	verify(equip, equipCfg.getAttrRep(), num);
            }
            equip.setAppraise(true);
        }
        return equip;
    }

    public PbEquip build(Equip equip) {
        PbEquip.Builder builder = PbEquip.newBuilder();
        builder.setId(equip.getId());  //装备唯一ID
        builder.setIdentity(equip.getIdentity());//装备标识
        builder.setLock(equip.isLock()); //是否锁定
        builder.setWear(equip.isWear()); //是否穿戴
        builder.setAppraise(equip.isAppraise());//是否鉴定
        builder.setScore(equip.getScore());
        //副属性词条
        for (EquipAttr attr : equip.getSubAttrs()) {
            PbEquipAttr.Builder entry = PbEquipAttr.newBuilder();
            entry.setAttrId(attr.getAttrId());
            entry.setValue(attr.getValue());
            entry.setId(attr.getId());
            builder.addSubAttrs(entry.build());
        }
        return builder.build();
    }

    /**
     * 解析装备副属性条目数
     */
    public int analyzeEquipSubAttr(int quality) {
        List<Keyv> equipAttrNumbr = battleConstCache.getEquip_attr_numbr();
        Keyv keyv = equipAttrNumbr.stream().filter(k -> k.getId() == quality).findFirst().orElse(null);
        if (keyv == null) {
            return 0;
        }
        return keyv.getValue();
    }

    @Autowired
    private EquipRateCache equipRateCache;
    
    /**
     * 装备鉴定
     * @param equip
     * @param attrRep 属性库ID
     * @param num 词条数量
     * @return
     */
    public void verify(Equip equip, int attrRep, int num) {
        List<EquipattrStashConfig> configList = equipattrStashCache.getInStashIdCollector(attrRep);
        List<EquipattrStashConfig> randomList = new ArrayList<>();
        Set<Integer> attrSet = new HashSet<>();
        
        for(int i = 0; i < num; i++) {
        	randomList.clear();
        	for(EquipattrStashConfig config : configList) {
            	if(attrSet.contains(config.getType())) {
            		continue;
            	}
            	randomList.add(config);
            }
        	EquipattrStashConfig config = DropUtil.randomDrop(randomList);
        	attrSet.add(config.getType());
        	
        	EquipRateCfg equipRateCfg = DropUtil.randomDrop(equipRateCache.all());
        	
        	EquipAttr equipAttr = new EquipAttr();
        	equipAttr.setId(config.getId());
            equipAttr.setAttrId(config.getType());
            
            int attrRate = RandomUtil.closeClose(equipRateCfg.getAttrDown(), equipRateCfg.getAttrUp());
            equipAttr.setValue((long)(config.getMax() * (attrRate / 10000.0f)));
            equip.getSubAttrs().add(equipAttr);
            
            float rate = equipAttr.getValue() * 1.0f / config.getMax();
            equip.setScore(equip.getScore() + (long)(config.getScore() * rate));
        }
    }


    public int getListEquipNum(List<? extends IThing> things) {
        int num = 0;
        for (IThing thing : things) {
            EquipCfg equipCfg = equipCache.getOrNull(thing.getThingId());
            if (equipCfg == null) {
                continue;
            }
            num++;
        }
        return num;
    }


}
