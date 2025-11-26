/**
 * 
 */
package org.gaming.prefab.task;

import java.util.List;

/**
 * @author YY
 * 任务配置接口的缓存
 */
public interface ITaskConfig {
	/**
	 * 任务类型的标识字符串
	 * @return
	 */
	String getIdentity();
	/**
	 * 需要的条件
	 * @return
	 */
	List<Integer> getNeedConditions();
	/**
	 * 目标数量需要使用long，比如需要对某怪物造成多少伤害、收取多少金币数量之类
	 * @return
	 */
	long getTargetNum();
}
