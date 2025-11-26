
package com.xiugou.x1.battle.attr;


public class BattleAttr implements IBattleAttr {
	private long hp;//当前血量
	private long maxHp;//生命
	private long atk;//攻击
	private long def;//防御
	private long hpRate;//血量加成
	private long atkRate;//攻击加成
	private long defRate;//防御加成
	private long dmgRate;//伤害加成
	private long hit;//命中
	private long dodge;//闪避
	private long crit;//暴击
	private long critDmgRate;//暴击伤害
	private long vampireRate;//吸血
	private long reduceDmg;//减伤固定值
	private long reduceDmgRate;//伤害减免
	private long cdSpdRate;//冷却时间
	private long moveSpd;//移动速度
	private long moveSpdRate;//移动速度加成
	private long atkSpd;//攻击速度
	private long atkSpdRate;//攻击速度加成
	private long dmgbl;//技能伤害加成
	private long dmg;//增伤固定值
	private long llmaxRate;//人族生命加成
	private long lldmgRate;//人族伤害加成
	private long mjmaxRate;//灵族生命加成
	private long mjdmgRate;//灵族伤害加成
	private long zlmaxRate;//仙族生命加成
	private long zldmgRate;//仙族伤害加成
	private long woodDmg;//伐木伤害
	private long mineDmg;//挖矿伤害
	private long llmax;//人族生命固定值
	private long lldmg;//人族攻击固定值
	private long mjmax;//灵族生命固定值
	private long mjdmg;//灵族攻击固定值
	private long zlmax;//仙族生命固定值
	private long zldmg;//仙族攻击固定值
	private long battleRange;//进战范围
	private long llpvedmg;//人族伙伴对怪物伤害加成
	private long mjpvedmg;//灵族伙伴对怪物伤害加成
	private long zlpvedmg;//仙族伙伴对怪物伤害加成
	private long pvedmg;//对怪物伤害加成
	private long fovRange;//视野范围
	private long reducecrit;//暴击抵抗
	private long hpBrate;//某养成系统内基础生命加成
	private long atkBrate;//某养成系统内基础攻击加成
	@Override
	public void merge(IBattleAttr battleAttr) {
		this.hp += battleAttr.getHp();
		this.maxHp += battleAttr.getMaxHp();
		this.atk += battleAttr.getAtk();
		this.def += battleAttr.getDef();
		this.hpRate += battleAttr.getHpRate();
		this.atkRate += battleAttr.getAtkRate();
		this.defRate += battleAttr.getDefRate();
		this.dmgRate += battleAttr.getDmgRate();
		this.hit += battleAttr.getHit();
		this.dodge += battleAttr.getDodge();
		this.crit += battleAttr.getCrit();
		this.critDmgRate += battleAttr.getCritDmgRate();
		this.vampireRate += battleAttr.getVampireRate();
		this.reduceDmg += battleAttr.getReduceDmg();
		this.reduceDmgRate += battleAttr.getReduceDmgRate();
		this.cdSpdRate += battleAttr.getCdSpdRate();
		this.moveSpd += battleAttr.getMoveSpd();
		this.moveSpdRate += battleAttr.getMoveSpdRate();
		this.atkSpd += battleAttr.getAtkSpd();
		this.atkSpdRate += battleAttr.getAtkSpdRate();
		this.dmgbl += battleAttr.getDmgbl();
		this.dmg += battleAttr.getDmg();
		this.llmaxRate += battleAttr.getLlmaxRate();
		this.lldmgRate += battleAttr.getLldmgRate();
		this.mjmaxRate += battleAttr.getMjmaxRate();
		this.mjdmgRate += battleAttr.getMjdmgRate();
		this.zlmaxRate += battleAttr.getZlmaxRate();
		this.zldmgRate += battleAttr.getZldmgRate();
		this.woodDmg += battleAttr.getWoodDmg();
		this.mineDmg += battleAttr.getMineDmg();
		this.llmax += battleAttr.getLlmax();
		this.lldmg += battleAttr.getLldmg();
		this.mjmax += battleAttr.getMjmax();
		this.mjdmg += battleAttr.getMjdmg();
		this.zlmax += battleAttr.getZlmax();
		this.zldmg += battleAttr.getZldmg();
		this.battleRange += battleAttr.getBattleRange();
		this.llpvedmg += battleAttr.getLlpvedmg();
		this.mjpvedmg += battleAttr.getMjpvedmg();
		this.zlpvedmg += battleAttr.getZlpvedmg();
		this.pvedmg += battleAttr.getPvedmg();
		this.fovRange += battleAttr.getFovRange();
		this.reducecrit += battleAttr.getReducecrit();
		this.hpBrate += battleAttr.getHpBrate();
		this.atkBrate += battleAttr.getAtkBrate();
	}
	@Override
	public void topMerge(IBattleAttr battleAttr) {
		if(battleAttr.getHp() > this.hp) {
			this.hp = battleAttr.getHp();
		}
		if(battleAttr.getMaxHp() > this.maxHp) {
			this.maxHp = battleAttr.getMaxHp();
		}
		if(battleAttr.getAtk() > this.atk) {
			this.atk = battleAttr.getAtk();
		}
		if(battleAttr.getDef() > this.def) {
			this.def = battleAttr.getDef();
		}
		if(battleAttr.getHpRate() > this.hpRate) {
			this.hpRate = battleAttr.getHpRate();
		}
		if(battleAttr.getAtkRate() > this.atkRate) {
			this.atkRate = battleAttr.getAtkRate();
		}
		if(battleAttr.getDefRate() > this.defRate) {
			this.defRate = battleAttr.getDefRate();
		}
		if(battleAttr.getDmgRate() > this.dmgRate) {
			this.dmgRate = battleAttr.getDmgRate();
		}
		if(battleAttr.getHit() > this.hit) {
			this.hit = battleAttr.getHit();
		}
		if(battleAttr.getDodge() > this.dodge) {
			this.dodge = battleAttr.getDodge();
		}
		if(battleAttr.getCrit() > this.crit) {
			this.crit = battleAttr.getCrit();
		}
		if(battleAttr.getCritDmgRate() > this.critDmgRate) {
			this.critDmgRate = battleAttr.getCritDmgRate();
		}
		if(battleAttr.getVampireRate() > this.vampireRate) {
			this.vampireRate = battleAttr.getVampireRate();
		}
		if(battleAttr.getReduceDmg() > this.reduceDmg) {
			this.reduceDmg = battleAttr.getReduceDmg();
		}
		if(battleAttr.getReduceDmgRate() > this.reduceDmgRate) {
			this.reduceDmgRate = battleAttr.getReduceDmgRate();
		}
		if(battleAttr.getCdSpdRate() > this.cdSpdRate) {
			this.cdSpdRate = battleAttr.getCdSpdRate();
		}
		if(battleAttr.getMoveSpd() > this.moveSpd) {
			this.moveSpd = battleAttr.getMoveSpd();
		}
		if(battleAttr.getMoveSpdRate() > this.moveSpdRate) {
			this.moveSpdRate = battleAttr.getMoveSpdRate();
		}
		if(battleAttr.getAtkSpd() > this.atkSpd) {
			this.atkSpd = battleAttr.getAtkSpd();
		}
		if(battleAttr.getAtkSpdRate() > this.atkSpdRate) {
			this.atkSpdRate = battleAttr.getAtkSpdRate();
		}
		if(battleAttr.getDmgbl() > this.dmgbl) {
			this.dmgbl = battleAttr.getDmgbl();
		}
		if(battleAttr.getDmg() > this.dmg) {
			this.dmg = battleAttr.getDmg();
		}
		if(battleAttr.getLlmaxRate() > this.llmaxRate) {
			this.llmaxRate = battleAttr.getLlmaxRate();
		}
		if(battleAttr.getLldmgRate() > this.lldmgRate) {
			this.lldmgRate = battleAttr.getLldmgRate();
		}
		if(battleAttr.getMjmaxRate() > this.mjmaxRate) {
			this.mjmaxRate = battleAttr.getMjmaxRate();
		}
		if(battleAttr.getMjdmgRate() > this.mjdmgRate) {
			this.mjdmgRate = battleAttr.getMjdmgRate();
		}
		if(battleAttr.getZlmaxRate() > this.zlmaxRate) {
			this.zlmaxRate = battleAttr.getZlmaxRate();
		}
		if(battleAttr.getZldmgRate() > this.zldmgRate) {
			this.zldmgRate = battleAttr.getZldmgRate();
		}
		if(battleAttr.getWoodDmg() > this.woodDmg) {
			this.woodDmg = battleAttr.getWoodDmg();
		}
		if(battleAttr.getMineDmg() > this.mineDmg) {
			this.mineDmg = battleAttr.getMineDmg();
		}
		if(battleAttr.getLlmax() > this.llmax) {
			this.llmax = battleAttr.getLlmax();
		}
		if(battleAttr.getLldmg() > this.lldmg) {
			this.lldmg = battleAttr.getLldmg();
		}
		if(battleAttr.getMjmax() > this.mjmax) {
			this.mjmax = battleAttr.getMjmax();
		}
		if(battleAttr.getMjdmg() > this.mjdmg) {
			this.mjdmg = battleAttr.getMjdmg();
		}
		if(battleAttr.getZlmax() > this.zlmax) {
			this.zlmax = battleAttr.getZlmax();
		}
		if(battleAttr.getZldmg() > this.zldmg) {
			this.zldmg = battleAttr.getZldmg();
		}
		if(battleAttr.getBattleRange() > this.battleRange) {
			this.battleRange = battleAttr.getBattleRange();
		}
		if(battleAttr.getLlpvedmg() > this.llpvedmg) {
			this.llpvedmg = battleAttr.getLlpvedmg();
		}
		if(battleAttr.getMjpvedmg() > this.mjpvedmg) {
			this.mjpvedmg = battleAttr.getMjpvedmg();
		}
		if(battleAttr.getZlpvedmg() > this.zlpvedmg) {
			this.zlpvedmg = battleAttr.getZlpvedmg();
		}
		if(battleAttr.getPvedmg() > this.pvedmg) {
			this.pvedmg = battleAttr.getPvedmg();
		}
		if(battleAttr.getFovRange() > this.fovRange) {
			this.fovRange = battleAttr.getFovRange();
		}
		if(battleAttr.getReducecrit() > this.reducecrit) {
			this.reducecrit = battleAttr.getReducecrit();
		}
		if(battleAttr.getHpBrate() > this.hpBrate) {
			this.hpBrate = battleAttr.getHpBrate();
		}
		if(battleAttr.getAtkBrate() > this.atkBrate) {
			this.atkBrate = battleAttr.getAtkBrate();
		}
	}
	public void clear() {
		this.hp = 0;
		this.maxHp = 0;
		this.atk = 0;
		this.def = 0;
		this.hpRate = 0;
		this.atkRate = 0;
		this.defRate = 0;
		this.dmgRate = 0;
		this.hit = 0;
		this.dodge = 0;
		this.crit = 0;
		this.critDmgRate = 0;
		this.vampireRate = 0;
		this.reduceDmg = 0;
		this.reduceDmgRate = 0;
		this.cdSpdRate = 0;
		this.moveSpd = 0;
		this.moveSpdRate = 0;
		this.atkSpd = 0;
		this.atkSpdRate = 0;
		this.dmgbl = 0;
		this.dmg = 0;
		this.llmaxRate = 0;
		this.lldmgRate = 0;
		this.mjmaxRate = 0;
		this.mjdmgRate = 0;
		this.zlmaxRate = 0;
		this.zldmgRate = 0;
		this.woodDmg = 0;
		this.mineDmg = 0;
		this.llmax = 0;
		this.lldmg = 0;
		this.mjmax = 0;
		this.mjdmg = 0;
		this.zlmax = 0;
		this.zldmg = 0;
		this.battleRange = 0;
		this.llpvedmg = 0;
		this.mjpvedmg = 0;
		this.zlpvedmg = 0;
		this.pvedmg = 0;
		this.fovRange = 0;
		this.reducecrit = 0;
		this.hpBrate = 0;
		this.atkBrate = 0;
	}
	public java.util.Map<String, Long> attrMap() {
		java.util.Map<String, Long> map = new java.util.HashMap<>();
		map.put("hp", hp);
		map.put("maxHp", maxHp);
		map.put("atk", atk);
		map.put("def", def);
		map.put("hpRate", hpRate);
		map.put("atkRate", atkRate);
		map.put("defRate", defRate);
		map.put("dmgRate", dmgRate);
		map.put("hit", hit);
		map.put("dodge", dodge);
		map.put("crit", crit);
		map.put("critDmgRate", critDmgRate);
		map.put("vampireRate", vampireRate);
		map.put("reduceDmg", reduceDmg);
		map.put("reduceDmgRate", reduceDmgRate);
		map.put("cdSpdRate", cdSpdRate);
		map.put("moveSpd", moveSpd);
		map.put("moveSpdRate", moveSpdRate);
		map.put("atkSpd", atkSpd);
		map.put("atkSpdRate", atkSpdRate);
		map.put("dmgbl", dmgbl);
		map.put("dmg", dmg);
		map.put("llmaxRate", llmaxRate);
		map.put("lldmgRate", lldmgRate);
		map.put("mjmaxRate", mjmaxRate);
		map.put("mjdmgRate", mjdmgRate);
		map.put("zlmaxRate", zlmaxRate);
		map.put("zldmgRate", zldmgRate);
		map.put("woodDmg", woodDmg);
		map.put("mineDmg", mineDmg);
		map.put("llmax", llmax);
		map.put("lldmg", lldmg);
		map.put("mjmax", mjmax);
		map.put("mjdmg", mjdmg);
		map.put("zlmax", zlmax);
		map.put("zldmg", zldmg);
		map.put("battleRange", battleRange);
		map.put("llpvedmg", llpvedmg);
		map.put("mjpvedmg", mjpvedmg);
		map.put("zlpvedmg", zlpvedmg);
		map.put("pvedmg", pvedmg);
		map.put("fovRange", fovRange);
		map.put("reducecrit", reducecrit);
		map.put("hpBrate", hpBrate);
		map.put("atkBrate", atkBrate);
		return map;
	}
	public BattleAttr copy() {
		BattleAttr copy = new BattleAttr();
		copy.hp = this.hp;
		copy.maxHp = this.maxHp;
		copy.atk = this.atk;
		copy.def = this.def;
		copy.hpRate = this.hpRate;
		copy.atkRate = this.atkRate;
		copy.defRate = this.defRate;
		copy.dmgRate = this.dmgRate;
		copy.hit = this.hit;
		copy.dodge = this.dodge;
		copy.crit = this.crit;
		copy.critDmgRate = this.critDmgRate;
		copy.vampireRate = this.vampireRate;
		copy.reduceDmg = this.reduceDmg;
		copy.reduceDmgRate = this.reduceDmgRate;
		copy.cdSpdRate = this.cdSpdRate;
		copy.moveSpd = this.moveSpd;
		copy.moveSpdRate = this.moveSpdRate;
		copy.atkSpd = this.atkSpd;
		copy.atkSpdRate = this.atkSpdRate;
		copy.dmgbl = this.dmgbl;
		copy.dmg = this.dmg;
		copy.llmaxRate = this.llmaxRate;
		copy.lldmgRate = this.lldmgRate;
		copy.mjmaxRate = this.mjmaxRate;
		copy.mjdmgRate = this.mjdmgRate;
		copy.zlmaxRate = this.zlmaxRate;
		copy.zldmgRate = this.zldmgRate;
		copy.woodDmg = this.woodDmg;
		copy.mineDmg = this.mineDmg;
		copy.llmax = this.llmax;
		copy.lldmg = this.lldmg;
		copy.mjmax = this.mjmax;
		copy.mjdmg = this.mjdmg;
		copy.zlmax = this.zlmax;
		copy.zldmg = this.zldmg;
		copy.battleRange = this.battleRange;
		copy.llpvedmg = this.llpvedmg;
		copy.mjpvedmg = this.mjpvedmg;
		copy.zlpvedmg = this.zlpvedmg;
		copy.pvedmg = this.pvedmg;
		copy.fovRange = this.fovRange;
		copy.reducecrit = this.reducecrit;
		copy.hpBrate = this.hpBrate;
		copy.atkBrate = this.atkBrate;
		return copy;
	}
	public static BattleAttr icopy(IBattleAttr battleAttr) {
		BattleAttr copy = new BattleAttr();
		copy.hp = battleAttr.getHp();
		copy.maxHp = battleAttr.getMaxHp();
		copy.atk = battleAttr.getAtk();
		copy.def = battleAttr.getDef();
		copy.hpRate = battleAttr.getHpRate();
		copy.atkRate = battleAttr.getAtkRate();
		copy.defRate = battleAttr.getDefRate();
		copy.dmgRate = battleAttr.getDmgRate();
		copy.hit = battleAttr.getHit();
		copy.dodge = battleAttr.getDodge();
		copy.crit = battleAttr.getCrit();
		copy.critDmgRate = battleAttr.getCritDmgRate();
		copy.vampireRate = battleAttr.getVampireRate();
		copy.reduceDmg = battleAttr.getReduceDmg();
		copy.reduceDmgRate = battleAttr.getReduceDmgRate();
		copy.cdSpdRate = battleAttr.getCdSpdRate();
		copy.moveSpd = battleAttr.getMoveSpd();
		copy.moveSpdRate = battleAttr.getMoveSpdRate();
		copy.atkSpd = battleAttr.getAtkSpd();
		copy.atkSpdRate = battleAttr.getAtkSpdRate();
		copy.dmgbl = battleAttr.getDmgbl();
		copy.dmg = battleAttr.getDmg();
		copy.llmaxRate = battleAttr.getLlmaxRate();
		copy.lldmgRate = battleAttr.getLldmgRate();
		copy.mjmaxRate = battleAttr.getMjmaxRate();
		copy.mjdmgRate = battleAttr.getMjdmgRate();
		copy.zlmaxRate = battleAttr.getZlmaxRate();
		copy.zldmgRate = battleAttr.getZldmgRate();
		copy.woodDmg = battleAttr.getWoodDmg();
		copy.mineDmg = battleAttr.getMineDmg();
		copy.llmax = battleAttr.getLlmax();
		copy.lldmg = battleAttr.getLldmg();
		copy.mjmax = battleAttr.getMjmax();
		copy.mjdmg = battleAttr.getMjdmg();
		copy.zlmax = battleAttr.getZlmax();
		copy.zldmg = battleAttr.getZldmg();
		copy.battleRange = battleAttr.getBattleRange();
		copy.llpvedmg = battleAttr.getLlpvedmg();
		copy.mjpvedmg = battleAttr.getMjpvedmg();
		copy.zlpvedmg = battleAttr.getZlpvedmg();
		copy.pvedmg = battleAttr.getPvedmg();
		copy.fovRange = battleAttr.getFovRange();
		copy.reducecrit = battleAttr.getReducecrit();
		copy.hpBrate = battleAttr.getHpBrate();
		copy.atkBrate = battleAttr.getAtkBrate();
		return copy;
	}
	public java.util.Map<String, Long> notZeroAttrMap() {
		java.util.Map<String, Long> map = new java.util.LinkedHashMap<>();
		if(hp > 0) {
			map.put("hp", hp);
		}
		if(maxHp > 0) {
			map.put("maxHp", maxHp);
		}
		if(atk > 0) {
			map.put("atk", atk);
		}
		if(def > 0) {
			map.put("def", def);
		}
		if(hpRate > 0) {
			map.put("hpRate", hpRate);
		}
		if(atkRate > 0) {
			map.put("atkRate", atkRate);
		}
		if(defRate > 0) {
			map.put("defRate", defRate);
		}
		if(dmgRate > 0) {
			map.put("dmgRate", dmgRate);
		}
		if(hit > 0) {
			map.put("hit", hit);
		}
		if(dodge > 0) {
			map.put("dodge", dodge);
		}
		if(crit > 0) {
			map.put("crit", crit);
		}
		if(critDmgRate > 0) {
			map.put("critDmgRate", critDmgRate);
		}
		if(vampireRate > 0) {
			map.put("vampireRate", vampireRate);
		}
		if(reduceDmg > 0) {
			map.put("reduceDmg", reduceDmg);
		}
		if(reduceDmgRate > 0) {
			map.put("reduceDmgRate", reduceDmgRate);
		}
		if(cdSpdRate > 0) {
			map.put("cdSpdRate", cdSpdRate);
		}
		if(moveSpd > 0) {
			map.put("moveSpd", moveSpd);
		}
		if(moveSpdRate > 0) {
			map.put("moveSpdRate", moveSpdRate);
		}
		if(atkSpd > 0) {
			map.put("atkSpd", atkSpd);
		}
		if(atkSpdRate > 0) {
			map.put("atkSpdRate", atkSpdRate);
		}
		if(dmgbl > 0) {
			map.put("dmgbl", dmgbl);
		}
		if(dmg > 0) {
			map.put("dmg", dmg);
		}
		if(llmaxRate > 0) {
			map.put("llmaxRate", llmaxRate);
		}
		if(lldmgRate > 0) {
			map.put("lldmgRate", lldmgRate);
		}
		if(mjmaxRate > 0) {
			map.put("mjmaxRate", mjmaxRate);
		}
		if(mjdmgRate > 0) {
			map.put("mjdmgRate", mjdmgRate);
		}
		if(zlmaxRate > 0) {
			map.put("zlmaxRate", zlmaxRate);
		}
		if(zldmgRate > 0) {
			map.put("zldmgRate", zldmgRate);
		}
		if(woodDmg > 0) {
			map.put("woodDmg", woodDmg);
		}
		if(mineDmg > 0) {
			map.put("mineDmg", mineDmg);
		}
		if(llmax > 0) {
			map.put("llmax", llmax);
		}
		if(lldmg > 0) {
			map.put("lldmg", lldmg);
		}
		if(mjmax > 0) {
			map.put("mjmax", mjmax);
		}
		if(mjdmg > 0) {
			map.put("mjdmg", mjdmg);
		}
		if(zlmax > 0) {
			map.put("zlmax", zlmax);
		}
		if(zldmg > 0) {
			map.put("zldmg", zldmg);
		}
		if(battleRange > 0) {
			map.put("battleRange", battleRange);
		}
		if(llpvedmg > 0) {
			map.put("llpvedmg", llpvedmg);
		}
		if(mjpvedmg > 0) {
			map.put("mjpvedmg", mjpvedmg);
		}
		if(zlpvedmg > 0) {
			map.put("zlpvedmg", zlpvedmg);
		}
		if(pvedmg > 0) {
			map.put("pvedmg", pvedmg);
		}
		if(fovRange > 0) {
			map.put("fovRange", fovRange);
		}
		if(reducecrit > 0) {
			map.put("reducecrit", reducecrit);
		}
		if(hpBrate > 0) {
			map.put("hpBrate", hpBrate);
		}
		if(atkBrate > 0) {
			map.put("atkBrate", atkBrate);
		}
		return map;
	}
	@Override
	public void multiple(int multiple) {
		this.hp *= multiple;
		this.maxHp *= multiple;
		this.atk *= multiple;
		this.def *= multiple;
		this.hpRate *= multiple;
		this.atkRate *= multiple;
		this.defRate *= multiple;
		this.dmgRate *= multiple;
		this.hit *= multiple;
		this.dodge *= multiple;
		this.crit *= multiple;
		this.critDmgRate *= multiple;
		this.vampireRate *= multiple;
		this.reduceDmg *= multiple;
		this.reduceDmgRate *= multiple;
		this.cdSpdRate *= multiple;
		this.moveSpd *= multiple;
		this.moveSpdRate *= multiple;
		this.atkSpd *= multiple;
		this.atkSpdRate *= multiple;
		this.dmgbl *= multiple;
		this.dmg *= multiple;
		this.llmaxRate *= multiple;
		this.lldmgRate *= multiple;
		this.mjmaxRate *= multiple;
		this.mjdmgRate *= multiple;
		this.zlmaxRate *= multiple;
		this.zldmgRate *= multiple;
		this.woodDmg *= multiple;
		this.mineDmg *= multiple;
		this.llmax *= multiple;
		this.lldmg *= multiple;
		this.mjmax *= multiple;
		this.mjdmg *= multiple;
		this.zlmax *= multiple;
		this.zldmg *= multiple;
		this.battleRange *= multiple;
		this.llpvedmg *= multiple;
		this.mjpvedmg *= multiple;
		this.zlpvedmg *= multiple;
		this.pvedmg *= multiple;
		this.fovRange *= multiple;
		this.reducecrit *= multiple;
		this.hpBrate *= multiple;
		this.atkBrate *= multiple;
	}
	@Override
	public void dmultiple(double multiple) {
		this.hp = (long)(this.hp * multiple);
		this.maxHp = (long)(this.maxHp * multiple);
		this.atk = (long)(this.atk * multiple);
		this.def = (long)(this.def * multiple);
		this.hpRate = (long)(this.hpRate * multiple);
		this.atkRate = (long)(this.atkRate * multiple);
		this.defRate = (long)(this.defRate * multiple);
		this.dmgRate = (long)(this.dmgRate * multiple);
		this.hit = (long)(this.hit * multiple);
		this.dodge = (long)(this.dodge * multiple);
		this.crit = (long)(this.crit * multiple);
		this.critDmgRate = (long)(this.critDmgRate * multiple);
		this.vampireRate = (long)(this.vampireRate * multiple);
		this.reduceDmg = (long)(this.reduceDmg * multiple);
		this.reduceDmgRate = (long)(this.reduceDmgRate * multiple);
		this.cdSpdRate = (long)(this.cdSpdRate * multiple);
		this.moveSpd = (long)(this.moveSpd * multiple);
		this.moveSpdRate = (long)(this.moveSpdRate * multiple);
		this.atkSpd = (long)(this.atkSpd * multiple);
		this.atkSpdRate = (long)(this.atkSpdRate * multiple);
		this.dmgbl = (long)(this.dmgbl * multiple);
		this.dmg = (long)(this.dmg * multiple);
		this.llmaxRate = (long)(this.llmaxRate * multiple);
		this.lldmgRate = (long)(this.lldmgRate * multiple);
		this.mjmaxRate = (long)(this.mjmaxRate * multiple);
		this.mjdmgRate = (long)(this.mjdmgRate * multiple);
		this.zlmaxRate = (long)(this.zlmaxRate * multiple);
		this.zldmgRate = (long)(this.zldmgRate * multiple);
		this.woodDmg = (long)(this.woodDmg * multiple);
		this.mineDmg = (long)(this.mineDmg * multiple);
		this.llmax = (long)(this.llmax * multiple);
		this.lldmg = (long)(this.lldmg * multiple);
		this.mjmax = (long)(this.mjmax * multiple);
		this.mjdmg = (long)(this.mjdmg * multiple);
		this.zlmax = (long)(this.zlmax * multiple);
		this.zldmg = (long)(this.zldmg * multiple);
		this.battleRange = (long)(this.battleRange * multiple);
		this.llpvedmg = (long)(this.llpvedmg * multiple);
		this.mjpvedmg = (long)(this.mjpvedmg * multiple);
		this.zlpvedmg = (long)(this.zlpvedmg * multiple);
		this.pvedmg = (long)(this.pvedmg * multiple);
		this.fovRange = (long)(this.fovRange * multiple);
		this.reducecrit = (long)(this.reducecrit * multiple);
		this.hpBrate = (long)(this.hpBrate * multiple);
		this.atkBrate = (long)(this.atkBrate * multiple);
	}
	@Override
	public void add(String attrName, long value) {
		if("hp".equals(attrName)) {
			this.hp += value;
		}
		if("maxHp".equals(attrName)) {
			this.maxHp += value;
		}
		if("atk".equals(attrName)) {
			this.atk += value;
		}
		if("def".equals(attrName)) {
			this.def += value;
		}
		if("hpRate".equals(attrName)) {
			this.hpRate += value;
		}
		if("atkRate".equals(attrName)) {
			this.atkRate += value;
		}
		if("defRate".equals(attrName)) {
			this.defRate += value;
		}
		if("dmgRate".equals(attrName)) {
			this.dmgRate += value;
		}
		if("hit".equals(attrName)) {
			this.hit += value;
		}
		if("dodge".equals(attrName)) {
			this.dodge += value;
		}
		if("crit".equals(attrName)) {
			this.crit += value;
		}
		if("critDmgRate".equals(attrName)) {
			this.critDmgRate += value;
		}
		if("vampireRate".equals(attrName)) {
			this.vampireRate += value;
		}
		if("reduceDmg".equals(attrName)) {
			this.reduceDmg += value;
		}
		if("reduceDmgRate".equals(attrName)) {
			this.reduceDmgRate += value;
		}
		if("cdSpdRate".equals(attrName)) {
			this.cdSpdRate += value;
		}
		if("moveSpd".equals(attrName)) {
			this.moveSpd += value;
		}
		if("moveSpdRate".equals(attrName)) {
			this.moveSpdRate += value;
		}
		if("atkSpd".equals(attrName)) {
			this.atkSpd += value;
		}
		if("atkSpdRate".equals(attrName)) {
			this.atkSpdRate += value;
		}
		if("dmgbl".equals(attrName)) {
			this.dmgbl += value;
		}
		if("dmg".equals(attrName)) {
			this.dmg += value;
		}
		if("llmaxRate".equals(attrName)) {
			this.llmaxRate += value;
		}
		if("lldmgRate".equals(attrName)) {
			this.lldmgRate += value;
		}
		if("mjmaxRate".equals(attrName)) {
			this.mjmaxRate += value;
		}
		if("mjdmgRate".equals(attrName)) {
			this.mjdmgRate += value;
		}
		if("zlmaxRate".equals(attrName)) {
			this.zlmaxRate += value;
		}
		if("zldmgRate".equals(attrName)) {
			this.zldmgRate += value;
		}
		if("woodDmg".equals(attrName)) {
			this.woodDmg += value;
		}
		if("mineDmg".equals(attrName)) {
			this.mineDmg += value;
		}
		if("llmax".equals(attrName)) {
			this.llmax += value;
		}
		if("lldmg".equals(attrName)) {
			this.lldmg += value;
		}
		if("mjmax".equals(attrName)) {
			this.mjmax += value;
		}
		if("mjdmg".equals(attrName)) {
			this.mjdmg += value;
		}
		if("zlmax".equals(attrName)) {
			this.zlmax += value;
		}
		if("zldmg".equals(attrName)) {
			this.zldmg += value;
		}
		if("battleRange".equals(attrName)) {
			this.battleRange += value;
		}
		if("llpvedmg".equals(attrName)) {
			this.llpvedmg += value;
		}
		if("mjpvedmg".equals(attrName)) {
			this.mjpvedmg += value;
		}
		if("zlpvedmg".equals(attrName)) {
			this.zlpvedmg += value;
		}
		if("pvedmg".equals(attrName)) {
			this.pvedmg += value;
		}
		if("fovRange".equals(attrName)) {
			this.fovRange += value;
		}
		if("reducecrit".equals(attrName)) {
			this.reducecrit += value;
		}
		if("hpBrate".equals(attrName)) {
			this.hpBrate += value;
		}
		if("atkBrate".equals(attrName)) {
			this.atkBrate += value;
		}
	}
	@Override
	public void addById(int attrId, long value) {
		if(attrId == 201) {
			this.hp += value;
		}
		if(attrId == 202) {
			this.maxHp += value;
		}
		if(attrId == 203) {
			this.atk += value;
		}
		if(attrId == 204) {
			this.def += value;
		}
		if(attrId == 205) {
			this.hpRate += value;
		}
		if(attrId == 206) {
			this.atkRate += value;
		}
		if(attrId == 207) {
			this.defRate += value;
		}
		if(attrId == 208) {
			this.dmgRate += value;
		}
		if(attrId == 209) {
			this.hit += value;
		}
		if(attrId == 210) {
			this.dodge += value;
		}
		if(attrId == 211) {
			this.crit += value;
		}
		if(attrId == 212) {
			this.critDmgRate += value;
		}
		if(attrId == 213) {
			this.vampireRate += value;
		}
		if(attrId == 214) {
			this.reduceDmg += value;
		}
		if(attrId == 215) {
			this.reduceDmgRate += value;
		}
		if(attrId == 216) {
			this.cdSpdRate += value;
		}
		if(attrId == 217) {
			this.moveSpd += value;
		}
		if(attrId == 218) {
			this.moveSpdRate += value;
		}
		if(attrId == 219) {
			this.atkSpd += value;
		}
		if(attrId == 220) {
			this.atkSpdRate += value;
		}
		if(attrId == 221) {
			this.dmgbl += value;
		}
		if(attrId == 222) {
			this.dmg += value;
		}
		if(attrId == 223) {
			this.llmaxRate += value;
		}
		if(attrId == 224) {
			this.lldmgRate += value;
		}
		if(attrId == 225) {
			this.mjmaxRate += value;
		}
		if(attrId == 226) {
			this.mjdmgRate += value;
		}
		if(attrId == 227) {
			this.zlmaxRate += value;
		}
		if(attrId == 228) {
			this.zldmgRate += value;
		}
		if(attrId == 229) {
			this.woodDmg += value;
		}
		if(attrId == 230) {
			this.mineDmg += value;
		}
		if(attrId == 231) {
			this.llmax += value;
		}
		if(attrId == 232) {
			this.lldmg += value;
		}
		if(attrId == 233) {
			this.mjmax += value;
		}
		if(attrId == 234) {
			this.mjdmg += value;
		}
		if(attrId == 235) {
			this.zlmax += value;
		}
		if(attrId == 236) {
			this.zldmg += value;
		}
		if(attrId == 237) {
			this.battleRange += value;
		}
		if(attrId == 238) {
			this.llpvedmg += value;
		}
		if(attrId == 239) {
			this.mjpvedmg += value;
		}
		if(attrId == 240) {
			this.zlpvedmg += value;
		}
		if(attrId == 241) {
			this.pvedmg += value;
		}
		if(attrId == 242) {
			this.fovRange += value;
		}
		if(attrId == 243) {
			this.reducecrit += value;
		}
		if(attrId == 301) {
			this.hpBrate += value;
		}
		if(attrId == 302) {
			this.atkBrate += value;
		}
	}
	@Override
	public void subById(int attrId, long value) {
		if(attrId == 201) {
			this.hp -= value;
		}
		if(attrId == 202) {
			this.maxHp -= value;
		}
		if(attrId == 203) {
			this.atk -= value;
		}
		if(attrId == 204) {
			this.def -= value;
		}
		if(attrId == 205) {
			this.hpRate -= value;
		}
		if(attrId == 206) {
			this.atkRate -= value;
		}
		if(attrId == 207) {
			this.defRate -= value;
		}
		if(attrId == 208) {
			this.dmgRate -= value;
		}
		if(attrId == 209) {
			this.hit -= value;
		}
		if(attrId == 210) {
			this.dodge -= value;
		}
		if(attrId == 211) {
			this.crit -= value;
		}
		if(attrId == 212) {
			this.critDmgRate -= value;
		}
		if(attrId == 213) {
			this.vampireRate -= value;
		}
		if(attrId == 214) {
			this.reduceDmg -= value;
		}
		if(attrId == 215) {
			this.reduceDmgRate -= value;
		}
		if(attrId == 216) {
			this.cdSpdRate -= value;
		}
		if(attrId == 217) {
			this.moveSpd -= value;
		}
		if(attrId == 218) {
			this.moveSpdRate -= value;
		}
		if(attrId == 219) {
			this.atkSpd -= value;
		}
		if(attrId == 220) {
			this.atkSpdRate -= value;
		}
		if(attrId == 221) {
			this.dmgbl -= value;
		}
		if(attrId == 222) {
			this.dmg -= value;
		}
		if(attrId == 223) {
			this.llmaxRate -= value;
		}
		if(attrId == 224) {
			this.lldmgRate -= value;
		}
		if(attrId == 225) {
			this.mjmaxRate -= value;
		}
		if(attrId == 226) {
			this.mjdmgRate -= value;
		}
		if(attrId == 227) {
			this.zlmaxRate -= value;
		}
		if(attrId == 228) {
			this.zldmgRate -= value;
		}
		if(attrId == 229) {
			this.woodDmg -= value;
		}
		if(attrId == 230) {
			this.mineDmg -= value;
		}
		if(attrId == 231) {
			this.llmax -= value;
		}
		if(attrId == 232) {
			this.lldmg -= value;
		}
		if(attrId == 233) {
			this.mjmax -= value;
		}
		if(attrId == 234) {
			this.mjdmg -= value;
		}
		if(attrId == 235) {
			this.zlmax -= value;
		}
		if(attrId == 236) {
			this.zldmg -= value;
		}
		if(attrId == 237) {
			this.battleRange -= value;
		}
		if(attrId == 238) {
			this.llpvedmg -= value;
		}
		if(attrId == 239) {
			this.mjpvedmg -= value;
		}
		if(attrId == 240) {
			this.zlpvedmg -= value;
		}
		if(attrId == 241) {
			this.pvedmg -= value;
		}
		if(attrId == 242) {
			this.fovRange -= value;
		}
		if(attrId == 243) {
			this.reducecrit -= value;
		}
		if(attrId == 301) {
			this.hpBrate -= value;
		}
		if(attrId == 302) {
			this.atkBrate -= value;
		}
	}
	@Override
	public long get(String attrName) {
		if("hp".equals(attrName)) {
			return this.hp;
		}
		if("maxHp".equals(attrName)) {
			return this.maxHp;
		}
		if("atk".equals(attrName)) {
			return this.atk;
		}
		if("def".equals(attrName)) {
			return this.def;
		}
		if("hpRate".equals(attrName)) {
			return this.hpRate;
		}
		if("atkRate".equals(attrName)) {
			return this.atkRate;
		}
		if("defRate".equals(attrName)) {
			return this.defRate;
		}
		if("dmgRate".equals(attrName)) {
			return this.dmgRate;
		}
		if("hit".equals(attrName)) {
			return this.hit;
		}
		if("dodge".equals(attrName)) {
			return this.dodge;
		}
		if("crit".equals(attrName)) {
			return this.crit;
		}
		if("critDmgRate".equals(attrName)) {
			return this.critDmgRate;
		}
		if("vampireRate".equals(attrName)) {
			return this.vampireRate;
		}
		if("reduceDmg".equals(attrName)) {
			return this.reduceDmg;
		}
		if("reduceDmgRate".equals(attrName)) {
			return this.reduceDmgRate;
		}
		if("cdSpdRate".equals(attrName)) {
			return this.cdSpdRate;
		}
		if("moveSpd".equals(attrName)) {
			return this.moveSpd;
		}
		if("moveSpdRate".equals(attrName)) {
			return this.moveSpdRate;
		}
		if("atkSpd".equals(attrName)) {
			return this.atkSpd;
		}
		if("atkSpdRate".equals(attrName)) {
			return this.atkSpdRate;
		}
		if("dmgbl".equals(attrName)) {
			return this.dmgbl;
		}
		if("dmg".equals(attrName)) {
			return this.dmg;
		}
		if("llmaxRate".equals(attrName)) {
			return this.llmaxRate;
		}
		if("lldmgRate".equals(attrName)) {
			return this.lldmgRate;
		}
		if("mjmaxRate".equals(attrName)) {
			return this.mjmaxRate;
		}
		if("mjdmgRate".equals(attrName)) {
			return this.mjdmgRate;
		}
		if("zlmaxRate".equals(attrName)) {
			return this.zlmaxRate;
		}
		if("zldmgRate".equals(attrName)) {
			return this.zldmgRate;
		}
		if("woodDmg".equals(attrName)) {
			return this.woodDmg;
		}
		if("mineDmg".equals(attrName)) {
			return this.mineDmg;
		}
		if("llmax".equals(attrName)) {
			return this.llmax;
		}
		if("lldmg".equals(attrName)) {
			return this.lldmg;
		}
		if("mjmax".equals(attrName)) {
			return this.mjmax;
		}
		if("mjdmg".equals(attrName)) {
			return this.mjdmg;
		}
		if("zlmax".equals(attrName)) {
			return this.zlmax;
		}
		if("zldmg".equals(attrName)) {
			return this.zldmg;
		}
		if("battleRange".equals(attrName)) {
			return this.battleRange;
		}
		if("llpvedmg".equals(attrName)) {
			return this.llpvedmg;
		}
		if("mjpvedmg".equals(attrName)) {
			return this.mjpvedmg;
		}
		if("zlpvedmg".equals(attrName)) {
			return this.zlpvedmg;
		}
		if("pvedmg".equals(attrName)) {
			return this.pvedmg;
		}
		if("fovRange".equals(attrName)) {
			return this.fovRange;
		}
		if("reducecrit".equals(attrName)) {
			return this.reducecrit;
		}
		if("hpBrate".equals(attrName)) {
			return this.hpBrate;
		}
		if("atkBrate".equals(attrName)) {
			return this.atkBrate;
		}
		return 0;
	}
	@Override
	public boolean isEqual(IBattleAttr attr) {
		if(this.hp != attr.getHp()) {
			return false;
		}
		if(this.maxHp != attr.getMaxHp()) {
			return false;
		}
		if(this.atk != attr.getAtk()) {
			return false;
		}
		if(this.def != attr.getDef()) {
			return false;
		}
		if(this.hpRate != attr.getHpRate()) {
			return false;
		}
		if(this.atkRate != attr.getAtkRate()) {
			return false;
		}
		if(this.defRate != attr.getDefRate()) {
			return false;
		}
		if(this.dmgRate != attr.getDmgRate()) {
			return false;
		}
		if(this.hit != attr.getHit()) {
			return false;
		}
		if(this.dodge != attr.getDodge()) {
			return false;
		}
		if(this.crit != attr.getCrit()) {
			return false;
		}
		if(this.critDmgRate != attr.getCritDmgRate()) {
			return false;
		}
		if(this.vampireRate != attr.getVampireRate()) {
			return false;
		}
		if(this.reduceDmg != attr.getReduceDmg()) {
			return false;
		}
		if(this.reduceDmgRate != attr.getReduceDmgRate()) {
			return false;
		}
		if(this.cdSpdRate != attr.getCdSpdRate()) {
			return false;
		}
		if(this.moveSpd != attr.getMoveSpd()) {
			return false;
		}
		if(this.moveSpdRate != attr.getMoveSpdRate()) {
			return false;
		}
		if(this.atkSpd != attr.getAtkSpd()) {
			return false;
		}
		if(this.atkSpdRate != attr.getAtkSpdRate()) {
			return false;
		}
		if(this.dmgbl != attr.getDmgbl()) {
			return false;
		}
		if(this.dmg != attr.getDmg()) {
			return false;
		}
		if(this.llmaxRate != attr.getLlmaxRate()) {
			return false;
		}
		if(this.lldmgRate != attr.getLldmgRate()) {
			return false;
		}
		if(this.mjmaxRate != attr.getMjmaxRate()) {
			return false;
		}
		if(this.mjdmgRate != attr.getMjdmgRate()) {
			return false;
		}
		if(this.zlmaxRate != attr.getZlmaxRate()) {
			return false;
		}
		if(this.zldmgRate != attr.getZldmgRate()) {
			return false;
		}
		if(this.woodDmg != attr.getWoodDmg()) {
			return false;
		}
		if(this.mineDmg != attr.getMineDmg()) {
			return false;
		}
		if(this.llmax != attr.getLlmax()) {
			return false;
		}
		if(this.lldmg != attr.getLldmg()) {
			return false;
		}
		if(this.mjmax != attr.getMjmax()) {
			return false;
		}
		if(this.mjdmg != attr.getMjdmg()) {
			return false;
		}
		if(this.zlmax != attr.getZlmax()) {
			return false;
		}
		if(this.zldmg != attr.getZldmg()) {
			return false;
		}
		if(this.battleRange != attr.getBattleRange()) {
			return false;
		}
		if(this.llpvedmg != attr.getLlpvedmg()) {
			return false;
		}
		if(this.mjpvedmg != attr.getMjpvedmg()) {
			return false;
		}
		if(this.zlpvedmg != attr.getZlpvedmg()) {
			return false;
		}
		if(this.pvedmg != attr.getPvedmg()) {
			return false;
		}
		if(this.fovRange != attr.getFovRange()) {
			return false;
		}
		if(this.reducecrit != attr.getReducecrit()) {
			return false;
		}
		if(this.hpBrate != attr.getHpBrate()) {
			return false;
		}
		if(this.atkBrate != attr.getAtkBrate()) {
			return false;
		}
		return true;
	}
	public long getHp() {
		return hp;
	}
	public void setHp(long hp) {
		this.hp = hp;
	}
	public long getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}
	public long getAtk() {
		return atk;
	}
	public void setAtk(long atk) {
		this.atk = atk;
	}
	public long getDef() {
		return def;
	}
	public void setDef(long def) {
		this.def = def;
	}
	public long getHpRate() {
		return hpRate;
	}
	public void setHpRate(long hpRate) {
		this.hpRate = hpRate;
	}
	public long getAtkRate() {
		return atkRate;
	}
	public void setAtkRate(long atkRate) {
		this.atkRate = atkRate;
	}
	public long getDefRate() {
		return defRate;
	}
	public void setDefRate(long defRate) {
		this.defRate = defRate;
	}
	public long getDmgRate() {
		return dmgRate;
	}
	public void setDmgRate(long dmgRate) {
		this.dmgRate = dmgRate;
	}
	public long getHit() {
		return hit;
	}
	public void setHit(long hit) {
		this.hit = hit;
	}
	public long getDodge() {
		return dodge;
	}
	public void setDodge(long dodge) {
		this.dodge = dodge;
	}
	public long getCrit() {
		return crit;
	}
	public void setCrit(long crit) {
		this.crit = crit;
	}
	public long getCritDmgRate() {
		return critDmgRate;
	}
	public void setCritDmgRate(long critDmgRate) {
		this.critDmgRate = critDmgRate;
	}
	public long getVampireRate() {
		return vampireRate;
	}
	public void setVampireRate(long vampireRate) {
		this.vampireRate = vampireRate;
	}
	public long getReduceDmg() {
		return reduceDmg;
	}
	public void setReduceDmg(long reduceDmg) {
		this.reduceDmg = reduceDmg;
	}
	public long getReduceDmgRate() {
		return reduceDmgRate;
	}
	public void setReduceDmgRate(long reduceDmgRate) {
		this.reduceDmgRate = reduceDmgRate;
	}
	public long getCdSpdRate() {
		return cdSpdRate;
	}
	public void setCdSpdRate(long cdSpdRate) {
		this.cdSpdRate = cdSpdRate;
	}
	public long getMoveSpd() {
		return moveSpd;
	}
	public void setMoveSpd(long moveSpd) {
		this.moveSpd = moveSpd;
	}
	public long getMoveSpdRate() {
		return moveSpdRate;
	}
	public void setMoveSpdRate(long moveSpdRate) {
		this.moveSpdRate = moveSpdRate;
	}
	public long getAtkSpd() {
		return atkSpd;
	}
	public void setAtkSpd(long atkSpd) {
		this.atkSpd = atkSpd;
	}
	public long getAtkSpdRate() {
		return atkSpdRate;
	}
	public void setAtkSpdRate(long atkSpdRate) {
		this.atkSpdRate = atkSpdRate;
	}
	public long getDmgbl() {
		return dmgbl;
	}
	public void setDmgbl(long dmgbl) {
		this.dmgbl = dmgbl;
	}
	public long getDmg() {
		return dmg;
	}
	public void setDmg(long dmg) {
		this.dmg = dmg;
	}
	public long getLlmaxRate() {
		return llmaxRate;
	}
	public void setLlmaxRate(long llmaxRate) {
		this.llmaxRate = llmaxRate;
	}
	public long getLldmgRate() {
		return lldmgRate;
	}
	public void setLldmgRate(long lldmgRate) {
		this.lldmgRate = lldmgRate;
	}
	public long getMjmaxRate() {
		return mjmaxRate;
	}
	public void setMjmaxRate(long mjmaxRate) {
		this.mjmaxRate = mjmaxRate;
	}
	public long getMjdmgRate() {
		return mjdmgRate;
	}
	public void setMjdmgRate(long mjdmgRate) {
		this.mjdmgRate = mjdmgRate;
	}
	public long getZlmaxRate() {
		return zlmaxRate;
	}
	public void setZlmaxRate(long zlmaxRate) {
		this.zlmaxRate = zlmaxRate;
	}
	public long getZldmgRate() {
		return zldmgRate;
	}
	public void setZldmgRate(long zldmgRate) {
		this.zldmgRate = zldmgRate;
	}
	public long getWoodDmg() {
		return woodDmg;
	}
	public void setWoodDmg(long woodDmg) {
		this.woodDmg = woodDmg;
	}
	public long getMineDmg() {
		return mineDmg;
	}
	public void setMineDmg(long mineDmg) {
		this.mineDmg = mineDmg;
	}
	public long getLlmax() {
		return llmax;
	}
	public void setLlmax(long llmax) {
		this.llmax = llmax;
	}
	public long getLldmg() {
		return lldmg;
	}
	public void setLldmg(long lldmg) {
		this.lldmg = lldmg;
	}
	public long getMjmax() {
		return mjmax;
	}
	public void setMjmax(long mjmax) {
		this.mjmax = mjmax;
	}
	public long getMjdmg() {
		return mjdmg;
	}
	public void setMjdmg(long mjdmg) {
		this.mjdmg = mjdmg;
	}
	public long getZlmax() {
		return zlmax;
	}
	public void setZlmax(long zlmax) {
		this.zlmax = zlmax;
	}
	public long getZldmg() {
		return zldmg;
	}
	public void setZldmg(long zldmg) {
		this.zldmg = zldmg;
	}
	public long getBattleRange() {
		return battleRange;
	}
	public void setBattleRange(long battleRange) {
		this.battleRange = battleRange;
	}
	public long getLlpvedmg() {
		return llpvedmg;
	}
	public void setLlpvedmg(long llpvedmg) {
		this.llpvedmg = llpvedmg;
	}
	public long getMjpvedmg() {
		return mjpvedmg;
	}
	public void setMjpvedmg(long mjpvedmg) {
		this.mjpvedmg = mjpvedmg;
	}
	public long getZlpvedmg() {
		return zlpvedmg;
	}
	public void setZlpvedmg(long zlpvedmg) {
		this.zlpvedmg = zlpvedmg;
	}
	public long getPvedmg() {
		return pvedmg;
	}
	public void setPvedmg(long pvedmg) {
		this.pvedmg = pvedmg;
	}
	public long getFovRange() {
		return fovRange;
	}
	public void setFovRange(long fovRange) {
		this.fovRange = fovRange;
	}
	public long getReducecrit() {
		return reducecrit;
	}
	public void setReducecrit(long reducecrit) {
		this.reducecrit = reducecrit;
	}
	public long getHpBrate() {
		return hpBrate;
	}
	public void setHpBrate(long hpBrate) {
		this.hpBrate = hpBrate;
	}
	public long getAtkBrate() {
		return atkBrate;
	}
	public void setAtkBrate(long atkBrate) {
		this.atkBrate = atkBrate;
	}
	public pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr build() {
		pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr.Builder builder = pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr.newBuilder();
		builder.setHp(this.hp);
		builder.setMaxHp(this.maxHp);
		builder.setAtk(this.atk);
		builder.setDef(this.def);
		builder.setHpRate(this.hpRate);
		builder.setAtkRate(this.atkRate);
		builder.setDefRate(this.defRate);
		builder.setDmgRate(this.dmgRate);
		builder.setHit(this.hit);
		builder.setDodge(this.dodge);
		builder.setCrit(this.crit);
		builder.setCritDmgRate(this.critDmgRate);
		builder.setVampireRate(this.vampireRate);
		builder.setReduceDmg(this.reduceDmg);
		builder.setReduceDmgRate(this.reduceDmgRate);
		builder.setCdSpdRate(this.cdSpdRate);
		builder.setMoveSpd(this.moveSpd);
		builder.setMoveSpdRate(this.moveSpdRate);
		builder.setAtkSpd(this.atkSpd);
		builder.setAtkSpdRate(this.atkSpdRate);
		builder.setDmgbl(this.dmgbl);
		builder.setDmg(this.dmg);
		builder.setLlmaxRate(this.llmaxRate);
		builder.setLldmgRate(this.lldmgRate);
		builder.setMjmaxRate(this.mjmaxRate);
		builder.setMjdmgRate(this.mjdmgRate);
		builder.setZlmaxRate(this.zlmaxRate);
		builder.setZldmgRate(this.zldmgRate);
		builder.setWoodDmg(this.woodDmg);
		builder.setMineDmg(this.mineDmg);
		builder.setLlmax(this.llmax);
		builder.setLldmg(this.lldmg);
		builder.setMjmax(this.mjmax);
		builder.setMjdmg(this.mjdmg);
		builder.setZlmax(this.zlmax);
		builder.setZldmg(this.zldmg);
		builder.setBattleRange(this.battleRange);
		builder.setLlpvedmg(this.llpvedmg);
		builder.setMjpvedmg(this.mjpvedmg);
		builder.setZlpvedmg(this.zlpvedmg);
		builder.setPvedmg(this.pvedmg);
		builder.setFovRange(this.fovRange);
		builder.setReducecrit(this.reducecrit);
		builder.setHpBrate(this.hpBrate);
		builder.setAtkBrate(this.atkBrate);
		return builder.build();
	}
}