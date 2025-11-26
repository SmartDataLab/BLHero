/**
 * 
 */
package org.gaming.db.mysql.dao;

import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.orm.EntityMeta;

/**
 * @author YY
 *
 */
public class DefaultSyncDao<T extends AbstractEntity> extends DefaultBaseDao<T> {

	public DefaultSyncDao(DataBase dataBase, EntityMeta<T> entityMeta) {
		super(dataBase, entityMeta);
	}
}
