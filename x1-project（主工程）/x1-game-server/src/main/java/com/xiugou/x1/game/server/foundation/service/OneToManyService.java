/**
 * 
 */
package com.xiugou.x1.game.server.foundation.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.CacheRepository;
import org.gaming.db.usecase.SlimDao;

/**
 * @author YY
 *
 */
public abstract class OneToManyService<T extends AbstractEntity> {

	private final Class<T> clazz;
	private CacheRepository<T> repository;
	
	@SuppressWarnings("unchecked")
	public OneToManyService() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected CacheRepository<T> repository() {
		if(repository == null) {
			repository = SlimDao.getCacheRepository(clazz);
		}
		return repository;
	}
	
	public List<T> getEntities(long ownerId) {
		return repository().listByKeys(ownerId);
	}
	
	public final void insert(T entity) {
		repository().insert(entity);
	}
	
	public final void insert(List<T> entities) {
		repository().insertAll(entities);
	}
	
	public final void update(T entity) {
		repository().update(entity);
	}
	
	public final void update(List<T> entities) {
		repository().updateAll(entities);
	}
}
