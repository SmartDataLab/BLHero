/**
 * 
 */
package org.gaming.db.valueformat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YY
 *
 */
public class ValueFormatters {

	private static final Map<String, ValueFormatter> VALUE_FORMATERS = new HashMap<>();
	
	static {
		addFormater(new JsonFormatter());
	}
	
	public static void addFormater(ValueFormatter... valueFormater) {
		for(ValueFormatter formater : valueFormater) {
			VALUE_FORMATERS.put(formater.formatter(), formater);
		}
	}
	
	public static interface ValueFormatter {
		String formatter();
		
		Object decode(Field field, String value);
		
		String encode(Object object);
	}
	
	public static ValueFormatter getFormatter(String formatter) {
		return VALUE_FORMATERS.get(formatter);
	}
}
