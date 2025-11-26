/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.service;

import java.lang.reflect.ParameterizedType;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class OneToOneBaseService<T extends AbstractEntity> {
	
	protected static Logger logger = LoggerFactory.getLogger(OneToOneBaseService.class);
	
	private final Class<T> clazz;
	private BaseRepository<T> repository;
	
	@SuppressWarnings("unchecked")
	public OneToOneBaseService() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected BaseRepository<T> repository() {
		if(repository == null) {
			repository = SlimDao.getRepository(clazz);
		}
		return repository;
	}
	
	public T getEntity(long entityId) {
		return repository().getByMainKey(entityId);
	}
	
	
	public final void insert(T entity) {
		repository().insert(entity);
	}
	
	public final void update(T entity) {
		repository().update(entity);
	}
}
