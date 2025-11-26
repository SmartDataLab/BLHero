/**
 * 
 */
package com.xiugou.x1.game.server.foundation.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.orm.AbstractEntity;

/**
 * @author YY
 *
 */
public abstract class PlayerOneToOneService<T extends AbstractEntity> extends OneToOneService<T> {

	private static List<PlayerOneToOneService<?>> playerOneToOneServices = new ArrayList<>();
	
	public PlayerOneToOneService() {
		playerOneToOneServices.add(this);
	}
	
	private void newWhenPlayerCreate(long entityId) {
		T entity = createWhenNull(entityId);
		if(entity != null) {
			repository().insert(entity);
		}
	}
	
	public static void createPlayerDatas(long playerId) {
		for(PlayerOneToOneService<?> service : playerOneToOneServices) {
			service.newWhenPlayerCreate(playerId);
		}
	}
}
