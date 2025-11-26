/**
 * 
 */
package org.gaming.db.orm;

import org.gaming.db.annotation.enuma.AsyncType;

/**
 * @author YY
 *
 */
public interface IAsyncEntityMeta {
	
	public AsyncType getAsyncType();

	public int getAsyncSize();

	public int getAsyncDelay();
	
	public String getAsyncName();
}
