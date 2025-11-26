/**
 * 
 */
package org.gaming.backstage.module.user.struct;

/**
 * @author YY
 *
 */
public class FunctionGranting {
	private long id;
	private String name;
	private boolean has;
	private boolean writee;
	private String module;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isWritee() {
		return writee;
	}
	public void setWritee(boolean writee) {
		this.writee = writee;
	}
	public boolean isHas() {
		return has;
	}
	public void setHas(boolean has) {
		this.has = has;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
}
