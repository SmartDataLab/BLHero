/**
 * 
 */
package org.gaming.prefab.task;

/**
 * @author YY
 * base包下的类都应该是脱离具体业务层面
 * 比如A项目和B项目中都存在任务系统，那么base包可以直接复制至AB两个项目中，而无须进行修改，因此设定了任务系统的接口
 */
public interface ITaskSystem {

	int getValue();
	
	String getDesc();
}
