/**
 * 
 */
package com.xiugou.x1.game.server.foundation.service;

import java.lang.reflect.ParameterizedType;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.CacheRepository;
import org.gaming.db.usecase.SlimDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class OneToOneService<T extends AbstractEntity> {

	protected static Logger logger = LoggerFactory.getLogger(OneToOneService.class);
	
	private final Class<T> clazz;
	private CacheRepository<T> repository;
	
	@SuppressWarnings("unchecked")
	public OneToOneService() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected final CacheRepository<T> repository() {
		if(repository == null) {
			repository = SlimDao.getCacheRepository(clazz);
		}
		return repository;
	}
	
	public T getEntity(long entityId) {
		T entity = repository().getByMainKey(entityId);
		if(entity == null) {
			entity = createWhenNull(entityId);
			if(entity != null) {
				repository().insert(entity);
			}
		}
		onGet(entity);
		return entity;
	}
	
	protected abstract T createWhenNull(long entityId);
	protected void onGet(T t) {}
	
	public final void insert(T entity) {
		repository().insert(entity);
	}
	
	public final void update(T entity) {
		repository().update(entity);
		onUpdate(entity);
	}
	protected void onUpdate(T t) {}
	
}
