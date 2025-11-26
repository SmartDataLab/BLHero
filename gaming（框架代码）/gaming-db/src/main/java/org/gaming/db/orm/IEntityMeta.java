/**
 * 
 */
package org.gaming.db.orm;

import java.util.List;

import org.gaming.db.orm.column.ColumnMeta;

/**
 * @author YY
 *
 */
public interface IEntityMeta<T> {

	T newInstance() throws Exception ;
	
	ColumnMeta getColumnMeta(String columnName);
	
	List<ColumnMeta> getColumnList();
}
