/**
 * 
 */
package org.gaming.backstage.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.gaming.backstage.PageData;
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
	
	private BaseRepository<T> repository;
	protected final Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public AbstractService() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected final BaseRepository<T> repository() {
		if(repository == null) {
			repository = SlimDao.getRepository(clazz);
		}
		return repository;
	}
	
	public T getById(long id) {
		return this.repository().getByMainKey(id);
	}
	
	public void insert(T t) {
		this.repository().insert(t);
	}
	
	public void insertAll(List<T> ts) {
		this.repository().insertAll(ts);
	}
	
	public void update(T t) {
		this.repository().update(t);
	}
	
	public void updateAll(List<T> ts) {
		this.repository().updateAll(ts);
	}
	
	public void delete(T t) {
		repository().delete(t);
	}
	
	public PageData<T> query(QuerySet querySet) {
		querySet.formWhere();
		List<T> result = repository().getBaseDao().queryListWhere(querySet.getWhere(), querySet.getParams());
		
		long count = repository().getBaseDao().count(querySet.getCountWhere(), querySet.getCountParams());
		PageData<T> queryPage = new PageData<>(count, result);
		return queryPage;
	}
}
