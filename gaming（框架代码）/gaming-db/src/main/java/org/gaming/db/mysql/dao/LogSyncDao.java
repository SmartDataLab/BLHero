/**
 * 
 */
package org.gaming.db.mysql.dao;

import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.orm.LogEntityMeta;

/**
 * @author YY
 *
 */
public class LogSyncDao<T extends AbstractEntity> extends LogBaseDao<T> {
	
	public LogSyncDao(DataBase dataBase, LogEntityMeta<T> entityMeta) {
		super(dataBase, entityMeta);
	}
}
