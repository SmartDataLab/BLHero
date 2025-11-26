/**
 * 
 */
package org.gaming.db.orm;

import org.gaming.db.annotation.Table;
import org.gaming.db.orm.table.TableInfo;

/**
 * @author YY
 *
 */
public class DefaultEntityMeta<T> extends EntityMeta<T> {

	public DefaultEntityMeta(Class<T> clazz) {
		super(clazz, new TableInfo(clazz.getAnnotation(Table.class)));
	}
}
