/**
 * 
 */
package org.gaming.db.orm.column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.gaming.db.annotation.Column;

/**
 * @author YY
 *
 */
public class LongColumnMeta extends ColumnMeta {
	
	private final boolean time;
	
	protected LongColumnMeta(Field field) {
		super(field);
		Column column = field.getAnnotation(Column.class);
		if(column != null && column.extra().length >= 1) {
			this.time = "time".equals(column.extra()[0]);
		} else {
			this.time = false;
		}
	}

	@Override
	public Object takeValue(Object t) throws Exception {
		if(time) {
			return new Timestamp(field.getLong(t));
		} else {
			return field.getLong(t);
		}
	}

	@Override
	public void fillValue(Object t, int columnIndex, ResultSet rs) throws Exception {
		if(time) {
			Timestamp timestamp = rs.getTimestamp(columnIndex);
			field.set(t, timestamp.getTime());
		} else {
			field.set(t, rs.getLong(columnIndex));
		}
	}

	@Override
	public String dbColumnType() {
		if(time) {
			return "datetime";
		} else {
			return "bigint";
		}
	}
	
	@Override
	public String defaultValue() {
		if(time) {
			if(defaultValue != null && !"".equals(defaultValue)) {
				return "DEFAULT '" + defaultValue + "'";
			} else {
				return "DEFAULT NULL";
			}
		} else {
			return "DEFAULT 0";
		}
	}
	
	@Override
	public boolean isChange(String dbColumnType, String dbColumnExtra) {
		if(time) {
			if(!dbColumnType.startsWith("datetime")) {
				return true;
			}
			return false;
		} else {
			if(!dbColumnType.startsWith("bigint")) {
				return true;
			}
			return false;
		}
	}
}
