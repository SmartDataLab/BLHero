/**
 * 
 */
package org.gaming.db.orm;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.table.LogTableInfo;

/**
 * @author YY
 *
 */
public class LogAsyncEntityMeta<T> extends LogEntityMeta<T> implements IAsyncEntityMeta {

	private final AsyncType asyncType;
	private final int asyncSize;
	private final int asyncDelay;
	private final String asyncName;
	
	public LogAsyncEntityMeta(Class<T> clazz) {
		super(clazz);
		LogTableInfo table = new LogTableInfo(clazz.getAnnotation(LogTable.class));
		this.asyncType = table.asyncType();
		this.asyncSize = table.asyncSize();
		this.asyncDelay = table.asyncDelay();
		this.asyncName = table.name();
	}

	@Override
	public AsyncType getAsyncType() {
		return asyncType;
	}

	@Override
	public int getAsyncSize() {
		return asyncSize;
	}

	@Override
	public int getAsyncDelay() {
		return asyncDelay;
	}

	@Override
	public String getAsyncName() {
		return asyncName;
	}
}
