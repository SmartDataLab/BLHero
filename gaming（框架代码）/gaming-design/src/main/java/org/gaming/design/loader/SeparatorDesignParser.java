/**
 * 
 */
package org.gaming.design.loader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author YY
 *
 */
public abstract class SeparatorDesignParser implements DesignParser {

	//字段之间的分隔符
	protected abstract String fieldSeparator();
	//对象之间的分隔符
	protected abstract String objSeparator();

	@Override
	public Object parseList(String value, Class<?> clazz) {
		if(clazz == Integer.class || clazz == int.class) {
			List<Integer> list = new ArrayList<>();
			String[] valueParts = value.split(fieldSeparator());
			for(String valuePart : valueParts) {
				if("".equals(valuePart)) {
					continue;
				}
				list.add(Integer.parseInt(valuePart));
			}
			return Collections.unmodifiableList(list);
			
		} else if(clazz == Long.class || clazz == long.class) {
			List<Long> list = new ArrayList<>();
			String[] valueParts = value.split(fieldSeparator());
			for(String valuePart : valueParts) {
				if("".equals(valuePart)) {
					continue;
				}
				list.add(Long.parseLong(valuePart));
			}
			return Collections.unmodifiableList(list);
			
			
		} else if(clazz == Float.class || clazz == float.class) {
			List<Float> list = new ArrayList<>();
			String[] valueParts = value.split(fieldSeparator());
			for(String valuePart : valueParts) {
				if("".equals(valuePart)) {
					continue;
				}
				list.add(Float.parseFloat(valuePart));
			}
			return Collections.unmodifiableList(list);
			
		} else if(clazz == String.class) {
			List<String> list = new ArrayList<>();
			String[] valueParts = value.split(fieldSeparator());
			for(String valuePart : valueParts) {
				if("".equals(valuePart)) {
					continue;
				}
				list.add(valuePart);
			}
			return Collections.unmodifiableList(list);
		} else {
			List<Object> list = new ArrayList<>();
			String[] objParts = value.split(objSeparator());
			for(String objPart : objParts) {
				if("".equals(objPart)) {
					continue;
				}
				list.add(parseStruct(objPart, clazz));
			}
			return Collections.unmodifiableList(list);
		}
	}

	@Override
	public Object parseMap(String value, Class<?> keyClazz, Class<?> valueClazz) {
		throw new RuntimeException("待完善");
	}

	@Override
	public Object parseStruct(String value, Class<?> clazz) {
		try {
			Object struct = clazz.newInstance();
			if("".equals(value)) {
				return struct;
			}
			String[] fieldParts = value.split(fieldSeparator());
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length && i < fieldParts.length; i++) {
				String fieldValue = fieldParts[i];
				
				Field field = fields[i];
				field.setAccessible(true);
				if(int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
					field.setInt(struct, Integer.parseInt(fieldValue));
					
				} else if(long.class.equals(field.getType()) || Long.class.equals(field.getType())) {
					field.setLong(struct, Long.parseLong(fieldValue));
					
				} else if(float.class.equals(field.getType()) || Float.class.equals(field.getType())) {
					field.setFloat(struct, Float.parseFloat(fieldValue));
					
				} else if(BigDecimal.class.equals(field.getType())) {
					field.set(struct, new BigDecimal(fieldValue));
					
				} else if(String.class.equals(field.getType())) {
					
					field.set(struct, fieldValue);
				} else {
					System.out.println("未处理的类型" + field.getType().getClass().getSimpleName());
				}
			}
			return struct;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
