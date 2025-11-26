/**
 * 
 */
package org.gaming.db.orm.table;

import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.Charset;

/**
 * @author YY
 *
 */
public class TableInfo implements ITable {

	private final String name;
	private final String comment;
	private final String dbAlias;
	private final Index[] indexs;
	private final Charset charset;
	private final AsyncType asyncType;
	private final int asyncSize;
	private final int asyncDelay;
	private final boolean autoBuild;
	
	public TableInfo(Table table) {
		this.name = table.name();
		this.comment = table.comment();
		this.dbAlias = table.dbAlias();
		this.indexs = table.indexs();
		this.charset = table.charset();
		this.asyncType = table.asyncType();
		this.asyncSize = table.asyncSize();
		this.asyncDelay = table.asyncDelay();
		this.autoBuild = table.autoBuild();
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public String comment() {
		return comment;
	}

	@Override
	public String dbAlias() {
		return dbAlias;
	}

	@Override
	public Index[] indexs() {
		return indexs;
	}

	@Override
	public Charset charset() {
		return charset;
	}

	@Override
	public AsyncType asyncType() {
		return asyncType;
	}

	@Override
	public int asyncSize() {
		return asyncSize;
	}

	@Override
	public int asyncDelay() {
		return asyncDelay;
	}

	@Override
	public boolean autoBuild() {
		return autoBuild;
	}
}
