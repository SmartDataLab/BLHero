/**
 * 
 */
package org.gaming.prefab.rank.top;

/**
 * @author YY
 *
 */
public interface TopComparator<T> {
	
	//判断积分大小
	int compara(T t1, T t2);
	//判断标识（ID）大小，用于在积分相同时，判断对象是否同一个
	int sdscmp(T t1, T t2);
}
