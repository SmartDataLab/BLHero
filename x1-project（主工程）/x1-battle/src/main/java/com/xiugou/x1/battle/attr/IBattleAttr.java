
package com.xiugou.x1.battle.attr;


public interface IBattleAttr {
	default void merge(IBattleAttr attr) {
	}
	default void topMerge(IBattleAttr attr) {
	}
	default java.util.Map<String, Long> attrMap() {
		return null;
	}
	default java.util.Map<String, Long> notZeroAttrMap() {
		return null;
	}
	/**
	 * 当前血量
	 */
	default long getHp() {
		return 0;
	}
	/**
	 * 生命
	 */
	default long getMaxHp() {
		return 0;
	}
	/**
	 * 攻击
	 */
	default long getAtk() {
		return 0;
	}
	/**
	 * 防御
	 */
	default long getDef() {
		return 0;
	}
	/**
	 * 血量加成
	 */
	default long getHpRate() {
		return 0;
	}
	/**
	 * 攻击加成
	 */
	default long getAtkRate() {
		return 0;
	}
	/**
	 * 防御加成
	 */
	default long getDefRate() {
		return 0;
	}
	/**
	 * 伤害加成
	 */
	default long getDmgRate() {
		return 0;
	}
	/**
	 * 命中
	 */
	default long getHit() {
		return 0;
	}
	/**
	 * 闪避
	 */
	default long getDodge() {
		return 0;
	}
	/**
	 * 暴击
	 */
	default long getCrit() {
		return 0;
	}
	/**
	 * 暴击伤害
	 */
	default long getCritDmgRate() {
		return 0;
	}
	/**
	 * 吸血
	 */
	default long getVampireRate() {
		return 0;
	}
	/**
	 * 减伤固定值
	 */
	default long getReduceDmg() {
		return 0;
	}
	/**
	 * 伤害减免
	 */
	default long getReduceDmgRate() {
		return 0;
	}
	/**
	 * 冷却时间
	 */
	default long getCdSpdRate() {
		return 0;
	}
	/**
	 * 移动速度
	 */
	default long getMoveSpd() {
		return 0;
	}
	/**
	 * 移动速度加成
	 */
	default long getMoveSpdRate() {
		return 0;
	}
	/**
	 * 攻击速度
	 */
	default long getAtkSpd() {
		return 0;
	}
	/**
	 * 攻击速度加成
	 */
	default long getAtkSpdRate() {
		return 0;
	}
	/**
	 * 技能伤害加成
	 */
	default long getDmgbl() {
		return 0;
	}
	/**
	 * 增伤固定值
	 */
	default long getDmg() {
		return 0;
	}
	/**
	 * 人族生命加成
	 */
	default long getLlmaxRate() {
		return 0;
	}
	/**
	 * 人族伤害加成
	 */
	default long getLldmgRate() {
		return 0;
	}
	/**
	 * 灵族生命加成
	 */
	default long getMjmaxRate() {
		return 0;
	}
	/**
	 * 灵族伤害加成
	 */
	default long getMjdmgRate() {
		return 0;
	}
	/**
	 * 仙族生命加成
	 */
	default long getZlmaxRate() {
		return 0;
	}
	/**
	 * 仙族伤害加成
	 */
	default long getZldmgRate() {
		return 0;
	}
	/**
	 * 伐木伤害
	 */
	default long getWoodDmg() {
		return 0;
	}
	/**
	 * 挖矿伤害
	 */
	default long getMineDmg() {
		return 0;
	}
	/**
	 * 人族生命固定值
	 */
	default long getLlmax() {
		return 0;
	}
	/**
	 * 人族攻击固定值
	 */
	default long getLldmg() {
		return 0;
	}
	/**
	 * 灵族生命固定值
	 */
	default long getMjmax() {
		return 0;
	}
	/**
	 * 灵族攻击固定值
	 */
	default long getMjdmg() {
		return 0;
	}
	/**
	 * 仙族生命固定值
	 */
	default long getZlmax() {
		return 0;
	}
	/**
	 * 仙族攻击固定值
	 */
	default long getZldmg() {
		return 0;
	}
	/**
	 * 进战范围
	 */
	default long getBattleRange() {
		return 0;
	}
	/**
	 * 人族伙伴对怪物伤害加成
	 */
	default long getLlpvedmg() {
		return 0;
	}
	/**
	 * 灵族伙伴对怪物伤害加成
	 */
	default long getMjpvedmg() {
		return 0;
	}
	/**
	 * 仙族伙伴对怪物伤害加成
	 */
	default long getZlpvedmg() {
		return 0;
	}
	/**
	 * 对怪物伤害加成
	 */
	default long getPvedmg() {
		return 0;
	}
	/**
	 * 视野范围
	 */
	default long getFovRange() {
		return 0;
	}
	/**
	 * 暴击抵抗
	 */
	default long getReducecrit() {
		return 0;
	}
	/**
	 * 某养成系统内基础生命加成
	 */
	default long getHpBrate() {
		return 0;
	}
	/**
	 * 某养成系统内基础攻击加成
	 */
	default long getAtkBrate() {
		return 0;
	}
	/**
	 * 当前血量
	 */
	default void setHp(long hp) {
	}
	/**
	 * 生命
	 */
	default void setMaxHp(long maxHp) {
	}
	/**
	 * 攻击
	 */
	default void setAtk(long atk) {
	}
	/**
	 * 防御
	 */
	default void setDef(long def) {
	}
	/**
	 * 血量加成
	 */
	default void setHpRate(long hpRate) {
	}
	/**
	 * 攻击加成
	 */
	default void setAtkRate(long atkRate) {
	}
	/**
	 * 防御加成
	 */
	default void setDefRate(long defRate) {
	}
	/**
	 * 伤害加成
	 */
	default void setDmgRate(long dmgRate) {
	}
	/**
	 * 命中
	 */
	default void setHit(long hit) {
	}
	/**
	 * 闪避
	 */
	default void setDodge(long dodge) {
	}
	/**
	 * 暴击
	 */
	default void setCrit(long crit) {
	}
	/**
	 * 暴击伤害
	 */
	default void setCritDmgRate(long critDmgRate) {
	}
	/**
	 * 吸血
	 */
	default void setVampireRate(long vampireRate) {
	}
	/**
	 * 减伤固定值
	 */
	default void setReduceDmg(long reduceDmg) {
	}
	/**
	 * 伤害减免
	 */
	default void setReduceDmgRate(long reduceDmgRate) {
	}
	/**
	 * 冷却时间
	 */
	default void setCdSpdRate(long cdSpdRate) {
	}
	/**
	 * 移动速度
	 */
	default void setMoveSpd(long moveSpd) {
	}
	/**
	 * 移动速度加成
	 */
	default void setMoveSpdRate(long moveSpdRate) {
	}
	/**
	 * 攻击速度
	 */
	default void setAtkSpd(long atkSpd) {
	}
	/**
	 * 攻击速度加成
	 */
	default void setAtkSpdRate(long atkSpdRate) {
	}
	/**
	 * 技能伤害加成
	 */
	default void setDmgbl(long dmgbl) {
	}
	/**
	 * 增伤固定值
	 */
	default void setDmg(long dmg) {
	}
	/**
	 * 人族生命加成
	 */
	default void setLlmaxRate(long llmaxRate) {
	}
	/**
	 * 人族伤害加成
	 */
	default void setLldmgRate(long lldmgRate) {
	}
	/**
	 * 灵族生命加成
	 */
	default void setMjmaxRate(long mjmaxRate) {
	}
	/**
	 * 灵族伤害加成
	 */
	default void setMjdmgRate(long mjdmgRate) {
	}
	/**
	 * 仙族生命加成
	 */
	default void setZlmaxRate(long zlmaxRate) {
	}
	/**
	 * 仙族伤害加成
	 */
	default void setZldmgRate(long zldmgRate) {
	}
	/**
	 * 伐木伤害
	 */
	default void setWoodDmg(long woodDmg) {
	}
	/**
	 * 挖矿伤害
	 */
	default void setMineDmg(long mineDmg) {
	}
	/**
	 * 人族生命固定值
	 */
	default void setLlmax(long llmax) {
	}
	/**
	 * 人族攻击固定值
	 */
	default void setLldmg(long lldmg) {
	}
	/**
	 * 灵族生命固定值
	 */
	default void setMjmax(long mjmax) {
	}
	/**
	 * 灵族攻击固定值
	 */
	default void setMjdmg(long mjdmg) {
	}
	/**
	 * 仙族生命固定值
	 */
	default void setZlmax(long zlmax) {
	}
	/**
	 * 仙族攻击固定值
	 */
	default void setZldmg(long zldmg) {
	}
	/**
	 * 进战范围
	 */
	default void setBattleRange(long battleRange) {
	}
	/**
	 * 人族伙伴对怪物伤害加成
	 */
	default void setLlpvedmg(long llpvedmg) {
	}
	/**
	 * 灵族伙伴对怪物伤害加成
	 */
	default void setMjpvedmg(long mjpvedmg) {
	}
	/**
	 * 仙族伙伴对怪物伤害加成
	 */
	default void setZlpvedmg(long zlpvedmg) {
	}
	/**
	 * 对怪物伤害加成
	 */
	default void setPvedmg(long pvedmg) {
	}
	/**
	 * 视野范围
	 */
	default void setFovRange(long fovRange) {
	}
	/**
	 * 暴击抵抗
	 */
	default void setReducecrit(long reducecrit) {
	}
	/**
	 * 某养成系统内基础生命加成
	 */
	default void setHpBrate(long hpBrate) {
	}
	/**
	 * 某养成系统内基础攻击加成
	 */
	default void setAtkBrate(long atkBrate) {
	}
	default void multiple(int multiple) {
	}
	default void dmultiple(double multiple) {
	}
	default void add(String attrName, long value) {
	}
	default void addById(int attrId, long value) {
	}
	default void subById(int attrId, long value) {
	}
	default long get(String attrName) {
		return 0;
	}
	default boolean isEqual(IBattleAttr attr) {
		return false;
	}
}