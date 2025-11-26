/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

import com.xiugou.x1.battle.config.ISkillLevelConfig;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public abstract class Skill<S> {

	protected ISkillLevelConfig skillCfg;
	protected Object struct;
	
	private Class<S> structClazz;
	
	//本次技能过程中命中过添加多BUFF的单位
	protected final Set<Sprite> buffedTargets = new HashSet<>();
	
	@SuppressWarnings("unchecked")
	public Skill() {
		//S结构的取值
		Class<?> currClz = this.getClass();
		while(currClz != null) {
			if(currClz.getGenericSuperclass() instanceof ParameterizedType) {
				ParameterizedType ptype = (ParameterizedType)currClz.getGenericSuperclass();
				if(ptype.getActualTypeArguments().length == 1) {
					structClazz = (Class<S>) ptype.getActualTypeArguments()[0];
					break;
				}
			}
			currClz = currClz.getSuperclass();
		}
	}

	public ISkillLevelConfig getSkillCfg() {
		return skillCfg;
	}

	public void setSkillCfg(ISkillLevelConfig skillCfg) {
		this.skillCfg = skillCfg;
	}
	
	public final int getSkillId() {
		return skillCfg.getSkillId();
	}
	
	@SuppressWarnings("unchecked")
	public S getStruct() {
		if(struct == null) {
//			struct = GsonUtil.parseJson(skillCfg.getServerParams(), structClazz);
		}
		return (S)struct;
	}

	public Set<Sprite> getBuffedTargets() {
		return buffedTargets;
	}
	
	public boolean canAddBuff(Sprite sprite) {
		return buffedTargets.add(sprite);
	}
	
	public static void main(String[] args) {
		Set<Integer> buffedTargets = new HashSet<>();
		System.out.println(buffedTargets.add(1));
		System.out.println(buffedTargets.add(1));
	}
}
