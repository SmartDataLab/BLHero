/**
 * 
 */
package org.gaming.db.orm.column;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import org.gaming.db.annotation.Column;
import org.gaming.db.valueformat.ValueFormatters;
import org.gaming.db.valueformat.ValueFormatters.ValueFormatter;


/**
 * @author YY
 *
 */
public class StructColumnMeta extends StringColumnMeta {

	private final String formatter;
	
	protected StructColumnMeta(Field field) {
		super(field);
		Column column = field.getAnnotation(Column.class);
		if("".equals(column.formatter())) {
			this.formatter = "json";
		} else {
			this.formatter = column.formatter();
		}
	}

	@Override
	public Object takeValue(Object t) throws Exception {
		ValueFormatter valueFormatter = ValueFormatters.getFormatter(formatter);
		return valueFormatter.encode(field.get(t));
	}

	@Override
	public void fillValue(Object t, int columnIndex, ResultSet rs) throws Exception {
		String value = rs.getString(columnIndex);
		ValueFormatter valueFormatter = ValueFormatters.getFormatter(formatter);
		Object v = valueFormatter.decode(field, value);
		field.set(t, v);
	}
}
