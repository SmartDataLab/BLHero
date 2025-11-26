/**
 * 
 */
package org.gaming.db.orm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.LogTable;

/**
 * @author YY
 *
 */
public class MetaManager {
	/**
	 * 实体类元数据 
	 */
	private static Map<Class<?>, EntityMeta<?>> METAS;
	
	public static Map<Class<?>, EntityMeta<?>> getMetas() {
		return METAS;
	}
	
	public static void buildMeta(Collection<Class<?>> entityClasses) {
		Map<Class<?>, EntityMeta<?>> tempMetas = new HashMap<>();
		for(Class<?> clazz : entityClasses) {
			EntityMeta<?> entityMeta = null;
			
			Table table = clazz.getAnnotation(Table.class);
			LogTable logTable = clazz.getAnnotation(LogTable.class);
			if(table != null && logTable != null) {
				throw new RuntimeException("entity contains multiple @?Table annotation");
			}
			if(table == null && logTable == null) {
				throw new RuntimeException(clazz.getSimpleName() + " entity class not contains any @?Table annotation");
			}
			if(table != null) {
				//普通的数据表
				if(table.asyncType() == AsyncType.NONE) {
					entityMeta = new DefaultEntityMeta<>(clazz);
				} else {
					entityMeta = new DefaultAsyncEntityMeta<>(clazz);
				}
			} else if(logTable != null) {
				//按时间分表的数据表
				if(logTable.asyncType() == AsyncType.NONE) {
					entityMeta = new LogEntityMeta<>(clazz);
				} else {
					entityMeta = new LogAsyncEntityMeta<>(clazz);
				}
			}
			tempMetas.put(entityMeta.getClazz(), entityMeta);
		}
		METAS = tempMetas;
	}
}
