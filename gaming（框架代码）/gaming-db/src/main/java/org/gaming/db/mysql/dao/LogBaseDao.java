/**
 * 
 */
package org.gaming.db.mysql.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.gaming.db.annotation.LogTable.TimeWay;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.mysql.sql.ISql;
import org.gaming.db.mysql.sql.TimeSplitSql;
import org.gaming.db.mysql.table.TableBuilder;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.orm.LogEntityMeta;

/**
 * @author YY
 *
 */
public abstract class LogBaseDao<T extends AbstractEntity> extends OriginDao<T> {
	/**
	 * <表名，Sql组合>
	 */
	private Map<String, TimeSplitSql<T>> splitSqlMap = new ConcurrentHashMap<>();
	
	private LogEntityMeta<T> logEntityMeta;
	
	public LogBaseDao(DataBase dataBase, LogEntityMeta<T> entityMeta) {
		super(dataBase, entityMeta);
		this.logEntityMeta = entityMeta;
		//创建Dao对象的时候检查当前时间对应的表
		TimeWay timeWay = entityMeta.getTimeWay();
		if(timeWay != TimeWay.VALUE) {
			String splitName = TimeSplitSql.getFormat(timeWay, System.currentTimeMillis());
			String tableName = entityMeta.getTableName() + "_" + splitName;
			TimeSplitSql<T> sql = createSql(tableName);
			splitSqlMap.put(tableName, sql);
		}
	}

	public final String getTableName(LogEntityMeta<T> entityMeta, T t) {
		TimeWay timeWay = entityMeta.getTimeWay();
		try {
			long millisTime = entityMeta.splitEpochMilli(t);
			String splitName = TimeSplitSql.getFormat(timeWay, millisTime);
			return entityMeta.getTableName() + "_" + splitName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public final String getTableName(long millisTime) {
		TimeWay timeWay = logEntityMeta.getTimeWay();
		try {
			String splitName = TimeSplitSql.getFormat(timeWay, millisTime);
			return logEntityMeta.getTableName() + "_" + splitName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	protected ISql<T> getSql(T t) {
		return ensureSqlInit(t);
	}
	
	private TimeSplitSql<T> ensureSqlInit(T t) {
		String tableName = getTableName((LogEntityMeta<T>)this.getEntityMeta(), t);
		TimeSplitSql<T> sql = splitSqlMap.get(tableName);
		if(sql == null) {
			synchronized (this) {
				sql = splitSqlMap.get(tableName);
				if(sql == null) {
					sql = createSql(tableName);
					splitSqlMap.put(tableName, sql);
				}
			}
		}
		return sql;
	}
	
	private synchronized TimeSplitSql<T> createSql(String tableName) {
		//自动建表
		TableBuilder.build(this, tableName);
		//构建Sql对象
		TimeSplitSql<T> sql = new TimeSplitSql<>(this.getEntityMeta(), tableName);
		return sql;
	} 
	

	@Override
	protected void insertAllWithAutoId(List<T> ts) {
		Map<TimeSplitSql<T>, List<T>> splitMap = splitIntoMap(ts);
		for(Entry<TimeSplitSql<T>, List<T>> entry : splitMap.entrySet()) {
			insertAllWithAutoId(entry.getValue(), entry.getKey());
		}
	}
	
	private void insertAllWithAutoId(List<T> ts, TimeSplitSql<T> sqlTemplate) {
		String sql = sqlTemplate.insert();
		super.insertAllWithAutoId(ts, sql);
	}
	
	private Map<TimeSplitSql<T>, List<T>> splitIntoMap(List<T> ts) {
		Map<TimeSplitSql<T>, List<T>> splitMap = new HashMap<>();
		for(T t : ts) {
			TimeSplitSql<T> sql = ensureSqlInit(t);
			List<T> list = splitMap.get(sql);
			if(list == null) {
				list = new ArrayList<>();
				splitMap.put(sql, list);
			}
			list.add(t);
		}
		return splitMap;
	}

	@Override
	protected void insertAllWithIdentityId(List<T> ts) {
		Map<TimeSplitSql<T>, List<T>> splitMap = splitIntoMap(ts);
		for(Entry<TimeSplitSql<T>, List<T>> entry : splitMap.entrySet()) {
			insertAllWithIdentityId(entry.getValue(), entry.getKey());
		}
	}
	
	/**
	 * 插入所有具有自定义ID属性的数据
	 * @param ts 必须要求是列表，以保证返回的ID数组顺序与列表对象顺序一致
	 * @return
	 */
	private void insertAllWithIdentityId(List<T> ts, TimeSplitSql<T> sqlTemplate) {
		String sql = sqlTemplate.insert();
		super.insertAllWithIdentityId(ts, sql);
	}

	@Override
	public void updateAll(List<T> ts) {
		Map<TimeSplitSql<T>, List<T>> splitMap = splitIntoMap(ts);
		for(Entry<TimeSplitSql<T>, List<T>> entry : splitMap.entrySet()) {
			updateAll(entry.getValue(), entry.getKey());
		}
	}
	
	private void updateAll(List<T> ts, TimeSplitSql<T> sqlTemplate) {
		String sql = sqlTemplate.update();
		super.updateAll(ts, sql);
	}
	

	@Override
	public void deleteAll(List<T> ts) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public void deleteWhere(String where, Object... params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public T query(Object id) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public List<T> queryAll() {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public T queryOneWhere(String where, Object... params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public List<T> queryListWhere(String where, Object... params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public void insertUpdate(List<T> ts) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	public long count(String where, Object... params) {
		throw new UnsupportedOperationException("不该被调用的函数");
	}
}
