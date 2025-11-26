/**
 * 
 */
package org.gaming.backstage.service;

import java.util.List;

import org.gaming.db.repository.QueryOptions;


/**
 * @author YY
 *
 */
public abstract class SystemOneToManyService<T extends OneToManyRedisHashEntity> extends OneToManyRedisService<T> {

	@Override
	protected String cacheContext() {
		return "system";
	}

	public List<T> getEntities() {
		return super.getEntityList(0);
	}

	public T getEntity(long uniqueId) {
		return super.getEntity(0, uniqueId);
	}

	@Override
	protected QueryOptions queryForOwner(long ownerId) {
		return null;
	}
}
