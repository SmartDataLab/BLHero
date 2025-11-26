/**
 * 
 */
package org.gaming.db.orm;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.LogTable.TimeWay;
import org.gaming.db.orm.column.ColumnMeta;
import org.gaming.db.orm.column.DateColumnMeta;
import org.gaming.db.orm.column.IntegerColumnMeta;
import org.gaming.db.orm.column.LocalDateTimeColumnMeta;
import org.gaming.db.orm.column.LongColumnMeta;
import org.gaming.db.orm.table.LogTableInfo;

/**
 * @author YY
 *
 */
public class LogEntityMeta<T> extends EntityMeta<T> {

	/**
	 * 分表的字段元数据
	 */
	private final ColumnMeta splitMeta;
	
	private final TimeWay timeWay;
	
	public LogEntityMeta(Class<T> clazz) {
		super(clazz, new LogTableInfo(clazz.getAnnotation(LogTable.class)));
		
		//分表的元数据信息
		LogTable splitTable = clazz.getAnnotation(LogTable.class);
		
		if(splitTable != null) {
			this.timeWay = splitTable.way();
			ColumnMeta splitingMeta = this.getColumnMap().get(splitTable.byColumn());
			if(splitingMeta == null) {
				throw new RuntimeException(
						clazz.getSimpleName() + " can not find @TimeTable column named " + splitTable.byColumn());
			}
			if(splitingMeta instanceof DateColumnMeta) {
				this.splitMeta = splitingMeta;
			} else if(splitingMeta instanceof LocalDateTimeColumnMeta) {
				this.splitMeta = splitingMeta;
			} else if(splitingMeta instanceof LongColumnMeta) {
				this.splitMeta = splitingMeta;
			} else if(splitingMeta instanceof IntegerColumnMeta) {
				this.splitMeta = splitingMeta;
			} else {
				throw new RuntimeException(String.format("实体类%s的字段%s无法作为时间分表的元数据", clazz.getSimpleName(), splitingMeta.getFieldName()));
			}
		} else {
			this.timeWay = TimeWay.NULL;
			this.splitMeta = null;
		}
	}
	
	public long splitEpochMilli(Object t) {
		try {
			Object timeObj = splitMeta.takeValue(t);
			if(timeObj != null) {
				if(timeObj instanceof Date) {
					Date date = (Date)timeObj;
					ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneOffset.systemDefault());
					return zonedDateTime.toInstant().toEpochMilli();
				} else if(timeObj instanceof LocalDateTime) {
					LocalDateTime localDateTime = (LocalDateTime)timeObj;
					ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.systemDefault());
					return zonedDateTime.toInstant().toEpochMilli();
				} else if(timeObj instanceof Long) {
					return ((Long) timeObj).longValue();
				} else if(timeObj instanceof Integer) {
					return ((Integer) timeObj).intValue() * 1000L;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	public TimeWay getTimeWay() {
		return timeWay;
	}
}
