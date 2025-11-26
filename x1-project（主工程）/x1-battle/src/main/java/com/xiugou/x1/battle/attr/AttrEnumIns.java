
package com.xiugou.x1.battle.attr;

import com.xiugou.x1.battle.sprite.Sprite;

public interface AttrEnumIns {
	public static final AttrEnum hp = new AttrEnum(201, "hp") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getHp();
		}
	};
	public static final AttrEnum maxHp = new AttrEnum(202, "maxHp") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMaxHp();
		}
	};
	public static final AttrEnum atk = new AttrEnum(203, "atk") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getAtk();
		}
	};
	public static final AttrEnum def = new AttrEnum(204, "def") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getDef();
		}
	};
	public static final AttrEnum hpRate = new AttrEnum(205, "hpRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getHpRate();
		}
	};
	public static final AttrEnum atkRate = new AttrEnum(206, "atkRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getAtkRate();
		}
	};
	public static final AttrEnum defRate = new AttrEnum(207, "defRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getDefRate();
		}
	};
	public static final AttrEnum dmgRate = new AttrEnum(208, "dmgRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getDmgRate();
		}
	};
	public static final AttrEnum hit = new AttrEnum(209, "hit") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getHit();
		}
	};
	public static final AttrEnum dodge = new AttrEnum(210, "dodge") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getDodge();
		}
	};
	public static final AttrEnum crit = new AttrEnum(211, "crit") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getCrit();
		}
	};
	public static final AttrEnum critDmgRate = new AttrEnum(212, "critDmgRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getCritDmgRate();
		}
	};
	public static final AttrEnum vampireRate = new AttrEnum(213, "vampireRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getVampireRate();
		}
	};
	public static final AttrEnum reduceDmg = new AttrEnum(214, "reduceDmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getReduceDmg();
		}
	};
	public static final AttrEnum reduceDmgRate = new AttrEnum(215, "reduceDmgRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getReduceDmgRate();
		}
	};
	public static final AttrEnum cdSpdRate = new AttrEnum(216, "cdSpdRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getCdSpdRate();
		}
	};
	public static final AttrEnum moveSpd = new AttrEnum(217, "moveSpd") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMoveSpd();
		}
	};
	public static final AttrEnum moveSpdRate = new AttrEnum(218, "moveSpdRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMoveSpdRate();
		}
	};
	public static final AttrEnum atkSpd = new AttrEnum(219, "atkSpd") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getAtkSpd();
		}
	};
	public static final AttrEnum atkSpdRate = new AttrEnum(220, "atkSpdRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getAtkSpdRate();
		}
	};
	public static final AttrEnum dmgbl = new AttrEnum(221, "dmgbl") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getDmgbl();
		}
	};
	public static final AttrEnum dmg = new AttrEnum(222, "dmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getDmg();
		}
	};
	public static final AttrEnum llmaxRate = new AttrEnum(223, "llmaxRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getLlmaxRate();
		}
	};
	public static final AttrEnum lldmgRate = new AttrEnum(224, "lldmgRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getLldmgRate();
		}
	};
	public static final AttrEnum mjmaxRate = new AttrEnum(225, "mjmaxRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMjmaxRate();
		}
	};
	public static final AttrEnum mjdmgRate = new AttrEnum(226, "mjdmgRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMjdmgRate();
		}
	};
	public static final AttrEnum zlmaxRate = new AttrEnum(227, "zlmaxRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getZlmaxRate();
		}
	};
	public static final AttrEnum zldmgRate = new AttrEnum(228, "zldmgRate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getZldmgRate();
		}
	};
	public static final AttrEnum woodDmg = new AttrEnum(229, "woodDmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getWoodDmg();
		}
	};
	public static final AttrEnum mineDmg = new AttrEnum(230, "mineDmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMineDmg();
		}
	};
	public static final AttrEnum llmax = new AttrEnum(231, "llmax") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getLlmax();
		}
	};
	public static final AttrEnum lldmg = new AttrEnum(232, "lldmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getLldmg();
		}
	};
	public static final AttrEnum mjmax = new AttrEnum(233, "mjmax") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMjmax();
		}
	};
	public static final AttrEnum mjdmg = new AttrEnum(234, "mjdmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMjdmg();
		}
	};
	public static final AttrEnum zlmax = new AttrEnum(235, "zlmax") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getZlmax();
		}
	};
	public static final AttrEnum zldmg = new AttrEnum(236, "zldmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getZldmg();
		}
	};
	public static final AttrEnum battleRange = new AttrEnum(237, "battleRange") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getBattleRange();
		}
	};
	public static final AttrEnum llpvedmg = new AttrEnum(238, "llpvedmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getLlpvedmg();
		}
	};
	public static final AttrEnum mjpvedmg = new AttrEnum(239, "mjpvedmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getMjpvedmg();
		}
	};
	public static final AttrEnum zlpvedmg = new AttrEnum(240, "zlpvedmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getZlpvedmg();
		}
	};
	public static final AttrEnum pvedmg = new AttrEnum(241, "pvedmg") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getPvedmg();
		}
	};
	public static final AttrEnum fovRange = new AttrEnum(242, "fovRange") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getFovRange();
		}
	};
	public static final AttrEnum reducecrit = new AttrEnum(243, "reducecrit") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getReducecrit();
		}
	};
	public static final AttrEnum hpBrate = new AttrEnum(301, "hpBrate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getHpBrate();
		}
	};
	public static final AttrEnum atkBrate = new AttrEnum(302, "atkBrate") {
		public long getAttrValue(Sprite sprite) {
			return sprite.getBattleAttr().getAtkBrate();
		}
	};
}