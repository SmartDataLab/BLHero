/**
 * 
 */
package com.xiugou.x1.battle.attr;

/**
 * @author YY
 *
 */
public enum BattleAttrEnum {
	hp(201,"当前血量"),
	maxHp(202,"生命"),
	atk(203,"攻击"),
	def(204,"防御"),
	hpRate(205,"血量加成"),
	atkRate(206,"攻击加成"),
	defRate(207,"防御加成"),
	dmgRate(208,"伤害加成"),
	hit(209,"命中"),
	dodge(210,"闪避"),
	crit(211,"暴击"),
	critDmgRate(212,"暴击伤害"),
	vampireRate(213,"吸血"),
	reduceDmg(214,"减伤固定值"),
	reduceDmgRate(215,"伤害减免"),
	cdSpdRate(216,"冷却时间"),
	moveSpd(217,"移动速度"),
	moveSpdRate(218,"移动速度加成"),
	atkSpd(219,"攻击速度"),
	atkSpdRate(220,"攻击速度加成"),
	dmgbl(221,"技能伤害加成"),
	dmg(222,"增伤固定值"),
	llmaxRate(223,"人族生命加成"),
	lldmgRate(224,"人族伤害加成"),
	mjmaxRate(225,"灵族生命加成"),
	mjdmgRate(226,"灵族伤害加成"),
	zlmaxRate(227,"仙族生命加成"),
	zldmgRate(228,"仙族伤害加成"),
	woodDmg(229,"伐木伤害"),
	mineDmg(230,"挖矿伤害"),
	llmax(231,"人族生命固定值"),
	lldmg(232,"人族攻击固定值"),
	mjmax(233,"灵族生命固定值"),
	mjdmg(234,"灵族攻击固定值"),
	zlmax(235,"仙族生命固定值"),
	zldmg(236,"仙族攻击固定值"),
	battleRange(237,"进战范围"),
	llpvedmg(238,"人族伙伴对怪物伤害加成"),
	mjpvedmg(239,"灵族伙伴对怪物伤害加成"),
	zlpvedmg(240,"仙族伙伴对怪物伤害加成"),
	pvedmg(241,"对怪物伤害加成"),
	fovRange(242,"视野范围"),
	reducecrit(243,"暴击抵抗"),
	hpBrate(301,"某养成系统内基础生命加成"),
	atkBrate(302,"某养成系统内基础攻击加成"),
	;
	
	private final int id;
	private final String desc;
	
	private BattleAttrEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	public int getId() {
		return id;
	}
}
