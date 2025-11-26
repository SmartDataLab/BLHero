/**
 * 
 */
package com.xiugou.x1.battle.sprite.skill;

import java.util.List;

import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.config.BuffTrigger;
import com.xiugou.x1.battle.constant.BuffTarget;
import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public class BuffTriggerManager {
	
	private static Logger logger = LoggerFactory.getLogger(BuffTriggerManager.class);
	
	/**
	 * 出手前不用判断buff是否已经添加到对应的精灵身上，因为出手前的判定只在第一个打击帧触发
	 * @param context
	 * @param sprite
	 * @param skill
	 * @param buffTriggers
	 * @param targets
	 */
	public static void beforeRelease(BattleContext context, Sprite sprite, Skill<?> skill, List<Sprite> targets) {
		for(BuffTrigger buffTrigger : skill.getSkillCfg().getBuffs0()) {
			if(buffTrigger.getTiming() != 1) {
				continue;
			}
			if(context.randomRate() > buffTrigger.getRate()) {
				continue;
			}
			if(buffTrigger.getTargetType() == BuffTarget.SELF.getValue()) {
				sprite.addBuffPush(context, sprite, buffTrigger.getBuffId());
				
			} else if(buffTrigger.getTargetType() == BuffTarget.TARGET.getValue()) {
				for(Sprite target : targets) {
					target.addBuffPush(context, sprite, buffTrigger.getBuffId());
				}
				
			} else if(buffTrigger.getTargetType() == BuffTarget.FRIEND.getValue()) {
				addBuffToFriend(context, sprite, context.getFriends(sprite), buffTrigger.getNum(), buffTrigger.getBuffId());
				
			} else if(buffTrigger.getTargetType() == BuffTarget.RANDOM_FRIEND.getValue()) {
				List<Sprite> list = context.getFriends(sprite);
				context.shuffle(list);
				addBuffToFriend(context, sprite, list, buffTrigger.getNum(), buffTrigger.getBuffId());
				
			} else {
				logger.error("技能{}触发BUFF异常，未知的目标类型{}，配置数据{}", skill.getSkillId(), buffTrigger.getTargetType(), GsonUtil.toJson(buffTrigger));
			}
		}
	}
	
	private static void addBuffToFriend(BattleContext context, Sprite sprite, List<Sprite> friends, int friendNum, int buffId) {
		int count = 0;
		for(Sprite friend : friends) {
			if(friendNum == 0) {
				friend.addBuffPush(context, sprite, buffId);
			} else {
				if(count < friendNum) {
					friend.addBuffPush(context, sprite, buffId);
					count += 1;
				} else {
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param sprite
	 * @param skill
	 * @param target
	 */
	public static void onHitedTarget(BattleContext context, Sprite sprite, Skill<?> skill, Sprite target) {
		if(!skill.canAddBuff(target)) {
			return;
		}
		for(BuffTrigger buffTrigger : skill.getSkillCfg().getBuffs0()) {
			if(buffTrigger.getTiming() != 2) {
				continue;
			}
			if(buffTrigger.getTargetType() != BuffTarget.TARGET.getValue()) {
				continue;
			}
			if(context.randomRate() > buffTrigger.getRate()) {
				continue;
			}
			target.addBuffPush(context, sprite, buffTrigger.getBuffId());
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param sprite
	 * @param skill
	 * @param target
	 */
	public static void onCuredTarget(BattleContext context, Sprite sprite, Skill<?> skill, Sprite target) {
		if(!skill.canAddBuff(target)) {
			return;
		}
		for(BuffTrigger buffTrigger : skill.getSkillCfg().getBuffs0()) {
			if(buffTrigger.getTiming() != 2) {
				continue;
			}
			if(buffTrigger.getTargetType() != BuffTarget.TARGET.getValue()) {
				continue;
			}
			if(context.randomRate() > buffTrigger.getRate()) {
				continue;
			}
			target.addBuffPush(context, sprite, buffTrigger.getBuffId());
		}
	}
	
	
	/**
	 * 第一段伤害后触发
	 * @param context
	 * @param sprite
	 * @param skill
	 */
	public static void afterRelease(BattleContext context, Sprite sprite, Skill<?> skill) {
		for(BuffTrigger buffTrigger : skill.getSkillCfg().getBuffs0()) {
			if(buffTrigger.getTiming() != 2) {
				continue;
			}
			if(context.randomRate() > buffTrigger.getRate()) {
				continue;
			}
			if(buffTrigger.getTargetType() == BuffTarget.SELF.getValue()) {
				sprite.addBuffPush(context, sprite, buffTrigger.getBuffId());
				
			} else if(buffTrigger.getTargetType() == BuffTarget.TARGET.getValue()) {
				//不用处理
			} else if(buffTrigger.getTargetType() == BuffTarget.FRIEND.getValue()) {
				
				addBuffToFriend(context, sprite, context.getFriends(sprite), buffTrigger.getNum(), buffTrigger.getBuffId());
				
			} else if(buffTrigger.getTargetType() == BuffTarget.RANDOM_FRIEND.getValue()) {
				List<Sprite> list = context.getFriends(sprite);
				context.shuffle(list);
				addBuffToFriend(context, sprite, list, buffTrigger.getNum(), buffTrigger.getBuffId());
			} else {
				logger.error("技能{}触发BUFF异常，未知的目标类型{}，配置数据{}", skill.getSkillId(), buffTrigger.getTargetType(), GsonUtil.toJson(buffTrigger));
			}
		}
	}
}
