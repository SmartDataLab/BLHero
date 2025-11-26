/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class AbstractService<T extends AbstractEntity> {

	protected static Logger logger = LoggerFactory.getLogger(AbstractService.class);
	
	private final Class<T> clazz;
	private BaseRepository<T> repository;
	
	@SuppressWarnings("unchecked")
	public AbstractService() {
		Class<?> currClz = this.getClass();
		Class<T> entityClazz = null;
		while(currClz != null) {
			if(currClz.getGenericSuperclass() instanceof ParameterizedType) {
				ParameterizedType ptype = (ParameterizedType)currClz.getGenericSuperclass();
				if(ptype.getActualTypeArguments().length == 1) {
					entityClazz = (Class<T>) ptype.getActualTypeArguments()[0];
					break;
				}
			}
			currClz = currClz.getSuperclass();
		}
		this.clazz = entityClazz;
	}
	
	protected final BaseRepository<T> repository() {
		if(repository == null) {
			repository = SlimDao.getRepository(clazz);
		}
		return repository;
	}
	
	public void insert(T t) {
		this.repository().insert(t);
	}
	
	public void update(T t) {
		this.repository().update(t);
	}
	
	public List<T> query(QuerySet querySet) {
		querySet.formWhere();
		List<T> result = this.repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		return result;
	}
}
