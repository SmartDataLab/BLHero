/**
 * 
 */
package org.gaming.prefab.rank;

/**
 * @author YY
 *
 */
public interface TopEntity<Key> {

//	Key GetKey();
	
	//排名下标，排名=排名下标+1
	int getRankIndex();
	
	void setRankIndex(int rankIndex);
	
}
