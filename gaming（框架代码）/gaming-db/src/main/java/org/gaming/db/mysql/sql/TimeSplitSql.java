/**
 * 
 */
package org.gaming.db.mysql.sql;

import static org.gaming.db.mysql.symbol.Symbol.DOT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.gaming.db.annotation.LogTable.TimeWay;
import org.gaming.db.orm.EntityMeta;
import org.gaming.db.orm.column.ColumnMeta;

/**
 * @author YY
 *
 */
public class TimeSplitSql<T> implements ISql<T> {

	private static ThreadLocal<DateFormat> yyyy = new ThreadLocal<>();
	private static ThreadLocal<DateFormat> yyyyMM = new ThreadLocal<>();
	private static ThreadLocal<DateFormat> yyyyww = new ThreadLocal<>();
	private static ThreadLocal<DateFormat> yyyyMMdd = new ThreadLocal<>();
	private static ThreadLocal<DateFormat> yyyyMMddHH = new ThreadLocal<>();
	private static ThreadLocal<DateFormat> yyyyMMddHHmm = new ThreadLocal<>();
	
	public final String TABLE_NAME;
	public final String INSERT;
	public final String UPDATE;
	public final String DELETE;
	
	public TimeSplitSql(EntityMeta<T> entityMeta, String tableName) {
		this.TABLE_NAME = tableName;
		
//		SELECT = select(entityMeta);
//		SELECT_ALL = selectAll(entityMeta);
		INSERT = insert(entityMeta);
		UPDATE = update(entityMeta);
		DELETE = delete(entityMeta);
//		DELETE_IDS = deleteIds(entityMeta);
//		DELETE_ALL = deleteAll(entityMeta);
	}
	
	/**
	 * 插入数据的SQL
	 * @param entityMeta
	 * @return
	 */
	public String insert(EntityMeta<T> entityMeta) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + DOT + TABLE_NAME + DOT + " (");
		boolean isFirst = true;
		for(ColumnMeta column : entityMeta.getColumnList()) {
			if(!isFirst) {
				sql.append(",");
			}
			sql.append(DOT + column.getColumnName() + DOT);
			isFirst = false;
		}
		sql.append(") VALUES (");
		isFirst = true;
		for(int i = 0; i < entityMeta.getColumnList().size(); i++) {
			if(!isFirst) {
				sql.append(",");
			}
			sql.append("?");
			isFirst = false;
		}
		sql.append(")");
		return sql.toString();
	}
	/**
	 * 根据ID更新数据的SQL
	 * @param entityMeta
	 * @return
	 */
	public String update(EntityMeta<T> entityMeta) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + DOT + TABLE_NAME + DOT + " SET ");
		boolean isFirst = true;
		for(ColumnMeta column : entityMeta.getColumnList()) {
			if(!isFirst) {
				sql.append(",");
			}
			sql.append(DOT + column.getColumnName() + DOT).append("=?");
			isFirst = false;
		}
		sql.append(" WHERE " + DOT + entityMeta.getPrimaryKeyMeta().getColumnName() + DOT + "=?");
		return sql.toString();
	}
	/**
	 * 根据ID删除数据的SQL
	 * @param entityMeta
	 * @return
	 */
	public String delete(EntityMeta<T> entityMeta) {
		return "DELETE FROM " + DOT + TABLE_NAME + DOT + " WHERE " + DOT + entityMeta.getPrimaryKeyMeta().getColumnName() + DOT + "=?";
	}
	
	public static String getFormat(TimeWay timeWay, long millisTime) {
		DateFormat dateFormat  = null;
		if(timeWay == TimeWay.YEAR) {
			dateFormat = yyyy.get();
			if(dateFormat == null) {
				dateFormat = new SimpleDateFormat("yyyy");
				yyyy.set(dateFormat);
			}
			return dateFormat.format(new Date(millisTime));
		} else if(timeWay == TimeWay.MONTH) {
			dateFormat = yyyyMM.get();
			if(dateFormat == null) {
				dateFormat = new SimpleDateFormat("yyyyMM");
				yyyyMM.set(dateFormat);
			}
			return dateFormat.format(new Date(millisTime));
		} else if(timeWay == TimeWay.WEEK) {
			dateFormat = yyyyww.get();
			if(dateFormat == null) {
				dateFormat = new SimpleDateFormat("yyyyww");
				yyyyww.set(dateFormat);
			}
			return dateFormat.format(new Date(millisTime));
		} else if(timeWay == TimeWay.DAY) {
			dateFormat = yyyyMMdd.get();
			if(dateFormat == null) {
				dateFormat = new SimpleDateFormat("yyyyMMdd");
				yyyyMMdd.set(dateFormat);
			}
			return dateFormat.format(new Date(millisTime));
		} else if(timeWay == TimeWay.HOUR) {
			dateFormat = yyyyMMddHH.get();
			if(dateFormat == null) {
				dateFormat = new SimpleDateFormat("yyyyMMddHH");
				yyyyMMddHH.set(dateFormat);
			}
			return dateFormat.format(new Date(millisTime));
		} else if(timeWay == TimeWay.MINUTE) {
			dateFormat = yyyyMMddHHmm.get();
			if(dateFormat == null) {
				dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
				yyyyMMddHHmm.set(dateFormat);
			}
			return dateFormat.format(new Date(millisTime));
		} else if(timeWay == TimeWay.VALUE) {
			return millisTime + "";
		} else {
			throw new RuntimeException("no supporting split way " + timeWay);
		}
	}

	@Override
	public String insert() {
		return INSERT;
	}

	@Override
	public String update() {
		return UPDATE;
	}

	@Override
	public String delete() {
		return DELETE;
	}

}
