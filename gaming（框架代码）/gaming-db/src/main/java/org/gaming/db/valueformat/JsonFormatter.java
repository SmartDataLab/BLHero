/**
 * 
 */
package org.gaming.db.valueformat;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.gaming.db.valueformat.ValueFormatters.ValueFormatter;
import org.gaming.tool.GsonUtil;

/**
 * @author YY
 *
 */
public class JsonFormatter implements ValueFormatter {
	@Override
	public String formatter() {
		return "json";
	}

	@Override
	public Object decode(Field field, String value) {
		if(List.class.isAssignableFrom(field.getType())) {
			AnnotatedType annotatedType = field.getAnnotatedType();
			ParameterizedType type = (ParameterizedType)annotatedType.getType();
			Class<?> listType = (Class<?>)type.getActualTypeArguments()[0];
			return GsonUtil.getList(value, listType);
		} else if(ConcurrentMap.class.isAssignableFrom(field.getType())) {
			AnnotatedType annotatedType = field.getAnnotatedType();
			ParameterizedType type = (ParameterizedType)annotatedType.getType();
			Class<?> keyType = (Class<?>)type.getActualTypeArguments()[0];
			Class<?> valueType = (Class<?>)type.getActualTypeArguments()[1];
			return GsonUtil.getConcurrentMap(value, keyType, valueType);
		} else if(Map.class.isAssignableFrom(field.getType())) {
			AnnotatedType annotatedType = field.getAnnotatedType();
			ParameterizedType type = (ParameterizedType)annotatedType.getType();
			Class<?> keyType = (Class<?>)type.getActualTypeArguments()[0];
			Class<?> valueType = (Class<?>)type.getActualTypeArguments()[1];
			return GsonUtil.getMap(value, keyType, valueType);
		} else if(Set.class.isAssignableFrom(field.getType())) {
			AnnotatedType annotatedType = field.getAnnotatedType();
			ParameterizedType type = (ParameterizedType)annotatedType.getType();
			Class<?> setType = (Class<?>)type.getActualTypeArguments()[0];
			return GsonUtil.getSet(value, setType);
		} else {
			return GsonUtil.parseJson(value, field.getType());
		}
	}

	@Override
	public String encode(Object object) {
		return GsonUtil.toJson(object);
	}
}
