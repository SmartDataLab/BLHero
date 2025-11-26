/**
 * 
 */
package org.gaming.db.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.db.annotation.MappedSuperclass;

/**
 * @author YY
 *
 */
public class ReflectUtil {

	/**
     * 获取一个类所有的属性，包括其父类中的属性
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
    	List<Field> list = new ArrayList<>();
		for(Field field : clazz.getDeclaredFields()) {
			list.add(field);
		}
		Map<Integer, List<Field>> sortMap = new HashMap<>();
		sortMap.put(MappedSuperclass.Priority.NORMAL.getValue(), list);
		
		Class<?> superClazz = clazz.getSuperclass();
		while(superClazz != null) {
			//超类注解
			MappedSuperclass mapped = superClazz.getAnnotation(MappedSuperclass.class);
			if (mapped != null) {
				List<Field> fieldList = sortMap.get(mapped.sort().getValue());
				if(fieldList == null) {
					fieldList = new ArrayList<>();
					sortMap.put(mapped.sort().getValue(), fieldList);
				}
				for(Field field : superClazz.getDeclaredFields()) {
					fieldList.add(field);
				}
			}
			superClazz = superClazz.getSuperclass();
		}
		
		List<Integer> sorts = new ArrayList<>(sortMap.keySet());
		Collections.sort(sorts);
		
		List<Field> results = new ArrayList<>();
		for(int sort : sorts) {
			results.addAll(sortMap.get(sort));
		}
		return results;
    }
    
    public static Map<String, Field> getAllFieldsMap(Class<?> clazz) {
    	Map<String, Field> map = new HashMap<>();
    	for(Field field : getAllFields(clazz)) {
    		map.put(field.getName(), field);
    	}
    	return map;
    }
}
