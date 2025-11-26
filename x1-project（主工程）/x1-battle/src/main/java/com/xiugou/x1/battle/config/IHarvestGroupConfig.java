/**
 * 
 */
package com.xiugou.x1.battle.config;

import java.util.List;

/**
 * @author YY
 *
 */
public interface IHarvestGroupConfig {
	int getGroupId();
	List<? extends IBorn> getBorns();
}
