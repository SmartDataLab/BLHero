/**
 * 
 */
package com.xiugou.x1.battle.config;

import java.util.List;

/**
 * @author YY
 *
 */
public interface IBuffConfig {
	int getId();
	//枚举ID
	int getBuffEnum();
	//堆叠方式
	int getStackWay();
	//属性
	List<Attr> getAttrParams();
	//整形参数
	List<Integer> getIntParams();
	//持续时间
	int getLastTime();
	//是否可被驱散
	int getCanDispel();
}
