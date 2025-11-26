/**
 * 
 */
package org.gaming.db.repository;

import java.util.List;

import org.gaming.db.mysql.dao.OriginDao;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.repository.QueryOptions.QueryOption;

/**
 * @author YY
 *
 */
public abstract class BaseRepository<T extends AbstractEntity> {
	
	protected OriginDao<T> baseDao;
	
	public BaseRepository(OriginDao<T> baseDao) {
		this.baseDao = baseDao;
	}
	
	public T getByMainKey(Object key) {
		return baseDao.query(key);
	}
	
	/**
	 * 以（键，值，键，值，...）的形式进行对象查询与获取，参数列表必须是2的倍数
	 * 键是类的属性名，不是数据库的字段名
	 * @param args
	 * @return
	 */
	public T get(Object... args) {
		if(args == null || args.length <= 0) {
			throw new RuntimeException("查询条件为空");
		}
		if(args.length % 2 != 0) {
			throw new RuntimeException("查询参数必须以（键，值，键，值，...）的形式进行成对传递");
		}
		QueryOptions options = new QueryOptions();
		for(int i = 0; i < args.length / 2; i++) {
			Object key = args[i * 2 + 0];
			Object val = args[i * 2 + 1];
			options.put(key.toString(), val);
		}
		return get(options);
	}
	
	public T get(QueryOptions options) {
		List<T> list = getList0(options);
		if(list.size() > 1) {
			throw new RuntimeException("result set for options " + options + " more than one");
		} else if(list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	private final List<T> getList0(QueryOptions options) {
		if(options == null || options.isEmpty()) {
			return this.baseDao.queryAll();
		}
		StringBuilder where = new StringBuilder();
		where.append("WHERE ");
		
		Object[] keys = new Object[options.size()];
		
		for(int index = 0; index < options.size(); index++) {
			QueryOption option = options.get(index);
			String columnName = baseDao.getEntityMeta().getColumnName(option.getFieldName());
			if(columnName == null) {
				throw new RuntimeException("can not find columnName with fieldName : " + option.getFieldName());
			}
			if(index == 0) {
				where.append(columnName).append("=?");
			} else {
				where.append(" AND ").append(columnName).append("=?");
			}
			keys[index] = option.getValue();
		}
		List<T> list = this.baseDao.queryListWhere(where.toString(), keys);
		return list;
	}
	
	public List<T> getList(QueryOptions options) {
		return getList0(options);
	}
	
	public List<T> getAllInDb() {
		return baseDao.queryAll();
	}
	
	public void insert(T t) {
		baseDao.insert(t);
	}
	
	public void insertAll(List<T> ts) {
		baseDao.insertAll(ts);
	}
	
	public void update(T t) {
		baseDao.update(t);
	}
	
	public void updateAll(List<T> ts) {
		baseDao.updateAll(ts);
	}
	
	public boolean updateWhen(T t, String when) {
		return baseDao.updateWhen(t, when);
	}
	
	public void delete(T t) {
		baseDao.delete(t);
	}
	
	public void deleteAll(List<T> ts) {
		baseDao.deleteAll(ts);
	}
	
	public void loadOnStart() {
	}

	public void deleteInDb(String where, Object... params) {
		baseDao.deleteWhere(where, params);
	}

	public OriginDao<T> getBaseDao() {
		return baseDao;
	}
}
