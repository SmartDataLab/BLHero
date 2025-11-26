/**
 * 
 */
package org.gaming.db.mysql.sql;

/**
 * @author YY
 *
 */
public interface ISql<T> {
	
	public String insert();
	
	public String update();
	
	public String delete();
}
