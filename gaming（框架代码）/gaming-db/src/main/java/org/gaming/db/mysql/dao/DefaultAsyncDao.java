/**
 * 
 */
package org.gaming.db.mysql.dao;

import java.util.List;

import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.orm.DefaultAsyncEntityMeta;
import org.gaming.db.orm.IAsyncEntityMeta;

/**
 * @author YY
 *
 */
public class DefaultAsyncDao<T extends AbstractEntity> extends DefaultBaseDao<T> implements IAsyncDao<T> {
	
	private IAsyncEntityMeta asyncMeta;
	
	private AsyncDaoPlugin<T> asyncDaoPlugin;
	
	public DefaultAsyncDao(DataBase dataBase, DefaultAsyncEntityMeta<T> entityMeta) {
		super(dataBase, entityMeta);
		this.asyncMeta = entityMeta;
		this.asyncDaoPlugin = new AsyncDaoPlugin<T>(this);
	}

	@Override
	public void insertNow(T t) {
		super.insert(t);
	}

	@Override
	public void insertAllNow(List<T> ts) {
		super.insertAll(ts);
	}
	
	@Override
	public void updateNow(T t) {
		super.update(t);
	}

	@Override
	public void updateAllNow(List<T> ts) {
		super.updateAll(ts);
	}

	@Override
	public IAsyncEntityMeta getAsyncMeta() {
		return asyncMeta;
	}

	@Override
	public AsyncDaoPlugin<T> getAsyncPlugin() {
		return asyncDaoPlugin;
	}
	
	@Override
	public void insert(T t) {
		getAsyncPlugin().insert(t);
	}
	
	@Override
	public void insertAll(List<T> ts) {
		getAsyncPlugin().insertAll(ts);
	}
	
	@Override
	public void update(T t) {
		getAsyncPlugin().update(t);
	}
	
	@Override
	public void updateAll(List<T> ts) {
		getAsyncPlugin().updateAll(ts);
	}
}
