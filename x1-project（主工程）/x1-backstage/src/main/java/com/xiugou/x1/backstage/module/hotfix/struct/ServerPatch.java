/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.struct;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YY
 *
 */
public class ServerPatch {
	private Set<Integer> serverUids = new HashSet<>();
	private String param;
	public Set<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(Set<Integer> serverUids) {
		this.serverUids = serverUids;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
}
