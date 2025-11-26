/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.struct;

import java.util.ArrayList;
import java.util.List;

import com.xiugou.x1.backstage.module.hotfix.model.FixCode;
import com.xiugou.x1.backstage.module.hotfix.model.FixCodeResult;

/**
 * @author YY
 *
 */
public class FixCodeTask {
	private FixCode fixCode;
	private List<FixCodeResult> results = new ArrayList<>();
	public FixCode getFixCode() {
		return fixCode;
	}
	public void setFixCode(FixCode fixCode) {
		this.fixCode = fixCode;
	}
	public List<FixCodeResult> getResults() {
		return results;
	}
	public void setResults(List<FixCodeResult> results) {
		this.results = results;
	}
}
