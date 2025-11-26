/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.CacheRepository;
import org.gaming.db.usecase.SlimDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class SystemOneToManyService<T extends AbstractEntity> {
	
	protected static Logger logger = LoggerFactory.getLogger(SystemOneToManyService.class);
	
	private final Class<T> clazz;
	private CacheRepository<T> repository;
	
	@SuppressWarnings("unchecked")
	public SystemOneToManyService() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected CacheRepository<T> repository() {
		if(repository == null) {
			repository = SlimDao.getCacheRepository(clazz);
		}
		return repository;
	}
	
	public abstract List<T> getEntities();
	
	public final void insert(T entity) {
		repository().insert(entity);
	}
	
	public final void insertAll(List<T> entities) {
		repository().insertAll(entities);
	}
	
	public final void update(T entity) {
		repository().update(entity);
	}
	
	public final void updateAll(List<T> entities) {
		repository().updateAll(entities);
	}
}
