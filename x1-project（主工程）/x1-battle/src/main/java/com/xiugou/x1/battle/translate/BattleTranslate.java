/**
 * 
 */
package com.xiugou.x1.battle.translate;

import com.xiugou.x1.battle.attr.IBattleAttr;
import com.xiugou.x1.battle.sprite.HarvestThing;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.sprite.Zone;

import pb.xiugou.x1.protobuf.battle.Battle.PbZone;

/**
 * @author YY
 *
 */
public class BattleTranslate {


	public static pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr build(IBattleAttr battleAttr) {
		pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr.Builder builder = pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr.newBuilder();
		builder.setHp(battleAttr.getHp());
		builder.setMaxHp(battleAttr.getMaxHp());
		builder.setAtk(battleAttr.getAtk());
		builder.setDef(battleAttr.getDef());
		builder.setHpRate(battleAttr.getHpRate());
		builder.setAtkRate(battleAttr.getAtkRate());
		builder.setDefRate(battleAttr.getDefRate());
		builder.setDmgRate(battleAttr.getDmgRate());
		builder.setHit(battleAttr.getHit());
		builder.setDodge(battleAttr.getDodge());
		builder.setCrit(battleAttr.getCrit());
		builder.setCritDmgRate(battleAttr.getCritDmgRate());
		builder.setVampireRate(battleAttr.getVampireRate());
		builder.setReduceDmg(battleAttr.getReduceDmg());
		builder.setReduceDmgRate(battleAttr.getReduceDmgRate());
		builder.setCdSpdRate(battleAttr.getCdSpdRate());
		builder.setMoveSpd(battleAttr.getMoveSpd());
		builder.setMoveSpdRate(battleAttr.getMoveSpdRate());
		builder.setAtkSpd(battleAttr.getAtkSpd());
		builder.setAtkSpdRate(battleAttr.getAtkSpdRate());
		builder.setDmgbl(battleAttr.getDmgbl());
		builder.setDmg(battleAttr.getDmg());
		builder.setLlmaxRate(battleAttr.getLlmaxRate());
		builder.setLldmgRate(battleAttr.getLldmgRate());
		builder.setMjmaxRate(battleAttr.getMjmaxRate());
		builder.setMjdmgRate(battleAttr.getMjdmgRate());
		builder.setZlmaxRate(battleAttr.getZlmaxRate());
		builder.setZldmgRate(battleAttr.getZldmgRate());
		builder.setWoodDmg(battleAttr.getWoodDmg());
		builder.setMineDmg(battleAttr.getMineDmg());
		builder.setLlmax(battleAttr.getLlmax());
		builder.setLldmg(battleAttr.getLldmg());
		builder.setMjmax(battleAttr.getMjmax());
		builder.setMjdmg(battleAttr.getMjdmg());
		builder.setZlmax(battleAttr.getZlmax());
		builder.setZldmg(battleAttr.getZldmg());
		builder.setBattleRange(battleAttr.getBattleRange());
		builder.setLlpvedmg(battleAttr.getLlpvedmg());
		builder.setMjpvedmg(battleAttr.getMjpvedmg());
		builder.setZlpvedmg(battleAttr.getZlpvedmg());
		builder.setPvedmg(battleAttr.getPvedmg());
		builder.setFovRange(battleAttr.getFovRange());
		builder.setReducecrit(battleAttr.getReducecrit());
		return builder.build();
	}

	
	public static PbZone build(Zone zone) {
		PbZone.Builder builder = PbZone.newBuilder();
		builder.setZoneId(zone.getId());
		for(Sprite sprite : zone.getSprites()) {
			builder.addSprites(sprite.build());
		}
		for(HarvestThing harvest : zone.getHarvests().values()) {
			builder.addHarvests(harvest.build());
		}
		if(zone.getAidSprite() != null) {
			builder.setAidSprite(zone.getAidSprite().build());
		}
		builder.setRemoveSpriteId(zone.getRemoveSpriteId());
		return builder.build();
	}
}
