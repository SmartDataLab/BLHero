/**
 * 
 */
package com.xiugou.x1.battle.sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.attr.AttrEnum;
import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.attr.IBattleAttr;
import com.xiugou.x1.battle.buff.Buff;
import com.xiugou.x1.battle.buff.BuffEffect;
import com.xiugou.x1.battle.buff.status.AbstractStatus;
import com.xiugou.x1.battle.buff.status.StatusEnum;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.config.IBuffConfig;
import com.xiugou.x1.battle.constant.SpriteType;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.result.sprite.SpriteBuffEvent;
import com.xiugou.x1.battle.result.sprite.SpriteBuffRemoveEvent;
import com.xiugou.x1.battle.result.sprite.SpriteCureEvent;
import com.xiugou.x1.battle.result.sprite.SpriteInjureEvent;
import com.xiugou.x1.battle.result.sprite.SpriteRebornEvent;
import com.xiugou.x1.battle.sprite.skill.ActiveSkill;
import com.xiugou.x1.battle.sprite.skill.BuffCureResult;
import com.xiugou.x1.battle.sprite.skill.BuffDamageResult;
import com.xiugou.x1.battle.sprite.skill.PassiveSkill;
import com.xiugou.x1.battle.sprite.skill.SkillCureResult;
import com.xiugou.x1.battle.sprite.skill.SkillDamageResult;
import com.xiugou.x1.battle.translate.BattleTranslate;

import pb.xiugou.x1.protobuf.battle.Battle.PbSprite;

/**
 * @author YY
 *
 */
public class Sprite {

	private static Logger logger = LoggerFactory.getLogger(Sprite.class);

	// 精灵ID
	protected final Integer id;
	//怪物类型时，是怪物配置ID，英雄类型时，是英雄标识
	protected int identity;
	protected int level;
	protected TeamSide side;
	// 力量、敏捷、智力
	protected int element;

	// 基础属性
	protected IBattleSprite spriteEntity;
	
	protected final BattleAttr battleAttr = new BattleAttr();
	// 战斗开始时需要额外增加的属性
//	protected final IBattleAttr tempBattleAttr = new BattleAttr();

	protected long hp;

	protected ActiveSkill<?> normalSkill;
	protected final Map<Integer, ActiveSkill<?>> activeSkillMap = new HashMap<>();
	protected final List<PassiveSkill<?>> passiveSkills = new ArrayList<>();

	protected final List<Buff> buffs = new LinkedList<>();
	protected final List<AbstractStatus> status = new LinkedList<>();
	// 精灵类型
	public final SpriteType spriteType;

	protected int configId;

	// 是否活着，当alive=true，但hp<=0时表示单位刚刚死了
	// 当alive=false时，需要进行复活处理
	protected boolean alive = true;
	// 重生时间
	protected long rebornTime;

	// 对于怪物来说的刷新方式1世界坐标附近区域随机刷新，2世界坐标定点刷新
	protected byte ctype;
	// 地图文件中的世界坐标
	protected int cx;
	protected int cy;

	// 精灵做在的格子ID，TODO 判断圆形当方形来处理
	protected int x;
	protected int y;
	protected int fogArea;
	
	//是不是在战斗中的状态
	protected boolean inBattle;
	
	public Sprite(int id, SpriteType spriteType) {
		this.id = id;
		this.spriteType = spriteType;
	}

	public Integer getId() {
		return id;
	}

	public boolean checkReborn(BattleContext context) {
		if (rebornTime <= 0) {
			return false;
		}
		if (context.getNow() < rebornTime) {
			return false;
		}
		rebornTime = 0;
		this.hp = this.battleAttr.getMaxHp();
		this.alive = true;
		
		SpriteRebornEvent event = new SpriteRebornEvent();
		event.setSprite(this);
		context.addEvent(event);
		return true;
	}

	public PbSprite build() {
		PbSprite.Builder builder = PbSprite.newBuilder();
		builder.setId(id);
		builder.setIdentity(identity);
		builder.setTypeValue(this.spriteType.getValue());
		builder.setLevel(level);
		builder.setRebornTime(rebornTime);
		builder.setCtype(ctype);
		builder.setCx(cx);
		builder.setCy(cy);
		builder.setBattleAttr(BattleTranslate.build(this.battleAttr));
		builder.setConfigId(configId);
		builder.setFogArea(fogArea);
		return builder.build();
	}

	public long getAttrValue(AttrEnum attrEnum, long now) {
		return attrEnum.getAttrValue(this) + getBuffAttr(attrEnum, now);
	}

	private long getBuffAttr(AttrEnum attrEnum, long now) {
		long buffValue = 0;
		//BUFF上的属性值
		for (Buff buff : buffs) {
			if(buff.isEnd(now)) {
				continue;
			}
			for(Attr attr : buff.getBuffConfig().getAttrParams()) {
 				if(attr.getAttrId() == attrEnum.getAttrId()) {
 					buffValue += attr.getValue();
 				}
			}
		}
		return buffValue;
	}

	public long getHp() {
		return hp;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}

	public int getIdentity() {
		return identity;
	}

	public void setIdentity(int identity) {
		this.identity = identity;
	}

	public TeamSide getSide() {
		return side;
	}

	public void setSide(TeamSide side) {
		this.side = side;
	}

	public int getElement() {
		return element;
	}

	public void setElement(int element) {
		this.element = element;
	}

	public Map<Integer, ActiveSkill<?>> getActiveSkillMap() {
		return activeSkillMap;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isDeath() {
		return hp <= 0;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	/**
	 * 添加BUFF
	 * 
	 * @param context
	 * @param buffIds
	 * @param now
	 * @param slient
	 *            是否需要推送前端结果
	 */
	private void addBuffs(BattleContext context, Sprite caster, List<Integer> buffIds, boolean slient) {
		if (buffIds.isEmpty()) {
			return;
		}
		for (Integer buffId : buffIds) {
			IBuffConfig buffConfig = context.getBuffConfigCache().getConfig(buffId);
			if (buffConfig == null) {
				logger.error("未找到ID为{}的buff配置", buffId);
				continue;
			}
//			if (buffConfig.getTriggerRate() == -1) {
//				// 强制命中
//			} else {
//				if (context.randomRate() <= buffConfig.getTriggerRate()) {
//					// 命中了
//				} else {
//					// BUFF没有命中
//					continue;
//				}
//			}
			Buff buff = createBuff(context, buffConfig);
			if (buff == null) {
				continue;
			}
			buff.setCaster(caster);
			buff.setOwner(this);
			this.buffs.add(buff);
			if (!slient) {
				SpriteBuffEvent event = new SpriteBuffEvent();
				event.setSpriteId(this.id);
				event.setBuff(buff);
				context.addEvent(event);
			}
		}
	}
	
	public void addBuffPush(BattleContext context, Sprite caster, int buffId) {
		this.addBuff(context, caster, buffId, false);
	}
	
	private void addBuff(BattleContext context, Sprite caster, int buffId, boolean slient) {
		IBuffConfig buffConfig = context.getBuffConfigCache().getConfig(buffId);
		if (buffConfig == null) {
			logger.error("未找到ID为{}的buff配置", buffId);
			return;
		}
		Buff buff = createBuff(context, buffConfig);
		if (buff == null) {
			return;
		}
		if(!buff.needAddToTarget(context, this)) {
			return;
		}
		buff.setCaster(caster);
		buff.setOwner(this);
		this.buffs.add(buff);
		buff.onAddToTarget(context, caster, this);
		if (!slient) {
			SpriteBuffEvent event = new SpriteBuffEvent();
			event.setSpriteId(this.id);
			event.setBuff(buff);
			context.addEvent(event);
		}
	}
	
	public void checkBuffEnd(BattleContext context) {
		int index = 0;
		while(index < buffs.size()) {
			Buff buff = buffs.get(0);
			if(!buff.isEnd(context.getNow())) {
				index += 1;
			} else {
				buffs.remove(index);
				SpriteBuffRemoveEvent event = new SpriteBuffRemoveEvent();
				event.setSpriteId(this.id);
				event.setBuff(buff);
				context.addEvent(event);
			}
		}
	}
	

	public static Buff createBuff(BattleContext context, IBuffConfig buffConfig) {
		BuffEffect buffEffect = BuffEffect.valueOf(buffConfig);
		if (buffEffect == null) {
			return null;
		}
		Buff buff = buffEffect.supplier.get();
		buff.setId(context.getBuffIdGen().incrementAndGet());
		buff.setBuffConfig(buffConfig);
		if (buffConfig.getLastTime() == -1) {
			buff.setEndTime(-1);
		} else {
			buff.setEndTime(context.getNow() + buffConfig.getLastTime());
		}
		return buff;
	}

	// 添加buff但不推送前端
	public void addBuffsSlient(BattleContext context, Sprite caster, List<Integer> buffIds) {
		this.addBuffs(context, caster, buffIds, true);
	}

	public void addBuffsPush(BattleContext context, Sprite caster, List<Integer> buffIds) {
		this.addBuffs(context, caster, buffIds, false);
	}

	public void takeSkillDamage(BattleContext context, SkillDamageResult result) {
		if (result.isHit()) {
			this.setHp(this.getHp() - result.getDamage());
		}
		
		SpriteInjureEvent event = new SpriteInjureEvent();
		event.setSpriteId(this.getId());
		event.setDamageResult(result);
		event.setHp(this.getHp());
		context.addEvent(event);
		
		if(result.isHit()) {
			// 精灵受到伤害后如果还存活，才进行技能命中后的后续处理
			if (!this.isDeath()) {
				// TODO 触发命中添加buff
				result.getSkill().onAttackHited(context, result.getAttacker(), result.getDefender());
			}
		}
	}
	
	public void takeBuffDamage(BattleContext context, BuffDamageResult result) {
		this.setHp(this.getHp() - result.getDamage());
		
		SpriteInjureEvent event = new SpriteInjureEvent();
		event.setSpriteId(this.getId());
		event.setDamageResult(result);
		event.setHp(this.getHp());
		context.addEvent(event);
	}
	
	
	public void takeSkillCure(BattleContext context, SkillCureResult result) {
		this.setHp(this.getHp() + result.getCure());
		
		SpriteCureEvent event = new SpriteCureEvent();
		event.setSpriteId(this.getId());
		event.setCure(result.getCure());
		event.setHp(this.getHp());
		context.addEvent(event);
	}
	
	public void takeBuffCure(BattleContext context, BuffCureResult result) {
		this.setHp(this.getHp() + result.getCure());
		
		SpriteCureEvent event = new SpriteCureEvent();
		event.setSpriteId(this.getId());
		event.setCure(result.getCure());
		event.setHp(this.getHp());
		context.addEvent(event);
	}
	

	public void afterNormalSkill() {
	}

	public long getRebornTime() {
		return rebornTime;
	}

	public void setRebornTime(long rebornTime) {
		this.rebornTime = rebornTime;
	}

	public byte getCtype() {
		return ctype;
	}

	public void setCtype(byte ctype) {
		this.ctype = ctype;
	}

	public int getCx() {
		return cx;
	}

	public void setCx(int cx) {
		this.cx = cx;
	}

	public int getCy() {
		return cy;
	}

	public void setCy(int cy) {
		this.cy = cy;
	}

	public boolean isInBattle() {
		return inBattle;
	}

	public void setInBattle(boolean inBattle) {
		this.inBattle = inBattle;
	}

	public List<Buff> getBuffs() {
		return buffs;
	}

	public IBattleSprite getSpriteEntity() {
		return spriteEntity;
	}

	public void setSpriteEntity(IBattleSprite spriteEntity) {
		this.spriteEntity = spriteEntity;
	}
	
	public IBattleAttr getBattleAttr() {
		return battleAttr;
	}

	public SpriteType getSpriteType() {
		return spriteType;
	}

	public List<AbstractStatus> getStatus() {
		return status;
	}

	public ActiveSkill<?> getNormalSkill() {
		return normalSkill;
	}
	
	public List<Buff> getBuffs(BuffEffect buffEffect) {
		List<Buff> result = new ArrayList<>();
		for(Buff buff : this.buffs) {
			if(buff.buffEffect() == buffEffect) {
				result.add(buff);
			}
		}
		return result;
	}
	
	/**
	 * 改变状态
	 * @param battle
	 * @param status
	 * @param time
	 */
	public final void changeStatus(BattleContext context, StatusEnum status, int time) {
		AbstractStatus abstractStatus = status.create();
		if(abstractStatus == null) {
			return;
		}
		abstractStatus.setTime(context.getNow() + time);
		this.status.add(abstractStatus);
	}
	
	public final void changeStatusWithEndTime(BattleContext context, StatusEnum status, long endTime) {
		AbstractStatus abstractStatus = status.create();
		if(abstractStatus == null) {
			return;
		}
		abstractStatus.setTime(endTime);
		this.status.add(abstractStatus);
	}

	public void setNormalSkill(ActiveSkill<?> normalSkill) {
		this.normalSkill = normalSkill;
	}

	public List<PassiveSkill<?>> getPassiveSkills() {
		return passiveSkills;
	}

//	public IBattleAttr getTempBattleAttr() {
//		return tempBattleAttr;
//	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getFogArea() {
		return fogArea;
	}

	public void setFogArea(int fogArea) {
		this.fogArea = fogArea;
	}
}
