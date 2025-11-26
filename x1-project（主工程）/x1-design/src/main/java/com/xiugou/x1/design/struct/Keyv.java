/**
 * 
 */
package com.xiugou.x1.design.struct;

import org.gaming.ruler.util.DropUtil.IDrop;

import com.xiugou.x1.battle.config.IBattleKeyV;
import com.xiugou.x1.battle.config.IMonsterKeyV;
import com.xiugou.x1.battle.config.ISkillCfg;

/**
 * @author YY
 *
 */
public class Keyv implements ISkillCfg, IBattleKeyV, IMonsterKeyV, IDrop {
	private int key;
	private int value;
	public int getKey() {
		return key;
	}
	public int getValue() {
		return value;
	}
	@Override
	public int getId() {
		return key;
	}
	@Override
	public int getLevel() {
		return value;
	}
	@Override
	public int getPos() {
		return key;
	}
	@Override
	public int getMonsterId() {
		return value;
	}
	@Override
	public int getDropId() {
		return key;
	}
	@Override
	public int getDropRate() {
		return value;
	}
}
