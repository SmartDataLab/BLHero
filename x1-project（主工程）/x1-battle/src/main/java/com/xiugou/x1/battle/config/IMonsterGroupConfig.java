/**
 * 
 */
package com.xiugou.x1.battle.config;

import java.util.List;

/**
 * @author YY
 *
 */
public interface IMonsterGroupConfig {
	int getGroupId();
	List<? extends IBorn> getBorns();
}
